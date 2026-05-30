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

    @ManyToMany(mappedBy = "techniques")
    private List<MissionEntity> missions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public long getDamage() { return damage; }
    public void setDamage(long damage) { this.damage = damage; }
    public List<MissionEntity> getMissions() { return missions; }
    public void setMissions(List<MissionEntity> missions) { this.missions = missions; }
}