package parser;

import com.jujutsu.archive.exception.InvalidMissionFormatException;
import java.io.*;
import java.util.*;

public class PipeParser implements MissionParser {
    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Mission mission = new Mission();
            List<Mission.Sorcerer> sorcerers = new ArrayList<>();
            List<Mission.Technique> techniques = new ArrayList<>();
            List<String> notes = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 2) continue;
                String cmd = parts[0];
                switch (cmd) {
                    case "MISSION_CREATED":
                        if (parts.length >= 4) {
                            mission.setMissionId(parts[1]);
                            mission.setDate(parts[2]);
                            mission.setLocation(parts[3]);
                        }
                        break;
                    case "CURSE_DETECTED":
                        if (parts.length >= 3) {
                            Mission.Curse curse = new Mission.Curse();
                            curse.setName(parts[1]);
                            curse.setThreatLevel(parts[2]);
                            mission.setCurse(curse);
                        }
                        break;
                    case "SORCERER_ASSIGNED":
                        if (parts.length >= 3) {
                            Mission.Sorcerer s = new Mission.Sorcerer();
                            s.setName(parts[1]);
                            s.setRank(parts[2]);
                            sorcerers.add(s);
                        }
                        break;
                    case "TECHNIQUE_USED":
                        if (parts.length >= 5) {
                            Mission.Technique t = new Mission.Technique();
                            t.setName(parts[1]);
                            t.setType(parts[2]);
                            t.setOwner(parts[3]);
                            try { t.setDamage(Long.parseLong(parts[4])); } catch (NumberFormatException e) {}
                            techniques.add(t);
                        }
                        break;
                    case "MISSION_RESULT":
                        if (parts.length >= 2) mission.setOutcome(parts[1]);
                        if (parts.length >= 3 && parts[2].startsWith("damageCost=")) {
                            try { mission.setDamageCost(Long.parseLong(parts[2].substring(10))); } catch (NumberFormatException e) {}
                        }
                        break;
                    case "NOTE":
                        if (parts.length >= 2) notes.add(parts[1]);
                        break;
                    default:
                        notes.add("Unprocessed: " + line);
                        break;
                }
            }
            mission.setSorcerers(sorcerers);
            mission.setTechniques(techniques);
            if (!notes.isEmpty()) mission.setNote(String.join("\n", notes));
            mission.setExtensions(new HashMap<>());
            if (mission.getMissionId() == null) throw new InvalidMissionFormatException("Missing missionId");
            if (mission.getCurse() == null) throw new InvalidMissionFormatException("Missing curse");
            if (mission.getSorcerers() == null || mission.getSorcerers().isEmpty()) throw new InvalidMissionFormatException("No sorcerers");
            return mission;
        } catch (IOException e) {
            throw new InvalidMissionFormatException("Pipe parse error: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String first = br.readLine();
            return first != null && first.startsWith("MISSION_CREATED|");
        } catch (IOException e) {
            return false;
        }
    }
}