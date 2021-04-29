package com.example.duelt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.popWindows.PopWindow;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i){
        Toast.makeText(context, "Alarm waked", Toast.LENGTH_LONG).show();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        int id = i.getIntExtra("EDMID", 0);
        databaseHelper.updateWakedStatusInDueDate(id, 1);
        String table = i.getStringExtra("Table");
        Intent i2= new Intent(context, PopWindow.class);
        i2.putExtra("EDMID", id);
        i2.putExtra("Table", table);
        i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(i2);
    }
}
