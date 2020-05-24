package com.yunuskaratepe.semester_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.widget.ArrayAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class DatabaseHelper {

    private static DatabaseHelper helper = null;

    private DatabaseHelper () {

    }

    public static DatabaseHelper objectCreation () {
        if (helper == null) {
            helper = new DatabaseHelper();
        }
        return helper;
    }


    public void insert2Database(Event event, Context context){
        try {
            SQLiteDatabase database = context.openOrCreateDatabase("App_Data", context.MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS app_data (id INTEGER PRIMARY KEY, name VARCHAR, starting VARCHAR," +
                    " ending VARCHAR, period VARCHAR, details VARCHAR, recalls VARCHAR, addresses VARCHAR)");

            String sqlString = "INSERT INTO app_data (name, starting, ending, period, details, recalls, addresses) VALUES (?, ?, ?, ?, ?, ?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1, event.getName());
            sqLiteStatement.bindString(2, event.getStartingDate().toString());
            sqLiteStatement.bindString(3, event.getEndingDate().toString());
            sqLiteStatement.bindString(4, Integer.toString(event.getPeriod()));
            sqLiteStatement.bindString(5, event.getDetails());

            // recall dates are saving to database using json
            Array2String array2String = Array2String.objectCreation();
            sqLiteStatement.bindString(6, array2String.dateList2String(event.getRecallDates()));
            // addresses are saving to database using json
            sqLiteStatement.bindString(7, array2String.addressList2String(event.getAddresses()));
            sqLiteStatement.execute();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("!!! error in function: (insert2Database)");
        }
    }


    public ArrayList<Event> getData(Context context){

        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase database = context.openOrCreateDatabase("App_Data", context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS app_data (id INTEGER PRIMARY KEY, name VARCHAR, starting VARCHAR," +
                " ending VARCHAR, period VARCHAR, details VARCHAR, recalls VARCHAR, addresses VARCHAR)");
        Cursor cursor = database.rawQuery("SELECT * FROM app_data", null);

        int idIx = cursor.getColumnIndex("id");
        int nameIx = cursor.getColumnIndex("name");
        int startingIx = cursor.getColumnIndex("starting");
        int endingIx = cursor.getColumnIndex("ending");
        int detailsIx = cursor.getColumnIndex("details");
        int periodIx = cursor.getColumnIndex("period");
        int recallsIx = cursor.getColumnIndex("recalls");
        int addressesIx = cursor.getColumnIndex("addresses");

        while(cursor.moveToNext()){

            Event event = new Event(cursor.getString(nameIx), new MyDate(cursor.getString(startingIx)),
                    new MyDate(cursor.getString(endingIx)), Integer.parseInt(cursor.getString(periodIx)),
                    cursor.getString(detailsIx));

            event.setPrimaryKey(cursor.getInt(idIx));
            Array2String array2String = Array2String.objectCreation();
            ArrayList<MyDate> recallsList = array2String.string2DateList(cursor.getString(recallsIx));
            ArrayList<String> addressesList = array2String.string2AddressList(cursor.getString(addressesIx));
            event.setRecallDates(recallsList);
            event.setAddresses(addressesList);

            events.add(event);
        }
        cursor.close();
        return events;
    }

    public void updateEvent (Event event, Context context) {

        String key = Integer.toString(event.getPrimaryKey());
        Array2String array2String = Array2String.objectCreation();
        String recallDates = array2String.dateList2String(event.getRecallDates());
        String addresses = array2String.addressList2String(event.getAddresses());

        SQLiteDatabase database = context.openOrCreateDatabase("App_Data", context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS app_data (id INTEGER PRIMARY KEY, name VARCHAR, starting VARCHAR," +
                " ending VARCHAR, period VARCHAR, details VARCHAR, recalls VARCHAR, addresses VARCHAR)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", event.getName());
        contentValues.put("starting", event.getStartingDate().toString());
        contentValues.put("ending", event.getEndingDate().toString());
        contentValues.put("period", event.getPeriod());
        contentValues.put("details", event.getDetails());
        contentValues.put("recalls", recallDates);
        contentValues.put("addresses", addresses);


        database.update("app_data", contentValues, "id = ?", new String[] {key});
    }


    public void deleteEvent (Event event, Context context) {

        int key = event.getPrimaryKey();
        SQLiteDatabase database = context.openOrCreateDatabase("App_Data", context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS app_data (id INTEGER PRIMARY KEY, name VARCHAR, starting VARCHAR," +
                " ending VARCHAR, period VARCHAR, details VARCHAR, recalls VARCHAR, addresses VARCHAR)");

        database.delete("app_data", "id = ?", new String[] {Integer.toString(key)});
    }

    public void updateSettings (Settings settings, Context context) {

        SQLiteDatabase database = context.openOrCreateDatabase("App_Data", context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS settings (id INTEGER PRIMARY KEY, recallHour VARCHAR, recallMin VARCHAR, vibration VARCHAR, ringtoneUri VARCHAR)");

        Cursor cursor = database.rawQuery("SELECT count(*) FROM settings", null);
        cursor.moveToFirst();
        if (!(cursor.getInt(0) == 0)) {
            cursor = database.rawQuery("SELECT * FROM settings", null);
            int idIx = cursor.getColumnIndex("id");
            cursor.moveToNext();
            int id = cursor.getInt(idIx);
            cursor.close();
            System.out.println("id bu bak: " + id);

            ContentValues contentValues = new ContentValues();
            contentValues.put("recallHour", settings.getRecallHour());
            contentValues.put("recallMin", settings.getRecallMin());
            contentValues.put("vibration", settings.getVibration());
            contentValues.put("ringtoneUri", settings.getRingtoneUri());
            System.out.println("h:m " + settings.getRecallHour() + settings.getRecallMin());

            database.update("settings", contentValues, "id = ?", new String[] {Integer.toString(id)});
        }else {
            try {
                String sqlString = "INSERT INTO settings (recallHour, recallMin, vibration, ringtoneUri) VALUES (?, ?, ?, ?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1, Integer.toString(settings.getRecallHour()));
                sqLiteStatement.bindString(2, Integer.toString(settings.getRecallMin()));
                sqLiteStatement.bindString(3, settings.getVibration());
                sqLiteStatement.bindString(4, settings.getRingtoneUri());
                sqLiteStatement.execute();
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("!!! error in function: (insert2Database)");
            }
        }
    }

    public Settings getSettings (Context context) {

        SQLiteDatabase database = context.openOrCreateDatabase("App_Data", context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS settings (id INTEGER PRIMARY KEY, recallHour VARCHAR, recallMin VARCHAR, vibration VARCHAR, ringtoneUri VARCHAR)");

        Cursor cursor = database.rawQuery("SELECT count(*) FROM settings", null);
        cursor.moveToFirst();

        if (!(cursor.getInt (0) == 0)) {
            cursor = database.rawQuery("SELECT * FROM settings", null);
            System.out.println("True Returns.");
            int hourIx = cursor.getColumnIndex("recallHour");
            int minIx = cursor.getColumnIndex("recallMin");
            int vibIx = cursor.getColumnIndex("vibration");
            int ringtoneIx = cursor.getColumnIndex("ringtoneUri");
            cursor.moveToNext();
            Settings settings = new Settings(Integer.parseInt(cursor.getString(hourIx)), Integer.parseInt(cursor.getString(minIx)),
                    cursor.getString(vibIx), cursor.getString(ringtoneIx));
            System.out.println("Settings Time: " + settings.getRecallHour() + ":" + settings.getRecallMin());
            cursor.close();
            return settings;
        }

        System.out.println("False Returns.");
        return new Settings();
    }


}
