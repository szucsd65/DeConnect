package com.example.deconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private LocalDateTime postedAt;
    private String message;
    private String fileName;
    private String mime;
    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] data;
    @ElementCollection
    @CollectionTable(
            name = "material_faculty",
            joinColumns = @JoinColumn(name = "material_id")
    )
    @Column(name = "faculty")
    private Set<String> faculty = new HashSet<>();
}
