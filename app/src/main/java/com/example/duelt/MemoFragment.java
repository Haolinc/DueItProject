package com.example.duelt;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.duelt.db.DatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.duelt.MainActivity.CHANNEL_1_ID;

//In a Fragment, whenever need to use 'this' or 'getContext()' as in an activity class, it should be replaced with 'getActivity().
public class MemoFragment extends Fragment {
    //test code
    private Date date;
    private String text;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");


    ListView listView;

    public MemoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_memo,container,false);


        //Buttons in Fragments should be written here
        // ↓↓↓↓↓↓↓↓

        //jumpToCalendar button function
        Button btn_jumpToCalendar = (Button) rootView.findViewById(R.id.buttonToCalendar);
        btn_jumpToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
            }
        });

        //back button function
        Button btn_back = (Button) rootView.findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        //viewAll button function
        Button btn_viewAll = (Button) rootView.findViewById(R.id.viewAll);
        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                List<EventDateModel> list = databaseHelper.getAll();
                Toast.makeText(getActivity(), list.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //getDate button function
        Button btn_getDate = (Button) rootView.findViewById(R.id.button3);  //Consider changing id naming
        btn_getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderTimer calenderTimer = new CalenderTimer();
                EventDateModel edm = calenderTimer.getSystemDay();
                Toast.makeText(getActivity(), edm.toStringTimeOnly(), Toast.LENGTH_SHORT).show();
            }
        });

        //testNotification button function
        Button btn_testNotification = (Button) rootView.findViewById(R.id.button4); //Consider changing id naming
        btn_testNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                List<EventDateModel> list = databaseHelper.getAll();
                EventDateModel edm = list.get(0);
                String eventTitle = edm.getEventTitle();
                String eventDetail = edm.getEventDetail();

                Notification notification = new NotificationCompat.Builder(getActivity(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_looks_one_24)
                        .setContentTitle(eventTitle)
                        .setContentText(eventDetail)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                //Have to assign notificationManagerCompat.
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                notificationManagerCompat.notify(1,notification);
            }
        });

        return rootView;
    }

}
