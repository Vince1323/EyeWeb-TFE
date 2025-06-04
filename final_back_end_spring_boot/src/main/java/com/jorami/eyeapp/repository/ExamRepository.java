package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.exam.Exam;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    /**
     * Récupère tous les examens liés à un patient donné via la table Link
     * @param patientId ID du patient
     * @return Liste des examens liés
     */
    @Query("SELECT e FROM Exam e " +
            "JOIN Link l ON l.exam.id = e.id " +
            "WHERE l.patient.id = :patientId")
    List<Exam> findAllByPatientId(Long patientId);

    /**
     * Récupère tous les examens liés à un patient + un côté (OD/OS)
     */
    @Query("SELECT e FROM Exam e " +
            "JOIN Link l ON l.exam.id = e.id " +
            "WHERE l.patient.id = :patientId AND e.eyeSide = :eyeSide")
    List<Exam> findAllByPatientIdAndEyeSide(Long patientId, com.jorami.eyeapp.model.Enum.EyeSide eyeSide);
}
