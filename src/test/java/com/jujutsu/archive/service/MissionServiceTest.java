package com.jujutsu.archive.service;

import com.jujutsu.archive.dto.MissionSummary;
import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.entity.*;
import com.jujutsu.archive.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository repository;

    @InjectMocks
    private MissionService missionService;

    private MissionEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new MissionEntity();
        testEntity.setId(1L);
        testEntity.setMissionId("M-001");
        testEntity.setDate("2024-05-23");
        testEntity.setLocation("Токио");
        testEntity.setOutcome("SUCCESS");
        testEntity.setDamageCost(100000);

        CurseEntity curse = new CurseEntity();
        curse.setName("Тестовое проклятие");
        curse.setThreatLevel("HIGH");
        testEntity.setCurse(curse);
    }

    @Test
    void getAllMissions_ShouldReturnListOfMissionSummaries() {
        when(repository.findAll()).thenReturn(Arrays.asList(testEntity));

        List<MissionSummary> result = missionService.getAllMissions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("M-001", result.get(0).getMissionId());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getMissionById_WhenMissionExists_ShouldReturnMissionEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(testEntity));

        MissionEntity result = missionService.getMissionById(1L);

        assertNotNull(result);
        assertEquals("M-001", result.getMissionId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getMissionById_WhenMissionNotFound_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            missionService.getMissionById(99L);
        });

        assertEquals("Миссия с id 99 не найдена", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void generateReport_WhenMissionExists_ShouldReturnReportData() {
        ReportParameters params = new ReportParameters();
        params.setShowBasic(true);
        params.setShowCurse(true);
        params.setShowSorcerers(true);
        params.setShowTechniques(true);
        params.setShowExtensions(true);

        when(repository.findById(1L)).thenReturn(Optional.of(testEntity));

        ReportData result = missionService.generateReport(1L, params);

        assertNotNull(result);
        assertEquals("M-001", result.getMissionId());
        assertEquals("SUCCESS", result.getOutcome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void generateReport_WhenMissionNotFound_ShouldThrowException() {
        ReportParameters params = new ReportParameters();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            missionService.generateReport(99L, params);
        });

        assertEquals("Mission not found", exception.getMessage());
    }

    @Test
    void generateReport_ShouldOnlyShowSelectedSections() {
        ReportParameters params = new ReportParameters();
        params.setShowBasic(true);
        params.setShowCurse(false);
        params.setShowSorcerers(false);
        params.setShowTechniques(false);
        params.setShowExtensions(false);

        when(repository.findById(1L)).thenReturn(Optional.of(testEntity));

        ReportData result = missionService.generateReport(1L, params);

        assertNotNull(result);
        assertEquals("M-001", result.getMissionId());
        assertNull(result.getCurse());
    }
}