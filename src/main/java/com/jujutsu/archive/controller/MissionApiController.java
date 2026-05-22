package com.jujutsu.archive.controller;

import com.jujutsu.archive.dto.MissionSummary;
import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.entity.MissionEntity;
import com.jujutsu.archive.repository.MissionRepository;
import com.jujutsu.archive.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission Archive API")
public class MissionApiController {
    private final MissionService missionService;
    private final MissionRepository repository;

    public MissionApiController(MissionService missionService, MissionRepository repository) {
        this.missionService = missionService;
        this.repository = repository;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload mission file (JSON, XML, TXT, YAML, pipe)")
    public MissionSummary uploadMission(@RequestParam("file") MultipartFile file) throws Exception {
        MissionEntity saved = missionService.uploadMission(file);
        return new MissionSummary(saved.getId(), saved.getMissionId(), saved.getDate(), saved.getLocation(), saved.getOutcome());
    }

    @GetMapping
    @Operation(summary = "List all missions in archive")
    public List<MissionSummary> listMissions() {
        return repository.findAll().stream()
                .map(m -> new MissionSummary(m.getId(), m.getMissionId(), m.getDate(), m.getLocation(), m.getOutcome()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/report")
    @Operation(summary = "Generate report for a specific mission")
    public ReportData generateReport(@PathVariable Long id, @RequestBody ReportParameters params) {
        return missionService.generateReport(id, params);
    }
}