package com.jorami.eyeapp.service;

import com.jorami.eyeapp.dto.operation.PlanningDto;
import java.util.List;

public interface PlanningService {
    PlanningDto savePlanning(PlanningDto dto);
    List<PlanningDto> getPlanningsByExam(Long examId);
}
