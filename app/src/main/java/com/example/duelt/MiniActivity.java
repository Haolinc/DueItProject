package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MiniActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini);
    }
    public void back(View v){
        finish();
    }

    public void onclik(View v){
        Intent i = new Intent(this, Minigame.class);
        startActivity(i);
    }
}