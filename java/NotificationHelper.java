package com.yunuskaratepe.semester_project;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1ID = "channel1_id";
    public static final String channel1NAME = "channel1_name";

    private NotificationManager myManager;

    public NotificationHelper (Context context) {
        super (context);
        createChannels(context);
    }

    public void createChannels (Context context) {

        DatabaseHelper helper = DatabaseHelper.objectCreation();
        Settings settings = helper.getSettings(context);

        System.out.println("createChannels Came!");
        NotificationChannel channel1 = new NotificationChannel(channel1ID, channel1NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableVibration(settings.getVibration().toLowerCase().matches("yes"));
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager () {
        if (myManager == null) {
            myManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return myManager;
    }

    public NotificationCompat.Builder getChannel1Notification (String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_android_black_24dp);
    }

}
