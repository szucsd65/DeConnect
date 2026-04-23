package com.example.deconnect.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Menu;

@PageTitle("Login")
@Route(value = "login")
@Menu(title = "LoginView")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        LoginI18n login = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = login.getForm();
        i18nForm.setTitle("Bejelentkezés");
        i18nForm.setUsername("Email");
        i18nForm.setPassword("Jelszó");
        i18nForm.setSubmit("Belépés");
        i18nForm.setForgotPassword("Elfelejtett jelszó?");
        login.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = login.getErrorMessage();
        i18nErrorMessage.setTitle("Helytelen felhasználónév vagy jelszó");
        i18nErrorMessage.setMessage(
                "Ellenőrizze a felhasználónevét és jelszavát, majd próbálja újra.");
        login.setErrorMessage(i18nErrorMessage);

        loginForm.setI18n(login);
        loginForm.setAction("login");
        setAlignItems(Alignment.CENTER);

        add(loginForm);
    }

    public void beforeEnter(BeforeEvent e) {
        if (e.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }

}

