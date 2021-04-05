package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Minigame extends AppCompatActivity {
    private TextView petHungry;
    private TextView petMood;
    private TextView petexp;
    private TextView petlv;
    private Button mHungry;
    private Button mMood;
    private Button mExp;
    private Button mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);

        petHungry = findViewById(R.id.pet_hungriness);
        petMood = findViewById(R.id.pet_mood);
        petexp = findViewById(R.id.pet_exp);
        petlv = findViewById(R.id.pet_lv);
        mHungry = findViewById(R.id.btn_hungry);
        mMood = findViewById(R.id.btn_mood);
        mExp = findViewById(R.id.btn_exp);
        mLevel = findViewById(R.id.btn_lv);


        mHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText1();
            }
        });

        mMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText2();
            }
        });

        mExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText3();
            }
        });

        mLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText4();
            }
        });
    }

    private void updateText1(){
        petHungry.setText("you will have a five minute break");
    }
    private void updateText2(){
        petMood.setText("you will have a five minute break");
    }
    private void updateText3(){
        petexp.setText("you will have a five minute break");
    }
    private void updateText4(){
        petlv.setText("you will have a five minute break");
    }

}