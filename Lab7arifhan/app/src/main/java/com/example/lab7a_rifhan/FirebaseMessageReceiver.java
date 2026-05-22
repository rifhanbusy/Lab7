package com.example.lab7a_rifhan;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageReceiver"; // Tag for logging
    private static final String CHANNEL_ID = "alarm_notification_channel_final"; // Notification channel ID

    @Override
    public void onNewToken(String token) {
        // Called when FCM issues a new token for this device/app
        Log.d(TAG, "FCM Token: " + token); // Log the token for debugging
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Called when a message is received from FCM
        String title = "Notification"; // Default title
        String message = "You have a new message!"; // Default message

        // Check if the message has a notification payload
        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getTitle() != null)
                title = remoteMessage.getNotification().getTitle(); // Override title if present
            if (remoteMessage.getNotification().getBody() != null)
                message = remoteMessage.getNotification().getBody(); // Override message if present
        }

        // Check if the message has a data payload
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get("title") != null)
                title = remoteMessage.getData().get("title"); // Override title from data
            if (remoteMessage.getData().get("body") != null)
                message = remoteMessage.getData().get("body"); // Override message from data
        }

        // Show the notification
        showNotification(title, message);
    }

    private void showNotification(String title, String message) {
        Context context = getApplicationContext();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // Get notification manager

        // Create notification channel for Android 8+ (required)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Notification Channel", // Channel name
                    NotificationManager.IMPORTANCE_HIGH // High importance → heads-up popup
            );
            channel.setDescription("Channel for FCM notifications"); // Channel description
            channel.enableLights(true); // Enable LED lights
            channel.setLightColor(Color.RED); // LED color
            channel.enableVibration(true); // Enable vibration
            channel.setVibrationPattern(new long[]{0, 500, 250, 500}); // Vibration pattern
            channel.setSound(
                    android.provider.Settings.System.DEFAULT_NOTIFICATION_URI, // Default notification sound
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
            );
            notificationManager.createNotificationChannel(channel); // Create the channel
        }

        // Vibrate device briefly (500ms)
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500);
        }

        // Create an intent to open MainActivity when notification is tapped
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear activity stack
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) // Notification icon
                .setContentTitle(title) // Notification title
                .setContentText(message) // Notification body
                .setAutoCancel(true) // Dismiss notification when tapped
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Priority high → heads-up popup
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Use default sound, vibration, lights
                .setContentIntent(pendingIntent) // Action when tapped
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // Show on lock screen

        // Show the notification (unique ID using timestamp)
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}