package com.jujutsu.archive.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "techniques")
public class TechniqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private String owner;

    private long damage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sorcerer_id")
    private SorcererEntity sorcerer;

    @ManyToMany(mappedBy = "techniques")
    private List<MissionEntity> missions;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getDamage() {
        return damage;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public SorcererEntity getSorcerer() {
        return sorcerer;
    }

    public void setSorcerer(SorcererEntity sorcerer) {
        this.sorcerer = sorcerer;
    }

    public List<MissionEntity> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionEntity> missions) {
        this.missions = missions;
    }

    public void addMission(MissionEntity mission) {
        if (mission == null) {
            return;
        }
        if (this.missions == null) {
            this.missions = new java.util.ArrayList<>();
        }
        if (!this.missions.contains(mission)) {
            this.missions.add(mission);
        }
        if (mission.getTechniques() == null) {
            mission.setTechniques(new java.util.ArrayList<>());
        }
        if (!mission.getTechniques().contains(this)) {
            mission.getTechniques().add(this);
        }
    }

    public void attachToSorcerer(SorcererEntity sorcerer) {
        this.sorcerer = sorcerer;
        if (sorcerer == null) {
            return;
        }
        if (this.owner == null || this.owner.isBlank()) {
            this.owner = sorcerer.getName();
        }
        if (sorcerer.getTechniques() == null) {
            sorcerer.setTechniques(new java.util.ArrayList<>());
        }
        if (!sorcerer.getTechniques().contains(this)) {
            sorcerer.getTechniques().add(this);
        }
    }
}
