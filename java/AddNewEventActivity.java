package com.yunuskaratepe.semester_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int clickCount;
    private EditText eventName, eventDetails, eventAddress;
    private CalendarView calendarView;
    private Button dateSelectionButton, endAddingRecallDateButton;
    private MyDate myDate = null;
    private Spinner recallTypeSpinner;

    static String alertString = "";

    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        eventName = findViewById(R.id.eventNameText);
        eventDetails = findViewById(R.id.eventDetailsText);
        eventAddress = findViewById(R.id.eventAddressText);
        calendarView = findViewById(R.id.dateCalendarView);
        dateSelectionButton = findViewById(R.id.dateSelectionButton);
        endAddingRecallDateButton = findViewById(R.id.endAddingRecallDateButton);
        recallTypeSpinner = findViewById(R.id.eventRecallTypeSpinner);
        event = new Event();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recallType,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recallTypeSpinner.setAdapter(adapter);
        recallTypeSpinner.setOnItemSelectedListener(this);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                myDate = new MyDate(dayOfMonth, month + 1, year);
                System.out.println("(AddNewEventActivity) date: " + myDate);
            }
        });

        clickCount = 0;
    }

    public void dateSelection (View view) {
        if (event.getStartingDate() == null) {
            if (myDate != null) {
                event.setStartingDate(myDate);
                event.addRecallDate(event.getStartingDate());
                dateSelectionButton.setText("Select Event Ending Date");
                Toast.makeText(getApplicationContext(), "Event starting date selected.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please first select a date.", Toast.LENGTH_SHORT).show();
            }
            myDate = null;
            return;
        }
        if (event.getEndingDate() == null) {
            if (myDate != null) {
                event.setEndingDate(myDate);
                dateSelectionButton.setText("Add Event Recall Date");
                endAddingRecallDateButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Event ending date selected.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), "Please first select a date.", Toast.LENGTH_SHORT).show();
            }
            myDate = null;
            return;
        }
        if (myDate != null) {
            if(event.addRecallDate(myDate)){
                Toast.makeText(getApplicationContext(), "Event recall date added.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "This date already added before.", Toast.LENGTH_SHORT).show();
            }
            myDate = null;
        }
        else {
            Toast.makeText(getApplicationContext(), "Please first select a date.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel (View view) {
        finish();
    }

    public void next (View view) {

        String name = eventName.getText().toString();
        String details = eventDetails.getText().toString();
        if (name.matches("")){
            Toast.makeText(getApplicationContext(), "Event name can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (event.getPeriod() == -1){
            Toast.makeText(getApplicationContext(), "Please select a recall type.", Toast.LENGTH_SHORT).show();
            return;
        }
        event.setName(name);
        event.setDetails(details);

        findViewById(R.id.constraintLayout1).setVisibility(View.INVISIBLE);
        findViewById(R.id.constraintLayout2).setVisibility(View.VISIBLE);
    }

    public void next2 (View view) {
        findViewById(R.id.constraintLayout2).setVisibility(View.INVISIBLE);
        findViewById(R.id.constraintLayout3).setVisibility(View.VISIBLE);
    }

    public void addNewAddress (View view) {
        if (event.addAddress(eventAddress.getText().toString())){
            Toast.makeText(getApplicationContext(), "Address succesfully added.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Address already added before.", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveEvent (View view) {
        DatabaseHelper helper = DatabaseHelper.objectCreation();
        helper.insert2Database(event, this);

        startIntervalAlarm();

        Toast.makeText(getApplicationContext(), "Event successfully added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("(AddNewEventActivity) selected period index: " + position);
        event.setPeriod(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void startIntervalAlarm () {

        DatabaseHelper helper = DatabaseHelper.objectCreation();
        Settings settings = helper.getSettings(AddNewEventActivity.this);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, settings.getRecallHour());
        calendar.set(Calendar.MINUTE, settings.getRecallMin());
        System.out.println("Our date: " + calendar);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);


        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY, calendar.getTimeInMillis(), pendingIntent);
    }




}
