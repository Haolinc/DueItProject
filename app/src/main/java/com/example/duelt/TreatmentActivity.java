package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import pl.droidsonroids.gif.GifImageView;

public class TreatmentActivity extends AppCompatActivity {
    AnimationDrawable go321Animation;
    TextView informat;
    public static long mStartTimeInMillis;

    private EditText mEditTimeInput;
    private Button mButtonSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);
        mEditTimeInput = findViewById(R.id.edit_time_input);
        mButtonSet = findViewById(R.id.btn_setTime);
        ImageButton imageButton = (ImageButton) findViewById(R.id.go321button);
        imageButton.setBackgroundResource(R.drawable.go_3_2_1);
        informat = (TextView) findViewById(R.id.startText);
        go321Animation = (AnimationDrawable) imageButton.getBackground();

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTimeInput.getText().toString();
                    if(input.length() == 0) {
                        Toast.makeText(TreatmentActivity.this, "Field can't be empty",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long millisInput = Long.parseLong(input);
                    if (millisInput == 0) {
                        Toast.makeText(TreatmentActivity.this, "please enter a positive number",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setTime(millisInput);
                    //mEditTimeInput.setText("");
                    Toast.makeText(TreatmentActivity.this, "time set to " +mStartTimeInMillis + " minutes",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
    }

    public static long getmStartTimeInMillis(){
        return mStartTimeInMillis;
    }
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        go321Animation.start();
    }*/

    public void startAnimation(View v){
        v.setVisibility(View.GONE);
        go321Animation.start();
        informat.setVisibility(View.VISIBLE);

    }

    public void back(View v){
        finish();
    }

    public void onClick(View v){
        Intent i = new Intent(this, treatmentpage.class);
        startActivity(i);
    }
    
}

