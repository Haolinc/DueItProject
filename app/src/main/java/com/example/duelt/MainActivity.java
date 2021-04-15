package com.example.duelt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    protected static final String CHANNEL_1_ID = "channel1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNoticficationChannels();


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCheckBox();
    }


    private void createNoticficationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
    //**************************************************************************************************************************
    private View rootView;
    Button btn_testMain = (Button) rootView.findViewById(R.id.button4); //Consider change naming
    //****************************************************************************************************************************
    public void jumpToMemo(View v){
        Intent i = new Intent(this, MemoActivity.class);
        startActivity(i);
    }
//testing
    
    public void jumpToTestMain(View v){
        Intent i = new Intent(this, MiniFragment.class);
        startActivity(i);
    }

    public void jumpToTP(View v){
        Intent i = new Intent(this, TreatmentActivity.class);
        startActivity(i);
    }

    public void jumpToDaily(View v){
        Intent i = new Intent(this, DailyActivity.class);
        startActivity(i);
    }

    public void jumpToTab(View v){
        startActivity(new Intent(this,TabActivity.class));
    }

    //use ScrollView as parent and call linearlayout for action, change ScrollView values in xml files
    //Orientation is set in xml file
    private void createCheckBoxInDueDate(EventDateModel edm) {
        CheckBox cb = new CheckBox(this);
        DatabaseHelper dh = new DatabaseHelper(this);        //for deletion in onclick
        LinearLayout dueDate = findViewById(R.id.dueDate_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText(edm.getTitleAndDate());
        cb.setId(edm.getID());
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){      //when checked
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("alert");
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dueDate.removeView(cb);   //click to remove checkbox view
                                    cancelAlarm(edm.getID());
                                    dh.deleteOneFromDueDate(edm.getID());
                                    updateCheckBox();
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

        dueDate.addView(cb);

    }

    private void updateCheckBox() {
        DatabaseHelper dh = new DatabaseHelper(this);
        List<EventDateModel> list = dh.getDueDateReminder();
        removeAllViews();
        createCheckBox();
    }

    private void createCheckBox() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<EventDateModel> list = databaseHelper.getDueDateReminder();
        for (int i=0; i<list.size(); i++){
            createCheckBoxInDueDate(list.get(i));
        }

    }
    private void removeAllViews() {
        LinearLayout dueDate = findViewById(R.id.dueDate_layout);
        dueDate.removeAllViews();
        LinearLayout reminder = findViewById(R.id.reminder_layout);
        reminder.removeAllViews();
    }


    private void createCheckBoxInReminder(View v) {
        CheckBox cb = new CheckBox(this);
        LinearLayout reminder = findViewById(R.id.reminder_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText("new");
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        reminder.addView(cb);
    }

    public void cancelAlarm(int requestedCode) {
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, MemoAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, requestedCode, i, 0);
        am.cancel(pi);
    }
}