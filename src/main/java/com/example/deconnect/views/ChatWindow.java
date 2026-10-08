package com.example.deconnect.views;

import com.example.deconnect.model.UserInfo;
import com.example.deconnect.repository.PrivateMessageRepo;
import com.example.deconnect.service.PrivateMessageService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ChatWindow extends Dialog {
    private final PrivateMessageService privateMessageService;
    private final PrivateMessageRepo privateMessageRepo;
    private UserInfo currentUser;
    private UserInfo recieverUser;
    private TextField recipientUser = new TextField("Címzett Email:");
    private TextField messageField = new TextField("Üzenet:");

    public ChatWindow(PrivateMessageService privateMessageService, PrivateMessageRepo privateMessageRepo, UserInfo currentUser){
        this.privateMessageRepo = privateMessageRepo;
        this.privateMessageService = privateMessageService;
        this.currentUser = currentUser;


        Button sendBtn = new Button(VaadinIcon.PAPERPLANE.create(), e -> {
            if (messageField.isEmpty()){
                messageField.setErrorMessage("Az üzenet mező nem lehet üres!");
                messageField.setInvalid(true);
                return;
            }
            if (recipientUser.isEmpty()){
                recipientUser.setErrorMessage("A címzett mező nem lehet üres!");
                recipientUser.setInvalid(true);
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

    public ChatWindow(PrivateMessageService privateMessageService, PrivateMessageRepo privateMessageRepo, UserInfo currentUser, UserInfo recieverUser){
        this.privateMessageRepo = privateMessageRepo;
        this.privateMessageService = privateMessageService;
        this.currentUser = currentUser;


        Button sendBtn = new Button(VaadinIcon.PAPERPLANE.create(), e -> {
            if (messageField.isEmpty()){
                messageField.setErrorMessage("Az üzenet mező nem lehet üres!");
                messageField.setInvalid(true);
                return;
            }
            privateMessageService.sendMessage(currentUser, recieverUser, messageField.getValue());

            messageField.clear();
            close();
        });
        sendBtn.addClassNames("postBtn");
        HorizontalLayout input = new HorizontalLayout(messageField, sendBtn);
        VerticalLayout content = new VerticalLayout();
        content.add(input);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        add(content);
    }
}
