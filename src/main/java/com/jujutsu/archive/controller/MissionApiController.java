package com.jujutsu.archive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.jujutsu.archive.dto.MissionSummary;
import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.entity.MissionEntity;
import com.jujutsu.archive.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Mission Archive API", description = "API для работы с архивом миссий")
@RestController
@RequestMapping("/api/missions")
public class MissionApiController {
    private final MissionService missionService;
    public MissionApiController(MissionService missionService) {
        this.missionService = missionService;

    }

    @PostMapping("/upload")
    @Operation(summary = "Upload mission file (JSON, XML, TXT, YAML, pipe)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Миссия успешно загружена"),
            @ApiResponse(responseCode = "400", description = "Неподдерживаемый формат файла")
    })
    public MissionSummary uploadMission(@RequestParam("file") MultipartFile file) throws Exception {
        MissionEntity saved = missionService.uploadMission(file);
        return new MissionSummary(saved.getId(), saved.getMissionId(), saved.getDate(), saved.getLocation(), saved.getOutcome());
    }

    @GetMapping
    @Operation(summary = "List all missions in archive")

    public List<MissionSummary> listMissions() {
        return missionService.getAllMissions();
    }

    @PostMapping("/{id}/report")
    @Operation(summary = "Generate report for a specific mission")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
                    description = "Отчёт успешно сгенерирован"),
            @ApiResponse(responseCode = "404",
                    description = "Миссия не найдена")
    })
    public ReportData generateReport(@PathVariable Long id, @RequestBody ReportParameters params) {
        return missionService.generateReport(id, params);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mission by ID", description = "Возвращает миссию по её ID в базе данных")
    public ResponseEntity<MissionEntity> getMissionById(@PathVariable Long id) {
        try {
            MissionEntity mission = missionService.getMissionById(id);
            return ResponseEntity.ok(mission);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}