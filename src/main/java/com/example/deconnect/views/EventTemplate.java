package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.model.Event;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.el.lang.ELArithmetic.add;

public class EventTemplate extends VerticalLayout{
    private final VerticalLayout template = new VerticalLayout();
    private final List<Event> eventItems = new ArrayList<>();

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
        Paragraph location = new Paragraph(event.getEventLocation());
        Paragraph plannedTime = new Paragraph(Objects.toString(event.getPlannedDate(), "Nincs megadott dátum"));
        Paragraph message = new Paragraph(event.getMessage());

        eventPost.add(eventName, user, time, location, plannedTime, message);
        template.add(eventPost);
    }

    public void clearContent() {
        eventItems.clear();
        template.removeAll();
    }
}

