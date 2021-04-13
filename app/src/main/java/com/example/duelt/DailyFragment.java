package com.example.duelt;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.duelt.db.DatabaseHelper;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                AlarmManager am = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

                EditText et = rootView.findViewById(R.id.daily_routine_title);

                EventDateModel edm = new EventDateModel(et.getText().toString(), tp.getCurrentHour(), tp.getCurrentMinute(), getActivity());
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                databaseHelper.addOne(edm);

                Intent i = new Intent(getActivity(), AlarmReceiver.class);
                i.putExtra("EDMID", edm.getID());
                PendingIntent pi = PendingIntent.getBroadcast(getActivity(), edm.getID(), i, 0);
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

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
        DatabaseHelper dh = new DatabaseHelper(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        if (edm.getWaked() == 1){
            cb.setTextColor(Color.RED);
        }
        cb.setText(edm.getDailyRoutineString());
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
                                    layoutView.removeView(cb);   //click to remove checkbox view
                                    dh.deleteOne(edm);
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

        layoutView.addView(cb);
    }

    public void cancelAlarm(int requestedCode) {
        AlarmManager am = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), requestedCode, i, 0);
        am.cancel(pi);
    }

    private void deleteView() {
        LinearLayout daily_routine_layout = Objects.requireNonNull(getActivity()).findViewById(R.id.daily_routine_checkbox);
        daily_routine_layout.removeAllViews();
    }

    public void updateView(){
        deleteView();
        createCheckBox();
    }


}
