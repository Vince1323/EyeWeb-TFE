package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.jorami.eyeapp.model.Enum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CataractRepository extends JpaRepository<Exam, Long> {

    @Query(value = "SELECT b " +
            "FROM Exam b " +
            "JOIN b.links l " +
            "WHERE b.id = :examId "+
            "AND l.organization.id IN :organizationsId")
    Optional<Exam> findExamById(@Param("examId") Long examId, @Param("organizationsId") List<Long> organizationsId);

    @Query(value="SELECT b " +
            "FROM Exam b " +
            "JOIN b.links l " +
            "WHERE l.patient.id = :patientId " +
            "AND b.examDate = :examDate " +
            "AND b.eyeSide = :eyeSide " +
            "AND b.machine = :biometer " +
            "AND l.organization.id IN :organizationsId")
    Exam findExamByPatientIdAndExamDateAndEyeSideAndMachine(@Param("patientId") Long patientId,
                                                                 @Param("examDate") LocalDateTime examDate,
                                                                 @Param("eyeSide") Enum.EyeSide eyeSide,
                                                                 @Param("biometer") Enum.Biometer biometer,
                                                                 @Param("organizationsId") List<Long> organizationsId);

    @Query(value = "SELECT b " +
            "FROM Exam b " +
            "JOIN b.links l " +
            "WHERE l.patient.id = :patientId " +
            "AND b.eyeSide = :eyeSide " +
            "AND l.organization.id IN :organizationsId " +
            "AND (:selected IS NULL OR b.selected = :selected) " +
            "ORDER BY b.createdAt DESC")
    List<Exam> findAllBiometriesByPatientIdAndEyeSide(@Param("patientId") Long patientId,
                                                      @Param("eyeSide") Enum.EyeSide eyeSide,
                                                      @Param("selected") Boolean selected,
                                                      @Param("organizationsId") List<Long> organizationsId);

    @Query(value = "SELECT b " +
            "FROM Exam b " +
            "JOIN b.links l " +
            "JOIN Calcul c on c.exam.id = b.id " +
            "JOIN Diopter d on d.calcul.id = c.id " +
            "WHERE l.patient.id = :patientId " +
            "AND b.eyeSide = :eyeSide " +
            "AND l.organization.id IN :organizationsId " +
            "AND (:selected IS NULL OR b.selected = :selected) " +
            "AND d.selected = true " +
            "ORDER BY b.createdAt DESC")
    List<Exam> findAllBiometriesByPatientIdEyeSideCalculPowerTrue(@Param("patientId") Long patientId,
                                                      @Param("eyeSide") Enum.EyeSide eyeSide,
                                                      @Param("selected") Boolean selected,
                                                      @Param("organizationsId") List<Long> organizationsId);

}
