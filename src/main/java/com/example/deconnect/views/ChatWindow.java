package com.example.deconnect.views;

import com.example.deconnect.model.PrivateMessage;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.repository.PrivateMessageRepo;
import com.example.deconnect.service.PrivateMessageService;
import com.example.deconnect.service.UserInfoDetails;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class ChatWindow extends Dialog {
    private final PrivateMessageService privateMessageService;
    private final PrivateMessageRepo privateMessageRepo;
    private UserInfo currentUser;
    private UserInfo recieverUser;
    private TextField recipientUser = new TextField("Címzett Email");
    private TextField messageField = new TextField();

    public ChatWindow(PrivateMessageService privateMessageService, PrivateMessageRepo privateMessageRepo, UserInfo currentUser){
        this.privateMessageRepo = privateMessageRepo;
        this.privateMessageService = privateMessageService;
        this.currentUser = currentUser;


        Button sendBtn = new Button("Küldés", e -> {
            if (messageField.isEmpty()){
                return;
            }
            recieverUser = privateMessageService.getUserByEmail(recipientUser.getValue());
            if (recieverUser == null){
                recipientUser.setErrorMessage("Nem található a felhasználó!");
                recipientUser.setInvalid(true);
                return;
            }
            recipientUser.setInvalid(false);
            privateMessageService.sendMessage(currentUser, recieverUser, messageField.getValue());

            messageField.clear();
            close();
        });
        sendBtn.addClassNames("postBtn");
        HorizontalLayout input = new HorizontalLayout(messageField, sendBtn);
        VerticalLayout content = new VerticalLayout();
        content.add(recipientUser, input);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        add(content);
    }
}
