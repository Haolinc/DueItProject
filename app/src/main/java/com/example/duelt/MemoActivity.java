package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



/*test code*/
import com.example.duelt.db.DatabaseHelper;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.duelt.MainActivity.CHANNEL_1_ID;

public class MemoActivity extends AppCompatActivity implements Serializable {
    //test code
    private Date date;
    private String text;
    private static DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");
    private NotificationManagerCompat notificationManagerCompat;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    public void jumpToCalendar(View v){
        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);
    }
    public void jumpToTextEnter(View v){
        Intent i = new Intent(this, TextEntering.class);
        startActivity(i);
    }

    public void back(View v){
        finish();
    }

    public void ViewAll(View v){
        DatabaseHelper databaseHelper = new DatabaseHelper(MemoActivity.this);
        List<EventDateModel> list = databaseHelper.getDueDateReminder();
        Toast.makeText(this, list.toString(), Toast.LENGTH_SHORT).show();
    }

    //testcode

    public MemoActivity(){
        this.date = new Date();
    }

    public MemoActivity(int time, String text) {
        this.date = new Date(time);
        this.text = text;
    }


    public String getDate(){
        return dateFormate.format(date);
    }

    public long getTime() {
        return date.getTime();
    }

    public void setTime(int time) {
        this.date = new Date(time);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public void getDate(View view) {
      CalenderTimer calenderTimer = new CalenderTimer();
      EventDateModel edm = calenderTimer.getSystemDay();
      Toast.makeText(this, edm.toStringTimeOnly(), Toast.LENGTH_SHORT).show();
    }

    public void testNotification(View v){
        DatabaseHelper databaseHelper = new DatabaseHelper(MemoActivity.this);
        List<EventDateModel> list = databaseHelper.getDueDateReminder();
        EventDateModel edm = list.get(0);
        String eventTitle = edm.getEventTitle();
        String eventDetail = edm.getEventDetail();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
                .setContentTitle(eventTitle)
                .setContentText(eventDetail)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1,notification);
    }
}