package com.example.gj94g.b10509034_hw4.urilite;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.gj94g.b10509034_hw4.MainActivity;

public class MyNotification {

    private static final int NOTIFICATION_ID = 6547;
    private static final int NOTIFICATION_INTENT_ID = 6877;
    private static final String NOTIFICATION_CHANNEL_NAME = "channel";

    public static void sendNotification(Context context, String title, String body){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_NAME)
                .setContentTitle(title)
                .setSmallIcon(android.support.v4.R.drawable.notification_icon_background)
                .setContentText("text")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                NOTIFICATION_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
