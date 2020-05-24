package com.yunuskaratepe.semester_project;

import android.content.Context;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Calendar;

public class Event {
    int primaryKey; // this helps us deleting from database.
    String name;
    MyDate startingDate;
    MyDate endingDate;
    int period; // 0:every day - 1:every week - 2:every month - 3:every year
    String details;
    ArrayList<MyDate> recallDates;
    ArrayList<String> addresses;

    public Event (String name, MyDate startingDate, MyDate endingDate, int period, String details) {
        this.name = name;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.period = period;
        this.details = details;
        recallDates = new ArrayList<MyDate>();
        addresses = new ArrayList<String>();
    }

    public Event () {
        recallDates = new ArrayList<MyDate>();
        addresses = new ArrayList<String>();
        period = -1;
    }

    public boolean addRecallDate(MyDate date){
        for (MyDate d: recallDates){
            if (d.toString().matches(date.toString())){
                return false;
            }
        }
        recallDates.add(date);
        return true;
    }

    public void removeRecallDate (int index) {
        recallDates.remove(index);
    }

    public boolean addAddress(String address){
        for (String a: addresses){
            if (a.matches(address)){
                return false;
            }
        }
        addresses.add(address);
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public MyDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(MyDate startingDate) {
        this.startingDate = startingDate;
    }

    public MyDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(MyDate endingDate) {
        this.endingDate = endingDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public ArrayList<MyDate> getRecallDates() {
        return recallDates;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public void setRecallDates(ArrayList<MyDate> recallDates) {
        this.recallDates = recallDates;
    }

    public void setAddresses(ArrayList<String> addresses) {
        this.addresses = addresses;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean hasRecallDateToday (Context context) {
        Calendar calendar = Calendar.getInstance();
        int todayDay = (calendar.get(Calendar.DAY_OF_MONTH));
        int todayMonth = (calendar.get(Calendar.MONTH)) + 1;
        int todayYear = (calendar.get(Calendar.YEAR));
        int todayHour = (calendar.get(Calendar.HOUR_OF_DAY));
        int todayMin = (calendar.get(Calendar.MINUTE));
        int index = 0;
        DatabaseHelper helper = DatabaseHelper.objectCreation();
        Settings settings = helper.getSettings(context);
        for (MyDate d: recallDates) {
            if (todayDay == d.getDay() && todayMonth == d.getMonth() && todayYear == d.getYear() &&
                    ((todayHour == settings.getRecallHour() && todayMin >= settings.getRecallMin())
                            || (todayHour > settings.getRecallHour()))) {
                recallDates.remove(index);
                helper.updateEvent(this, context);
                return true;
            }
            index++;
        }
        System.out.println("Returned false for: " + this.getName());
        return false;
    }
}
