package com.yunuskaratepe.semester_project;

public class MyDate {

    private int day;
    private int month;
    private int year;

    public MyDate(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public MyDate(String format){
        String[] array = format.split("/");
        day = Integer.parseInt(array[0]);
        month = Integer.parseInt(array[1]);
        year = Integer.parseInt(array[2]);
    }

    public MyDate string2MyDate(String string){
        String[] array = string.split("/");
        return new MyDate(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        String format = "";
        if (day < 10) {
            format += "0" + day + "/";
        }else {
            format += day + "/";
        }
        if (month < 10) {
            format += "0" + month + "/";
        }else {
            format += month + "/";
        }
        format += year;
        return format;
    }
}

