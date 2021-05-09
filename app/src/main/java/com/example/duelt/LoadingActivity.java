package com.example.duelt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.fragments.TabActivity;


public class LoadingActivity extends AppCompatActivity {

    final int delayMillis = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);

        if (new DatabaseHelper(this).getCurrentStat().getName().equals("")) {
            Intent intent = new Intent(LoadingActivity.this, PetNaming.class);
            startActivity(intent);
            finish();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoadingActivity.this, TabActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, delayMillis);
        }

    }
}