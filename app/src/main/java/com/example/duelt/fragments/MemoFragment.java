package com.example.duelt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duelt.CalendarActivity;
import com.example.duelt.HintHelper;
import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;
import com.example.duelt.popWindows.Memo_pop_window;

import java.util.ArrayList;
import java.util.List;


//In a Fragment, whenever need to use 'this' or 'getContext()' as in an activity class, it should be replaced with 'getActivity().
public class MemoFragment extends Fragment {
    //test code

    final private String FIRST_TIME_KEY = "MEMO_FIRST_TIME_KEY";

    public ArrayList<String> dayLeft_list= new ArrayList<String>();
    ListView mListView;

    static int[] edmID = new int[100];
    public MemoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_memo,container,false);
        //layoutView = rootView.findViewById(R.id.dayLeft_layout);

        mListView = rootView.findViewById(R.id.dayLeft_list);
        //Buttons in Fragments should be written here
        // ↓↓↓↓↓↓↓↓

        //jumpToCalendar button function
        Button btn_jumpToCalendar = (Button) rootView.findViewById(R.id.btn_memo_addEvent);
        btn_jumpToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation addbutton = AnimationUtils.loadAnimation(getActivity(),R.anim.treatment_button_bounce);
                btn_jumpToCalendar.startAnimation(addbutton);
                Intent i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
            }
        });

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) rootView.findViewById(R.id.btn_memo_hint1);
        ImageButton btn_hint2 = (ImageButton) rootView.findViewById(R.id.btn_memo_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint2);

        //createTextsViewInDayLeft();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTextViewInDayLeft();
    }

    private void updateTextViewInDayLeft() {
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, dayLeft_list);
        arrayAdapter1.clear();
        mListView.setAdapter(arrayAdapter1);
        createTextsViewInDayLeft();
    }


    public void createOneTextViewInDayLeft(EventDateModel edm){
        int days = edm.minusInDay(edm);


        dayLeft_list.add(edm.getEventTitle() + " still have " + days + " days left.");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, dayLeft_list);
        mListView.setId(edm.getID());
        mListView.setAdapter(arrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int eid = edmID[(int)id];
                Intent i = new Intent(getActivity().getApplication(), Memo_pop_window.class);
                i.putExtra("EDMID", eid);
                startActivity(i);
                Toast.makeText(getActivity(),"id passed in "+ edmID[(int)id], Toast.LENGTH_SHORT).show();
            }
        });
        //mListView.setAdapter(ad);

    }
//    private void createOneTextViewInDayLeft(EventDateModel edm) {
//        TextView tv = new TextView(getActivity());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content
//
//        int days = edm.minusInDay(edm);
//
//        tv.setText(edm.getEventTitle() + " ONLY " + days + " DAY LEFT !!");
//        tv.setLayoutParams(lp);
//        tv.setGravity(Gravity.CENTER_VERTICAL);
//        layoutView.addView(tv);
//    }

    private void createTextsViewInDayLeft(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        List<EventDateModel> list = databaseHelper.getDueDateReminder();

        for (int i=0; i<list.size(); i++){
            edmID[i] = list.get(i).getID();
            createOneTextViewInDayLeft(list.get(i));
            //Toast.makeText(getActivity(),"id passed in "+list.get(i).getID() , Toast.LENGTH_SHORT).show();
        }
    }

}
