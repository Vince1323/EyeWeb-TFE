package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

}
