package com.example.duelt;

import android.content.Context;
import android.widget.Toast;

import androidx.core.os.UserManagerCompat;

import com.example.duelt.db.DatabaseHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class EventDateModel {
    private String eventTitle;
    private String eventDetail;
    private Calendar setDate, dueDate;
    private int year, month, day, hour, minute;
    private int timeForOrder, ID, waked;
    private int ID2, ID3, ID4;
    Calendar cal = Calendar.getInstance();
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

    //use in activity
    public EventDateModel(String eventTitle, String eventDetail, int year, int month, int day, int hour, int minute, Context ctx) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        cal.set(year, month, day, hour, minute);
        dueDate = cal;
        this.ID = autoAssignID(ctx);
        this.ID2 = ID + 1;
        this.ID3 = ID + 2;
        this.ID4 = ID + 3;
        waked = 0;
    }

    //use for duedate constructor in database
    public EventDateModel(String eventTitle, String eventDetail, long setDate, long dueDate, int id, int waked) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        cal.setTimeInMillis(setDate);
        this.setDate = cal;
        cal.setTimeInMillis(dueDate);
        this.dueDate = cal;
        timeForOrder =   minute + hour*100 + day*10000 + month * 1000000 + year*100000000;
        this.ID = id;
        this.ID2 = id + 1;
        this.ID3 = id + 2;
        this.ID4 = id + 3;
         this.waked = waked;
    }

    //use for daily constructor in dailyroutine
    public EventDateModel(String eventTitle, int hour, int minute, int id, int waked) {
        this.eventTitle = eventTitle;
        this.eventDetail = "";
        this.hour = hour;
        this.minute = minute;
        timeForOrder = hour*100 + minute;
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
        this.eventDetail = "";
        this.eventTitle = eventTitle;
        this.hour= hour;
        this.minute=minute;
        this.timeForOrder = hour*100 + minute;
        this.ID = autoAssignID(ctx);
        waked = 0;
    }

    public EventDateModel(Calendar calendar){
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR);
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

    public Calendar getSetDate() {return setDate;}

    public Calendar getDueDate() {return dueDate;}

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getWaked() { return waked;}

    public Calendar getDate() { return Calendar.getInstance();}

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

    public int getID2() {
        return ID2;
    }

    public int getID3() {
        return ID3;
    }

    public int getID4() {
        return ID4;
    }

    private int autoAssignID(Context ctx){
        DatabaseHelper databaseHelper = new DatabaseHelper(ctx);
        List<Integer> idFromDatabase = databaseHelper.getIDFromDueDate();
        idFromDatabase.addAll(databaseHelper.getIDFromDaily());
        if (idFromDatabase.size() == 0) return 0;
        Collections.sort(idFromDatabase);
        for (int i=0;i<idFromDatabase.size();i++){
            if (i != idFromDatabase.get(i)/4){
                Toast.makeText(ctx, Integer.toString(i*4), Toast.LENGTH_SHORT).show();
                return i*4;
            }
        }
        Toast.makeText(ctx, Integer.toString(idFromDatabase.size()*4), Toast.LENGTH_SHORT).show();
        return idFromDatabase.size()*4;
    }

    //for current date and time
    public String toStringTimeOnly() {
        int correctMonth = month+1;
        return year + "/" + correctMonth +"/"+ day + "  "+ hour + ": " + minute +"\n";
    }

    //for duedate only
    public String getTitleAndDate() {
        int correctedMonth = dueDate.get(Calendar.MONTH) +1 ;
        String s = eventTitle+ " " + correctedMonth  + "/" + dueDate.get(Calendar.DAY_OF_MONTH) + "  " + dueDate.get(Calendar.HOUR_OF_DAY) + ":" + dueDate.get(Calendar.MINUTE)
                + "\n" + "id: " + ID;
        return s;
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
        long diffInMillSecond = edm.dueDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        long lDay = diffInMillSecond/86400000;
        return (int)lDay;
    }
}
