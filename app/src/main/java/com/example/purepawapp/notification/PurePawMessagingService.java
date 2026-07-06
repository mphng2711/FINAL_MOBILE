package com.example.purepawapp.notification;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class PurePawMessagingService extends FirebaseMessagingService {

    private final Random random = new Random();

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Token is fetched on-demand via FirebaseMessaging.getInstance().getToken() where needed
        // (e.g. saved to the user's Firestore document after login).
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification() != null ? message.getNotification().getTitle() : message.getData().get("title");
        if (title == null) return;
        String body = message.getNotification() != null ? message.getNotification().getBody() : message.getData().get("body");
        if (body == null) body = "";
        NotificationHelper.showNotification(getApplicationContext(), title, body, random.nextInt());
    }
}
