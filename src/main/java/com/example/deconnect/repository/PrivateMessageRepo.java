package com.example.deconnect.repository;

import com.example.deconnect.model.PrivateMessage;
import com.example.deconnect.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepo extends JpaRepository<PrivateMessage, Long> {
    @Query("SELECT m FROM PrivateMessage m WHERE (m.sender = :user1 AND m.reciever = :user2) OR (m.sender = :user2 AND m.reciever = :user1) ORDER BY m.sentDate ASC")
    List<PrivateMessage> findPrivateMessages(@Param("user1") UserInfo user1, @Param("user2") UserInfo user2);

    @Query("SELECT m FROM PrivateMessage m WHERE m.sender = :user OR m.reciever = :user Order BY m.sentDate ASC")
    List<PrivateMessage> findUserMessage(@Param("user") UserInfo user);
}