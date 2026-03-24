package com.example.deconnect.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.io.input.TeeInputStream;
import org.springframework.security.access.annotation.Secured;

@Route("/")
@RolesAllowed("ROLE_USER")
@Secured("ROLE_USER")
public class MainView extends VerticalLayout {
    public MainView() {

        Button loginButton = new Button("Bejelentkezés", e -> getUI().ifPresent(ui -> ui.navigate("login")));
        Button registerButton = new Button("Regisztráció");

        setAlignItems(Alignment.CENTER);

        add(new Text("Welcome to DEConnect!"));
        add(loginButton);
        add(registerButton);
    }
}
