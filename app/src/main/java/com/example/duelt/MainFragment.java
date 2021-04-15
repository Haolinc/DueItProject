package com.example.duelt;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.duelt.db.DatabaseHelper;

import java.util.List;

public class MainFragment extends Fragment {
    protected static final String CHANNEL_1_ID = "channel1";
    protected static final String CHANNEL_2_ID = "channel2";


    public MainFragment(){
        //Empty public constructor required
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_main,container,false);
        Button resetBtn= rootView.findViewById(R.id.resetButtonInMain);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                databaseHelper.upgrade();
            }
        });
        createNoticficationChannels();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCheckBox();
    }

    private void createNoticficationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            //Create channel 1
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");
            manager.createNotificationChannel(channel1);

            //Create channel 2
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is channel 2");
            manager.createNotificationChannel(channel2);


        }
    }


    private void updateCheckBox() {
        removeAllViews();
        createCheckBox();
    }

    private void createCheckBox() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        List<EventDateModel> list = databaseHelper.getDueDateReminder();
        for (int i=0; i<list.size(); i++){
            createCheckBoxInDueDate(list.get(i));
        }

    }
    private void removeAllViews() {
        LinearLayout dueDate = getView().findViewById(R.id.dueDate_layout);
        dueDate.removeAllViews();
        LinearLayout reminder = getView().findViewById(R.id.reminder_layout);
        reminder.removeAllViews();
    }

    private void createCheckBoxInDueDate(EventDateModel edm) {
        CheckBox cb = new CheckBox(getActivity());
        DatabaseHelper dh = new DatabaseHelper(getActivity());        //for deletion in onclick
        LinearLayout dueDate = getView().findViewById(R.id.dueDate_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText(edm.getTitleAndDate());
        cb.setId(edm.getID());
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){      //when checked
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("alert");
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dueDate.removeView(cb);   //click to remove checkbox view
                                    cancelAlarm(edm.getID());
                                    cancelAlarm(edm.getID2());
                                    cancelAlarm(edm.getID3());
                                    cancelAlarm(edm.getID4());
                                    dh.deleteOneFromDueDate(edm.getID());
                                    updateCheckBox();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cb.setChecked(false);
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.setOnCancelListener(
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cb.setChecked(false);
                                }
                            }
                    );
                    alertDialog.show();
                }
            }
        });

        dueDate.addView(cb);

    }

    public void jumpToTestMain(View v){
        Intent i = new Intent(getActivity(), MiniActivity.class);
        startActivity(i);
    }
    public void cancelAlarm(int requestedCode) {
        AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), MemoAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), requestedCode, i, 0);
        am.cancel(pi);
    }

}
