package com.jujutsu.archive.controller;

import com.jujutsu.archive.dto.ReportData;
import com.jujutsu.archive.dto.ReportParameters;
import com.jujutsu.archive.service.MissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MissionWebController {
    private final MissionService missionService;
    private final MissionApiController apiController;

    public MissionWebController(MissionService missionService, MissionApiController apiController) {
        this.missionService = missionService;
        this.apiController = apiController;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("missions", apiController.listMissions());
        return "index";
    }

    @PostMapping("/upload")
    public String uploadMission(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        try {
            apiController.uploadMission(file);
            ra.addFlashAttribute("success", "Mission uploaded successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/missions/{id}/report")
    public String showReportForm(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("params", new ReportParameters());
        return "report";
    }

    @PostMapping("/missions/{id}/report")
    public String generateReport(@PathVariable Long id, @ModelAttribute ReportParameters params, Model model) {
        ReportData report = missionService.generateReport(id, params);
        model.addAttribute("report", report);
        return "report";
    }
}