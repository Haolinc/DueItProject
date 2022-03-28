package com.example.duelt.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.duelt.HintHelper;
import com.example.duelt.R;
import com.example.duelt.treatmentpage;


public class TreatmentFragment extends Fragment {
    AnimationDrawable go321Animation;
    TextView informat;
    public static long mStartTimeInMillis;
    ImageButton imageButton;
    AppCompatButton btn_start;

    final Handler handler2 = new Handler(Looper.getMainLooper());

    private EditText mEditTimeInput;


    final private String FIRST_TIME_KEY = "TRAETMENT_FIRST_TIME_KEY12310";

    public TreatmentFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_treatment,container,false);

        mEditTimeInput = rootView.findViewById(R.id.edit_time_input);
//        mButtonSet = rootView.findViewById(R.id.btn_setTime);
        imageButton = (ImageButton) rootView.findViewById(R.id.go321button);
        imageButton.setBackgroundResource(R.drawable.btn_go_321_list);
        imageButton.setEnabled(false);
        informat = (TextView) rootView.findViewById(R.id.startText);
        go321Animation = (AnimationDrawable) imageButton.getBackground();

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) rootView.findViewById(R.id.btn_treament_hint);
        ImageButton btn_hint2 = (ImageButton) rootView.findViewById(R.id.btn_treament_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint2);


        ConstraintLayout cl = rootView.findViewById(R.id.treatment_layout);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });



        //image button function
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), treatmentpage.class);
                startActivity(i);
            }
        });

        //startAnimation button function
        btn_start = (AppCompatButton) rootView.findViewById(R.id.btnStart);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input = mEditTimeInput.getText().toString();
                if(!input.matches("-?\\d+")) {
                    mEditTimeInput.setError("Please enter an integer");
                    return;
                }


                if(input.length() == 0) {
                    Toast.makeText(getActivity(), "Field can't be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input);
                if (millisInput == 0) {
                    Toast.makeText(getActivity(), "please enter a positive number",Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);

                Toast.makeText(getActivity(), "time set to " +mStartTimeInMillis + " minutes",Toast.LENGTH_SHORT).show();

                view.setVisibility(View.GONE);
                go321Animation.start();
                informat.setVisibility(View.VISIBLE);

                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageButton.setEnabled(true);
                    }
                },4000);

            }
        });

        return rootView;
    }


    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
    }

    public static long getmStartTimeInMillis(){
        return mStartTimeInMillis;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AnimationDrawable)(imageButton.getBackground())).stop();
        imageButton.setBackgroundDrawable(null);
        imageButton.setBackgroundResource(R.drawable.btn_go_321_list);
        go321Animation = (AnimationDrawable) imageButton.getBackground();
        imageButton.setEnabled(false);
        btn_start.setVisibility(View.VISIBLE);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

}
