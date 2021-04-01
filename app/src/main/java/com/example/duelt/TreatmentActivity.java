package com.example.duelt;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import pl.droidsonroids.gif.GifImageView;

public class TreatmentActivity extends AppCompatActivity {
    GifImageView gifImageView;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

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

    public void back(View v){
        finish();
    }

    public void onClick(View v){
        Intent i = new Intent(this, treatmentpage.class);
        startActivity(i);
    }
    
}

