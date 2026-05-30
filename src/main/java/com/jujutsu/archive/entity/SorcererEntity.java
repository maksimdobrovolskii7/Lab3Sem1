package com.jujutsu.archive.entity;

import jakarta.persistence.*;
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public List<MissionEntity> getMissions() { return missions; }
    public void setMissions(List<MissionEntity> missions) { this.missions = missions; }
}