package com.yunuskaratepe.semester_project;

import android.media.RingtoneManager;
import android.net.Uri;

public class Settings {

    private int recallHour, recallMin;
    private String vibration; // true=lightTheme, false=darkTheme
    private String ringtoneUri;

    public Settings () {
        recallHour = 10;
        recallMin = 0;
        vibration = "yes";
        ringtoneUri = "default";
    }

    public Settings (int recallHour, int recallMin, String vibration, String ringtoneUri) {
        this.recallHour = recallHour;
        this.recallMin = recallMin;
        this.vibration = vibration;
        this.ringtoneUri = ringtoneUri;
    }



    public int getRecallHour() {
        return recallHour;
    }

    public void setRecallHour(int recallHour) {
        this.recallHour = recallHour;
    }

    public int getRecallMin() {
        return recallMin;
    }

    public void setRecallMin(int recallMin) {
        this.recallMin = recallMin;
    }

    public String getVibration() {
        return vibration;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }
}
