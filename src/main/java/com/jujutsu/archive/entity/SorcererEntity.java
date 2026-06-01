package com.jujutsu.archive.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sorcerers")
public class SorcererEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String rank;

    @ManyToMany(mappedBy = "sorcerers")
    private List<MissionEntity> missions;

    @OneToMany(mappedBy = "sorcerer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechniqueEntity> techniques = new ArrayList<>();

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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public List<MissionEntity> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionEntity> missions) {
        this.missions = missions;
    }

    public List<TechniqueEntity> getTechniques() {
        return techniques;
    }

    public void setTechniques(List<TechniqueEntity> techniques) {
        this.techniques = techniques;
    }

    public void addMission(MissionEntity mission) {
        if (mission == null) {
            return;
        }
        if (this.missions == null) {
            this.missions = new ArrayList<>();
        }
        if (!this.missions.contains(mission)) {
            this.missions.add(mission);
        }
        if (mission.getSorcerers() == null) {
            mission.setSorcerers(new ArrayList<>());
        }
        if (!mission.getSorcerers().contains(this)) {
            mission.getSorcerers().add(this);
        }
    }

    public void addTechnique(TechniqueEntity technique) {
        if (technique == null) {
            return;
        }
        if (this.techniques == null) {
            this.techniques = new ArrayList<>();
        }
        if (!this.techniques.contains(technique)) {
            this.techniques.add(technique);
        }
        if (technique.getSorcerer() != this) {
            technique.setSorcerer(this);
        }
    }

}
