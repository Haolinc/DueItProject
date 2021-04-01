package com.example.duelt;
import android.util.Log;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CalenderTimer {
    //static Timer timer;

    final static String TAG = "CalendarActivity";

    public CalenderTimer(){
       // timer = new Timer();
    }
    /*
    public void scheduleEvent(int year, int month, int day, String event){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, event);
            }
        };

        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, month);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        timer.schedule(task, date.getTime());
    }*/

    public EventDateModel getSystemDay(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        EventDateModel edm = new EventDateModel(year,month,day,hour,minute);
        return edm;
    }


}
