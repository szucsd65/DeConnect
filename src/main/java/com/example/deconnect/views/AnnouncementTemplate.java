package com.example.deconnect.views;

import com.example.deconnect.model.Announcement;
import com.example.deconnect.model.Event;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.service.AnnouncementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementTemplate extends VerticalLayout {
    private final VerticalLayout template = new VerticalLayout();
    private final List<Announcement> items = new ArrayList<>();
    private final List<Event> eventItems = new ArrayList<>();
    private UserInfo currentUser;
    private AnnouncementService announcementService;
    private final Runnable refreshCallback;
    private Runnable onDelete;

    public AnnouncementTemplate(UserInfo currentUser, AnnouncementService announcementService, Runnable refreshCallback) {
        this.currentUser = currentUser;
        this.announcementService = announcementService;
        this.refreshCallback = refreshCallback;
        setSizeFull();
        setPadding(false);
        setSpacing(true);

        template.setWidthFull();
        template.setPadding(false);
        template.setSpacing(true);
        template.addClassNames("postTemplate");

        add(new H4("Üzenetek"), template);
    }

    public void addAnnouncement(Announcement announcement) {
        items.add(announcement);

        Div post = new Div();
        post.addClassNames("posts");
        H4 user = new H4(announcement.getUsername());

        if (announcement.getUsername().equals(currentUser.getUsername())) {
            Button deleteBtn = new Button(VaadinIcon.CLOSE.create(), e -> {
                announcementService.deleteAnnouncement(announcement);
                if (onDelete != null) {
                    onDelete.run();
                }
            });
            Button updateBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
                Dialog textWindow = new Dialog();
                textWindow.open();
                VerticalLayout content = new VerticalLayout();
                content.setWidthFull();
                content.setAlignItems(FlexComponent.Alignment.CENTER);
                textWindow.setHeaderTitle("Új Bejegyzés");

                TextArea textInput = new TextArea("Szöveg...");
                textInput.addClassNames("textBox");
                Button save = new Button("Küldés", saveEvent -> {
                    String text = textInput.getValue();
                    announcement.setMessage(text);

                    announcementService.updateAnnouncement(announcement);
                    textInput.clear();
                    textWindow.close();
                });

                Button cancel = new Button("Mégse", cancelEvent -> textWindow.close());
                save.addClassNames("footerBtns");
                cancel.addClassNames("footerBtns");

                content.add(textInput);
                textWindow.add(content);
                textWindow.getFooter().add(cancel, save);
            });
            updateBtn.addClassNames("crudUpdate");
            deleteBtn.addClassNames("crudDelete");
            HorizontalLayout crudButtons = new HorizontalLayout(updateBtn, deleteBtn);
            crudButtons.addClassNames("crudBtnsLine");
            post.add(crudButtons);
        }

        Span time = new Span(announcement.getPostedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Paragraph message = new Paragraph(announcement.getMessage());

        post.add(user, time, message);
        template.add(post);
    }

    public void clearContent() {
        items.clear();
        eventItems.clear();
        template.removeAll();
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }
}
