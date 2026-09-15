package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.model.Event;
import com.example.deconnect.service.CalendarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static org.apache.el.lang.ELArithmetic.add;

public class EventTemplate extends VerticalLayout{
    private final VerticalLayout template = new VerticalLayout();
    private final List<Event> eventItems = new ArrayList<>();
    CalendarService calendarService = new CalendarService();

    public EventTemplate() {
        setSizeFull();
        setPadding(false);
        setSpacing(true);

        template.setWidthFull();
        template.setPadding(false);
        template.setSpacing(true);
        template.addClassNames("postTemplate");

        add(new H4("Események"), template);
    }

    public void addEvent(Event event) {
        eventItems.add(event);

        Div eventPost = new Div();
        eventPost.addClassNames("posts");
        H4 user = new H4(event.getUsername());
        H4 eventName = new H4(event.getEventName());

        Span time = new Span(event.getPostedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Paragraph location = new Paragraph("Helyszín: " + event.getEventLocation());
        Paragraph plannedTime = new Paragraph("Időpont: " + Objects.toString(event.getPlannedDate(), "Nincs megadott dátum"));
        Paragraph message = new Paragraph(event.getMessage());
        eventPost.add(eventName, user, time, location, plannedTime, message);
        if (event.getPlannedDate() != null){
            String export = calendarService.exportIcs(event.getEventName(), event.getPlannedDate(), event.getMessage(), event.getEventLocation());

            byte[] data = export.getBytes(StandardCharsets.UTF_8);
            String fileName = event.getEventName() + ".ics";

            DownloadHandler downloadHandler = DownloadHandler.fromInputStream(
                    e -> new DownloadResponse(
                            new ByteArrayInputStream(data), fileName, "text", data.length
                    )
            );

            Anchor exportBtn = new Anchor(downloadHandler, "Export");
            exportBtn.addClassNames("eventExportBtn");
            exportBtn.getElement().getThemeList().add("button");
            eventPost.add(exportBtn);
        }
        template.add(eventPost);
    }

    public void clearContent() {
        eventItems.clear();
        template.removeAll();
    }
}

