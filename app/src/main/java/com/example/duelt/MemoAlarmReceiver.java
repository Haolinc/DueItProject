package com.example.duelt;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.duelt.db.DatabaseHelper;

import static com.example.duelt.MainActivity.CHANNEL_1_ID;

public class MemoAlarmReceiver  extends BroadcastReceiver {
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    public void onReceive(Context context, Intent i) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        notificationManagerCompat = NotificationManagerCompat.from(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
                .setContentTitle(i.getStringExtra("TITLE"))
                .setContentText(i.getStringExtra("DETAIL"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1,notification);
        databaseHelper.deleteOne(i.getIntExtra("EDMID", 0));

    }
}
