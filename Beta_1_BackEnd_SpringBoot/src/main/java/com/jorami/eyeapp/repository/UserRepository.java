package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.auth.model.AuthenticationRequest;
import com.jorami.eyeapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Recherche un utilisateur par email
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);


    @Query("SELECT u FROM User u WHERE u.email = :email AND u.verified = true")
    Optional<User> findUserByEmailVerified(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Long id);

    // Vérifie si l'utilisateur appartient à une organisation
    @Query(value = "SELECT CASE WHEN(COUNT(o.id)= :listLength) THEN true ELSE false END " +
            "FROM User u " +
            "JOIN UserOrganizationRole uor ON u.id = uor.user.id " +
            "JOIN Organization o ON uor.organization.id = o.id " +
            "WHERE u.id = :userId AND o.id IN (:organizationsId) AND u.deleted = FALSE AND o.deleted = FALSE")
    boolean hasOrganizationsId(@Param("userId") Long userId, @Param("organizationsId") List<Long> organizationsId, @Param("listLength") int listLength);




    // Recherche un utilisateur par nom et prénom
    @Query("SELECT u FROM User u WHERE LOWER(u.lastname) = LOWER(:lastname) AND LOWER(u.firstname) = LOWER(:firstname)")
    Optional<User> findUserByLastnameAndFirstname(@Param("lastname") String lastname, @Param("firstname") String firstname);

    // Recherche tous les utilisateurs d'un rôle spécifique
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findUsersByRole(@Param("role") String role);

    // Vérifie si un email existe déjà
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmail(@Param("email") String email);

    // Recherche avancée d'utilisateurs par mot-clé (nom, prénom, email)
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstname) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(u.lastname) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(u.email) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<User> searchUsersByKeyword(@Param("keyword") String keyword);
}
