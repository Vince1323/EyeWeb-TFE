package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.dto.operation.PlanningDto;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.operation.Planning;
import com.jorami.eyeapp.repository.ExamRepository;
import com.jorami.eyeapp.repository.PlanningRepository;
import com.jorami.eyeapp.service.PlanningService;
import com.jorami.eyeapp.util.mapper.PlanningMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanningServiceImpl implements PlanningService {

    private final PlanningRepository planningRepository;
    private final ExamRepository examRepository;
    private final PlanningMapper planningMapper;

    @Override
    public PlanningDto savePlanning(PlanningDto dto) {
        Exam exam = examRepository.findById(dto.getExamId()).orElseThrow();
        Planning planning = planningMapper.toEntity(dto);
        planning.setExam(exam);
        planning.setPlanningDate(LocalDate.parse(dto.getPlanningDate()));
        Planning saved = planningRepository.save(planning);
        return planningMapper.toDto(saved);
    }

    @Override
    public List<PlanningDto> getPlanningsByExam(Long examId) {
        return planningRepository.findByExamId(examId)
                .stream()
                .map(planningMapper::toDto)
                .collect(Collectors.toList());
    }
}
