package com.example.deconnect.views;

import com.example.deconnect.model.PrivateMessage;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.repository.PrivateMessageRepo;
import com.example.deconnect.service.PrivateMessageService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Private Messages")
@Route(value = "privatemessages")
@Menu(title = "PrivateMessages")
@PermitAll
public class PrivateMessageView extends VerticalLayout {
    private final PrivateMessageService privateMessageService;
    private final PrivateMessageRepo privateMessageRepo;
    private UserInfo currentUser;
    private UserInfo recieverUser;

    private VerticalLayout messages = new VerticalLayout();
    private TextField messageField = new TextField();

    public PrivateMessageView(PrivateMessageService privateMessageService, PrivateMessageRepo privateMessageRepo){
        this.privateMessageService = privateMessageService;

        Button sendBtn = new Button("Send", e -> sendMessage());

        HorizontalLayout input = new HorizontalLayout(messageField, sendBtn);

        add(messages, input);
        this.privateMessageRepo = privateMessageRepo;
    }

    private void sendMessage(){
        if (messageField.isEmpty()){
            return;
        }

        privateMessageService.sendMessage(currentUser, recieverUser, messageField.getValue());

        messageField.clear();
        refreshMessages();
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
            messageRow.setJustifyContentMode(JustifyContentMode.END);
        }
        else{
            messageRow.setJustifyContentMode(JustifyContentMode.START);
        }

        return messageRow;
    }
}
