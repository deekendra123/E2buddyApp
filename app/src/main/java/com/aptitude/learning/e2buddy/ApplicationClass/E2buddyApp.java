package com.aptitude.learning.e2buddy.ApplicationClass;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class E2buddyApp extends Application {

    public static E2buddyApp instance;
    public static final String CHANNEL_ID = "E2buddyApp";
    public static final String CHANNEL_ID1 = "E2buddyAppNotice";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        createNotificationChannel1();

        instance = this;
    }

    public static E2buddyApp getInstance(){
        return instance;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "E2buddyApp",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            final NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void createNotificationChannel1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID1,
                    "E2buddyAppNotice",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            final NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
