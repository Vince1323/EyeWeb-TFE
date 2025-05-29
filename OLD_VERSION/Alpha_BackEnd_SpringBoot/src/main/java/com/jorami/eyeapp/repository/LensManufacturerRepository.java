package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.LensManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LensManufacturerRepository extends JpaRepository<LensManufacturer, Long> {

    LensManufacturer findLensManufacturerById(Long id);

    @Query(value = "SELECT l FROM LensManufacturer l WHERE l.name = :name")
    LensManufacturer findLensManufacturerByName(@Param("name") String name);

    @Query(value = "SELECT lm " +
            "FROM LensManufacturer lm")
    List<LensManufacturer> findAllLensManufacturers();
}
