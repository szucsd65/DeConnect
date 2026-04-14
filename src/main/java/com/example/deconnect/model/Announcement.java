package com.example.deconnect.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Announcement {
    private String username;
    private LocalDateTime postedAt;
    private String message;

    public Announcement() {}

    public Announcement (String username, LocalDateTime postedAt, String message) {
        this.username = username;
        this.postedAt = postedAt;
        this.message = message;
    }

    private final List<Announcement> items = new ArrayList<>();
}
