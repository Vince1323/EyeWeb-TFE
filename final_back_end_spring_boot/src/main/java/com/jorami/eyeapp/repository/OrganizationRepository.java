package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query(value = "SELECT o FROM Organization o JOIN UserOrganizationRole uor ON uor.organization.id = o.id WHERE uor.user.id = :userId ORDER BY o.id")
    List<Organization> findOrganizationsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT o " +
            "FROM Organization o " +
            "WHERE o.id IN :organizationId")
    List<Organization> findOrganizationsById(@Param("organizationId") List<Long> organizationsId);

    @Query(value = "SELECT o " +
            "FROM Organization o " +
            "WHERE o.name = :userEmail")
    Organization findOrganizationByUserEmail(@Param("userEmail") String userEmail);


    @Query(value = "SELECT o " +
            "FROM Organization o " +
            "JOIN o.links l " +
            "WHERE l.patient.id = :patientId ")
    List<Organization> findOrganizationsByPatientId(@Param("patientId") Long patientId);

}
