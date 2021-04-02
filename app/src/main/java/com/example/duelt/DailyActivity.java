package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.number.Scale;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DailyActivity extends AppCompatActivity {
    private TimePicker tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        tp = findViewById(R.id.datePicker1);
    }
    public void back(View v){
        finish();
    }

    public void setAlarm(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(tp.)
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, 1000, pi);
    }
    public void cancelAlarm(View view) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, i, 0);
        am.cancel(pi);
    }
}