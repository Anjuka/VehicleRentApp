package com.ahn.vehiclerentapp.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.constant.Constants;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FCMService extends FirebaseMessagingService {

    PendingIntent pendingIntent;
    Class notification_class;
    String notification_title = "";
    String notification_body = "";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
       /* Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("token", token);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("deviceTokens").document().set(tokenData);*/

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);


        int notificationId = new Random().nextInt();
        String channelId = "chat_message";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

        switch (message.getData().get(Constants.KEY_MESSAGE)) {
            case "new_post":
                notification_class = DriverDashoardActivity.class;
                notification_title = "New Post";
                notification_body = "You have a new tour to bid...";
                notifyTheUser(notificationId, channelId, message);
                break;

            case "bid":
                notification_class = HostDashoardActivity.class;
                notification_title = "New Bid";
                notification_body = "You have a new bid...";
                notifyTheUser(notificationId, channelId, message);
                break;

            case "bid_approve":
                notification_class = DriverDashoardActivity.class;
                notification_title = "Bid Approved";
                notification_body = "Your bid is approved...";
                notifyTheUser(notificationId, channelId, message);
                break;

            case "bid_cancel":
                notification_class = DriverDashoardActivity.class;
                notification_title = "Bid Canceled";
                notification_body = "Your bid is cancelled...";
                notifyTheUser(notificationId, channelId, message);
                break;

            case "job_completed":
                notification_class = HostDashoardActivity.class;
                notification_title = "Job Completion";
                notification_body = "Job Completion Notified";
                notifyTheUser(notificationId, channelId, message);
                break;
        }

    }

    private void notifyTheUser(int notificationId, String channelId, RemoteMessage message) {
        Intent intent = new Intent(this, notification_class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(notification_title);
        builder.setContentText(notification_body);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notification_body));
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Chat Message";
            String ChannelDescription = "This notification channel is used for message notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(ChannelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationId, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel("chat_message", "Chat Message", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("this is the description of the channel.");
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
    }
}
