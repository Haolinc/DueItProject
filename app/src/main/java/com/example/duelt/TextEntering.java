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
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.duelt.MainActivity.CHANNEL_1_ID;
import static com.example.duelt.MainFragment.CHANNEL_2_ID;
import static com.example.duelt.MainFragment.CHANNEL_3_ID;
import static com.example.duelt.MainFragment.CHANNEL_4_ID;

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

        databaseHelper.addDate(eventDateModel);

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
                        finish();
                    }
                });
        alertDialog.show();

    }

    public void setAlarm(View view) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH, month);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar now = Calendar.getInstance();




        //set exact due date
        setAlarmIntent(eventDateModel.getID(),
                      eventTitleInput.getText().toString() + " DUE NOW!! ",
                      date.getTimeInMillis(),
                      CHANNEL_1_ID,
                     true);

        //set half way to due date
        long halfTime = (date.getTimeInMillis()- now.getTimeInMillis())/2 + now.getTimeInMillis();
        setAlarmIntent(eventDateModel.getID2(),
                eventTitleInput.getText().toString() + " HALF WAYS!! ",
                halfTime,
                CHANNEL_2_ID,
                false);

        //set one Third of way to due date
        long oneThirdTime = (date.getTimeInMillis()- now.getTimeInMillis())*2/3 + now.getTimeInMillis();
        setAlarmIntent(eventDateModel.getID3(),
                eventTitleInput.getText().toString() + " ONLY ONE THIRD WAYS LEFT!! ",
                oneThirdTime,
                CHANNEL_3_ID,
                false);

        //last day to due date
        long lastday = date.getTimeInMillis() - 86400000;
        if(lastday - now.getTimeInMillis() > 0) {
            setAlarmIntent(eventDateModel.getID4(),
                    eventTitleInput.getText().toString() + " THE LAST 24 HOURS!! ",
                    lastday,
                    CHANNEL_4_ID,
                    false);
        }
    }

    private void setAlarmIntent(int id, String title, long time, String channelID, boolean isFinalDate){
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, MemoAlarmReceiver.class);
        i.putExtra("EDMID", id );
        i.putExtra("TITLE", title);
        i.putExtra("DETAIL", eventDetailInput.getText().toString());
        i.putExtra("ChannelID", channelID);
        i.putExtra("IsFinalDate", isFinalDate);

        PendingIntent pi = PendingIntent.getBroadcast(this, id , i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP,time, pi);
    }

}