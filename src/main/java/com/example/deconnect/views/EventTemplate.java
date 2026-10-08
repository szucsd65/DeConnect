package com.example.deconnect.views;

import com.example.deconnect.model.Event;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.service.CalendarService;
import com.example.deconnect.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class EventTemplate extends VerticalLayout{
    private final VerticalLayout template = new VerticalLayout();
    private final List<Event> eventItems = new ArrayList<>();
    CalendarService calendarService = new CalendarService();
    private UserInfo currentUser;
    private EventService eventService;
    private final Runnable refreshCallback;
    private Runnable onDelete;

    public EventTemplate(UserInfo currentUser, EventService eventService, Runnable refreshCallback) {
        this.currentUser = currentUser;
        this.eventService = eventService;
        this.refreshCallback = refreshCallback;
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

        if (event.getUsername().equals(currentUser.getUsername())) {
            Button deleteBtn = new Button(VaadinIcon.CLOSE.create(), e -> {
                eventService.deleteEvent(event);
                if (onDelete != null) {
                    onDelete.run();
                }
            });
            Button updateBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
                Dialog eventTextWindow = new Dialog();
                eventTextWindow.open();
                eventTextWindow.addClassName("updateDialog");
                VerticalLayout content = new VerticalLayout();
                content.setWidthFull();
                content.setAlignItems(FlexComponent.Alignment.CENTER);
                eventTextWindow.setHeaderTitle("Új Esemény");

                TextField eventNameInput = new TextField("Esemény neve:");
                TextField eventLocationInput = new TextField("Helyszín: ");
                HorizontalLayout nameLocationGroup = new HorizontalLayout(eventNameInput, eventLocationInput);
                nameLocationGroup.addClassNames("nameLoc");
                DatePicker plannedDatePicker = new DatePicker("Planned Date");
                TextArea eventTextInput = new TextArea("Szöveg...");
                eventTextInput.addClassNames("textBox");
                Button eventSave = new Button("Küldés", saveEvent -> {
                    event.setEventName(eventNameInput.getValue());
                    event.setEventLocation(eventLocationInput.getValue());
                    event.setMessage(eventTextInput.getValue());
                    event.setPlannedDate(plannedDatePicker.getValue());

                    eventService.updateEvent(event);
                    eventTextInput.clear();
                    eventTextWindow.close();
                });
                content.add(nameLocationGroup);
                content.add(new VerticalLayout(nameLocationGroup, plannedDatePicker));
                content.add(eventTextInput);
                eventTextWindow.add(content);

                Button eventCancel = new Button("Mégse", cancelEvent -> eventTextWindow.close());

                eventTextWindow.getFooter().add(eventCancel, eventSave);
                eventCancel.addClassNames("footerBtns");
                eventSave.addClassNames("footerBtns");
            });
            updateBtn.addClassNames("crudUpdate");
            deleteBtn.addClassNames("crudDelete");
            HorizontalLayout crudButtons = new HorizontalLayout(updateBtn, deleteBtn);
            eventPost.add(crudButtons);
        }

        Span time = new Span(event.getPostedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Paragraph location = new Paragraph("Helyszín: " + event.getEventLocation());
        Paragraph plannedTime = new Paragraph("Időpont: " + Objects.toString(event.getPlannedDate(), "Nincs megadott dátum"));
        Paragraph message = new Paragraph(event.getMessage());
        eventPost.add(eventName, user, time, location, plannedTime, message);
        if (event.getPlannedDate() != null){
            String export = calendarService.exportIcs(
                    event.getEventName(), event.getPlannedDate(), event.getMessage(), event.getEventLocation());

            byte[] data = export.getBytes(StandardCharsets.UTF_8);
            String fileName = event.getEventName() + ".ics";

            DownloadHandler downloadHandler = DownloadHandler.fromInputStream(
                    e -> new DownloadResponse(
                            new ByteArrayInputStream(data), fileName, "text", data.length
                    )
            );

            Anchor exportBtn = new Anchor(downloadHandler, "");
            exportBtn.addClassNames("eventExportBtn");
            exportBtn.getElement().getThemeList().add("button");
            exportBtn.add(VaadinIcon.DOWNLOAD.create());
            eventPost.add(exportBtn);
        }
        template.add(eventPost);
    }

    public void clearContent() {
        eventItems.clear();
        template.removeAll();
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }
}

