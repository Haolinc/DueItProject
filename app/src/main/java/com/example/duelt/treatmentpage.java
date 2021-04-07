package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class treatmentpage extends AppCompatActivity {
    //private static final long START_TIME_IN_MILLIS = 3600000;

    long START_TIME_IN_MILLIS = TreatmentActivity.getmStartTimeInMillis() * 60000;

    private TextView mCountDown;
    private TextView mCountDownDisplay;
    private Button mButtonStartPause;
    //MediaPlayer alarmSoundMP = MediaPlayer.create(this, R.raw.alarmclock);
    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatmentpage);
        final MediaPlayer alarmSoundMP = MediaPlayer.create(this, R.raw.alarmclock);


        mCountDown = findViewById(R.id.countdown_text);
        mCountDownDisplay = findViewById(R.id.countDown_distext);

        mButtonStartPause = findViewById(R.id.button_pause_start);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer(alarmSoundMP);
                }
            }
        });

        updateCountDownText();
    }

    private void startTimer(MediaPlayer alarmSoundMP){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                if(mTimeLeftInMillis==2100000){
                    updateText();
                    alarmSoundMP.start();
                }
                if(mTimeLeftInMillis==1800000){
                    updateText2();
                    alarmSoundMP.start();
                }
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

    private void updateText(){
        mCountDownDisplay.setText("you will have a five minute break");
    }

    private void updateText2(){
        mCountDownDisplay.setText("Keep on working, you still have");
    }

    public void back(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}