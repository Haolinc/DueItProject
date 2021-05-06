package com.example.duelt.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.duelt.R;
import com.example.duelt.treatmentpage;


public class TreatmentFragment extends Fragment {
    AnimationDrawable go321Animation;
    TextView informat;
    public static long mStartTimeInMillis;

    private EditText mEditTimeInput;
    private Button mButtonSet;

    private ImageButton btn_hint;
    private ImageButton btn_hint2;
    final private String FIRST_TIME_KEY = "TRAETMENT_FIRST_TIME_KEY5";

    public TreatmentFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_treatment,container,false);

        mEditTimeInput = rootView.findViewById(R.id.edit_time_input);
        mButtonSet = rootView.findViewById(R.id.btn_setTime);
        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.go321button);
        imageButton.setBackgroundResource(R.drawable.btn_go_321_list);
        informat = (TextView) rootView.findViewById(R.id.startText);
        go321Animation = (AnimationDrawable) imageButton.getBackground();

        checkFirstTime(rootView);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTimeInput.getText().toString();
                //test animation *********************************************************************************************************************************
                final Animation treatmentButton = AnimationUtils.loadAnimation(getActivity(),R.anim.treatment_button_bounce);
                mButtonSet.startAnimation(treatmentButton);

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
                //mEditTimeInput.setText("");
                Toast.makeText(getActivity(), "time set to " +mStartTimeInMillis + " minutes",Toast.LENGTH_SHORT).show();
            }
        });


        //onClick(ImageButton- go321) button function, consider better naming
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), treatmentpage.class);
                startActivity(i);
            }
        });

        //startAnimation button function
        AppCompatButton btn_start = (AppCompatButton) rootView.findViewById(R.id.btnStart);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                go321Animation.start();
                informat.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    private void checkFirstTime(View v) {
        SharedPreferences sp =   PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isFirstTime = sp.getBoolean(FIRST_TIME_KEY, true);
        btn_hint =   (ImageButton) v.findViewById(R.id.btn_treament_hint);
        btn_hint2 = (ImageButton) v.findViewById(R.id.btn_treament_hint2);
        if(!isFirstTime){
            btn_hint.setVisibility(View.INVISIBLE);
            btn_hint2.setVisibility(View.INVISIBLE);
        }else {
            btn_hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_hint.setVisibility(View.INVISIBLE);
                }
            });
            btn_hint2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_hint2.setVisibility(View.INVISIBLE);
                }
            });
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean(FIRST_TIME_KEY, false);
            edit.apply();
        }
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
    }

    public static long getmStartTimeInMillis(){
        return mStartTimeInMillis;
    }
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        go321Animation.start();
    }*/


}
