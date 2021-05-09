package com.example.duelt.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.duelt.HintHelper;
import com.example.duelt.R;
import com.example.duelt.alarm.AlarmReceiver;
import com.example.duelt.alarm.MemoAlarmReceiver;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;
import com.example.duelt.popWindows.PopWindow;

import java.util.Calendar;
import java.util.List;

import static com.example.duelt.fragments.MainFragment.CHANNEL_1_ID;

public class DailyFragment extends Fragment {
    DatabaseHelper databaseHelper;
    LinearLayout layoutView;
    final private String FIRST_TIME_KEY = "DAILY_FIRST_TIME_KEY";

    public DailyFragment(){
        //Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_daily,container,false);
        layoutView = rootView.findViewById(R.id.daily_routine_checkbox);
        databaseHelper = new DatabaseHelper(getActivity());
        createCheckBox();
        TimePicker tp = rootView.findViewById(R.id.datePicker1);

        ConstraintLayout cl = rootView.findViewById(R.id.dailyLayout);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });

        //**Back button might not be needed anymore

        //Button btn_resetDatabase = (Button)rootView.findViewById(R.id.resetDatabaseBtn);
//        btn_resetDatabase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                databaseHelper.upgrade();
//            }
//        });

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) rootView.findViewById(R.id.btn_daily_hint1);
        ImageButton btn_hint2 = (ImageButton) rootView.findViewById(R.id.btn_daily_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint2);

        //setAlarm button function
        Button btn_setAlarm = (Button)rootView.findViewById(R.id.setTimeButton);
        btn_setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                calendar.set(Calendar.MINUTE, tp.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
                AlarmManager am = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

                EditText et = rootView.findViewById(R.id.daily_routine_title);

                EventDateModel edm = new EventDateModel(et.getText().toString(), tp.getCurrentHour(), tp.getCurrentMinute(), getActivity());
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                databaseHelper.addOneToDaily(edm);

                //set notification
                setNotificationAlarmIntent(edm.getID()+1,
                        et.getText().toString() + " is coming up soon! ",
                        calendar.getTimeInMillis(),
                        CHANNEL_1_ID,
                        true);

                Intent i = new Intent(getActivity(), AlarmReceiver.class);
                i.putExtra("EDMID", edm.getID());
                i.putExtra("Table", "DailyReminder");
                PendingIntent pi = PendingIntent.getBroadcast(getActivity(), edm.getID(), i, 0);
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pi);
                updateView();
            }
        });

        return rootView;
    }

    private void setNotificationAlarmIntent(int id, String title, long time, String channelID, boolean isFinalDate){
        AlarmManager am = (AlarmManager)requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), MemoAlarmReceiver.class);
        i.putExtra("EDMID", id);
        i.putExtra("TITLE", title);
        i.putExtra("DETAIL", "");
        i.putExtra("ChannelID", channelID);
        i.putExtra("IsFinalDate", isFinalDate);

        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), id , i, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,time - 1000*60*5, 1000*60*60*24, pi);
    }

    private void createCheckBox(){
        List<EventDateModel> list = databaseHelper.getDailyRoutine();
        for (int i=0; i<list.size(); i++){
            addCheckBox(list.get(i));
        }
    }

    private void addCheckBox(EventDateModel edm) {
        CheckBox cb = new CheckBox(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content


        cb.setText(edm.getDailyRoutineString());
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);

        if (checkIfDoneToday(edm)) {
            cb.setChecked(true);
        }
        if (checkIfPassTime(edm) && !cb.isChecked()){
            cb.setTextColor(Color.RED);
        }

        if (edm.getWaked() > 1 && !checkIfPassTime(edm)){
            databaseHelper.updateWakedStatusInDaily(edm.getID(), edm.getWaked()-1);
        }

        if (edm.getWaked() > 0 && checkIfPassTime(edm)){
            edm.setWaked(0);
            dailyPenalty(edm.getID());
        }

        else
            dailyReward(cb, edm);

        layoutView.addView(cb);
    }



    private void dailyPenalty (int id) {
        Intent i = new Intent(getActivity(), PopWindow.class);
        i.putExtra("From", "DF167");
        i.putExtra("Table", "DailyPenalty");
        i.putExtra("EDMID", id);
        startActivity(i);
    }

    private void dailyReward (CheckBox cb, EventDateModel edm) {
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfDoneToday(edm)) {
                    cb.setChecked(true);
                }
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("alert");
                alertDialog.setMessage("Are you ready to start the task?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (checkIfDoneToday(edm)) {
                                    Toast.makeText(getActivity(), "You have already done this today!", Toast.LENGTH_SHORT).show();
                                } else if (checkIfPassTime(edm)) {
                                    Toast.makeText(getActivity(), "You have passed the time to do it for today!", Toast.LENGTH_SHORT).show();
                                    cb.setChecked(false);
                                } else {
                                    Intent i = new Intent(getActivity(), PopWindow.class);
                                    i.putExtra("Table", "DailyReward");
                                    i.putExtra("EDMID", edm.getID());
                                    startActivity(i);
                                }
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cb.setChecked(checkIfDoneToday(edm));
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Remove",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCheckBox(cb, edm);
                                cancelAlarm(edm.getID());
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
        });
    }

    public void deleteCheckBox(CheckBox cb, EventDateModel edm){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Remove");
        alertDialog.setMessage("Are you sure to remove this event?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        layoutView.removeView(cb);   //click to remove checkbox view
                        databaseHelper.deleteOneFromDaily(edm.getID());
                        cancelAlarm(edm.getID());
                        updateView();
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
        alertDialog.show();
    }

    public void cancelAlarm(int requestedCode) {
        AlarmManager am = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        Intent i2= new Intent(getActivity(), MemoAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), requestedCode, i, 0);
        PendingIntent pi2= PendingIntent.getBroadcast(getActivity(), requestedCode+1, i2, 0);
        am.cancel(pi);
        am.cancel(pi2);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    private void deleteView() {
        LinearLayout daily_routine_layout = requireActivity().findViewById(R.id.daily_routine_checkbox);
        daily_routine_layout.removeAllViews();
    }

    public void updateView(){
        deleteView();
        createCheckBox();
    }

    private boolean checkIfPassTime (EventDateModel edm) {
        return edm.getTimeForOrder() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*100+Calendar.getInstance().get(Calendar.MINUTE);
    }

    private boolean checkIfDoneToday(EventDateModel edm){
        Calendar currentTime = Calendar.getInstance();
        return currentTime.get(Calendar.YEAR) * 1000 + currentTime.get(Calendar.DAY_OF_YEAR) == edm.getWakedTime();
    }


}
