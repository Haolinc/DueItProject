package com.example.duelt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;

import com.example.duelt.db.DatabaseHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TextEntering extends AppCompatActivity {
    private String eventTitle;
    private String eventDetail;
    private EditText eventTitleInput;
    private EditText eventDetailInput;
    private int year, month, day, hour, minute;
    EventDateModel eventDateModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entering);
        eventTitleInput = findViewById(R.id.eventTitle);
        eventDetailInput = findViewById(R.id.eventDetail);

        //get date from calendarActivity
        Intent intent = getIntent();
        year = intent.getIntExtra(CalendarActivity.EXTRA_Year, 0);
        month = intent.getIntExtra(CalendarActivity.EXTRA_Month, 0);
        day = intent.getIntExtra(CalendarActivity.EXTRA_Day, 0);
        hour= intent.getIntExtra(CalendarActivity.EXTRA_Hour, 0);
        minute = intent.getIntExtra(CalendarActivity.EXTRA_Minute, 0);

    }

    public void back(View v){
        finish();
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    public void saveEvent(View v){
        DatabaseHelper databaseHelper = new DatabaseHelper(TextEntering.this);
        eventDetail = eventDetailInput.getText().toString();
        eventTitle = eventTitleInput.getText().toString();

        eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute,this);
        setAlarm(v);
        //insert into database

        databaseHelper.addOne(eventDateModel);

        //make a checkbox in main activity

        //testing
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("alert");
        alertDialog.setMessage(eventDateModel.getTitleAndDate());

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void setAlarm(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, MemoAlarmReceiver.class);
        i.putExtra("EDMID", eventDateModel.getID() );
        i.putExtra("TITLE", eventDateModel.getEventTitle() );
        i.putExtra("DETAIL", eventDateModel.getEventDetail());

        PendingIntent pi = PendingIntent.getBroadcast(this, eventDateModel.getID() , i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

    }
}