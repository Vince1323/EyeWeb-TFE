package com.jorami.eyeapp.service;

import com.jorami.eyeapp.model.patient.Patient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {

    //GET
    List<Patient> getAllPatientsSummariesByOrganizationsId(List<Long> organizationsId);

    Patient getPatientById(List<Long> organizationsId, Long patientId);


    Patient editPatient(Long patientId, Patient updatedPatient, List<Long> organizationsId);

    Patient deletePatient(Long patientId, List<Long> organizationsId);

    //POST
    Patient addPatient(Patient patient, List<Long> organizationsId);

    Page<Patient> getFilteredPatients(List<Long> organizationsId, String search, int page, int size);




    //PUT

    //Patient getPatientSummaryById(Long patientId);

    //List<Patient> getPatientByFirstNameAndLastNameAndBirthDateAndOrganizationId(String firstname, String lastname, String birthDate, Long organizationId);

    //CataractTimelineDto getPatientTimelineByPatientId(Long patientId);


}
