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
    private int timeForOrder, ID, waked;

    public EventDateModel() {
        this.eventTitle = "ERROR";
        this.eventDetail = "ERROR";
        this.year = -1;
        this.month = -1;
        this.day = -1;
        this.hour = -1;
        this.minute = -1;
        timeForOrder = -1;
        this.ID = -1;
        waked = -1;
    }

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
        waked = 0;
    }

    public EventDateModel(String eventTitle, String eventDetail, int year, int month, int day, int hour, int minute, int id, int waked) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.year = year;
        this.month = month ;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        timeForOrder =   minute + hour*100 + day*10000 + month * 1000000 + year*100000000;
        this.ID = id;
        this.waked = waked;
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
        waked = 0;
    }

    //get system date for daily
    public EventDateModel(int hour, int minute) {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.hour = hour;
        this.minute = minute;
        this.eventTitle = "";
        this.eventDetail = "";
        this.timeForOrder = hour*100 + minute;
        this.ID = -1;
        waked = 0;
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
        waked = 0;
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

    public int getWaked() { return waked;}

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

    public void setWaked(int waked) {this.waked = waked; }


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

    public boolean isLessThanInTime(EventDateModel edm){
        if(this.year < edm.year)return true;
        else if(this.month < edm.month) return true;
        else if(this.day < edm.day) return true;
        else if(this.hour < edm.hour) return true;
        else if(this.minute <= edm.minute) return true;
        else return false;
    }

    public int minusInDay(EventDateModel edm){
        Date endDay = new Date(edm.year,edm.month,edm.day);
        Date startDay = new Date(this.year,this.month,this.day);
        long diffInMillSecond = startDay.getTime() - endDay.getTime();
        long lDay = diffInMillSecond/86400000;
        int day = (int)lDay;
        return day;
    }
}
