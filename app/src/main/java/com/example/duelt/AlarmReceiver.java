package com.example.duelt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i){
        Toast.makeText(context, "Alarm waked", Toast.LENGTH_LONG).show();
    }
}
