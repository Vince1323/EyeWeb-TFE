package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.operation.PlanningDto;
import com.jorami.eyeapp.service.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planning")
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningService planningService;

    @PostMapping
    public PlanningDto createPlanning(@RequestBody PlanningDto dto) {
        return planningService.savePlanning(dto);
    }

    @GetMapping("/exam/{examId}")
    public List<PlanningDto> getByExam(@PathVariable Long examId) {
        return planningService.getPlanningsByExam(examId);
    }

    @GetMapping("/test")
    public String test() {
        return "Controller is alive";
    }

}
