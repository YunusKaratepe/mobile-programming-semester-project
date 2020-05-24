package com.yunuskaratepe.semester_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListEventsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    static Event selectedEvent;
    private LinearLayoutManager layoutManager;
    private CustomAdapter customAdapter;
    ArrayList<Event> events;
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        selectedEvent = null;

        DatabaseHelper helper = DatabaseHelper.objectCreation();


        events = helper.getData(this);
        settings = helper.getSettings(this);

        System.out.println("(ListEvents) - events: " + events);

        recyclerView = findViewById(R.id.recyclerViewEventList);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        customAdapter = new CustomAdapter(events, this);
        recyclerView.setAdapter(customAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_list_events);

        DatabaseHelper helper = DatabaseHelper.objectCreation();
        selectedEvent = null;

        ArrayList<Event> events = helper.getData(this);

        recyclerView = findViewById(R.id.recyclerViewEventList);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        customAdapter = new CustomAdapter(events, this);
        recyclerView.setAdapter(customAdapter);
    }

    public void goAddNewEventActivity (View view) {
        Intent intent = new Intent(ListEventsActivity.this, AddNewEventActivity.class);
        startActivity(intent);
    }

    public void deleteSelectedEvent (View view) {

        if (selectedEvent == null) {
            Toast.makeText(getApplicationContext(), "Nothing is selected.", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseHelper helper = DatabaseHelper.objectCreation();
        System.out.println("(ListEventsActivity) event: " + selectedEvent);
        helper.deleteEvent(selectedEvent, this);
        events.remove(selectedEvent);
        selectedEvent = null;

        onResume();
    }

    public void showSelectedEvent (View view) {
        if (selectedEvent == null) {
            Toast.makeText(getApplicationContext(), "Nothing is selected.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(ListEventsActivity.this, ShowEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_settings, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.user_settings) {
            Intent intent = new Intent(ListEventsActivity.this, UserSettingsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
