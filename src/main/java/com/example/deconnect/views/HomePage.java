package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.model.Event;
import com.example.deconnect.model.Faculty;
import com.example.deconnect.repository.AnnouncementRepo;
import com.example.deconnect.repository.EventRepo;
import com.example.deconnect.service.AnnouncementService;
import com.example.deconnect.service.EventService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.util.Arrays;
import java.util.HashSet;

@PageTitle("HomePage")
@Route("/")
@Menu(title = "HomePage")
@PermitAll
public class HomePage extends AppLayout {

    private final AuthenticationContext authenticationContext;
    private final AnnouncementService announcementService;
    private final AnnouncementRepo announcementRepo;
    private final EventRepo eventRepo;
    private final EventService eventService;

    public HomePage(AuthenticationContext authenticationContext, AnnouncementService announcementService, AnnouncementRepo announcementRepo, EventService eventService,
            EventRepo eventRepo) {
        this.authenticationContext = authenticationContext;
        this.announcementService = announcementService;
        this.announcementRepo = announcementRepo;
        this.eventRepo = eventRepo;
        this.eventService = eventService;
        AnnouncementTemplate posts = new AnnouncementTemplate();
        String[] facultyNames = {"Informatikai Kar", "Gazdaságtudományi Kar", "Zeneművészeti Kar",
                                    "Állam- és Jogtudományi Kar", "Általános Orvostudományi Kar", "Bölcsészettudományi Kar",
                                    "Egészségtudományi Kar", "Fogorvostudományi Kar", "Gyermeknevelési és Gyógypedagógiai Kar",
                                    "Gyógyszerésztudományi Kar", "Mezőgazdaság-, Élelmiszertudományi és Környezetgazdálkodási Kar",
                                    "Műszaki Kar", "Természettudományi Kar"};
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("DEConnect");

        RouterLink profileLink = new RouterLink("Profil", ProfileView.class);
        profileLink.addClassNames("navLink");
        Button logout = new Button("Logout", event -> authenticationContext.logout());
        logout.addClassNames("logoutBtn");
        addToNavbar(toggle, title, profileLink, logout);

        for (String facultyName : facultyNames) {
            Faculty faculty = new Faculty(facultyName);
            RouterLink facultyLink = new RouterLink(facultyName, FacultyGroup.class, facultyName);
            facultyLink.addClassNames("faculty");
            addToDrawer(facultyLink);
        }

        Button newPostBtn = new Button("Új Bejegyzés");
        newPostBtn.addClassName("postBtn");
        Button newEventBtn = new Button("Új Esemény");
        newEventBtn.addClassName("postBtn");
        VerticalLayout announcementBoard = new VerticalLayout();
        posts.addClassNames("messageBoard");
        announcementBoard.add(newPostBtn);
        announcementBoard.add(newEventBtn);
        announcementBoard.add(posts);

        announcementBoard.setWidthFull();
        posts.setWidthFull();

        setContent(announcementBoard);

        Dialog textWindow = new Dialog();
        textWindow.setHeaderTitle("Új Bejegyzés");

        CheckboxGroup<String> facultyGroup = new CheckboxGroup<>();
        facultyGroup.setLabel("Karok");
        facultyGroup.setItems(Arrays.asList(facultyNames));

        TextArea textInput = new TextArea("Szöveg...");
        Button save = new Button("Küldés", e -> {
            String text = textInput.getValue();
            Announcement announcement = new Announcement();
            announcement.setMessage(text);
            announcement.setFaculty(new HashSet<>(facultyGroup.getValue()));

            announcementService.saveAnnouncement(announcement);
            announcementRepo.findAll().forEach(posts::addAnnouncement);
            textWindow.close();
        });

        Button cancel = new Button("Mégse", e -> textWindow.close());

        textWindow.add(facultyGroup);
        textWindow.add(textInput);
        textWindow.getFooter().add(cancel, save);

        newPostBtn.addClickListener(e -> textWindow.open());
        announcementRepo.findAll().forEach(posts::addAnnouncement);

        Dialog eventTextWindow = new Dialog();
        eventTextWindow.setHeaderTitle("Új Esemény");
        CheckboxGroup<String> eventFacultyGroup = new CheckboxGroup<>();
        eventFacultyGroup.setLabel("Karok");
        eventFacultyGroup.setItems(Arrays.asList(facultyNames));

        TextField eventNameInput = new TextField("Esemény neve:");
        TextArea eventTextInput = new TextArea("Szöveg...");
        Button eventSave = new Button("Küldés", e -> {
            String text = eventTextInput.getValue();
            Event event = new Event();
            event.setMessage(text);
            event.setFaculty(new HashSet<>(facultyGroup.getValue()));

            eventService.saveEvent(event);
            //eventRepo.findAll().forEach(posts::addEvent);
            textWindow.close();
        });

        eventTextWindow.add(eventNameInput);
        eventTextWindow.add(eventFacultyGroup);
        eventTextWindow.add(eventTextInput);
        eventTextWindow.getFooter().add(cancel, save);

        newEventBtn.addClickListener(e -> eventTextWindow.open());
    }
}
