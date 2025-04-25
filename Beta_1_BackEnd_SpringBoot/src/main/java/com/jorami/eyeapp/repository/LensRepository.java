package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.Lens;
import com.jorami.eyeapp.model.LensManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LensRepository extends JpaRepository<Lens, Long> {

    @Query(value="SELECT CASE WHEN(COUNT(*)>0) THEN true ELSE false END FROM Lens l WHERE l.name = :name AND l.commentTradeName = :comment")
    boolean lensAlreadyExists(@Param("name") String name, @Param("comment") String comment);

    @Query(value="SELECT l FROM Lens l WHERE l.lensManufacturer = :manufacturer")
    List<Lens> findLensesByLensManufacturer(@Param("manufacturer") LensManufacturer manufacturer);

    @Query(value="SELECT l FROM Lens l WHERE l.name = :lensName AND l.commentTradeName = :comment")
    Lens findLensByNameAndComment(@Param("lensName") String lensName, @Param("comment") String comment);

    @Query(value="SELECT l.id FROM Lens l WHERE l.name = :lensName AND l.commentTradeName = :comment")
    int findLensIdByNameAndComment(@Param("lensName") String lensName, @Param("comment") String comment);

}
