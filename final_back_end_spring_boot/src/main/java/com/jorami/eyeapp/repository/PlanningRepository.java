package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.operation.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanningRepository extends JpaRepository<Planning, Long> {
    List<Planning> findByExamId(Long examId);
}
