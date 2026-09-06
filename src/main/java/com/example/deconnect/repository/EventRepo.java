package com.example.deconnect.repository;

import com.example.deconnect.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepo extends JpaRepository<Event, Long> {
    @Query("select distinct a from Announcement a join a.faculty f where f = :faculty")
    List<Event> findByFaculty(@Param("faculty") String faculty);
}
