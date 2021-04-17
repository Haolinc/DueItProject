package com.example.duelt;

import android.content.Context;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

import java.util.Collections;
import java.util.List;

public class WeeklyScheduleModel {
    private String eventName, color, weekDay, startTime, endTime;
    private int id, startPosition, endPosition;

    public WeeklyScheduleModel(){
        this.eventName = "Error";
        this.color = "Error";
        this.weekDay = "Error";
        this.id = -1;
        this.startTime = "Error";
        this.endTime = "Error";
        this.startPosition = -1;
        this.endPosition = -1;
    }

    //Constructor for activity usage
    public WeeklyScheduleModel(String eventName, String color, String weekDay, String startTime, String endTime, int startPosition, int endPosition, Context context){
        this.eventName = eventName;
        this.color = color;
        this.weekDay = weekDay;
        this.id = autoAssignID(context);
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this. endPosition = endPosition;
    }

    //Constructor for database usage
    public WeeklyScheduleModel(String eventName, String color, String weekDay, String startTime, String endTime, int startPosition, int endPosition, int id){
        this.eventName = eventName;
        this.color = color;
        this.weekDay = weekDay;
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPosition = startPosition;
        this. endPosition = endPosition;
    }

    private int autoAssignID(Context ctx){
        DatabaseHelper databaseHelper = new DatabaseHelper(ctx);
        List<Integer> idFromDatabase = databaseHelper.getIDFromWeeklySchedule();
        if (idFromDatabase.size() == 0) return 1;
        Collections.sort(idFromDatabase);
        return idFromDatabase.get(idFromDatabase.size()-1)+1;
    }

    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
}
