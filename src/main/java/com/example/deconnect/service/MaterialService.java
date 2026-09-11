package com.example.deconnect.service;

import com.example.deconnect.model.Event;
import com.example.deconnect.model.Material;
import com.example.deconnect.repository.EventRepo;
import com.example.deconnect.repository.MaterialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialService {
    private final MaterialRepo repository;

    @Autowired
    public MaterialService(MaterialRepo repository) {this.repository = repository;}

    public Material saveMaterial(Material material) {
        material.setPostedAt(LocalDateTime.now());
        material.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return repository.save(material);
    }


    public List<Material> loadByFaculty(String faculty) {
        return repository.findByFaculty(faculty);
    }
}
