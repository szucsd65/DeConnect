package com.example.deconnect.service;

import com.example.deconnect.model.PrivateMessage;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.repository.PrivateMessageRepo;
import com.example.deconnect.repository.UserInfoRepo;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class PrivateMessageService {
    private final PrivateMessageRepo repository;
    private final UserInfoRepo userInfoRepo;

    public PrivateMessageService(PrivateMessageRepo repository, UserInfoRepo userInfoRepo){
        this.repository = repository;
        this.userInfoRepo = userInfoRepo;
    }

    public PrivateMessage sendMessage(UserInfo sennder, UserInfo reciever, String message){
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(sennder);
        privateMessage.setReciever(reciever);
        privateMessage.setMessage(message);
        privateMessage.setSentDate(LocalDate.now());

        return repository.save(privateMessage);

    }

    public List<PrivateMessage> getPrivateMessages(UserInfo user1, UserInfo user2){
        return repository.findPrivateMessages(user1, user2);
    }

    public List<PrivateMessage> getUserMessage(UserInfo user){
        return repository.findUserMessage(user);
    }

    public UserInfo getUserByEmail(String email){
        return userInfoRepo.findByEmail(email.trim()).orElse(null);
    }
}
