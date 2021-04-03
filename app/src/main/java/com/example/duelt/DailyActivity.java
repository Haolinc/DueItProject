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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DailyActivity extends AppCompatActivity implements MyBroadcastListener{
    private TimePicker tp;
    TextView alarmStatus;
    String viewText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        alarmStatus = findViewById(R.id.alarm_status);
        tp = findViewById(R.id.datePicker1);
    }
    public void back(View v){
        finish();
    }

    public void setAlarm(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
        calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        viewText = "Set Alarm Time At: " + tp.getCurrentHour() + tp.getCurrentMinute();
        alarmStatus.setText(viewText);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("textView", viewText);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-3000, pi);
    }
    public void cancelAlarm(View view) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, i, 0);
        am.cancel(pi);
    }

    public void updateTheTextView(String t) {
        TextView textV1 = (TextView) findViewById(R.id.alarm_status);
        textV1.setText(t);
    }

    public void changeText(String result){
        updateTheTextView(result);          // Calling method from Interface
    }


}