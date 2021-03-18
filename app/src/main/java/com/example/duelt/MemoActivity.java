package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



/*test code*/
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoActivity extends AppCompatActivity implements Serializable {
    //test code
    private Date date;
    private String text;
    private static DateFormat dateFormate = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
    }

    public void jumpToCalendar(View v){
        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);
    }

    public void back(View v){

        finish();
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