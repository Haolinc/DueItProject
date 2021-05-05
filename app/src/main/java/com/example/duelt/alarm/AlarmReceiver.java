package com.example.duelt.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
            if (table.equals("DailyReminder") || table.equals("DailyReward")) {
                if (currentTime.get(Calendar.YEAR) * 1000 + currentTime.get(Calendar.DAY_OF_YEAR) !=
                        databaseHelper.getOneFromDaily(id).getWakedTime()) {
                    Toast.makeText(context, "Alarm waked", Toast.LENGTH_LONG).show();

                    databaseHelper.updateWakedStatusInDaily(id, databaseHelper.getOneFromDaily(id).getWaked() + 1);

                    Intent i2 = new Intent(context, PopWindow.class);
                    i2.putExtra("EDMID", id);
                    i2.putExtra("Table", table);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    context.startActivity(i2);
                }
            } else {
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
