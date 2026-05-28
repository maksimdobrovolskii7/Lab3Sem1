package com.jujutsu.archive.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissionSummaryTest {

    @Test
    void constructor_ShouldSetAllFields() {
        MissionSummary summary = new MissionSummary(1L, "M-001", "2024-05-23", "Токио", "SUCCESS");

        assertEquals(1L, summary.getId());
        assertEquals("M-001", summary.getMissionId());
        assertEquals("2024-05-23", summary.getDate());
        assertEquals("Токио", summary.getLocation());
        assertEquals("SUCCESS", summary.getOutcome());
    }

    @Test
    void setters_ShouldUpdateAllFields() {
        MissionSummary summary = new MissionSummary();

        summary.setId(2L);
        summary.setMissionId("M-002");
        summary.setDate("2024-05-24");
        summary.setLocation("Осака");
        summary.setOutcome("FAILURE");

        assertEquals(2L, summary.getId());
        assertEquals("M-002", summary.getMissionId());
        assertEquals("2024-05-24", summary.getDate());
        assertEquals("Осака", summary.getLocation());
        assertEquals("FAILURE", summary.getOutcome());
    }
}