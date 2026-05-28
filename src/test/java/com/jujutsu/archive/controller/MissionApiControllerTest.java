package com.jujutsu.archive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jujutsu.archive.dto.MissionSummary;
import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.entity.MissionEntity;
import com.jujutsu.archive.service.MissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionApiController.class)
class MissionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    @Autowired
    private ObjectMapper objectMapper;

    private MissionEntity testEntity;
    private MissionSummary testSummary;

    @BeforeEach
    void setUp() {
        testEntity = new MissionEntity();
        testEntity.setId(1L);
        testEntity.setMissionId("M-001");
        testEntity.setDate("2024-05-23");
        testEntity.setLocation("Токио");
        testEntity.setOutcome("SUCCESS");

        testSummary = new MissionSummary(1L, "M-001", "2024-05-23", "Токио", "SUCCESS");
    }

    @Test
    void uploadMission_ShouldReturnMissionSummary() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "mission.json",
                MediaType.APPLICATION_JSON_VALUE,
                "{\"missionId\":\"M-001\"}".getBytes()
        );

        when(missionService.uploadMission(any())).thenReturn(testEntity);

        mockMvc.perform(multipart("/api/missions/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missionId").value("M-001"));
    }

    @Test
    void listMissions_ShouldReturnListOfMissionSummaries() throws Exception {
        List<MissionSummary> summaries = Arrays.asList(testSummary);
        when(missionService.getAllMissions()).thenReturn(summaries);

        mockMvc.perform(get("/api/missions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].missionId").value("M-001"));
    }

    @Test
    void getMissionById_WhenMissionExists_ShouldReturnMissionEntity() throws Exception {
        when(missionService.getMissionById(1L)).thenReturn(testEntity);

        mockMvc.perform(get("/api/missions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missionId").value("M-001"));
    }

    @Test
    void getMissionById_WhenMissionNotFound_ShouldReturn404() throws Exception {
        when(missionService.getMissionById(99L)).thenThrow(new RuntimeException("Миссия не найдена"));

        mockMvc.perform(get("/api/missions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void generateReport_ShouldReturnReportData() throws Exception {
        ReportParameters params = new ReportParameters();
        params.setShowBasic(true);
        params.setShowCurse(true);
        params.setShowSorcerers(true);
        params.setShowTechniques(true);
        params.setShowExtensions(false);

        ReportData reportData = new ReportData();
        reportData.setMissionId("M-001");
        reportData.setOutcome("SUCCESS");

        when(missionService.generateReport(eq(1L), any(ReportParameters.class))).thenReturn(reportData);

        mockMvc.perform(post("/api/missions/1/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missionId").value("M-001"));
    }
}