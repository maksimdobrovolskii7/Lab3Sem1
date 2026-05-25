package com.jujutsu.archive.parser;

import com.jujutsu.archive.exception.InvalidMissionFormatException;
import java.io.*;
import java.util.*;

public class TxtParser implements MissionParser {
    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Mission mission = new Mission();
            List<Mission.Sorcerer> sorcerers = new ArrayList<>();
            List<Mission.Technique> techniques = new ArrayList<>();
            Map<String, Object> extensions = new HashMap<>();
            String currentSection = "";
            Map<String, String> currentItem = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("[") && line.endsWith("]")) {
                    if ("SORCERER".equals(currentSection) && !currentItem.isEmpty()) {
                        Mission.Sorcerer s = new Mission.Sorcerer();
                        s.setName(currentItem.get("name"));
                        s.setRank(currentItem.get("rank"));
                        sorcerers.add(s);
                        currentItem.clear();
                    } else if ("TECHNIQUE".equals(currentSection) && !currentItem.isEmpty()) {
                        Mission.Technique t = new Mission.Technique();
                        t.setName(currentItem.get("name"));
                        t.setType(currentItem.get("type"));
                        t.setOwner(currentItem.get("owner"));
                        t.setDamage(Long.parseLong(currentItem.getOrDefault("damage", "0")));
                        techniques.add(t);
                        currentItem.clear();
                    }
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length < 2) continue;
                String key = parts[0].trim();
                String value = parts[1].trim();
                switch (currentSection) {
                    case "MISSION":
                        switch (key) {
                            case "missionId": mission.setMissionId(value); break;
                            case "date": mission.setDate(value); break;
                            case "location": mission.setLocation(value); break;
                            case "outcome": mission.setOutcome(value); break;
                            case "damageCost": mission.setDamageCost(Long.parseLong(value)); break;
                            case "note": mission.setNote(value); break;
                        }
                        break;
                    case "CURSE":
                        if (mission.getCurse() == null) mission.setCurse(new Mission.Curse());
                        if ("name".equals(key)) mission.getCurse().setName(value);
                        else if ("threatLevel".equals(key)) mission.getCurse().setThreatLevel(value);
                        break;
                    case "SORCERER":
                        currentItem.put(key, value);
                        break;
                    case "TECHNIQUE":
                        currentItem.put(key, value);
                        break;
                    default:
                        extensions.put(key, value);
                        break;
                }
            }
            if ("SORCERER".equals(currentSection) && !currentItem.isEmpty()) {
                Mission.Sorcerer s = new Mission.Sorcerer();
                s.setName(currentItem.get("name"));
                s.setRank(currentItem.get("rank"));
                sorcerers.add(s);
            } else if ("TECHNIQUE".equals(currentSection) && !currentItem.isEmpty()) {
                Mission.Technique t = new Mission.Technique();
                t.setName(currentItem.get("name"));
                t.setType(currentItem.get("type"));
                t.setOwner(currentItem.get("owner"));
                t.setDamage(Long.parseLong(currentItem.getOrDefault("damage", "0")));
                techniques.add(t);
            }
            mission.setSorcerers(sorcerers);
            mission.setTechniques(techniques);
            mission.setExtensions(extensions);
            if (mission.getMissionId() == null) throw new InvalidMissionFormatException("Missing missionId");
            if (mission.getCurse() == null) throw new InvalidMissionFormatException("Missing curse");
            if (mission.getSorcerers() == null || mission.getSorcerers().isEmpty()) throw new InvalidMissionFormatException("No sorcerers");
            return mission;
        } catch (IOException e) {
            throw new InvalidMissionFormatException("TXT parse error: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        return file.getName().toLowerCase().endsWith(".txt");
    }
}