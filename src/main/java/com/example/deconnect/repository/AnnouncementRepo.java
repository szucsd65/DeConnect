package com.example.deconnect.repository;

import com.example.deconnect.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    @Query("select distinct a from Announcement a join a.faculty f where f = :faculty")
    List<Announcement> findByFaculty(@Param("faculty") String faculty);
}
