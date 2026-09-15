package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.repository.AnnouncementRepo;
import com.example.deconnect.repository.EventRepo;
import com.example.deconnect.service.AnnouncementService;
import com.example.deconnect.service.EventService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.util.HashSet;
import java.util.Set;

@PageTitle("Group")
@Route(value = "testGroup")
@Menu(title = "GroupPage")
@PermitAll
public class FacultyGroup extends AppLayout implements  HasUrlParameter<String>{
    private final H1 facultyTitle = new H1();
    private final AnnouncementTemplate facultyPosts = new AnnouncementTemplate();

    private final RouterLink materialsLink = new RouterLink();
    private final RouterLink announcementsLink = new RouterLink();
    private final RouterLink activitiesLink = new RouterLink();

    private final AuthenticationContext authenticationContext;
    private final AnnouncementService announcementService;
    private final AnnouncementRepo announcementRepo;

    private String facultyName;

    public FacultyGroup(AuthenticationContext authenticationContext, AnnouncementService announcementService, AnnouncementRepo announcementRepo/*, EventService eventService, EventRepo eventRepo*/) {
        this.authenticationContext = authenticationContext;
        this.announcementService = announcementService;
        this.announcementRepo = announcementRepo;
        DrawerToggle toggle = new DrawerToggle();
        Button newPostBtn = new Button("Új Bejegyzés");
        newPostBtn.addClassNames("postBtn");
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
        announcementBoard.add(newPostBtn);
        announcementBoard.add(facultyPosts);

        announcementBoard.setWidthFull();
        facultyPosts.setWidthFull();

        setContent(announcementBoard);

        Dialog textWindow = new Dialog();
        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        textWindow.setHeaderTitle("Új Bejegyzés");

        TextArea textInput = new TextArea("Szöveg...");
        textInput.addClassNames("textBox");
        Button save = new Button("Küldés", e -> {
            String text = textInput.getValue();
            Announcement announcement = new Announcement();
            announcement.setMessage(text);
            announcement.setFaculty(new HashSet<>(Set.of(facultyName)));

            announcementService.saveAnnouncement(announcement);
            loadAnnouncements();
            textInput.clear();
            textWindow.close();
        });

        Button cancel = new Button("Mégse", e -> textWindow.close());
        save.addClassNames("footerBtns");
        cancel.addClassNames("footerBtns");

        content.add(textInput);
        textWindow.add(content);
        textWindow.getFooter().add(cancel, save);

        newPostBtn.addClickListener(e -> textWindow.open());
    }

    @Override
    public void setParameter(BeforeEvent event, String facultyName) {
        this.facultyName = facultyName;
        facultyTitle.setText(facultyName);

        announcementsLink.setRoute(FacultyGroup.class, facultyName);
        materialsLink.setRoute(FacultyMaterialView.class, facultyName);
        activitiesLink.setRoute(FacultyEventsView.class, facultyName);

        loadAnnouncements();
    }

    private void loadAnnouncements() {
        facultyPosts.clearContent();
        announcementService.loadByFaculty(facultyName).forEach(facultyPosts::addAnnouncement);
    }
}
