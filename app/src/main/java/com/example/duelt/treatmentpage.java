package com.example.duelt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.fragments.TreatmentFragment;
import com.example.duelt.popWindows.PopWindow;

import java.util.Locale;

public class treatmentpage extends AppCompatActivity {
    //private static final long START_TIME_IN_MILLIS = 3600000;

    long START_TIME_IN_MILLIS = TreatmentFragment.getmStartTimeInMillis() * 60000;
    long mTimer = TreatmentFragment.getmStartTimeInMillis();
    long breakTime = 25*60000;
    long workTime = 30*60000;
    long tCount = 1;

    final private String FIRST_TIME_KEY = "TRAETMENTPAGE_FIRST_TIME_KEY10";

    private TextView mCountDown;
    private TextView mCountDownDisplay;
    private Button mButtonStartPause;
    //bug test****************************************************************************************
//    private TextView mbug;
//    private Button mTestButton;
    //bug test****************************************************************************************
    //MediaPlayer alarmSoundMP = MediaPlayer.create(this, R.raw.alarmclock);
    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatmentpage);
        final MediaPlayer alarmSoundMP = MediaPlayer.create(this, R.raw.alarmclock);
        //bug test****************************************************************************************
//        mbug = findViewById(R.id.text_bug);
//        mTestButton = findViewById(R.id.test_button);
//
//        mTestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), PopWindow.class);
//                i.putExtra("Table", "Treatment");
//                startActivity(i);
//            }
//        });
        //bug test****************************************************************************************
        mCountDown = findViewById(R.id.countdown_text);
        mCountDownDisplay = findViewById(R.id.countDown_distext);

        mButtonStartPause = findViewById(R.id.button_pause_start);

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) findViewById(R.id.btn_treamentpage_hint1);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(this,FIRST_TIME_KEY,btn_hint);
        ImageButton btn_hint2 = (ImageButton) findViewById(R.id.btn_treamentpage_hint3);
        HintHelper hh2 = new HintHelper();
        hh.checkFirstTime(this,FIRST_TIME_KEY,btn_hint2);


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
                if (mTimeLeftInMillis > START_TIME_IN_MILLIS-(breakTime*tCount) && mTimeLeftInMillis < START_TIME_IN_MILLIS-(breakTime*tCount)+1000) {
                        updateText();
                        alarmSoundMP.start();
                    //Toast.makeText(treatmentpage.this, "break" + tCount, Toast.LENGTH_SHORT).show();
                }
                if (mTimeLeftInMillis > START_TIME_IN_MILLIS-(workTime*tCount) && mTimeLeftInMillis < START_TIME_IN_MILLIS-(workTime*tCount)+1000) {
                        updateText2();
                        alarmSoundMP.start();
                    //Toast.makeText(treatmentpage.this, "work" + tCount, Toast.LENGTH_SHORT).show();
                        tCount++;
                }

                if (mTimeLeftInMillis < 1000 && mTimeLeftInMillis > 0){
                    Intent i = new Intent(treatmentpage.this, PopWindow.class);
                    i.putExtra("Table", "Treatment");
                    startActivity(i);
                }
                //bug test**************************************************************************************************************************
                //long mtime = START_TIME_IN_MILLIS-(breakTime*tCount);
                //Toast.makeText(treatmentpage.this, "time = " + mtime, Toast.LENGTH_SHORT).show();
                //Toast.makeText(treatmentpage.this, "time2 = " + mTimeLeftInMillis, Toast.LENGTH_SHORT).show();
                //mbug.setText("time = "+ mTimeLeftInMillis);
                //bug test**************************************************************************************************************************

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
        finish();
    }

}