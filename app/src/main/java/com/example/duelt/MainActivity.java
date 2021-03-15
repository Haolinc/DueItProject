package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpToMemo(View v){
        Intent i = new Intent(this, MemoActivity.class);
        startActivity(i);
    }


    public void jumpToMini(View v){
        Intent i = new Intent(this, MiniActivity.class);
        startActivity(i);
    }
    public void createCheckBox(View v) {
        CheckBox checkBox = new CheckBox(this);
        ConstraintLayout cl = findViewById(R.id.activity_main);
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        checkBox.setText("new");
        checkBox.setLayoutParams(lp);


        cl.addView(checkBox);

    }
}