package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class treatmentpage extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 900000;

    private TextView mCountDown;
    private Button mButtonStartPause;
    private Button mButtonBack;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatmentpage);

        mCountDown = findViewById(R.id.countdown_text);

        mButtonStartPause = findViewById(R.id.button_pause_start);
        mButtonBack = findViewById(R.id.button_back);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        updateCountDownText();
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setVisibility((View.INVISIBLE));
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
    }


    private void updateCountDownText(){
        int minutes = (int) mTimeLeftInMillis/ 1000/ 60;
        int seconds = (int) (mTimeLeftInMillis/ 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes,seconds);

        mCountDown.setText(timeLeftFormatted);
    }

    public void back(View v){
        finish();
    }
}