package com.example.duelt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView mCalendarView;
    //private EditText eventTitleInput;
    //private EditText eventDetailInput;
    //data to store information about date and event
    private int mYear, mMonth, mDay, mHour, mMinute;
    Context context= this;
    //private String eventTitle;
    //private String eventDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        //eventTitleInput = (EditText)findViewById(R.id.EventTitle);
        //eventDetailInput = (EditText)findViewById(R.id.EventDetail);

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
                                startActivity(new Intent(context, TextEntering.class));
                            }
                        }, 24,0,true
                );
                timePickerDialog.updateTime(mHour,mMinute);
                timePickerDialog.show();
            }

        });
    }
    public void back(View v){
        finish();
    }


}