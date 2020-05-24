package com.yunuskaratepe.semester_project;

import java.util.ArrayList;
import java.util.Collections;

public class Array2String {

    private static Array2String instance = null;

    private Array2String () {

    }

    public static Array2String objectCreation() {
        if (instance == null){
            instance = new Array2String();
        }
        return instance;
    }

    public String dateList2String(ArrayList<MyDate> dates){
        String string = "";
        if(!dates.isEmpty()){
            for (MyDate date: dates){
                string += date.toString() + ",";
            }
        }
        if(!string.equals("")){
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    public ArrayList<MyDate> string2DateList (String dates) {

        ArrayList<MyDate> dateList = new ArrayList<>();

        if (dates.matches("")){
            return dateList;
        }
        String[] tmpArray = dates.split(",");

        for (String s : tmpArray) {
            dateList.add(new MyDate(s));
        }
        return dateList;
    }


    public String addressList2String (ArrayList<String> addresses){
        String string = "";
        if (!addresses.isEmpty()){
            for (String address: addresses){
                string += address + "&";
            }
        }
        if(!string.equals("")){
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    public ArrayList<String> string2AddressList (String addresses) {

        ArrayList<String> addressList = new ArrayList<>();
        if(addresses.matches("")){
            return addressList;
        }

        String[] tmpArray = addresses.split("&");
        Collections.addAll(addressList, tmpArray);
        return addressList;
    }



}
