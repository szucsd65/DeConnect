package com.example.deconnect.service;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.repository.AnnouncementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {
    private final AnnouncementRepo repository;

    @Autowired
    @Lazy
    public AnnouncementService(AnnouncementRepo repository) {
        this.repository = repository;
    }

    public Announcement saveAnnouncement(Announcement announcement) {
        announcement.setPostedAt(LocalDateTime.now());
        announcement.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return repository.save(announcement);
    }


    public List<Announcement> loadByFaculty(String faculty) {
        return repository.findByFaculty(faculty);
    }
}
