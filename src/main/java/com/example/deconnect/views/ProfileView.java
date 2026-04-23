package com.example.deconnect.views;

import com.example.deconnect.model.UserInfo;
import com.example.deconnect.service.UserInfoService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile")
@Menu(title = "ProfilePage")
@AnonymousAllowed
@PermitAll
public class ProfileView extends VerticalLayout {
    public ProfileView(UserInfoService userInfoService) {
        add(new H1("Profil"));
        Grid<UserInfo> table = new Grid<>(UserInfo.class);

        Text emailLabel = new Text("Email cím: ");
        TextField emailTxt = new TextField();
        HorizontalLayout layout = new HorizontalLayout(emailLabel, emailTxt);
        layout.setAlignItems(Alignment.CENTER);
        Text usernameLabel = new Text("Felhasználó név: ");
        TextField usernameTxt = new TextField();
        HorizontalLayout layout2 = new HorizontalLayout(usernameLabel, usernameTxt);
        layout2.setAlignItems(Alignment.CENTER);
        add(layout2);
        add(layout);

        setAlignItems(Alignment.CENTER);
    }
}
