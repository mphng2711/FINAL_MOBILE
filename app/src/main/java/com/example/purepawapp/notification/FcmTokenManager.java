package com.example.purepawapp.notification;

import com.example.purepawapp.data.repository.RepoCallback;
import com.example.purepawapp.di.ServiceLocator;
import com.google.firebase.messaging.FirebaseMessaging;

public final class FcmTokenManager {

    private FcmTokenManager() {
    }

    public static void registerTokenForCurrentUser() {
        String userId = ServiceLocator.getSessionManager().getUserIdOnce();
        if (userId == null || userId.isBlank()) return;

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (token == null || token.isBlank()) return;
            ServiceLocator.getProfileRepository().updateFcmToken(userId, token, new RepoCallback<>() {
                @Override
                public void onSuccess(Void result) {
                    // Token saved silently; no UI feedback needed.
                }

                @Override
                public void onError(Exception error) {
                    // Non-critical: notifications will simply not be targetable until the next successful sync.
                }
            });
        });
    }
}
