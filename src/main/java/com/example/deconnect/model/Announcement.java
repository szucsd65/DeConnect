package com.example.deconnect.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Announcement {
    @Getter
    private String username;
    @Getter
    private LocalDateTime postedAt;
    @Getter
    private String message;

    public Announcement() {}

    public Announcement (String username, LocalDateTime postedAt, String message) {
        this.username = username;
        this.postedAt = postedAt;
        this.message = message;
    }

    private final List<Announcement> items = new ArrayList<>();
}
