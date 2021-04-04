package com.example.duelt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;

import com.example.duelt.db.DatabaseHelper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private int checkForID(List<Integer> idFromDatabase){
        Collections.sort(idFromDatabase);
        for (int i=0;i<idFromDatabase.size();i++){
            if (i != idFromDatabase.get(i)){
                idFromDatabase.add(i);
                return i;
            }
        }
        return -1;
    }

    public void saveEvent(View v){
        DatabaseHelper databaseHelper = new DatabaseHelper(TextEntering.this);
        eventDetail = eventDetailInput.getText().toString();
        eventTitle = eventTitleInput.getText().toString();
//        List<Integer> idFromDatabase = databaseHelper.getIDFromDatabase();
//        int id = checkForID(idFromDatabase);

        EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute,this);

        //insert into database

        databaseHelper.addOne(eventDateModel);

        //make a checkbox in main activity

        //testing
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("alert");
        alertDialog.setMessage(eventDateModel.toString());

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
        alertDialog.show();
    }
}