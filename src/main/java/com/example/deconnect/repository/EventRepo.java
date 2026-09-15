package com.example.deconnect.repository;

import com.example.deconnect.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {
    @Query("select distinct a from Event a join a.faculty f where f = :faculty")
    List<Event> findByFaculty(@Param("faculty") String faculty);
}
