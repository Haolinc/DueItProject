package com.example.duelt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.duelt.db.DatabaseHelper;

import java.util.Date;

public class TextEntering extends AppCompatActivity {
    private String eventTitle;
    private String eventDetail;
    private EditText eventTitleInput;
    private EditText eventDetailInput;
    private int year, month, day, hour, minute;
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

    public void saveEvent(View v){
        eventDetail = eventDetailInput.getText().toString();
        eventTitle = eventTitleInput.getText().toString();
        EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute);

        //insert into database
        DatabaseHelper databaseHelper = new DatabaseHelper(TextEntering.this);
        databaseHelper.addOne(eventDateModel);

        //make a checkbox in main activity

        //testing
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("alert");
        alertDialog.setMessage(eventDateModel.toString());

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