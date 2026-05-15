package com.jujutsu.archive.service.parser;

import com.jujutsu.archive.exception.InvalidMissionFormatException;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class YamlParser implements MissionParser {
    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try (FileInputStream fis = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(fis);
            Mission mission = new Mission();
            mission.setMissionId((String) data.get("missionId"));
            mission.setDate((String) data.get("date"));
            mission.setLocation((String) data.get("location"));
            mission.setOutcome((String) data.get("outcome"));
            mission.setDamageCost(toLong(data.get("damageCost")));
            mission.setNote((String) data.get("note"));
            Map<String, Object> curseMap = (Map<String, Object>) data.get("curse");
            if (curseMap != null) {
                Mission.Curse curse = new Mission.Curse();
                curse.setName((String) curseMap.get("name"));
                curse.setThreatLevel((String) curseMap.get("threatLevel"));
                mission.setCurse(curse);
            }
            List<Map<String, Object>> sorcerersList = (List<Map<String, Object>>) data.get("sorcerers");
            if (sorcerersList != null) {
                List<Mission.Sorcerer> sorcerers = new ArrayList<>();
                for (Map<String, Object> s : sorcerersList) {
                    Mission.Sorcerer sorc = new Mission.Sorcerer();
                    sorc.setName((String) s.get("name"));
                    sorc.setRank((String) s.get("rank"));
                    sorcerers.add(sorc);
                }
                mission.setSorcerers(sorcerers);
            }
            List<Map<String, Object>> techniquesList = (List<Map<String, Object>>) data.get("techniques");
            if (techniquesList != null) {
                List<Mission.Technique> techniques = new ArrayList<>();
                for (Map<String, Object> t : techniquesList) {
                    Mission.Technique tech = new Mission.Technique();
                    tech.setName((String) t.get("name"));
                    tech.setType((String) t.get("type"));
                    tech.setOwner((String) t.get("owner"));
                    tech.setDamage(toLong(t.get("damage")));
                    techniques.add(tech);
                }
                mission.setTechniques(techniques);
            }
            Map<String, Object> extensions = new HashMap<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (!Set.of("missionId", "date", "location", "outcome", "damageCost", "note", "curse", "sorcerers", "techniques").contains(entry.getKey())) {
                    extensions.put(entry.getKey(), entry.getValue());
                }
            }
            mission.setExtensions(extensions);
            if (mission.getMissionId() == null) throw new InvalidMissionFormatException("Missing missionId");
            if (mission.getCurse() == null) throw new InvalidMissionFormatException("Missing curse");
            if (mission.getSorcerers() == null || mission.getSorcerers().isEmpty()) throw new InvalidMissionFormatException("No sorcerers");
            return mission;
        } catch (IOException e) {
            throw new InvalidMissionFormatException("YAML parse error: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".yaml") || name.endsWith(".yml");
    }

    private long toLong(Object obj) {
        if (obj instanceof Number) return ((Number) obj).longValue();
        return 0;
    }
}