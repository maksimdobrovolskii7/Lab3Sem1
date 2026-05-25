package com.jujutsu.archive.service.parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mission {
    private String missionId;
    private Object date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;
    private List<Sorcerer> sorcerers;
    private List<Technique> techniques;
    private String note;
    private Map<String, Object> extensions;

    // getters/setters
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
    public String getDate() { return date != null ? date.toString() : null; }
    public void setDate(Object date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }
    public Curse getCurse() { return curse; }
    public void setCurse(Curse curse) { this.curse = curse; }
    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<Sorcerer> sorcerers) { this.sorcerers = sorcerers; }
    public List<Technique> getTechniques() { return techniques; }
    public void setTechniques(List<Technique> techniques) { this.techniques = techniques; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Map<String, Object> getExtensions() { return extensions; }
    public void setExtensions(Map<String, Object> extensions) { this.extensions = extensions; }

    // внутренние классы Curse, Sorcerer, Technique (без изменений)
    public static class Curse {
        private String name;
        private String threatLevel;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getThreatLevel() { return threatLevel; }
        public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }
    }

    public static class Sorcerer {
        private String name;
        private String rank;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRank() { return rank; }
        public void setRank(String rank) { this.rank = rank; }
    }

    public static class Technique {
        private String name;
        private String type;
        private String owner;
        private long damage;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public long getDamage() { return damage; }
        public void setDamage(long damage) { this.damage = damage; }
    }
}