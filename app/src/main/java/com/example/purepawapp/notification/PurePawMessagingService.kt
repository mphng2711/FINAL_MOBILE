package com.example.purepawapp.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class PurePawMessagingService : FirebaseMessagingService() {

    @Deprecated("Overrides FirebaseMessagingService.onNewToken, deprecated in the underlying Java SDK")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Token is fetched on-demand via FirebaseMessaging.getInstance().token where needed
        // (e.g. saved to the user's Firestore document after login).
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: message.data["title"] ?: return
        val body = message.notification?.body ?: message.data["body"].orEmpty()
        NotificationHelper.showNotification(applicationContext, title, body, Random.nextInt())
    }
}
