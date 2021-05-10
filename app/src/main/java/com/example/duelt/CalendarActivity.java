package com.example.duelt;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.db.EventDateModel;
import com.example.duelt.fragments.TextEntering;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView mCalendarView;
    //data to store information about date and event
    private int mYear, mMonth, mDay, mHour, mMinute;

    public static final String EXTRA_Year = "com.example.duelt.EXTRA_Year";
    public static final String EXTRA_Month = "com.example.duelt.EXTRA_Month";
    public static final String EXTRA_Day = "com.example.duelt.EXTRA_Day";
    public static final String EXTRA_Hour = "com.example.duelt.EXTRA_Hour";
    public static final String EXTRA_Minute = "com.example.duelt.EXTRA_Minute";
    private static final String errDateInfo = "Please select a future date";
    Context context= this;

    final private String FIRST_TIME_KEY = "CALENDAR_FIRST_TIME_KEY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) findViewById(R.id.btn_calendar_hint1);
        ImageButton btn_hint2 = (ImageButton) findViewById(R.id.btn_calendar_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(this,FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(this,FIRST_TIME_KEY,btn_hint2);

        //get user select date in CalenderView and store them
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;
                                if(checkDate(year,month,dayOfMonth,hourOfDay,minute)) return;
                                //pass data of date to textEnter activity
                                Intent intent = new Intent(context, TextEntering.class);
                                intent.putExtra(EXTRA_Year, mYear);
                                intent.putExtra(EXTRA_Month, mMonth);
                                intent.putExtra(EXTRA_Day, mDay);
                                intent.putExtra(EXTRA_Hour, mHour);
                                intent.putExtra(EXTRA_Minute, mMinute);
                                startActivity(intent);
                                finish();
                            }
                        }, 24,0,true
                );
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();
            }

        });
    }

    private boolean checkDate(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        EventDateModel currentDay = new EventDateModel(Calendar.getInstance());
        EventDateModel date = new EventDateModel(year,month,dayOfMonth,hourOfDay,minute);
        boolean flag = !currentDay.isLessThanInTime(date);
        if(flag)  Toast.makeText(this, errDateInfo, Toast.LENGTH_SHORT).show();
        return flag;
    }

    public void back(View v){
        finish();
    }


}