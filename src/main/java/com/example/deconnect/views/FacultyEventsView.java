package com.example.deconnect.views;

import com.example.deconnect.model.Event;
import com.example.deconnect.repository.EventRepo;
import com.example.deconnect.service.EventService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@PageTitle("Events")
@Route(value = "events")
@Menu(title = "EventPage")
@PermitAll
public class FacultyEventsView extends AppLayout implements HasUrlParameter<String> {
    private final H1 facultyTitle = new H1();
    private final EventTemplate facultyEvents = new EventTemplate();

    private final RouterLink materialsLink = new RouterLink();
    private final RouterLink announcementsLink = new RouterLink();
    private final RouterLink activitiesLink = new RouterLink();

    private final AuthenticationContext authenticationContext;
    private final EventService eventService;
    private final EventRepo eventRepo;

    private String facultyName;

    public FacultyEventsView(AuthenticationContext authenticationContext, EventRepo eventRepo, EventService eventService)
    {
        this.authenticationContext = authenticationContext;
        this.eventRepo = eventRepo;
        this.eventService = eventService;
        DrawerToggle toggle = new DrawerToggle();
        Button newEventBtn = new Button("Új Esemény");
        newEventBtn.addClassNames("postBtn");
        VerticalLayout announcementBoard = new VerticalLayout();

        materialsLink.setText("Tananyagok");
        materialsLink.addClassNames("contentLink");
        announcementsLink.setText("Bejegyzések");
        announcementsLink.addClassNames("contentLink");
        activitiesLink.setText("Események");
        activitiesLink.addClassNames("contentLink");

        addToDrawer(announcementsLink, materialsLink, activitiesLink);

        Button logout = new Button("Logout", event -> authenticationContext.logout());
        logout.addClassNames("logoutBtn");

        addToNavbar(toggle, facultyTitle, logout);

        facultyTitle.getStyle();
        announcementBoard.add(newEventBtn);
        announcementBoard.add(facultyEvents);

        announcementBoard.setWidthFull();
        facultyEvents.setWidthFull();

        setContent(announcementBoard);

        Dialog eventTextWindow = new Dialog();
        eventTextWindow.setHeaderTitle("Új Esemény");

        TextField eventNameInput = new TextField("Esemény neve:");
        TextField eventLocationInput = new TextField("Helyszín: ");
        HorizontalLayout nameLocationGroup = new HorizontalLayout(eventNameInput, eventLocationInput);
        nameLocationGroup.addClassNames("nameLoc");
        DatePicker plannedDatePicker = new DatePicker("Planned Date");
        TextArea eventTextInput = new TextArea("Szöveg...");
        eventTextInput.addClassNames("textBox");
        Button eventSave = new Button("Küldés", e -> {
            Event event = new Event();
            event.setEventName(eventNameInput.getValue());
            event.setEventLocation(eventLocationInput.getValue());
            event.setMessage(eventTextInput.getValue());
            event.setFaculty(new HashSet<>(Set.of(facultyName)));
            event.setPlannedDate(plannedDatePicker.getValue());

            eventService.saveEvent(event);
            loadEvents();
            eventTextInput.clear();
            eventTextWindow.close();
        });

        //eventTextWindow.add(eventNameInput);
        //eventTextWindow.add(eventLocationInput);
        //eventTextWindow.add(plannedDatePicker);
        eventTextWindow.add(nameLocationGroup);
        eventTextWindow.add(new VerticalLayout(nameLocationGroup, plannedDatePicker));
        eventTextWindow.add(eventTextInput);

        Button eventCancel = new Button("Mégse", e -> eventTextWindow.close());
        eventTextWindow.getFooter().add(eventCancel, eventSave);

        newEventBtn.addClickListener(e -> eventTextWindow.open());
    }

    @Override
    public void setParameter(BeforeEvent event, String facultyName) {
        this.facultyName = facultyName;
        facultyTitle.setText(facultyName);

        announcementsLink.setRoute(FacultyGroup.class, facultyName);
        materialsLink.setRoute(FacultyMaterialView.class, facultyName);
        activitiesLink.setRoute(FacultyEventsView.class, facultyName);

        loadEvents();
    }

    private void loadEvents() {
        facultyEvents.clearContent();
        eventService.loadByFaculty(facultyName).forEach(facultyEvents::addEvent);
    }
}
