package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.UserOrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrganizationRoleRepository extends JpaRepository<UserOrganizationRole, Long> {

    @Query(value = "SELECT u, o, us " +
            "FROM UserOrganizationRole u " +
            "JOIN u.organization o " +
            "JOIN u.user us " +
            "WHERE o.id IN :organizationId")
    List<UserOrganizationRole> findUserOrganizationsById(@Param("organizationId") List<Long> organizationsId);
}