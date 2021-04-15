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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DailyActivity extends AppCompatActivity {
    private TimePicker tp;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        tp = findViewById(R.id.datePicker1);
        createCheckBox();

    }
    public void back(View v){
        finish();
    }

    public void setAlarm(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
        calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        EditText et = findViewById(R.id.daily_routine_title);

        EventDateModel edm = new EventDateModel(et.getText().toString(), tp.getCurrentHour(), tp.getCurrentMinute(), this);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.addOneToDaily(edm);

        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("EDMID", edm.getID());
        PendingIntent pi = PendingIntent.getBroadcast(this, edm.getID(), i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

        addCheckBox(edm);
    }

    private void createCheckBox(){
        List<EventDateModel> list = databaseHelper.getDailyRoutine();
        for (int i=0; i<list.size(); i++){
            addCheckBox(list.get(i));
        }
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void addCheckBox(EventDateModel edm) {
        CheckBox cb = new CheckBox(this);
        DatabaseHelper dh = new DatabaseHelper(this);
        LinearLayout daily_routine_checkbox = findViewById(R.id.daily_routine_checkbox);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText(edm.getDailyRoutineString());
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){      //when checked
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    AlertDialog alertDialog = new AlertDialog.Builder(DailyActivity.this).create();
                    alertDialog.setTitle("alert");
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    daily_routine_checkbox.removeView(cb);   //click to remove checkbox view
                                    dh.deleteOneFromDaily(edm.getID());
                                    cancelAlarm(edm.getID());
                                    updateView();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cb.setChecked(false);
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setOnCancelListener(
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cb.setChecked(false);
                                }
                            }
                    );
                    alertDialog.show();
                }
            }
        });

        daily_routine_checkbox.addView(cb);
    }

    public void cancelAlarm(int requestedCode) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, requestedCode, i, 0);
        am.cancel(pi);
    }

    private int checkForID(){
        List<Integer> idFromDatabase = databaseHelper.getIDFromDaily();
        Collections.sort(idFromDatabase);
        for (int i=0;i<idFromDatabase.size();i++){
            if (i != idFromDatabase.get(i)){
                idFromDatabase.add(i);
                return i;
            }
        }
        return -1;
    }
    private void deleteView() {
        LinearLayout daily_routine_layout = findViewById(R.id.daily_routine_checkbox);
        daily_routine_layout.removeAllViews();
    }

    public void updateView(){
        deleteView();
        createCheckBox();
    }
}