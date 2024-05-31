package com.darzee.shankh.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

@Service
public class PushNotificationService {

    public void sendPushNotification(String deviceToken, String message) {
        Message msg = Message.builder()
                .putData("message", message)
                .setToken(deviceToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(msg);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }
}