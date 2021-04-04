package com.example.duelt;

import android.content.Context;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventDateModel {
    private String eventTitle;
    private String eventDetail;
    private int year, month, day, hour, minute;
    private int timeForOrder, ID;

    public EventDateModel(String eventTitle, String eventDetail, int year, int month, int day, int hour, int minute, Context ctx) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.year = year;
        this.month = month ;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        timeForOrder =   minute + hour*100 + day*10000 + month * 1000000 + year*100000000;
        this.ID = autoAssignID(ctx);
    }

    public EventDateModel(String eventTitle, String eventDetail, int year, int month, int day, int hour, int minute, int id) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.year = year;
        this.month = month ;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        timeForOrder =   minute + hour*100 + day*10000 + month * 1000000 + year*100000000;
        this.ID = id;
    }

    //get system date
    public EventDateModel(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.eventTitle = "";
        this.eventDetail = "";
        timeForOrder =   minute + hour*100 + day*10000 + month * 1000000 + year*100000000;
        this.ID = -1;
    }

    //constructor for daily routine
    public EventDateModel(String eventTitle, int hour, int minute, Context ctx) {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.eventDetail = "";
        this.eventTitle = eventTitle;
        this.hour= hour;
        this.minute=minute;
        this.timeForOrder = hour*100 + minute;
        this.ID = autoAssignID(ctx);
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public int getTimeForOrder() {
        return timeForOrder;
    }

    public int getID(){
        return ID;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


    private int autoAssignID(Context ctx){
        DatabaseHelper databaseHelper = new DatabaseHelper(ctx);
        List<Integer> idFromDatabase= databaseHelper.getIDFromDatabase();
        Collections.sort(idFromDatabase);
        for (int i=0;i<idFromDatabase.size();i++){
            if (i != idFromDatabase.get(i)){
                idFromDatabase.add(i);
                return i;
            }
        }
        return idFromDatabase.size();
    }
    public String toStringTimeOnly() {
        int correctedMonth = month +1 ;
        return
                year + "/" + correctedMonth +"/"+ day + "  "+ hour + ": " + minute +"\n";
    }

    public String getTitleAndDate() {
        int correctedMonth = month +1 ;
        return eventTitle+ " " + correctedMonth  + "/" + day + "  " + hour + ":" + minute;
    }

    public String getDailyRoutineString(){
        return eventTitle + " " + hour + ":" + minute;
    }

    public boolean isEqualInTime(EventDateModel edm){
        return this.year == edm.year
                && this.month == edm.month
                && this.day == edm.day
                && this.hour == edm.hour
                && this.minute == edm.minute;
    }
}
