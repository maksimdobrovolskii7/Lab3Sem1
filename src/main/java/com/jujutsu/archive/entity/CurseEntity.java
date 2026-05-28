package com.jujutsu.archive.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "curses")
public class CurseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String threatLevel;

    @OneToOne(mappedBy = "curse")
    private MissionEntity mission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
    }

    public MissionEntity getMission() {
        return mission;
    }

    public void setMission(MissionEntity mission) {
        this.mission = mission;
    }
}