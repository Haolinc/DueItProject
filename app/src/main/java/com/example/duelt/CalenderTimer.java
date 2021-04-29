package com.example.duelt;

import com.example.duelt.db.EventDateModel;

import java.util.Calendar;

public class CalenderTimer {

    public CalenderTimer(){
    }

//    public EventDateModel getSystemDay(){
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        EventDateModel edm = new EventDateModel(year,month,day,hour,minute);
//        return edm;
//    }

    public EventDateModel getDailyDate() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        EventDateModel edm = new EventDateModel(hour, minute);
        return edm;
    }


}
