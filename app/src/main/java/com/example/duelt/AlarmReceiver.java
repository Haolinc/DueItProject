package com.example.duelt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private MyBroadcastListener listener;

    @Override
    public void onReceive(Context context, Intent i){
        listener = (MyBroadcastListener)  context;
        listener.changeText("some thing");
        //Toast.makeText(context, "Alarm waked", Toast.LENGTH_LONG).show();
    }
}
