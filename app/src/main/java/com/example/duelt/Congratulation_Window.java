package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Congratulation_Window extends AppCompatActivity {
    AnimationDrawable mWinnerCup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation__window);

        ImageView vWinnerCup = (ImageView)findViewById(R.id.img_winner_cup);
        vWinnerCup.setBackgroundResource(R.drawable.winner_cup_list);
        mWinnerCup = (AnimationDrawable) vWinnerCup.getBackground();

        mWinnerCup.start();
    }
}