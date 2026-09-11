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
    private VerticalLayout messages = new VerticalLayout();
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
            refreshMessages();
            this.close();
        });
        HorizontalLayout input = new HorizontalLayout(messageField, sendBtn);

        add(recipientUser, messages, input);
    }

    private void refreshMessages(){
        messages.removeAll();

        if (currentUser == null || recieverUser == null){
            return;
        }

        List<PrivateMessage> messageList = privateMessageService.getPrivateMessages(currentUser, recieverUser);

        for (PrivateMessage privateMessage : messageList){
            HorizontalLayout messageRow = createPrivateMessageComponent(privateMessage);
            messages.add(messageRow);
        }
    }

    private HorizontalLayout createPrivateMessageComponent(PrivateMessage privateMessage){
        HorizontalLayout messageRow = new HorizontalLayout();

        messageRow.add(new Span(privateMessage.getMessage()));

        if (privateMessage.getSender().equals(currentUser)){
            messageRow.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        }
        else{
            messageRow.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        }

        return messageRow;
    }
}
