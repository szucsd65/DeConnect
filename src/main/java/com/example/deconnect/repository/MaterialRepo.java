package com.example.deconnect.repository;

import com.example.deconnect.model.Event;
import com.example.deconnect.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialRepo extends JpaRepository<Material, Long> {
    @Query("select distinct a from Material a join a.faculty f where f = :faculty")
    List<Material> findByFaculty(@Param("faculty") String faculty);
}
