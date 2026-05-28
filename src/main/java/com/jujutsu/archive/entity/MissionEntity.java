package com.jujutsu.archive.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "mission")
public class MissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "curse_id")
    private CurseEntity curse;
    @ElementCollection
    @CollectionTable(name = "mission_sorcerers", joinColumns = @JoinColumn(name = "mission_id"))
    private List<SorcererEntity> sorcerers;
    @ElementCollection
    @CollectionTable(name = "mission_techniques", joinColumns = @JoinColumn(name = "mission_id"))
    private List<TechniqueEntity> techniques;
    @Column(columnDefinition = "TEXT")
    private String note;
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> extensions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }
    public CurseEntity getCurse() { return curse; }
    public void setCurse(CurseEntity curse) { this.curse = curse; }
    public List<SorcererEntity> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererEntity> sorcerers) { this.sorcerers = sorcerers; }
    public List<TechniqueEntity> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueEntity> techniques) { this.techniques = techniques; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Map<String, Object> getExtensions() { return extensions; }
    public void setExtensions(Map<String, Object> extensions) { this.extensions = extensions; }
}