package com.yunuskaratepe.semester_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class ShowEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText nameText, detailsText, addressesText;
    private TextView startingDateTextView, endingDateTextView, changeDatesTextView;
    private Button showEventChangeDateButton;
    private CalendarView calendarView1, calendarView2;
    Spinner recallTypeSpinner;
    private Event event;
    private MyDate date, newRecallDate;
    private int recallDateIndex;
    private ConstraintLayout constraintLayout1, constraintLayout2, constraintLayout3;
    private RelativeLayout relativeLayout;
    private ListView recallDatesListView;
    private String[] recallDatesStringArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        recallTypeSpinner = findViewById(R.id.showEventRecallTypeSpinner);
        nameText = findViewById(R.id.showEventNameText);
        detailsText = findViewById(R.id.showEventDetailText);
        addressesText = findViewById(R.id.showEventAddressesText);
        startingDateTextView = findViewById(R.id.showEventStartingDateTextView);
        endingDateTextView = findViewById(R.id.showEventEndingDateTextView);
        changeDatesTextView = findViewById(R.id.showEventChangeDatesTextView);
        showEventChangeDateButton = findViewById(R.id.showEventChangeDateButton);
        calendarView1 = findViewById(R.id.showEventCalendarView1);
        calendarView2 = findViewById(R.id.showEventAddNewRecallDateCalendarView);
        constraintLayout1 = findViewById(R.id.constraintLayout1_1);
        constraintLayout2 = findViewById(R.id.constraintLayout1_2);
        constraintLayout3 = findViewById(R.id.constraintLayout1_3);
        relativeLayout = findViewById(R.id.showEventRecallDatesRelativeLayout);


        event = ListEventsActivity.selectedEvent;
        recallDateIndex = -1;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recallType,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recallTypeSpinner.setAdapter(adapter);
        recallTypeSpinner.setOnItemSelectedListener(this);
        recallTypeSpinner.setSelection(event.getPeriod());

        nameText.setText(event.getName());
        detailsText.setText(event.getDetails());
        String format = "";
        for (String s: event.getAddresses()){
            format += s + ";";
        }
        if (!format.matches("")) {
            format = format.substring(0, format.length() - 1);
        }
        addressesText.setText(format);

        startingDateTextView.setText("Starting: " + event.getStartingDate().toString());
        endingDateTextView.setText("Ending: " + event.getEndingDate().toString());

        calendarView1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = new MyDate(dayOfMonth, month + 1, year);
                System.out.println("Date: " + date);
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        event.setPeriod(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void changeDates1 (View view) {
        constraintLayout1.setVisibility(View.INVISIBLE);
        constraintLayout2.setVisibility(View.VISIBLE);
        changeDatesTextView.setText("Starting: " + event.getStartingDate().toString());

        showEventChangeDateButton.setText("Change Starting Date");
        showEventChangeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date != null) {
                    event.setStartingDate(date);
                    startingDateTextView.setText("Starting: " + date.toString());
                    date = null;
                    Toast.makeText(ShowEventActivity.this, "Starting date updated", Toast.LENGTH_SHORT).show();
                    constraintLayout1.setVisibility(View.VISIBLE);
                    constraintLayout2.setVisibility(View.INVISIBLE);
                }else {
                    Toast.makeText(ShowEventActivity.this, "First select a date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changeDates2 (View view) {
        constraintLayout1.setVisibility(View.INVISIBLE);
        constraintLayout2.setVisibility(View.VISIBLE);
        changeDatesTextView.setText("Ending: " + event.getEndingDate().toString());

        showEventChangeDateButton.setText("Change Ending Date");
        showEventChangeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date != null) {
                    event.setEndingDate(date);
                    endingDateTextView.setText("Ending: " + date.toString());
                    date = null;
                    Toast.makeText(ShowEventActivity.this, "Ending date updated", Toast.LENGTH_SHORT).show();
                    constraintLayout1.setVisibility(View.VISIBLE);
                    constraintLayout2.setVisibility(View.INVISIBLE);
                }else {
                    Toast.makeText(ShowEventActivity.this, "First select a date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void cancelUpdatingDate (View view) {
        date = null;
        constraintLayout1.setVisibility(View.VISIBLE);
        constraintLayout2.setVisibility(View.INVISIBLE);
    }

    public void manageRecallDates (View view) {

        constraintLayout1.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);

        recallDatesStringArray = new String[event.getRecallDates().size()];
        int index = 0;
        for (MyDate d: event.getRecallDates()) {
            recallDatesStringArray [index] = d.toString();
            index++;
        }
        recallDatesListView = findViewById(R.id.showEventRecallDatesListView);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowEventActivity.this, android.R.layout.simple_list_item_1
        , android.R.id.text1, recallDatesStringArray);
        recallDatesListView.setAdapter(arrayAdapter);

        recallDatesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.BLUE);
                recallDateIndex = position;
                for (int i = 0; i < recallDatesListView.getChildCount(); i++) {
                    recallDatesListView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                recallDatesListView.getChildAt(position).setBackgroundColor(Color.GRAY);
            }
        });

    }

    public void addRecallDate (View view) {
        constraintLayout3.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        recallDateIndex = -1;

        calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                newRecallDate = new MyDate(dayOfMonth, month + 1, year);
            }
        });

    }

    public void addNewRecallDate (View view) {
        if (newRecallDate == null) {
            Toast.makeText(ShowEventActivity.this, "First select a recall date", Toast.LENGTH_SHORT).show();
        }
        else {
            event.addRecallDate(newRecallDate);
            Toast.makeText(ShowEventActivity.this, "New recall date added", Toast.LENGTH_SHORT).show();
            recallDatesStringArray = new String[event.getRecallDates().size()];
            int index = 0;
            for (MyDate d: event.getRecallDates()) {
                recallDatesStringArray [index] = d.toString();
                index++;
            }
            recallDatesListView = findViewById(R.id.showEventRecallDatesListView);


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowEventActivity.this, android.R.layout.simple_list_item_1
                    , android.R.id.text1, recallDatesStringArray);
            recallDatesListView.setAdapter(arrayAdapter);
        }
    }

    public void backFromAddingNewRecallDate (View view) {
        constraintLayout3.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        newRecallDate = null;
    }

    public void removeSelectedRecallDate (View view) {
        if (recallDateIndex == -1) {
            Toast.makeText(ShowEventActivity.this, "First select a recall date", Toast.LENGTH_SHORT).show();
        }
        else {
            event.removeRecallDate(recallDateIndex);
            recallDatesStringArray = new String[event.getRecallDates().size()];
            int index = 0;
            for (MyDate d: event.getRecallDates()) {
                recallDatesStringArray [index] = d.toString();
                index++;
            }
            recallDatesListView = findViewById(R.id.showEventRecallDatesListView);


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShowEventActivity.this, android.R.layout.simple_list_item_1
                    , android.R.id.text1, recallDatesStringArray);
            recallDatesListView.setAdapter(arrayAdapter);
            recallDateIndex = -1;
        }
    }

    public void backFromRecallDates (View view) {
        constraintLayout1.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
    }


    public void updateEvent (View view) {

        DatabaseHelper helper = DatabaseHelper.objectCreation();
        event.setName(nameText.getText().toString());
        event.setDetails(detailsText.getText().toString());
        String[] addresses = addressesText.getText().toString().split(";");
        event.setAddresses(new ArrayList<>(Arrays.asList(addresses)));
        helper.updateEvent(event, this);
        startIntervalAlarm();
        Toast.makeText(getApplicationContext(), "Event updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void startIntervalAlarm () {

        DatabaseHelper helper = DatabaseHelper.objectCreation();
        Settings settings = helper.getSettings(ShowEventActivity.this);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, settings.getRecallHour());
        calendar.set(Calendar.MINUTE, settings.getRecallMin());
        System.out.println("Our date: " + calendar);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);


        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.INTERVAL_DAY, calendar.getTimeInMillis(), pendingIntent);
    }

    public void shareEvent (View view) {
        String format = "Hey, Join me!";
        format += "\nEvent Name: " + event.getName();
        format += "\nDetails: " + event.getDetails();
        format += "\nStarting at: " + event.startingDate;
        format += "\nAddresses: " + event.getAddresses();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, format);
        intent.setType("text/plain");
        startActivity(intent);
    }



}
