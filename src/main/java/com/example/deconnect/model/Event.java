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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String eventName;
    private String eventPlace;
    private LocalDateTime postedAt;
    private String message;
    private LocalDateTime plannedDate;
    @ElementCollection
    @CollectionTable(
            name = "event_faculty",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "faculty")
    private Set<String> faculty = new HashSet<>();
}
