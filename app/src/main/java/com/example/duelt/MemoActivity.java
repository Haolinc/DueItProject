package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;
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

public class MemoActivity extends AppCompatActivity implements Serializable {
    //test code
    private Date date;
    private String text;
    private static DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
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
        List<EventDateModel> list = databaseHelper.getAll();
        Toast.makeText(MemoActivity.this, list.toString(), Toast.LENGTH_SHORT).show();
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

   // public void getDate(View view) {
    //  getDate();
    //}
}