package com.example.duelt;

import java.util.Date;

public class EventDateModel {
    private String eventTitle;
    private String eventDetail;
    private int year, month, day, hour, minute;
    private int timeForOrder;
    private Date date;

    public EventDateModel(String eventTitle, String eventDetail, int year, int month, int day, int hour, int minute) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.year = year;
        this.month = month ;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        date = new Date(year, month, day, hour, minute);
        timeForOrder =  minute + hour*100 + day*10000 + month * 1000000 + year*100000000;
    }

    public EventDateModel() {
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public int getTimeForOrder() {
        return timeForOrder;
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

    @Override
    public String toString() {
        return
                "eventTitle= " + eventTitle + "\n" +
                        year + "/" + month+"/"+ day + "  "+ hour + ": " + minute +"\n"
                        + eventDetail
                ;
    }

    public String getTitleAndDate() {
        int correctedMonth = month +1;
        return eventTitle+ " " + correctedMonth  + "/" + day + "  " + hour + ":" + minute;
    }
}
