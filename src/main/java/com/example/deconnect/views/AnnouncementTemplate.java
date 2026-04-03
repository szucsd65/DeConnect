package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementTemplate extends VerticalLayout {
    private final Grid<Announcement> template = new Grid<>(Announcement.class, false);
    private final List<Announcement> items = new ArrayList<>();

    public AnnouncementTemplate() {
        template.addComponentColumn(announcement -> {
            Div post = new Div();
            post.getStyle()
                    .set("border", "1px solid #ccc")
                    .set("border-radius", "12px")
                    .set("padding", "12px")
                    .set("margin", "8px 0");

            H4 user = new H4(announcement.getUsername());
            Span time = new Span(announcement.getPostedAt().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            Paragraph message = new Paragraph(announcement.getMessage());

            post.add(user, time, message);
            return post;
        }).setHeader("Összes Bejegyzés").setFlexGrow(1);
        template.getStyle().set("border", "none");

        template.setItems(items);
        template.setWidthFull();

        add(template);
        setSizeFull();
    }

    public void addAnnouncement(Announcement announcement) {
        items.add(announcement);
        template.getDataProvider().refreshAll();
    }
}
