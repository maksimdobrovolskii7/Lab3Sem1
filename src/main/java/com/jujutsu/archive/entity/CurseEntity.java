package com.jujutsu.archive.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class CurseEntity {
    private String name;
    private String threatLevel;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getThreatLevel() { return threatLevel; }
    public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }
}