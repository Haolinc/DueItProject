package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

//testing
    
    public void jumpToMini(View v){
        Intent i = new Intent(this, MiniActivity.class);
        startActivity(i);
    }


    //use ScrollView as parent and call linearlayout for action, change ScrollView values in xml files
    //Orientation is set in xml file
    public void createCheckBox(View v) {
        CheckBox cb = new CheckBox(this);
        LinearLayout dueDate = findViewById(R.id.dueDate_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText("new");
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        dueDate.addView(cb);
    }
}