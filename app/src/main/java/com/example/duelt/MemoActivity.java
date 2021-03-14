package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MemoActivity extends AppCompatActivity {

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
}