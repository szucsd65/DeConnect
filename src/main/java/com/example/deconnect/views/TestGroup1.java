package com.example.deconnect.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@PageTitle("Group")
@Route(value = "testGroup")
@Menu(title = "GroupPage")
@AnonymousAllowed
@PermitAll
public class TestGroup1 extends VerticalLayout {
    public TestGroup1() {
        add(new Text("Group1"));
    }
}
