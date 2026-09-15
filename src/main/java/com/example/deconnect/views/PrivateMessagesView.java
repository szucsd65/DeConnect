package com.example.deconnect.views;

import com.example.deconnect.model.PrivateMessage;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.repository.PrivateMessageRepo;
import com.example.deconnect.service.PrivateMessageService;
import com.example.deconnect.service.UserInfoDetails;
import com.example.deconnect.service.UserInfoService;
import com.vaadin.flow.component.Component;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

@PageTitle("Private Messages")
@Route(value = "privatemessages")
@Menu(title = "PrivateMessages")
@PermitAll
public class PrivateMessagesView extends VerticalLayout {
    private final PrivateMessageService privateMessageService;
    private final PrivateMessageRepo privateMessageRepo;
    private UserInfo currentUser;
    private VerticalLayout messages = new VerticalLayout();

    public PrivateMessagesView(PrivateMessageService privateMessageService, PrivateMessageRepo privateMessageRepo){
        this.privateMessageService = privateMessageService;
        this.privateMessageRepo = privateMessageRepo;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
        currentUser = privateMessageService.getUserByEmail(userInfoDetails.getEmail());

        refreshMessages();
        Button privateMessageBtn = new Button("Privát üzenet küldése", e -> {
            ChatWindow privateChat = new ChatWindow(privateMessageService, privateMessageRepo, currentUser);
            privateChat.open();
        });
        privateMessageBtn.addClassNames("postBtn");

        add(privateMessageBtn, messages);
        messages.setAlignItems(Alignment.CENTER);
    }

    private void refreshMessages(){
        messages.removeAll();

        if (currentUser == null){
            return;
        }

        List<PrivateMessage> messageList = privateMessageService.getUserMessage(currentUser);

        for (PrivateMessage privateMessage : messageList){
            HorizontalLayout messageRow = createPrivateMessageComponent(privateMessage);
            messages.add(messageRow);
        }
    }

    private HorizontalLayout createPrivateMessageComponent(PrivateMessage privateMessage){
        HorizontalLayout messageRow = new HorizontalLayout();
        messageRow.add(new Span(privateMessage.getSender().getEmail()));
        messageRow.add(new Span(privateMessage.getMessage()));
        if (!currentUser.equals(privateMessage.getSender())){
            Button replyBtn = new Button("Válasz", e -> {
                ChatWindow privateChat = new ChatWindow(privateMessageService, privateMessageRepo, currentUser);
                privateChat.open();
            });
            replyBtn.addClassNames("postBtn");
            messageRow.add(replyBtn);
        }

        if (privateMessage.getSender().equals(currentUser)){
            messageRow.setJustifyContentMode(JustifyContentMode.END);
        }
        else{
            messageRow.setJustifyContentMode(JustifyContentMode.START);
        }

        messageRow.setWidth("600px");
        return messageRow;
    }
}
