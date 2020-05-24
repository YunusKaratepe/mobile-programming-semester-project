package com.yunuskaratepe.semester_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class AlertReceiver extends BroadcastReceiver {

    private ArrayList<Event> events;


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("AlertReceiver Came!");
        String alertString = "";

        DatabaseHelper helper = DatabaseHelper.objectCreation();
        events = helper.getData(context);
        int id = 1;
        for (Event e: events) {
            if (e.hasRecallDateToday(context)) { // if hasRecallDateToday returns true it deletes that recall date from recallDates list too.
                alertString = e.getName() + "\n" + e.getDetails() + "\nStarting at: " + e.getStartingDate();
                NotificationHelper notificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder builder = notificationHelper.getChannel1Notification("Remainder Alert!", alertString);
                notificationHelper.getManager().notify(id, builder.build());
                id++;
            }
        }


    }
}
