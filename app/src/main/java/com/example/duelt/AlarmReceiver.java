package com.example.duelt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i){
        Toast.makeText(context, "Alarm waked", Toast.LENGTH_LONG).show();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        int id = i.getIntExtra("EDMID", 0);
        Intent i2= new Intent(context, PopWindow.class);
        i2.putExtra("EDMID", i.getIntExtra("EDMID", 0));
        i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(i2);
    }
}
