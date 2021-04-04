package com.example.duelt;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.duelt.db.DatabaseHelper;

import java.util.Calendar;
import java.util.List;

public class DailyActivity extends AppCompatActivity {
    private TimePicker tp;
    TextView alarmStatus;
    String viewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        alarmStatus = findViewById(R.id.alarm_status);
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
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        viewText = "Set Alarm Time At: " + tp.getCurrentHour() + ":" + tp.getCurrentMinute();
        alarmStatus.setText(viewText);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("textView", viewText);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-3000, pi);

        EditText et = findViewById(R.id.daily_routine_title);

        EventDateModel edm = new EventDateModel(et.getText().toString(), tp.getCurrentHour(), tp.getCurrentMinute());
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.addOne(edm);
        addCheckBox(edm);

    }

    private void createCheckBox(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<EventDateModel> list = databaseHelper.getDailyRoutine();

        for (int i=0; i<list.size(); i++){
            addCheckBox(list.get(i));
            Log.i("line 74",(list.get(i).getTimeForOrder()+"\n"));
        }
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
                                    dh.deleteOne(edm);
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

    public void cancelAlarm(View view) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        viewText = "None";
        alarmStatus.setText(viewText);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, i, 0);
        am.cancel(pi);
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