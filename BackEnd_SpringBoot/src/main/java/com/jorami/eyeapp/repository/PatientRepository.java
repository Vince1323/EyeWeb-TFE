package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "SELECT p, l " +
            "FROM Patient p " +
            "LEFT JOIN FETCH p.links l " +
            "WHERE LOWER(p.firstname) LIKE CONCAT('%',LOWER(:firstname),'%') " +
            "AND LOWER(p.lastname) LIKE CONCAT('%',LOWER(:lastname),'%') " +
            "AND p.birthDate = :birthDate ")
    List<Patient> findPatientByFirstNameAndLastNameAndBirthDate(@Param("firstname") String firstname, @Param("lastname") String lastname, @Param("birthDate") LocalDate birthDate);

    @Query(value = "SELECT DISTINCT NEW com.jorami.eyeapp.model.patient.Patient(p.id, p.lastname, p.firstname, p.birthDate, p.niss, p.gender, p.phone, p.mail, p.job, p.hobbies)" +
            "FROM Patient p " +
            "JOIN p.links l " +
            "WHERE l.organization.id IN :organizationsId " +
            "GROUP BY p.id, p.lastname, p.firstname, p.birthDate, p.niss, p.gender, p.phone, p.mail, p.job, p.hobbies")
    List<Patient> findAllPatientsSummariesByOrganizationsId(@Param("organizationsId") List<Long> organizationsId);

    @Query(value = "SELECT p " +
            "FROM Patient p " +
            "JOIN p.links l " +
            "WHERE p.id = :patientId " +
            "AND l.organization.id IN :organizationsId")
    Optional<Patient> findPatientById(@Param("patientId") Long patientId, @Param("organizationsId") List<Long> organizationsId);
}
