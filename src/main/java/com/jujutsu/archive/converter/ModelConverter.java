package com.jujutsu.archive.converter;

import com.jujutsu.archive.entity.*;
import com.jujutsu.archive.parser.Mission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelConverter {
    public static MissionEntity toEntity(Mission mission) {
        MissionEntity entity = new MissionEntity();
        entity.setMissionId(mission.getMissionId());
        entity.setDate(mission.getDate());
        entity.setLocation(mission.getLocation());
        entity.setOutcome(mission.getOutcome());
        entity.setDamageCost(mission.getDamageCost());
        entity.setNote(mission.getNote());

        // Создаём карту для быстрого поиска мага по имени
        Map<String, SorcererEntity> sorcererMap = new HashMap<>();

        // Сначала создаём всех магов
        if (mission.getSorcerers() != null) {
            List<SorcererEntity> sorcererEntities = new ArrayList<>();
            for (Mission.Sorcerer s : mission.getSorcerers()) {
                SorcererEntity se = new SorcererEntity();
                se.setName(s.getName());
                se.setRank(s.getRank());
                sorcererEntities.add(se);
                sorcererMap.put(s.getName(), se);
            }
            entity.setSorcerers(sorcererEntities);
            for (SorcererEntity sorcererEntity : sorcererEntities) {
                sorcererEntity.addMission(entity);
            }
        }

        // Создаём техники и связываем с магами
        if (mission.getTechniques() != null) {
            List<TechniqueEntity> techniqueEntities = new ArrayList<>();
            for (Mission.Technique t : mission.getTechniques()) {
                TechniqueEntity te = new TechniqueEntity();
                te.setName(t.getName());
                te.setType(t.getType());
                te.setOwner(t.getOwner());
                te.setDamage(t.getDamage());

                // Связываем технику с магом по имени владельца
                if (t.getOwner() != null && sorcererMap.containsKey(t.getOwner())) {
                    SorcererEntity owner = sorcererMap.get(t.getOwner());
                    te.attachToSorcerer(owner);
                }

                techniqueEntities.add(te);
            }
            entity.setTechniques(techniqueEntities);
            for (TechniqueEntity techniqueEntity : techniqueEntities) {
                techniqueEntity.addMission(entity);
            }
        }

        // Создаём проклятие
        if (mission.getCurse() != null) {
            CurseEntity curse = new CurseEntity();
            curse.setName(mission.getCurse().getName());
            curse.setThreatLevel(mission.getCurse().getThreatLevel());
            entity.setCurse(curse);
        }

        // Устанавливаем расширения
        entity.setExtensions(mission.getExtensions());

        return entity;
    }
}
