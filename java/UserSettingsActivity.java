package com.yunuskaratepe.semester_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;

public class UserSettingsActivity extends AppCompatActivity {

    Settings settings;
    EditText recallTimeText;
    RadioGroup vibrationRadioGroup;
    TextView ringtoneTextView;
    DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        recallTimeText = findViewById(R.id.userSettingsRecallTimeText);
        vibrationRadioGroup = findViewById(R.id.vibrationRadioGroup);
        ringtoneTextView = findViewById(R.id.userSettingsRingtoneTextView);


        helper = DatabaseHelper.objectCreation();
        settings = helper.getSettings(this);

        recallTimeText.setText(settings.getRecallHour() + ":" + settings.getRecallMin());
        if (settings.getVibration().toLowerCase().matches("yes")) {
            vibrationRadioGroup.check(R.id.yesVibrationRadioButton);
        }else {
            vibrationRadioGroup.check(R.id.noVibrationRadioButton);
        }
        ringtoneTextView.setText(settings.getRingtoneUri());

    }


    public void updateSettings (View view) {

        String[] recallTimes = (recallTimeText.getText().toString()).split(":");

        try {
            int hour = Integer.parseInt(recallTimes[0]);
            int min = Integer.parseInt(recallTimes[1]);
            System.out.println(Arrays.toString(recallTimes));
            if (recallTimes.length == 2 & (hour < 24 && hour >= 0) && (min < 60 && min >= 0)) {
                settings.setRecallHour(hour);
                settings.setRecallMin(min);
                if (vibrationRadioGroup.getCheckedRadioButtonId() == R.id.yesVibrationRadioButton){
                    settings.setVibration("yes");
                }else {
                    settings.setVibration("no");
                }
                settings.setRingtoneUri(ringtoneTextView.getText().toString());
                helper.updateSettings(settings, UserSettingsActivity.this);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, settings.getRecallHour());
                calendar.set(Calendar.MINUTE, settings.getRecallMin());
                System.out.println("Our date: " + calendar);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(getApplicationContext(), "Settings updated", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Recall time should be this format (hour:min)", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Recall time should be this format (hour:min)", Toast.LENGTH_SHORT).show();
            return;
        }

    }



}
