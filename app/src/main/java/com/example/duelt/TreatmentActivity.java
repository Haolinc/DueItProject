package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import pl.droidsonroids.gif.GifImageView;

public class TreatmentActivity extends AppCompatActivity {
    AnimationDrawable go321Animation;
    TextView informat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        ImageButton imageButton = (ImageButton) findViewById(R.id.go321button);
        imageButton.setBackgroundResource(R.drawable.go_3_2_1);
        informat = (TextView) findViewById(R.id.startText);
        go321Animation = (AnimationDrawable) imageButton.getBackground();



        //gitsetting
       /* gifImageView = (GifImageView)findViewById(R.id.countDown3);
        btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifImageView.getDrawable();
            }
        }); */
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

