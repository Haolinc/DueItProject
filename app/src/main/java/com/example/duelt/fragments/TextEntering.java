package com.example.duelt.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.CalendarActivity;
import com.example.duelt.HintHelper;
import com.example.duelt.R;
import com.example.duelt.alarm.AlarmReceiver;
import com.example.duelt.alarm.MemoAlarmReceiver;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;

import java.util.Calendar;

import static com.example.duelt.fragments.MainFragment.CHANNEL_1_ID;
import static com.example.duelt.fragments.MainFragment.CHANNEL_2_ID;
import static com.example.duelt.fragments.MainFragment.CHANNEL_3_ID;
import static com.example.duelt.fragments.MainFragment.CHANNEL_4_ID;

public class TextEntering extends AppCompatActivity {
    private String eventTitle;
    private String eventDetail;
    private EditText eventTitleInput;
    private EditText eventDetailInput;
    private int year, month, day, hour, minute;
    EventDateModel eventDateModel;

    final private String FIRST_TIME_KEY = "TEXTENTERING_FIRST_TIME_KEY";


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

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) findViewById(R.id.btn_textentering_hint1);
        ImageButton btn_hint2 = (ImageButton) findViewById(R.id.btn_textentering_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(this,FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(this,FIRST_TIME_KEY,btn_hint2);

    }

    public void back(View v){
        finish();
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    //Save button method (Used in XML file, android:OnClick)
    public void saveEvent(View v){
        if(TextUtils.isEmpty(eventTitleInput.getText().toString())) {
            eventTitleInput.setError("Title can't be empty");
            return;
        }
        DatabaseHelper databaseHelper = new DatabaseHelper(TextEntering.this);
        eventDetail = eventDetailInput.getText().toString();
        eventTitle = eventTitleInput.getText().toString();

        eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute,this);
        setAlarm(v, eventDateModel.getID());
        //insert into database

        databaseHelper.addDate(eventDateModel);

        //make a checkbox in main activity
        finish();

    }

    public void setAlarm(View view, int id) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH, month);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar now = Calendar.getInstance();

        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("EDMID", id);
        i.putExtra("Table", "Duedate");
        PendingIntent pi = PendingIntent.getBroadcast(this, id, i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pi);



        //set exact due date
        setAlarmIntent(eventDateModel.getID(),
                      eventTitleInput.getText().toString() + " IS DUE NOW!! ",
                      date.getTimeInMillis(),
                      CHANNEL_1_ID,
                     true);

        //set half way to due date
        long halfTime = (date.getTimeInMillis()- now.getTimeInMillis())/2 + now.getTimeInMillis();
        setAlarmIntent(eventDateModel.getID2(),
                eventTitleInput.getText().toString() + " IS HALF WAYS!! ",
                halfTime,
                CHANNEL_2_ID,
                false);

        //set one Third of way to due date
        long oneThirdTime = (date.getTimeInMillis()- now.getTimeInMillis())*2/3 + now.getTimeInMillis();
        setAlarmIntent(eventDateModel.getID3(),
                eventTitleInput.getText().toString() + " IS ONLY ONE THIRD WAYS LEFT!! ",
                oneThirdTime,
                CHANNEL_3_ID,
                false);

        //last day to due date
        long lastday = date.getTimeInMillis() - 86400000;
        if(lastday - now.getTimeInMillis() > 0) {
            setAlarmIntent(eventDateModel.getID4(),
                    eventTitleInput.getText().toString() + " IS DUE WITHIN 24 HOURS!! ",
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