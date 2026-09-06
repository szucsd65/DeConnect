package com.example.deconnect.service;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.model.Event;
import com.example.deconnect.repository.EventRepo;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    private final EventRepo repository;

    @Autowired
    public EventService(EventRepo repository) {this.repository = repository;}

    public Event saveEvent(Event event) {
        event.setPostedAt(LocalDateTime.now());
        event.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return repository.save(event);
    }


    public List<Event> loadByFaculty(String faculty) {
        return repository.findByFaculty(faculty);
    }
}
