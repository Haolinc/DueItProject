package com.example.duelt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duelt.CalendarActivity;
import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;

import java.util.Calendar;
import java.util.List;


//In a Fragment, whenever need to use 'this' or 'getContext()' as in an activity class, it should be replaced with 'getActivity().
public class MemoFragment extends Fragment {
    //test code
    LinearLayout layoutView;

    public MemoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_memo,container,false);
        layoutView = rootView.findViewById(R.id.dayLeft_layout);


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


        //getDate button function
        Button btn_getDate = (Button) rootView.findViewById(R.id.button3);  //Consider changing id naming
        btn_getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                EventDateModel edm = new EventDateModel(calendar);
                Toast.makeText(getActivity(), edm.toStringTimeOnly(), Toast.LENGTH_SHORT).show();
            }
        });


        createTextsViewInDayLeft();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTextViewInDayLeft();
    }

    private void updateTextViewInDayLeft() {
        layoutView.removeAllViews();
        createTextsViewInDayLeft();
    }

    private void createOneTextViewInDayLeft(EventDateModel edm) {
        TextView tv = new TextView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        int days = edm.minusInDay(edm);

        tv.setText(edm.getEventTitle() + " ONLY " + days + " DAY LEFT !!");
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        layoutView.addView(tv);
    }

    private void createTextsViewInDayLeft(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        List<EventDateModel> list = databaseHelper.getDueDateReminder();
        for (int i=0; i<list.size(); i++){
            createOneTextViewInDayLeft(list.get(i));
        }
    }

}
