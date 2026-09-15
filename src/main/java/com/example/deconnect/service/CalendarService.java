package com.example.deconnect.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarService {
    public CalendarService() {
    }

    private static final DateTimeFormatter ICS_DATE =
            DateTimeFormatter.BASIC_ISO_DATE;

    public String exportIcs(
            String name,
            LocalDate startTime,
            String description,
            String location) {

        return """
                BEGIN:VCALENDAR
                VERSION:2.0
                PRODID:-//DEConnect//Calendar//EN
                BEGIN:VEVENT
                DTSTART;VALUE=DATE:%s
                SUMMARY:%s
                DESCRIPTION:%s
                LOCATION:%s
                END:VEVENT
                END:VCALENDAR
                """.formatted(
                startTime.format(ICS_DATE),
                name,
                description,
                location
        );
    }
}