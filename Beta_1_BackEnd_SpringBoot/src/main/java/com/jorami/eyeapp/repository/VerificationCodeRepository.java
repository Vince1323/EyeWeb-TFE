package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.auth.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByCode(String code);

    @Query(value = "SELECT v FROM VerificationCode v WHERE v.user.id = :userId")
    VerificationCode findByIdUser(Long userId);
}
