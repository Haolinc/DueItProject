package com.example.duelt.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.popWindows.PopWindow;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        int id = i.getIntExtra("EDMID", 0);
        Calendar currentTime = Calendar.getInstance();
        String table = i.getStringExtra("Table");
        if (table != null) {
            //Use for remind user with pop window if they are in the application
            if (table.equals("DailyReminder")) {
                if (currentTime.get(Calendar.YEAR) * 1000 + currentTime.get(Calendar.DAY_OF_YEAR) !=
                        databaseHelper.getOneFromDaily(id).getWakedTime()) {
                    databaseHelper.updateWakedStatusInDaily(id, databaseHelper.getOneFromDaily(id).getWaked() + 1);
                    Intent i2 = new Intent(context, PopWindow.class);
                    i2.putExtra("EDMID", id);
                    i2.putExtra("Table", table);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    context.startActivity(i2);
                }
            }
            else {
                //Use to increment penalty if user skipped their scheduled time in daily routine
                databaseHelper.updateWakedStatusInDueDate(id, databaseHelper.getOneFromDueDate(id).getWaked() + 1);

                Intent i2 = new Intent(context, PopWindow.class);
                i2.putExtra("EDMID", id);
                i2.putExtra("Table", table);
                i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(i2);
            }
        }
    }
}
