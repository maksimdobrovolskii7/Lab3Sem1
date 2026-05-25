package com.jujutsu.archive.converter;

import com.jujutsu.archive.entity.*;
import com.jujutsu.archive.parser.Mission;
import java.util.stream.Collectors;

public class ModelConverter {
    public static MissionEntity toEntity(Mission mission) {
        MissionEntity entity = new MissionEntity();
        entity.setMissionId(mission.getMissionId());
        entity.setDate(mission.getDate());
        entity.setLocation(mission.getLocation());
        entity.setOutcome(mission.getOutcome());
        entity.setDamageCost(mission.getDamageCost());
        entity.setNote(mission.getNote());
        if (mission.getCurse() != null) {
            CurseEntity curse = new CurseEntity();
            curse.setName(mission.getCurse().getName());
            curse.setThreatLevel(mission.getCurse().getThreatLevel());
            entity.setCurse(curse);
        }
        if (mission.getSorcerers() != null) {
            entity.setSorcerers(mission.getSorcerers().stream().map(s -> {
                SorcererEntity se = new SorcererEntity();
                se.setName(s.getName());
                se.setRank(s.getRank());
                return se;
            }).collect(Collectors.toList()));
        }
        if (mission.getTechniques() != null) {
            entity.setTechniques(mission.getTechniques().stream().map(t -> {
                TechniqueEntity te = new TechniqueEntity();
                te.setName(t.getName());
                te.setType(t.getType());
                te.setOwner(t.getOwner());
                te.setDamage(t.getDamage());
                return te;
            }).collect(Collectors.toList()));
        }
        entity.setExtensions(mission.getExtensions());
        return entity;
    }
}