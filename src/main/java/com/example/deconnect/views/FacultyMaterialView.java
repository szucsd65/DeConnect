package com.example.deconnect.views;

import com.example.deconnect.model.Event;
import com.example.deconnect.model.Material;
import com.example.deconnect.repository.EventRepo;
import com.example.deconnect.repository.MaterialRepo;
import com.example.deconnect.service.EventService;
import com.example.deconnect.service.MaterialService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.spring.security.AuthenticationContext;
import io.netty.handler.codec.mqtt.MqttReasonCodes;
import jakarta.annotation.security.PermitAll;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Materials")
@Route(value = "materials")
@Menu(title = "MaterialPage")
@PermitAll
public class FacultyMaterialView extends AppLayout implements HasUrlParameter<String> {
    private final H1 facultyTitle = new H1();
    private final MaterialTemplate facultyMaterials = new MaterialTemplate();

    private final RouterLink materialsLink = new RouterLink();
    private final RouterLink announcementsLink = new RouterLink();
    private final RouterLink activitiesLink = new RouterLink();

    private final AuthenticationContext authenticationContext;
    private final MaterialService materialService;
    private final MaterialRepo materialRepo;

    private String facultyName;

    public FacultyMaterialView(MaterialRepo materialRepo, AuthenticationContext authenticationContext, MaterialService materialService){
        this.authenticationContext = authenticationContext;
        this.materialRepo = materialRepo;
        this.materialService = materialService;

        AtomicReference<String> uploadedFileName = new AtomicReference<>();
        AtomicReference<String> uploadedContentType = new AtomicReference<>();
        AtomicReference<byte[]> uploadedData = new AtomicReference<>();

        DrawerToggle toggle = new DrawerToggle();
        Button newMaterialBtn = new Button("Új Tananyag");
        newMaterialBtn.addClassNames("postBtn");
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
        announcementBoard.add(newMaterialBtn);
        announcementBoard.add(facultyMaterials);

        announcementBoard.setWidthFull();
        facultyMaterials.setWidthFull();

        setContent(announcementBoard);

        Dialog materialTextWindow = new Dialog();
        materialTextWindow.setHeaderTitle("Új Tananyag");

        TextField materialNameInput = new TextField("Esemény neve:");
        TextArea materialTextInput = new TextArea("Leírás:");
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
            material.setFaculty(new HashSet<>(Set.of(facultyName)));
            material.setData(uploadedData.get());

            materialService.saveMaterial(material);
            loadMaterials();
            materialTextInput.clear();
            materialTextWindow.close();
        });

        materialTextWindow.add(materialNameInput);
        materialTextWindow.add(materialTextInput);
        materialTextWindow.add(upload);

        Button materialCancel = new Button("Mégse", e -> materialTextWindow.close());
        materialTextWindow.getFooter().add(materialCancel, materialSave);

        newMaterialBtn.addClickListener(e -> materialTextWindow.open());
    }
    @Override
    public void setParameter(BeforeEvent event, String facultyName) {
        this.facultyName = facultyName;
        facultyTitle.setText(facultyName);

        announcementsLink.setRoute(FacultyGroup.class, facultyName);
        materialsLink.setRoute(FacultyMaterialView.class, facultyName);
        activitiesLink.setRoute(FacultyEventsView.class, facultyName);

        loadMaterials();
    }

    private void loadMaterials() {
        facultyMaterials.clearContent();
        materialService.loadByFaculty(facultyName).forEach(facultyMaterials::addMaterial);
    }
}
