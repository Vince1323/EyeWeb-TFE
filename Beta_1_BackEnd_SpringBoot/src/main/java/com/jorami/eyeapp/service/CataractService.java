package com.jorami.eyeapp.service;

import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.model.Enum;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

public interface CataractService {

    //GET
    Exam getExamById(Long examId, List<Long> organizationsId);

    List<Exam> getAllBiometriesByPatientIdAndEyeSide(Long patientId, Enum.EyeSide eyeSide, Boolean isSelected, List<Long> organizationsId);

    List<Exam> getAllBiometriesByPatientIdEyeSideCalculPowerTrue(Long patientId, Enum.EyeSide eyeSide, Boolean isSelected, List<Long> organizationsId);

    void checkIfExist(Exam exam, Long idPatient, List<Long> organizationsId);

    //POST
    Exam addExam(Exam exam, Long idPatient, List<Long> organizationsId);

    void loopAddExams(List<Exam> exams, Long patientId, List<Long> organizationsId);

    void importBiometries(String tokenUser, MultipartFile file, String biometer, List<Long> organizationsId) throws IOException;

    Exam calculAverage(Long idPatient, List<Exam> biometries, List<Long> organizationsId);

    Exam editSelectedExam(Exam exam, List<Long> organizationsId);

    //PUT
    Exam editExam(Exam exam, List<Long> organizationsId);

    //DELETE
    void deleteExam(Long examId, List<Long> organizationsId);

    Flux<String> streamLogs(String tokenUser);

}
