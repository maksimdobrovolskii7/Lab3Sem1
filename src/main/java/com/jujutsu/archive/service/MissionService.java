package com.jujutsu.archive.service;

import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.entity.MissionEntity;
import com.jujutsu.archive.exception.InvalidMissionFormatException;
import com.jujutsu.archive.repository.MissionRepository;
import com.jujutsu.archive.converter.ModelConverter;
import com.jujutsu.archive.parser.Mission;
import com.jujutsu.archive.parser.ParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jujutsu.archive.dto.MissionSummary;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Сервис для управления миссиями.
 * Содержит бизнес-логику: загрузка, сохранение, генерация отчётов.
 *
 * @author Ваше Имя
 * @version 1.0
 */


@Service
public class MissionService {
    private final MissionRepository repository;

    public MissionService(MissionRepository repository) {
        this.repository = repository;
    }

    public MissionEntity uploadMission(MultipartFile file) throws InvalidMissionFormatException, IOException {
        Path temp = Files.createTempFile("upload_", "_" + file.getOriginalFilename());
        file.transferTo(temp.toFile());
        var parser = ParserFactory.getParserForFile(temp.toFile());
        if (parser == null) throw new InvalidMissionFormatException("Unsupported format");
        Mission mission = parser.parse(temp.toFile());
        MissionEntity entity = ModelConverter.toEntity(mission);
        return repository.save(entity);
    }

    public ReportData generateReport(Long id, ReportParameters params) {
        MissionEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found"));
        return ReportData.fromEntity(entity, params);
    }

    public List<MissionSummary> getAllMissions() {
        return repository.findAll().stream()
                .map(m -> new MissionSummary(m.getId(), m.getMissionId(), m.getDate(), m.getLocation(), m.getOutcome()))
                .collect(Collectors.toList());
    }
}