package com.example.duelt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView mCalendarView;
    private CalenderTimer mCalendarTimer;
    private EditText eventTitleInput;
    private EditText eventDetailInput;
    private TextView selectTimeView;
    //data to store information about date and event
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String eventTitle;
    private String eventDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        eventTitleInput = (EditText)findViewById(R.id.EventTitle);
        eventDetailInput = (EditText)findViewById(R.id.EventDetail);
        selectTimeView = (TextView) findViewById(R.id.SelectTime);

        //get user select date in CalenderView and store them
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
            }
        });
        //create  TimePickerDialog  to user input about time in hour and minute
        selectTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                           @Override
                           public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                              mHour = hourOfDay;
                              mMinute = minute;
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

    public void saveEvent(View v){
        eventDetail = eventDetailInput.getText().toString();
        eventTitle = eventTitleInput.getText().toString();

        //testing
        AlertDialog alertDialog = new AlertDialog.Builder(CalendarActivity.this).create();
        alertDialog.setTitle(eventTitle);
        alertDialog.setMessage(mYear + "/" + mMonth+"/"+ mDay + "  "+ mHour + ": " + mMinute +"\n" + eventDetail );

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
        alertDialog.show();
    }


}