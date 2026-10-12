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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.HashSet;
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

    private void loadMaterials(MaterialTemplate materialPosts) {
        materialPosts.clearContent();
        materialRepo.findAll().forEach(materialPosts::addMaterial);
    }

    private void loadEvents(EventTemplate eventPosts) {
        eventPosts.clearContent();
        eventRepo.findAll().forEach(eventPosts::addEvent);
    }

    private void loadAnnouncements(AnnouncementTemplate posts) {
        posts.clearContent();
        announcementRepo.findAll().forEach(posts::addAnnouncement);
    }

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

        UserInfoDetails userInfoDetails = authenticationContext.getAuthenticatedUser(UserInfoDetails.class).orElseThrow();

        final UserInfo currentUser = userInfoRepo.findByEmail(userInfoDetails.getEmail()).orElseThrow();

        AnnouncementTemplate posts =
                new AnnouncementTemplate(currentUser, announcementService, () -> {});

        posts.setOnDelete(() -> loadAnnouncements(posts));
        EventTemplate eventPosts =
                new EventTemplate(currentUser, eventService, () -> {});

        eventPosts.setOnDelete(() -> loadEvents(eventPosts));
        MaterialTemplate materialPosts =
                new MaterialTemplate(currentUser, materialService, () -> {});

        materialPosts.setOnDelete(() -> loadMaterials(materialPosts));
        String[] facultyNames = {"Informatikai Kar", "Gazdaságtudományi Kar", "Zeneművészeti Kar",
                                    "Állam- és Jogtudományi Kar", "Általános Orvostudományi Kar", "Bölcsészettudományi Kar",
                                    "Egészségtudományi Kar", "Fogorvostudományi Kar", "Gyermeknevelési és Gyógypedagógiai Kar",
                                    "Gyógyszerésztudományi Kar", "Mezőgazdaság-, Élelmiszertudományi és Környezetgazdálkodási Kar",
                                    "Műszaki Kar", "Természettudományi Kar"};
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("DEConnect");

        Button logout = new Button(VaadinIcon.SIGN_OUT.create(), e
                -> authenticationContext.logout());
        logout.addClassNames("logoutBtn");


        Button privateMessageBtn = new Button(VaadinIcon.EDIT.create(), e -> {
            ChatWindow privateChat = new ChatWindow(privateMessageService, privateMessageRepo, currentUser);
            privateChat.open();
        });
        privateMessageBtn.addClassNames("navLink");

        Button myPrivateMessagesBtn = new Button(VaadinIcon.ENVELOPE.create(), e -> {
            getUI().ifPresent(ui -> ui.navigate("privatemessages"));
        });
        myPrivateMessagesBtn.addClassNames("navLink");

        addToNavbar(toggle, title, privateMessageBtn, myPrivateMessagesBtn, logout);

        for (String facultyName : facultyNames) {
            Faculty faculty = new Faculty(facultyName);
            RouterLink facultyLink = new RouterLink(facultyName, FacultyGroup.class, facultyName);
            facultyLink.addClassNames("faculty");
            addToDrawer(facultyLink);
        }

        Button newPostBtn = new Button(VaadinIcon.MEGAPHONE.create());
        newPostBtn.addClassName("postBtn");
        Button newEventBtn = new Button(VaadinIcon.CALENDAR.create());
        newEventBtn.addClassName("postBtn");
        Button newMaterialBtn = new Button(VaadinIcon.BOOK.create());
        newMaterialBtn.addClassName("postBtn");
        VerticalLayout announcementBoard = new VerticalLayout();
        posts.addClassNames("messageBoard");
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

        //Bejegyzés
        Dialog textWindow = new Dialog();
        VerticalLayout announcementContent = new VerticalLayout();
        announcementContent.setWidthFull();
        announcementContent.setAlignItems(FlexComponent.Alignment.CENTER);
        textWindow.setHeaderTitle("Új Bejegyzés");

        CheckboxGroup<String> facultyGroup = new CheckboxGroup<>();
        facultyGroup.setLabel("Karok");
        facultyGroup.setItems(Arrays.asList(facultyNames));

        TextArea textInput = new TextArea("Szöveg...");
        textInput.addClassNames("textBox");
        Button save = new Button("Megosztás", e -> {
            String text = textInput.getValue();
            Announcement announcement = new Announcement();
            announcement.setMessage(text);
            announcement.setFaculty(new HashSet<>(facultyGroup.getValue()));

            announcementService.saveAnnouncement(announcement);
            posts.clearContent();
            announcementService.loadAll().forEach(posts::addAnnouncement);
            textWindow.close();
        });
        textWindow.addClassNames("announcementWindow");

        Button cancel = new Button("Mégse", e -> textWindow.close());

        announcementContent.add(facultyGroup, textInput);
        textWindow.add(announcementContent);
        textWindow.getFooter().add(cancel, save);


        newPostBtn.addClickListener(e -> textWindow.open());
        announcementService.loadAll().forEach(posts::addAnnouncement);

        // Esemény
        Dialog eventTextWindow = new Dialog();
        VerticalLayout eventContent = new VerticalLayout();
        eventContent.setWidthFull();
        eventContent.setAlignItems(FlexComponent.Alignment.CENTER);
        eventTextWindow.setHeaderTitle("Új Esemény");
        CheckboxGroup<String> eventFacultyGroup = new CheckboxGroup<>();
        eventFacultyGroup.setLabel("Karok");
        eventFacultyGroup.setItems(Arrays.asList(facultyNames));

        TextField eventNameInput = new TextField("Esemény neve:");
        TextField eventLocationInput = new TextField("Helyszín: ");
        DatePicker plannedDatePicker = new DatePicker("Tervezett Időpont:");
        TextArea eventTextInput = new TextArea("Szöveg...");
        eventTextInput.addClassNames("textBox");
        Button eventSave = new Button("Megosztás", e -> {
            Event event = new Event();
            event.setEventName(eventNameInput.getValue());
            event.setEventLocation(eventLocationInput.getValue());
            event.setMessage(eventTextInput.getValue());
            event.setFaculty(new HashSet<>(eventFacultyGroup.getValue()));
            event.setPlannedDate(plannedDatePicker.getValue());

            eventService.saveEvent(event);
            eventPosts.clearContent();
            eventService.loadAll().forEach(eventPosts::addEvent);
            eventTextWindow.close();
        });

        Button eventCancel = new Button("Mégse", e -> eventTextWindow.close());
        eventTextWindow.getFooter().add(eventCancel, eventSave);


        eventContent.add(new HorizontalLayout(eventNameInput, eventLocationInput, plannedDatePicker), eventFacultyGroup, eventTextInput);
        eventTextWindow.add(eventContent);

        newEventBtn.addClickListener(e -> eventTextWindow.open());
        eventService.loadAll().forEach(eventPosts::addEvent);

        //Tananyagok
        Dialog materialTextWindow = new Dialog();
        VerticalLayout materialContent = new VerticalLayout();
        materialContent.setWidthFull();
        materialContent.setAlignItems(FlexComponent.Alignment.CENTER);
        materialTextWindow.setHeaderTitle("Új Tananyag");

        TextArea materialTextInput = new TextArea("Leírás:");
        materialTextInput.addClassNames("textBox");
        CheckboxGroup<String> materialFacultyGroup = new CheckboxGroup<>();
        materialFacultyGroup.setLabel("Karok");
        materialFacultyGroup.setItems(Arrays.asList(facultyNames));
        InMemoryUploadHandler inMemoryHandler = UploadHandler
                .inMemory((metadata, data) -> {
                    uploadedFileName.set(metadata.fileName());
                    uploadedContentType.set(metadata.contentType());
                    uploadedData.set(data);
                });
        Upload upload = new Upload(inMemoryHandler);
        upload.setWidthFull();
        upload.setMaxFiles(1);
        Button materialSave = new Button("Megosztás", e -> {
            Material material = new Material();
            material.setFileName(uploadedFileName.get());
            material.setMime(uploadedContentType.get());
            material.setMessage(materialTextInput.getValue());
            material.setFaculty(new HashSet<>(materialFacultyGroup.getValue()));
            material.setData(uploadedData.get());

            materialService.saveMaterial(material);
            materialPosts.clearContent();
            materialService.loadAll().forEach(materialPosts::addMaterial);
            materialTextWindow.close();
        });


        materialContent.add(materialTextInput, materialFacultyGroup, upload);
        materialTextWindow.add(materialContent);

        Button materialCancel = new Button("Mégse", e -> materialTextWindow.close());
        materialSave.addClassNames("footerBtns");
        materialCancel.addClassNames("footerBtns");
        eventSave.addClassNames("footerBtns");
        eventCancel.addClassNames("footerBtns");
        save.addClassNames("footerBtns");
        cancel.addClassNames("footerBtns");
        materialTextWindow.getFooter().add(materialCancel, materialSave);

        newMaterialBtn.addClickListener(e -> materialTextWindow.open());
        materialService.loadAll().forEach(materialPosts::addMaterial);

    }
}
