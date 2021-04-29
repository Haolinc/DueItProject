package com.example.duelt;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.fragments.MainFragment;

public class MemoAlarmReceiver  extends BroadcastReceiver {
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    public void onReceive(Context context, Intent i) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        notificationManagerCompat = NotificationManagerCompat.from(context);

        Intent activityIntent = new Intent(context, MainFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent,0);

        Notification notification = new NotificationCompat.Builder(context, i.getStringExtra("ChannelID"))
                .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
                .setContentTitle(i.getStringExtra("TITLE"))
                .setContentText(i.getStringExtra("DETAIL"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .build();
        notificationManagerCompat.notify(i.getIntExtra("EDMID", 1),notification);

        if(i.getBooleanExtra("IsFinalDate", false)) {}
             //databaseHelper.deleteOneFromDueDate(i.getIntExtra("EDMID", 0));

    }
}
