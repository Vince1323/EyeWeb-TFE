package com.jorami.eyeapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jorami.eyeapp.model.Calcul;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.Lens;

import java.util.List;
import java.util.Map;

public interface CalculService {

    Calcul getCalculById(Long id, List<Long> organizationsId);

    List<Calcul> getAllCalculsByPatientIdAndEyeSide(Long patientId, Enum.EyeSide eyeSide, List<Long> organizationsId);

    List<Calcul> getAllCalculsByExamIdEyeSidePowerTrue(Long examId, Enum.EyeSide eyeSide, List<Long> organizationsId);

    void loopOnCalculs(List<Calcul> calculs, List<Long> organizationsId);

    void updateSelectedDiopters(Long idCalcul, Float power, List<Long> organizationsId);
}
