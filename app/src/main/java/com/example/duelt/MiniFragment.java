package com.example.duelt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.duelt.db.DatabaseHelper;

public class MiniFragment extends Fragment {
    private StatesView hungrinessState;
    private StatesView moodState;
    private StatesView expState;
    private TextView petLvNum;
    private Button states_pup_up_cancel_btn;


    public MiniFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_mini,container,false);

        //Buttons in Fragments should be written here
        // ↓↓↓↓↓↓↓↓

        //back button function
        Button btn_back = (Button) rootView.findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        //dataTesting button function
        Button btn_dataTesting = (Button) rootView.findViewById(R.id.data_testing);
        btn_dataTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Minigame.class);
                startActivity(i);
            }
        });

        //state_button function
        Button btn_state_button = (Button) rootView.findViewById(R.id.states_button);
        btn_state_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStateDialog(rootView);
            }
        });

        return rootView;
    }

    private void createStateDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View states_pop_up_view = getLayoutInflater().inflate(R.layout.states_pup_up, null);

        DatabaseHelper petDatabaseHelper = new DatabaseHelper(view.getContext());
        PetModel petmodel = petDatabaseHelper.getCurrentStat();

        //init  HungrinessState bar
        hungrinessState = states_pop_up_view.findViewById(R.id.hungrinessState);
        hungrinessState.setMaxCount(100);
        hungrinessState.setColor(Color.RED);
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //init MoodState bar
        moodState = states_pop_up_view.findViewById(R.id.moodState);
        moodState.setMaxCount(100);
        moodState.setColor(Color.BLUE);
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        petLvNum = states_pop_up_view.findViewById(R.id.pet_lv_number);
        int level_text = petmodel.getLv();
        petLvNum.setText(level_text + "  ");

        //init MoodState bar
        expState = states_pop_up_view.findViewById(R.id.expState);
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level_text));
        expState.setColor(Color.GREEN);
        int currentExp = petmodel.getExp();
        moodState.setCurrentCount(currentExp);


        states_pup_up_cancel_btn = states_pop_up_view.findViewById(R.id.close_button);

        builder.setView(states_pop_up_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        states_pup_up_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
