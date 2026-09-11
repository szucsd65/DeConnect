package com.example.deconnect.repository;

import com.example.deconnect.model.PrivateMessage;
import com.example.deconnect.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrivateMessageRepo extends JpaRepository<PrivateMessage, Long> {
    @Query("SELECT m FROM PrivateMessage m WHERE (m.sender = :user1 AND m.reciever = :user2) OR (m.sender = :user2 AND m.reciever = :user1) ORDER BY m.sentDate ASC")
    List<PrivateMessage> findPrivateMessages(UserInfo sender, UserInfo reciever);
}
