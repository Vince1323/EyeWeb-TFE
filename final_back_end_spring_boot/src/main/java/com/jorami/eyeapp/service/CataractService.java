package com.jorami.eyeapp.service;

import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.model.Enum;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

/**
 * Interface CataractService
 *
 * Déclare toutes les méthodes utilisées dans le cadre de la gestion des examens de cataracte.
 * Ce service est implémenté dans la classe CataractServiceImpl.
 */
public interface CataractService {

    // ========== MÉTHODES DE CONSULTATION ==========

    /**
     * Récupère un examen par son ID, en s’assurant que l’utilisateur a accès aux organisations spécifiées.
     */
    Exam getExamById(Long examId, List<Long> organizationsId);

    /**
     * Récupère toutes les biométries d’un patient pour un œil donné (OD, OS), avec un flag de sélection.
     */
    List<Exam> getAllBiometriesByPatientIdAndEyeSide(Long patientId, Enum.EyeSide eyeSide, Boolean isSelected, List<Long> organizationsId);

    /**
     * Récupère toutes les biométries avec puissance calculée (utilisées dans les calculs IOL), selon l’œil et la sélection.
     */
    List<Exam> getAllBiometriesByPatientIdEyeSideCalculPowerTrue(Long patientId, Enum.EyeSide eyeSide, Boolean isSelected, List<Long> organizationsId);

    /**
     * Vérifie si un examen équivalent existe déjà dans la base (pour éviter les doublons).
     */
    void checkIfExist(Exam exam, Long idPatient, List<Long> organizationsId);

    // ========== MÉTHODES DE CRÉATION ==========

    /**
     * Ajoute un nouvel examen pour un patient donné, en y associant les organisations.
     */
    Exam addExam(Exam exam, Long idPatient, List<Long> organizationsId);

    /**
     * Ajoute une liste d’examens pour un même patient (boucle sur check + add).
     */
    void loopAddExams(List<Exam> exams, Long patientId, List<Long> organizationsId);

    /**
     * Importe un fichier contenant des biométries (ex. Pentacam, IOLMaster) et les enregistre après traitement.
     */
    void importBiometries(String tokenUser, MultipartFile file, String biometer, List<Long> organizationsId) throws IOException;

    /**
     * Calcule une biométrie moyenne (Average) à partir de plusieurs mesures.
     */
    Exam calculAverage(Long idPatient, List<Exam> biometries, List<Long> organizationsId);

    /**
     * Met à jour uniquement le champ "selected" d’un examen.
     */
    Exam editSelectedExam(Exam exam, List<Long> organizationsId);

    // ========== MÉTHODES DE MISE À JOUR ==========

    /**
     * Met à jour un examen existant (tous les champs).
     */
    Exam editExam(Exam exam, List<Long> organizationsId);

    // ========== MÉTHODES DE SUPPRESSION ==========

    /**
     * Supprime (soft-delete) un examen (flag 'deleted' à true).
     */
    void deleteExam(Long examId, List<Long> organizationsId);

    // ========== FLUX RÉACTIFS / LOGGING ==========

    /**
     * Fournit un flux réactif (WebSocket ou SSE) des logs d’importation en cours.
     */
    Flux<String> streamLogs(String tokenUser);

    // ========== EXPORT ==========

    /**
     * Récupère tous les examens d’un patient, pour un œil donné.
     * Le paramètre 'isLight' permet de choisir un DTO allégé (ExamSummaryDto) ou complet (ExamDto).
     */
    List<?> getAllExamsByPatientId(Long patientId, List<Long> idOrga, String side, boolean isLight);
}
