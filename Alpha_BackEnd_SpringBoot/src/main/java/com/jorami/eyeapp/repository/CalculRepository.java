package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.Calcul;
import com.jorami.eyeapp.model.Enum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalculRepository extends JpaRepository<Calcul, Long> {

    @Query(value = "SELECT c " +
            "FROM Calcul c " +
            "JOIN Exam e ON e.id = c.exam.id " +
            "JOIN e.links l " +
            "WHERE c.id = :id " +
            "AND l.organization.id IN :organizationsId")
    Calcul getCalculById(@Param("id") Long id, @Param("organizationsId") List<Long> organizationsId);

    @Query(value = "SELECT c " +
            "FROM Calcul c " +
            "JOIN Exam e ON e.id = c.exam.id " +
            "JOIN e.links l " +
            "WHERE l.patient.id = :patientId " +
            "AND l.organization.id IN :organizationsId " +
            "AND c.eyeSide = :eyeSide "+
            "ORDER BY c.createdAt DESC")
    List<Calcul> findAllCalculsByPatientIdAndEyeSide(@Param("patientId") Long patientId, @Param("eyeSide") Enum.EyeSide eyeSide, @Param("organizationsId") List<Long> organizationsId);

    @Query(value = "SELECT c " +
            "FROM Calcul c " +
            "JOIN Diopter d on d.calcul.id = c.id " +
            "JOIN Exam e ON e.id = c.exam.id " +
            "JOIN e.links l " +
            "WHERE l.organization.id IN :organizationsId " +
            "AND c.eyeSide = :eyeSide " +
            "AND c.exam.id = :examId " +
            "AND d.selected = true " +
            "ORDER BY c.createdAt DESC")
    List<Calcul> findAllCalculsByExamIdEyeSidePowerTrue(@Param("examId") Long examId, @Param("eyeSide") Enum.EyeSide eyeSide, @Param("organizationsId") List<Long> organizationsId);

}
