package com.example.deconnect.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Faculty {
    String Name;
    ArrayList<String> announcements;
    ArrayList<String> materials;
    ArrayList<String> events;

    public Faculty(String name) {
        Name = name;
    }
}
