package com.example.deconnect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private LocalDateTime postedAt;
    private String message;
    @ElementCollection
    @CollectionTable(
            name = "announcement_faculty",
            joinColumns = @JoinColumn(name = "announcement_id")
    )
    @Column(name = "faculty")
    private Set<String> faculty = new HashSet<>();

}
