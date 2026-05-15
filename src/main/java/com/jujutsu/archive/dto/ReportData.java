package com.jujutsu.archive.dto;

import com.jujutsu.archive.entity.*;
import java.util.List;
import java.util.Map;

public class ReportData {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private CurseEntity curse;
    private List<SorcererEntity> sorcerers;
    private List<TechniqueEntity> techniques;
    private String note;
    private Map<String, Object> extensions;
    private ReportParameters appliedParams;

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
    public ReportParameters getAppliedParams() { return appliedParams; }
    public void setAppliedParams(ReportParameters appliedParams) { this.appliedParams = appliedParams; }

    public static ReportData fromEntity(MissionEntity entity, ReportParameters params) {
        ReportData data = new ReportData();
        if (params.isShowBasic()) {
            data.setMissionId(entity.getMissionId());
            data.setDate(entity.getDate());
            data.setLocation(entity.getLocation());
            data.setOutcome(entity.getOutcome());
            data.setDamageCost(entity.getDamageCost());
            data.setNote(entity.getNote());
        }
        if (params.isShowCurse()) data.setCurse(entity.getCurse());
        if (params.isShowSorcerers()) data.setSorcerers(entity.getSorcerers());
        if (params.isShowTechniques()) data.setTechniques(entity.getTechniques());
        if (params.isShowExtensions()) data.setExtensions(entity.getExtensions());
        data.setAppliedParams(params);
        return data;
    }
}