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
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.duelt.R;
import com.example.duelt.alarm.AlarmReceiver;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;
import com.example.duelt.popWindows.PopWindow;

import java.util.Calendar;
import java.util.List;

public class DailyFragment extends Fragment {
    DatabaseHelper databaseHelper;
    LinearLayout layoutView;

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

        Button btn_resetDatabase = (Button)rootView.findViewById(R.id.resetDatabaseBtn);
        btn_resetDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.upgrade();
            }
        });

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

                Intent i = new Intent(getActivity(), AlarmReceiver.class);
                i.putExtra("EDMID", edm.getID());
                i.putExtra("Table", "DailyReminder");
                PendingIntent pi = PendingIntent.getBroadcast(getActivity(), edm.getID(), i, 0);
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-1000*60*5, 24*60*60*1000, pi);

                addCheckBox(edm);
            }
        });

        return rootView;
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
            dailyPenalty(cb, edm.getID());
        }
        else if (edm.getWaked() > 0 && checkIfPassTime(edm))
            dailyPenalty(cb, edm.getID());
        else
            dailyReward(cb, edm);

        layoutView.addView(cb);
    }



    private void dailyPenalty (CheckBox cb, int id) {
        Intent i = new Intent(getActivity(), PopWindow.class);
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
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), requestedCode, i, 0);
        am.cancel(pi);
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
