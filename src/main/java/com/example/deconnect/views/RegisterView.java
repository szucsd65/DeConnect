package com.example.deconnect.views;

import com.example.deconnect.model.UserInfo;
import com.example.deconnect.service.UserInfoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import javax.management.Notification;

@PageTitle("Register")
@Route(value = "register")
@Menu(title = "RegisterView")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private final UserInfoService userInfoService;

    public RegisterView(UserInfoService userInfoService){
        this.userInfoService = userInfoService;
        H2 title = new H2("Regisztráció");
        TextField usernameInput = new TextField("Felhasználónév");
        PasswordField passwordInput = new PasswordField("Jelszó");
        TextField emailInput = new TextField("Email");
        PasswordField passwordVerifyInput = new PasswordField("Jelszó újra");
        Button registerBtn = new Button("Regisztráció");

        registerBtn.addClickListener( e -> {
            String username = usernameInput.getValue();
            String password = passwordInput.getValue();
            String email = emailInput.getValue();
            String passwordVerify = passwordVerifyInput.getValue();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || passwordVerify.isEmpty() ) {
                return;
            }

            if (!password.equals(passwordVerify)){
                passwordVerifyInput.setErrorMessage("A jelszónak egyeznie kell!");
                passwordVerifyInput.setInvalid(true);
                return;
            }

            UserInfo user = new UserInfo();

            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setRoles("ROLE_USER");

            String result = userInfoService.addUser(user);

            if (result.equals("Already in use!")){
                emailInput.setErrorMessage("Ez az email már regisztrálva van!");
                emailInput.setInvalid(true);
            }

            getUI().ifPresent(ui -> ui.navigate("login"));
        });
        registerBtn.addClassNames("registerBtn");

        setAlignItems(Alignment.CENTER);

        add(title, usernameInput, emailInput, passwordInput, passwordVerifyInput, registerBtn);
    }
}
