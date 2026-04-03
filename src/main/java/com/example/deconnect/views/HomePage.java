package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@PageTitle("HomePage")
@Route(value = "home")
@Menu(title = "HomePage")
@AnonymousAllowed
@PermitAll
public class HomePage extends AppLayout {
    public HomePage() {
        AnnouncementTemplate posts = new AnnouncementTemplate();
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("DEConnect");

        RouterLink profileLink = new RouterLink("Profil", ProfileView.class);
        RouterLink loginLink = new RouterLink("TestGroup", TestGroup1.class);
        addToNavbar(toggle, title, profileLink);
        addToDrawer(loginLink);
        Button newPostBtn = new Button("Új Bejegyzés");
        VerticalLayout announcementBoard = new VerticalLayout();
        announcementBoard.add(newPostBtn);
        announcementBoard.add(posts);

        setContent(announcementBoard);

        newPostBtn.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                posts.addAnnouncement(new Announcement("user", LocalDateTime.now().minusHours(2), "valami"));
            }
        });
    }
}
