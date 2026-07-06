package com.example.purepawapp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.purepawapp.R;

public final class NotificationHelper {

    public static final String CHANNEL_ID = "purepaw_updates";
    private static final String CHANNEL_NAME = "Order & Booking Updates";

    private NotificationHelper() {
    }

    public static void ensureChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Notifications about your order and spa booking status");
        manager.createNotificationChannel(channel);
    }

    public static void showNotification(Context context, String title, String body, int notificationId) {
        ensureChannel(context);
        android.app.Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        NotificationManagerCompat.from(context).notify(notificationId, notification);
    }
}
