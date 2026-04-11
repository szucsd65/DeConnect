package com.example.deconnect.views;

import com.helger.commons.url.IHasSimpleURL;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@PageTitle("Group")
@Route(value = "testGroup")
@Menu(title = "GroupPage")
@AnonymousAllowed
@PermitAll
public class TestGroup1 extends AppLayout implements  HasUrlParameter<String>{
    private final H1 facultyTitle = new H1();
    private final AnnouncementTemplate facultyPosts = new AnnouncementTemplate();

    private final RouterLink materialsLink = new RouterLink();
    private final RouterLink announcementsLink = new RouterLink();
    private final RouterLink activitiesLink = new RouterLink();

    public TestGroup1() {
        DrawerToggle toggle = new DrawerToggle();
        Button newPostBtn = new Button("Új Bejegyzés");
        VerticalLayout announcementBoard = new VerticalLayout();

        materialsLink.setText("Tananyagok");
        announcementsLink.setText("Bejegyzések");
        activitiesLink.setText("Események");

        addToDrawer(announcementsLink, materialsLink, activitiesLink);

        addToNavbar(toggle, facultyTitle);

        facultyTitle.getStyle().set("backgroundColor", "green");
        announcementBoard.add(newPostBtn);
        announcementBoard.add(facultyPosts);
        setContent(announcementBoard);
    }

    @Override
    public void setParameter(BeforeEvent event, String facultyName) {
        facultyTitle.setText(facultyName);

        announcementsLink.setRoute(TestGroup1.class, facultyName);
        materialsLink.setRoute(TestGroup1.class, facultyName);
        activitiesLink.setRoute(TestGroup1.class, facultyName);
    }
}
