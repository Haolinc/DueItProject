package com.example.duelt;
import android.util.Log;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
