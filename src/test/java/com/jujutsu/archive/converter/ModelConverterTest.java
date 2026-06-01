package com.jujutsu.archive.converter;

import com.jujutsu.archive.entity.*;
import com.jujutsu.archive.parser.Mission;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModelConverterTest {

    @Test
    void toEntity_ShouldConvertMissionToMissionEntity() {
        Mission mission = new Mission();
        mission.setMissionId("M-001");
        mission.setDate("2024-05-23");
        mission.setLocation("Токио");
        mission.setOutcome("SUCCESS");
        mission.setDamageCost(100000);
        mission.setNote("Тестовая заметка");

        Mission.Curse curse = new Mission.Curse();
        curse.setName("Тестовое проклятие");
        curse.setThreatLevel("HIGH");
        mission.setCurse(curse);

        Mission.Sorcerer sorcerer = new Mission.Sorcerer();
        sorcerer.setName("Тестовый маг");
        sorcerer.setRank("GRADE_1");
        mission.setSorcerers(List.of(sorcerer));

        Mission.Technique technique = new Mission.Technique();
        technique.setName("Тестовая техника");
        technique.setType("INNATE");
        technique.setOwner("Тестовый маг");
        technique.setDamage(50000);
        mission.setTechniques(List.of(technique));

        HashMap<String, Object> extensions = new HashMap<>();
        extensions.put("customField", "test");
        mission.setExtensions(extensions);

        MissionEntity entity = ModelConverter.toEntity(mission);

        assertNotNull(entity);
        assertEquals("M-001", entity.getMissionId());
        assertEquals("2024-05-23", entity.getDate());
        assertEquals("Токио", entity.getLocation());
        assertEquals("SUCCESS", entity.getOutcome());
        assertEquals(100000, entity.getDamageCost());
        assertEquals("Тестовая заметка", entity.getNote());

        assertNotNull(entity.getCurse());
        assertEquals("Тестовое проклятие", entity.getCurse().getName());
        assertEquals("HIGH", entity.getCurse().getThreatLevel());

        assertNotNull(entity.getSorcerers());
        assertEquals(1, entity.getSorcerers().size());
        assertEquals("Тестовый маг", entity.getSorcerers().get(0).getName());
        assertEquals("GRADE_1", entity.getSorcerers().get(0).getRank());

        assertNotNull(entity.getTechniques());
        assertEquals(1, entity.getTechniques().size());
        assertEquals("Тестовая техника", entity.getTechniques().get(0).getName());

        // Проверяем связь между техникой и магом
        TechniqueEntity savedTech = entity.getTechniques().get(0);
        assertNotNull(savedTech.getSorcerer());
        assertEquals("Тестовый маг", savedTech.getSorcerer().getName());

        // Проверяем обратную связь - у мага есть техника
        SorcererEntity savedSorcerer = entity.getSorcerers().get(0);
        assertNotNull(savedSorcerer.getTechniques());
        assertEquals(1, savedSorcerer.getTechniques().size());
        assertEquals("Тестовая техника", savedSorcerer.getTechniques().get(0).getName());

        assertNotNull(entity.getExtensions());
        assertEquals("test", entity.getExtensions().get("customField"));
    }

    @Test
    void toEntity_WithNullCurse_ShouldHandleGracefully() {
        Mission mission = new Mission();
        mission.setMissionId("M-001");
        mission.setDate("2024-05-23");
        mission.setLocation("Токио");
        mission.setOutcome("SUCCESS");
        mission.setCurse(null);
        mission.setSorcerers(List.of(new Mission.Sorcerer()));
        mission.getSorcerers().get(0).setName("Маг");

        MissionEntity entity = ModelConverter.toEntity(mission);

        assertNotNull(entity);
        assertNull(entity.getCurse());
    }

    @Test
    void toEntity_WithNullSorcerers_ShouldHandleGracefully() {
        Mission mission = new Mission();
        mission.setMissionId("M-001");
        mission.setDate("2024-05-23");
        mission.setLocation("Токио");
        mission.setOutcome("SUCCESS");
        mission.setSorcerers(null);

        MissionEntity entity = ModelConverter.toEntity(mission);

        assertNotNull(entity);
        assertNull(entity.getSorcerers());
    }

    @Test
    void toEntity_WithNullTechniques_ShouldHandleGracefully() {
        Mission mission = new Mission();
        mission.setMissionId("M-001");
        mission.setDate("2024-05-23");
        mission.setLocation("Токио");
        mission.setOutcome("SUCCESS");
        mission.setTechniques(null);
        mission.setSorcerers(List.of(new Mission.Sorcerer()));
        mission.getSorcerers().get(0).setName("Маг");

        MissionEntity entity = ModelConverter.toEntity(mission);

        assertNotNull(entity);
        assertNull(entity.getTechniques());
    }
}