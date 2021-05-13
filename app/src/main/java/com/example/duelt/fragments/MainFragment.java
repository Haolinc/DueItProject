package com.example.duelt.fragments;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.duelt.HintHelper;
import com.example.duelt.R;
import com.example.duelt.alarm.MemoAlarmReceiver;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;
import com.example.duelt.db.PetModel;
import com.example.duelt.popWindows.PopWindow;

import java.util.List;

public class MainFragment extends Fragment {
    protected static final String CHANNEL_1_ID = "channel1";
    protected static final String CHANNEL_2_ID = "channel2";
    protected static final String CHANNEL_3_ID = "channel3";
    protected static final String CHANNEL_4_ID = "channel4";


    final private String FIRST_TIME_KEY = "MAIN_FIRST_TIME_KEY";

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

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) rootView.findViewById(R.id.btn_main_hint1);
        ImageButton btn_hint2 = (ImageButton) rootView.findViewById(R.id.btn_main_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint2);
        createNoticficationChannels();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCheckBox();
        updateReminder();

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

            //Create channel 3
            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("This is channel 3");
            manager.createNotificationChannel(channel3);

            //Create channel 4
            NotificationChannel channel4 = new NotificationChannel(
                    CHANNEL_4_ID,
                    "Channel 4",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel4.setDescription("This is channel 4");
            manager.createNotificationChannel(channel4);

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

        //Make pop window immediately when the memo event due date has passed
        for (int i=0; i<list.size();i++){
            if (list.get(i).getWaked()==1){
                Intent intent= new Intent(getActivity(), PopWindow.class);
                intent.putExtra("EDMID", list.get(i).getID());
                intent.putExtra("Table", "Duedate");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                getActivity().startActivity(intent);
            }
        }

    }

    //Use for updating reminder textviews
    private void updateReminder(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        PetModel petModel = databaseHelper.getCurrentStat();
        int hungry = petModel.getHungriness();
        if(hungry<30) createTextViewInReminder("Your pet is hungry");

        int mood = petModel.getMood();
        if(mood<30) createTextViewInReminder("Your pet is sad");
    }

    private void createTextViewInReminder(String text) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        LinearLayout reminder = (LinearLayout) getView().findViewById(R.id.reminder_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        reminder.addView(textView);

    }

    private void removeAllViews() {
        LinearLayout dueDate = getView().findViewById(R.id.dueDate_layout);
        dueDate.removeAllViews();
        LinearLayout reminder = getView().findViewById(R.id.reminder_layout);
        reminder.removeAllViews();
    }

    //Function to create checkbox in memo due date scroll view
    private void createCheckBoxInDueDate(EventDateModel edm) {
        CheckBox cb = new CheckBox(getActivity());
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
                    alertDialog.setTitle("Due Date");
                    alertDialog.setMessage("Are you sure you have completed?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dueDate.removeView(cb);   //click to remove checkbox view
                                    cancelAlarm(edm.getID());
                                    cancelAlarm(edm.getID2());
                                    cancelAlarm(edm.getID3());
                                    cancelAlarm(edm.getID4());
                                    Intent i = new Intent(getActivity(), PopWindow.class);
                                    i.putExtra("Table", "Duedate");
                                    i.putExtra("EDMID", edm.getID());
                                    startActivity(i);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cb.setChecked(false);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Remove",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteCheckBox(cb, edm);
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

    //Method for extra alert dialogue when selecting remove in checkbox
    public void deleteCheckBox(CheckBox cb, EventDateModel edm){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Remove");
        alertDialog.setMessage("Are you sure to remove this event?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DatabaseHelper(getActivity()).deleteOneFromDueDate(edm.getID());
                        cancelAlarm(edm.getID());
                        cancelAlarm(edm.getID2());
                        cancelAlarm(edm.getID3());
                        cancelAlarm(edm.getID4());
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
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cb.setChecked(false);
            }
        });
        alertDialog.show();
    }

    public void cancelAlarm(int requestedCode) {
        AlarmManager am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), MemoAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), requestedCode, i, 0);
        am.cancel(pi);
    }

}
