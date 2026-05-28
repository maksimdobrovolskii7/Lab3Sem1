package com.jujutsu.archive.controller;

import com.jujutsu.archive.dto.MissionSummary;
import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.service.MissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionWebController.class)
class MissionWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    private MissionSummary testSummary;

    @BeforeEach
    void setUp() {
        testSummary = new MissionSummary(1L, "M-001", "2024-05-23", "Токио", "SUCCESS");
    }

    @Test
    void index_ShouldReturnIndexPageWithMissions() throws Exception {
        List<MissionSummary> missions = Arrays.asList(testSummary);
        when(missionService.getAllMissions()).thenReturn(missions);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("missions"));
    }

    @Test
    void showReportForm_ShouldReturnReportPageWithParams() throws Exception {
        mockMvc.perform(get("/missions/1/report"))
                .andExpect(status().isOk())
                .andExpect(view().name("report"))
                .andExpect(model().attributeExists("id"))
                .andExpect(model().attributeExists("params"));
    }

    @Test
    void generateReport_ShouldReturnReportPageWithReport() throws Exception {
        ReportData reportData = new ReportData();
        reportData.setMissionId("M-001");
        reportData.setOutcome("SUCCESS");

        when(missionService.generateReport(eq(1L), any(ReportParameters.class))).thenReturn(reportData);

        mockMvc.perform(post("/missions/1/report")
                        .param("showBasic", "true")
                        .param("showCurse", "true")
                        .param("showSorcerers", "true")
                        .param("showTechniques", "true")
                        .param("showExtensions", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("report"))
                .andExpect(model().attributeExists("report"));
    }
}