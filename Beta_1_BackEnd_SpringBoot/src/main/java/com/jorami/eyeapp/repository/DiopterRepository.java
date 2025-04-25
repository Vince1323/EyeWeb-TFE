package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.Diopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiopterRepository extends JpaRepository<Diopter, Long> {

}
