package com.aptitude.learning.e2buddy.PushNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.aptitude.learning.e2buddy.AppConfig.CheckInternet;
import com.aptitude.learning.e2buddy.R;
import com.aptitude.learning.e2buddy.School.Student.ActivityClass.StudHomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import static com.aptitude.learning.e2buddy.ApplicationClass.E2buddyApp.CHANNEL_ID;
import static com.aptitude.learning.e2buddy.ApplicationClass.E2buddyApp.CHANNEL_ID1;
import static com.aptitude.learning.e2buddy.ApplicationClass.E2buddyApp.getInstance;

public class FirebaseMessagingSerivce extends FirebaseMessagingService {
    private NotificationTarget notificationTarget;
    private CheckInternet checkInternet;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        checkInternet = new CheckInternet(getApplicationContext());

        if (checkInternet.isOnline()) {

            String type = remoteMessage.getData().get("type");

            Log.e("tpe", type);

            if (type.equals("notice")){
                String title = remoteMessage.getData().get("title");
                String schoolName = remoteMessage.getData().get("body");

                Log.e("tpe", schoolName);

                RemoteViews notificationLayoutExpanded = new RemoteViews(getInstance().getPackageName(), R.layout.notice_custom_notification);
                notificationLayoutExpanded.setTextViewText(R.id.tvSchoolName, schoolName);
                notificationLayoutExpanded.setImageViewResource(R.id.imageBanner1, R.drawable.notice);
                String text = "Urgent Notice from "+ schoolName;
                notificationLayoutExpanded.setTextViewText(R.id.tvClassDetails, text);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getInstance(), CHANNEL_ID1);
                builder.setCustomBigContentView(notificationLayoutExpanded);
                builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
                builder.setContentTitle(schoolName);
                builder.setContentTitle("Urgent Information. Click to view Detail");
                builder.setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

                Intent intent = new Intent(getApplicationContext(), StudHomeActivity.class);
                intent.putExtra("menuFragment", "noticeFragment");

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                PendingIntent pendingIntent = PendingIntent.getActivity(getInstance(),
                        0,
                        intent
                        , PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                final NotificationManager notificationManager = (NotificationManager) getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(123, builder.build());

            }
            else {

                String title = remoteMessage.getData().get("title");
                String schoolName = remoteMessage.getData().get("body");
                String schoolLogo = remoteMessage.getData().get("schoolLogo");
                String subjectName = remoteMessage.getData().get("subjectName") + " Test is Live";
                String classDetails = remoteMessage.getData().get("classDetails");

                Log.e("notification_data", schoolName + "  " + schoolLogo);
                //   String click_action =remoteMessage.getData().get;

                RemoteViews notificationLayoutExpanded = new RemoteViews(getInstance().getPackageName(), R.layout.custom_notification);

                notificationLayoutExpanded.setTextViewText(R.id.tvSchoolName, schoolName);
                notificationLayoutExpanded.setTextViewText(R.id.tvSubjectName, subjectName);
                notificationLayoutExpanded.setTextViewText(R.id.tvClassDetails, classDetails);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getInstance(), CHANNEL_ID);
                builder.setCustomBigContentView(notificationLayoutExpanded);
                builder.setSmallIcon(R.drawable.logo_e2);
                builder.setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

                Intent intent = new Intent(getApplicationContext(), StudHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(getInstance(),
                        0,
                        intent
                        , PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                final NotificationManager notificationManager = (NotificationManager) getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(123, builder.build());

                notificationTarget = new NotificationTarget(
                        getApplicationContext(),
                        R.id.imgSchoolLogo,
                        notificationLayoutExpanded,
                        builder.build(),
                        123);

                Glide
                        .with(getApplicationContext())
                        .asBitmap()
                        .load(schoolLogo)
                        .into(notificationTarget);
            }
        }

    }

}
