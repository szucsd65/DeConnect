package com.example.deconnect.views;

import com.example.deconnect.model.*;
import com.example.deconnect.repository.*;
import com.example.deconnect.service.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("HomePage")
@Route("/")
@Menu(title = "HomePage")
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public class HomePage extends AppLayout {

    private final AuthenticationContext authenticationContext;
    private final AnnouncementService announcementService;
    private final AnnouncementRepo announcementRepo;
    private final EventRepo eventRepo;
    private final EventService eventService;
    private final MaterialService materialService;
    private final MaterialRepo materialRepo;
    private final PrivateMessageService privateMessageService;
    private final PrivateMessageRepo privateMessageRepo;
    private final UserInfoRepo userInfoRepo;


    public HomePage(AuthenticationContext authenticationContext, AnnouncementService announcementService, AnnouncementRepo announcementRepo, EventService eventService,
                    EventRepo eventRepo, MaterialRepo materialRepo, MaterialService materialService, PrivateMessageService privateMessageService, PrivateMessageRepo privateMessageRepo, UserInfoRepo userInfoRepo) {
        this.authenticationContext = authenticationContext;
        this.announcementService = announcementService;
        this.announcementRepo = announcementRepo;
        this.eventRepo = eventRepo;
        this.eventService = eventService;
        this.materialRepo = materialRepo;
        this.materialService = materialService;
        this.privateMessageRepo = privateMessageRepo;
        this.privateMessageService = privateMessageService;
        this.userInfoRepo = userInfoRepo;

        AtomicReference<String> uploadedFileName = new AtomicReference<>();
        AtomicReference<String> uploadedContentType = new AtomicReference<>();
        AtomicReference<byte[]> uploadedData = new AtomicReference<>();

        AnnouncementTemplate posts = new AnnouncementTemplate();
        EventTemplate eventPosts = new EventTemplate();
        MaterialTemplate materialPosts = new MaterialTemplate();
        //EventTemplate eventPosts = new EventTemplate();
        String[] facultyNames = {"Informatikai Kar", "Gazdaságtudományi Kar", "Zeneművészeti Kar",
                                    "Állam- és Jogtudományi Kar", "Általános Orvostudományi Kar", "Bölcsészettudományi Kar",
                                    "Egészségtudományi Kar", "Fogorvostudományi Kar", "Gyermeknevelési és Gyógypedagógiai Kar",
                                    "Gyógyszerésztudományi Kar", "Mezőgazdaság-, Élelmiszertudományi és Környezetgazdálkodási Kar",
                                    "Műszaki Kar", "Természettudományi Kar"};
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("DEConnect");

        RouterLink profileLink = new RouterLink("Profil", ProfileView.class);
        profileLink.addClassNames("navLink");
        Button logout = new Button("Logout", e -> authenticationContext.logout());
        logout.addClassNames("logoutBtn");

        UserInfoDetails userInfoDetails = authenticationContext.getAuthenticatedUser(UserInfoDetails.class).orElseThrow();

        final UserInfo currentUser = userInfoRepo.findByEmail(userInfoDetails.getUsername()).orElseThrow();

        Button privateMessageBtn = new Button("Privát üzenet", e -> {
            ChatWindow privateChat = new ChatWindow(privateMessageService, privateMessageRepo, currentUser);
            privateChat.open();
        });
        privateMessageBtn.addClassNames("navLink");

        addToNavbar(toggle, title, profileLink, privateMessageBtn, logout);

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
        Button newMaterialBtn = new Button("Új Tananyag");
        newMaterialBtn.addClassName("postBtn");
        VerticalLayout announcementBoard = new VerticalLayout();
        posts.addClassNames("messageBoard");
        //announcementBoard.add(newPostBtn);
        //announcementBoard.add(newEventBtn);
        //announcementBoard.add(newMaterialBtn);
        HorizontalLayout actionBtnGroup = new HorizontalLayout(newPostBtn, newEventBtn, newMaterialBtn);
        announcementBoard.add(actionBtnGroup);
        announcementBoard.add(posts);
        announcementBoard.add(eventPosts);
        announcementBoard.add(materialPosts);
        announcementBoard.setWidthFull();
        posts.setWidthFull();
        eventPosts.setWidthFull();
        materialPosts.setWidthFull();

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
        textWindow.addClassNames("announcementWindow");

        Button cancel = new Button("Mégse", e -> textWindow.close());

        textWindow.add(facultyGroup);
        textWindow.add(textInput);
        textWindow.getFooter().add(cancel, save);

        newPostBtn.addClickListener(e -> textWindow.open());
        announcementRepo.findAll().forEach(posts::addAnnouncement);

        // Esemény
        Dialog eventTextWindow = new Dialog();
        eventTextWindow.setHeaderTitle("Új Esemény");
        CheckboxGroup<String> eventFacultyGroup = new CheckboxGroup<>();
        eventFacultyGroup.setLabel("Karok");
        eventFacultyGroup.setItems(Arrays.asList(facultyNames));

        TextField eventNameInput = new TextField("Esemény neve:");
        TextField eventLocationInput = new TextField("Helyszín: ");
        DatePicker plannedDatePicker = new DatePicker("Planned Date");
        TextArea eventTextInput = new TextArea("Szöveg...");
        Button eventSave = new Button("Küldés", e -> {
            //String text = eventTextInput.getValue();
            Event event = new Event();
            event.setEventName(eventNameInput.getValue());
            event.setEventLocation(eventLocationInput.getValue());
            event.setMessage(eventTextInput.getValue());
            event.setFaculty(new HashSet<>(eventFacultyGroup.getValue()));
            event.setPlannedDate(plannedDatePicker.getValue());

            eventService.saveEvent(event);
            eventRepo.findAll().forEach(eventPosts::addEvent);
            eventTextWindow.close();
        });

        eventTextWindow.add(eventNameInput);
        eventTextWindow.add(eventLocationInput);
        eventTextWindow.add(plannedDatePicker);
        eventTextWindow.add(eventFacultyGroup);
        eventTextWindow.add(eventTextInput);

        Button eventCancel = new Button("Mégse", e -> eventTextWindow.close());
        eventTextWindow.getFooter().add(eventCancel, eventSave);

        newEventBtn.addClickListener(e -> eventTextWindow.open());
        eventRepo.findAll().forEach(eventPosts::addEvent);

        Dialog materialTextWindow = new Dialog();
        materialTextWindow.setHeaderTitle("Új Tananyag");

        TextField materialNameInput = new TextField("Tananyag neve:");
        TextArea materialTextInput = new TextArea("Leírás:");
        CheckboxGroup<String> materialFacultyGroup = new CheckboxGroup<>();
        materialFacultyGroup.setLabel("Karok");
        materialFacultyGroup.setItems(Arrays.asList(facultyNames));
        InMemoryUploadHandler inMemoryHandler = UploadHandler
                .inMemory((metadata, data) -> {
                    uploadedFileName.set( metadata.fileName());
                    uploadedContentType.set(metadata.contentType());
                    uploadedData.set(data);
                });
        Upload upload = new Upload(inMemoryHandler);
        upload.setWidthFull();
        upload.setMaxFiles(1);
        Button materialSave = new Button("Küldés", e -> {
            Material material = new Material();
            material.setFileName(materialNameInput.getValue());
            material.setMessage(materialTextInput.getValue());
            material.setFaculty(new HashSet<>(materialFacultyGroup.getValue()));
            material.setData(uploadedData.get());

            materialService.saveMaterial(material);
            materialRepo.findAll().forEach(materialPosts::addMaterial);
            materialTextWindow.close();
        });

        materialTextWindow.add(materialNameInput);
        materialTextWindow.add(materialTextInput);
        materialTextWindow.add(materialFacultyGroup);
        materialTextWindow.add(upload);

        Button materialCancel = new Button("Mégse", e -> materialTextWindow.close());
        materialTextWindow.getFooter().add(materialCancel, materialSave);

        newMaterialBtn.addClickListener(e -> materialTextWindow.open());
        materialRepo.findAll().forEach(materialPosts::addMaterial);

    }
}
