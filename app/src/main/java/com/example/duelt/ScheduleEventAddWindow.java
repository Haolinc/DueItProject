package com.example.duelt;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.duelt.db.DatabaseHelper;

import java.util.Arrays;

public class ScheduleEventAddWindow extends AppCompatActivity {

    private Spinner spinner_start, spinner_end, spinner_weekDay, spinner_color;
    private int startPosition, endPosition;
    private EditText eventText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_add_event);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //Solve the outside part that makes the rounded corner looks bad. (不加这两行会导致窗口内部是圆角，外面是直角）
        Window window=this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //set pop up window size
        getWindow().setLayout((int)(width * .8),(int)(height*.7));

        //Initialize and locate EditText
        eventText = findViewById(R.id.eventText);

        //Initialize and locate corresponding spinners
        spinner_start = findViewById(R.id.spinner_start);
        spinner_end = findViewById(R.id.spinner_end);
        spinner_weekDay = findViewById(R.id.spinner_week);
        spinner_color = findViewById(R.id.spinner_color);

        //Declare items that will be showed in the spinners
        String[] startHourRange = {"12am","1am","2am","3am","4am","5am","6am","7am","8am","9am","10am","11am","12pm","1pm","2pm","3pm","4pm","5pm","6pm","7pm","8pm","9pm","10pm","11pm"};
        String[] weekDay = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] color = {"White","Blue","Pink","Yellow","Green","Purple"};

        //Set content/adapter for startTimeSpinner.
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,startHourRange);
        spinner_start.setAdapter(startAdapter);
        //Listener to handle selected item for StartTimeSpinner (EndTimeSpinner is within this, because it is dependent)
        spinner_start.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startPosition = i;

                String[] endHourRange;
                //If the last item(11pm) is selected, set the endTime spinner to the only option(11:59pm)
                //Else set the endTime spinner items to start from item selected position+1 to the end.
                if(i == 23){
                    endHourRange = new String[]{"11:59pm"};
                }else {
                    endHourRange = Arrays.copyOfRange(startHourRange, i + 1, startHourRange.length);
                }

                //Set content/adapter for endTimeSpinner.
                ArrayAdapter<String> endAdapter = new ArrayAdapter<>(ScheduleEventAddWindow.this, android.R.layout.simple_spinner_dropdown_item, endHourRange);
                spinner_end.setAdapter(endAdapter);

                spinner_end.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        endPosition = i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Set content/adapter for weekDaySpinner.
        ArrayAdapter<String> weekAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,weekDay);
        spinner_weekDay.setAdapter(weekAdapter);

        //Set content/adapter for weekDaySpinner.
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,color);
        spinner_color.setAdapter(colorAdapter);

    }

    // !!!!Write logic to check for existing hour range to prevent duplicates.
    public void addEventToWeeklySchedule(View view) {
        //Toast.makeText(this,"start: " + spinner_start.getSelectedItem().toString() + " ,end: " + spinner_end.getSelectedItem().toString() + " ,weekday: " + spinner_weekDay.getSelectedItem().toString() + " ,color: " + spinner_color.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
        DatabaseHelper databaseHelper = new DatabaseHelper(ScheduleEventAddWindow.this);
        String eventName = eventText.getText().toString();
        String color = spinner_color.getSelectedItem().toString();
        String weekDay = spinner_weekDay.getSelectedItem().toString();
        String startTime = spinner_start.getSelectedItem().toString();
        String endTime = spinner_end.getSelectedItem().toString();
        endPosition = startPosition + endPosition + 1;

        WeeklyScheduleModel model = new WeeklyScheduleModel(eventName, color, weekDay, startTime, endTime, startPosition, endPosition,this);
        //insert into database

        databaseHelper.addOneToWeeklySchedule(model);

        //make a checkbox in main activity

        //testing
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("alert");
        alertDialog.setMessage("Event: " + model.getEventName() + " created!");

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
}
