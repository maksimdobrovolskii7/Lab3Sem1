package com.jujutsu.archive.service.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jujutsu.archive.exception.InvalidMissionFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YamlParser implements MissionParser {
    private final ObjectMapper yamlMapper;

    public YamlParser() {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> rawData = yamlMapper.readValue(file, Map.class);

            Mission mission = new Mission();

            mission.setMissionId(getString(rawData, "missionId"));
            mission.setDate(getString(rawData, "date"));
            mission.setLocation(getString(rawData, "location"));
            mission.setOutcome(getString(rawData, "outcome"));
            mission.setDamageCost(toLong(rawData.get("damageCost")));
            mission.setNote(getString(rawData, "note"));

            Map<String, Object> curseMap = getMap(rawData, "curse");
            if (curseMap != null) {
                Mission.Curse curse = new Mission.Curse();
                curse.setName(getString(curseMap, "name"));
                curse.setThreatLevel(getString(curseMap, "threatLevel"));
                mission.setCurse(curse);
            }

            if (rawData.containsKey("sorcerers")) {
                java.util.List<Map<String, Object>> sorcerersList = getList(rawData, "sorcerers");
                if (sorcerersList != null) {
                    java.util.List<Mission.Sorcerer> sorcerers = new java.util.ArrayList<>();
                    for (Map<String, Object> s : sorcerersList) {
                        Mission.Sorcerer sorc = new Mission.Sorcerer();
                        sorc.setName(getString(s, "name"));
                        sorc.setRank(getString(s, "rank"));
                        sorcerers.add(sorc);
                    }
                    mission.setSorcerers(sorcerers);
                }
            }

            if (rawData.containsKey("techniques")) {
                java.util.List<Map<String, Object>> techniquesList = getList(rawData, "techniques");
                if (techniquesList != null) {
                    java.util.List<Mission.Technique> techniques = new java.util.ArrayList<>();
                    for (Map<String, Object> t : techniquesList) {
                        Mission.Technique tech = new Mission.Technique();
                        tech.setName(getString(t, "name"));
                        tech.setType(getString(t, "type"));
                        tech.setOwner(getString(t, "owner"));
                        tech.setDamage(toLong(t.get("damage")));
                        techniques.add(tech);
                    }
                    mission.setTechniques(techniques);
                }
            }

            Map<String, Object> extensions = new HashMap<>();
            java.util.Set<String> knownKeys = java.util.Set.of(
                    "missionId", "date", "location", "outcome", "damageCost", "note",
                    "curse", "sorcerers", "techniques"
            );

            for (Map.Entry<String, Object> entry : rawData.entrySet()) {
                String key = entry.getKey();
                if (!knownKeys.contains(key)) {
                    extensions.put(key, entry.getValue());
                }
            }
            mission.setExtensions(extensions);

            if (mission.getMissionId() == null || mission.getMissionId().trim().isEmpty()) {
                throw new InvalidMissionFormatException("Missing missionId");
            }
            if (mission.getCurse() == null) {
                throw new InvalidMissionFormatException("Missing curse");
            }
            if (mission.getSorcerers() == null || mission.getSorcerers().isEmpty()) {
                throw new InvalidMissionFormatException("No sorcerers");
            }

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

    private String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val != null ? val.toString() : null;
    }

    private Map<String, Object> getMap(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return (val instanceof Map) ? (Map<String, Object>) val : null;
    }

    @SuppressWarnings("unchecked")
    private java.util.List<Map<String, Object>> getList(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return (val instanceof java.util.List) ? (java.util.List<Map<String, Object>>) val : null;
    }

    private long toLong(Object obj) {
        if (obj instanceof Number) return ((Number) obj).longValue();
        if (obj instanceof String) {
            try { return Long.parseLong((String) obj); } catch (NumberFormatException e) {}
        }
        return 0;
    }
}