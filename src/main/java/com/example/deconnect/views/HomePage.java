package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.model.Faculty;
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

@PageTitle("HomePage")
@Route(value = "home")
@Menu(title = "HomePage")
@AnonymousAllowed
@PermitAll
public class HomePage extends AppLayout {

    public HomePage() {
        AnnouncementTemplate posts = new AnnouncementTemplate();
        String[] facultyNames = {"Informatikai Kar", "Gazdaságtudományi Kar", "Zeneművészeti Kar",
                                    "Állam- és Jogtudományi Kar", "Általános Orvostudományi Kar", "Bölcsészettudományi Kar",
                                    "Egészségtudományi Kar", "Fogorvostudományi Kar", "Gyermeknevelési és Gyógypedagógiai Kar",
                                    "Gyógyszerésztudományi Kar", "Mezőgazdaság-, Élelmiszertudományi és Környezetgazdálkodási Kar",
                                    "Műszaki Kar", "Természettudományi Kar"};
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("DEConnect");

        RouterLink profileLink = new RouterLink("Profil", ProfileView.class);
        addToNavbar(toggle, title, profileLink);

        for (String facultyName : facultyNames) {
            Faculty faculty = new Faculty(facultyName);
            RouterLink facultyLink = new RouterLink(facultyName, TestGroup1.class, facultyName);
            addToDrawer(facultyLink);
        }

        Button newPostBtn = new Button("Új Bejegyzés");
        newPostBtn.addClassName("postBtn");
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
