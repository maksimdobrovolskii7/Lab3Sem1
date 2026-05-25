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

    public MissionWebController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("missions", missionService.getAllMissions());
        return "index";
    }

    @PostMapping("/upload")
    public String uploadMission(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        try {
            missionService.uploadMission(file);
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
    public String generateReport(
            @PathVariable Long id,
            @RequestParam(value = "showBasic", required = false, defaultValue = "false") boolean showBasic,
            @RequestParam(value = "showCurse", required = false, defaultValue = "false") boolean showCurse,
            @RequestParam(value = "showSorcerers", required = false, defaultValue = "false") boolean showSorcerers,
            @RequestParam(value = "showTechniques", required = false, defaultValue = "false") boolean showTechniques,
            @RequestParam(value = "showExtensions", required = false, defaultValue = "false") boolean showExtensions,
            Model model) {

        ReportParameters params = new ReportParameters();
        params.setShowBasic(showBasic);
        params.setShowCurse(showCurse);
        params.setShowSorcerers(showSorcerers);
        params.setShowTechniques(showTechniques);
        params.setShowExtensions(showExtensions);

        ReportData report = missionService.generateReport(id, params);
        model.addAttribute("report", report);
        model.addAttribute("id", id);
        model.addAttribute("params", params);
        return "report";
    }
}