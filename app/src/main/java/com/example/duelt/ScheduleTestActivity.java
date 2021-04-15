package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ScheduleTestActivity extends AppCompatActivity {

    com.google.android.material.button.MaterialButton btn_addEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weeklyn_schedule);

        btn_addEvent = (com.google.android.material.button.MaterialButton)findViewById(R.id.btn_addEvent);
        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ScheduleEventAddWindow.class);
                startActivity(i);
            }
        });


    }
}