package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementTemplate extends VerticalLayout {
    private final VerticalLayout template = new VerticalLayout();
    private final List<Announcement> items = new ArrayList<>();

    public AnnouncementTemplate() {
        setSizeFull();
        setPadding(false);
        setSpacing(true);

        template.setWidthFull();
        template.setPadding(false);
        template.setSpacing(true);
        template.addClassNames("postTemplate");

        add(new H4("Összes Bejegyzés"), template);
    }

    public void addAnnouncement(Announcement announcement) {
        items.add(announcement);

        Div post = new Div();
        post.addClassNames("posts");
        H4 user = new H4(announcement.getUsername());

        Span time = new Span(announcement.getPostedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Paragraph message = new Paragraph(announcement.getMessage());

        post.add(user, time, message);
        template.add(post);
    }

    public void clearContent() {
        items.clear();
        template.removeAll();
    }
}
