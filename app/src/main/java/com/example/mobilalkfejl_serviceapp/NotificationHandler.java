package com.example.mobilalkfejl_serviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "Service_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private Context context;
    private NotificationManager notificationManager;

    public NotificationHandler(Context context){
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();

    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Service notification",
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Ertesites a szerviz alkalmazasbol!");
        this.notificationManager.createNotificationChannel(channel);


    }

    public void send(String message){
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(!notificationManager.areNotificationsEnabled()){
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Service application")
                .setContentText(message)
                .setSmallIcon(R.drawable.small_wrench);
        this.notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
