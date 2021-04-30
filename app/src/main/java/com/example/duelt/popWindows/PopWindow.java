package com.example.duelt.popWindows;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.R;
import com.example.duelt.RewardCalculation;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;

import java.util.HashMap;

public class PopWindow extends AppCompatActivity {
    AnimationDrawable mWinnerCup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));


        TextView textView = findViewById(R.id.textView2);

        int id= getIntent().getIntExtra("EDMID", -1);
        String table = getIntent().getStringExtra("Table");
        if (table.equals("Duedate")){
            dueDatePopWindow(id, textView);
        }
        else if (table.equals("Daily")){
            dailyPopWindow(id, textView);
        }
    }

    private void dueDatePopWindow(int id, TextView textView){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        EventDateModel eventDateModel = databaseHelper.getOneFromDueDate(id);
        HashMap<String, Integer> penalty= RewardCalculation.calculateReward(eventDateModel.getSetDateMillis(), eventDateModel.getDueDateMillis());
        cupAnimation();
        String textViewText = "";
        if (penalty.get("exp")>0){
            textViewText = "You have completed " + eventDateModel.getEventTitle() + "! You have gain " + penalty.get("exp") + " exp and " +
                    penalty.get("currency") + " currency!";
        }
        else if (penalty.get("exp")<0){
            textViewText = "Your duedate of " + eventDateModel.getEventTitle() + " has passed or you have passed above 90% of the time towards duedate! " +
                    "You have loss " + penalty.get("exp") + " exp and " + penalty.get("currency") + " currency!";
        }
        else {
            textViewText = "You have reached 90% of the time towards duedate! You did not gain anything or lose anything.";
        }

        textView.setText(textViewText);
        databaseHelper.deleteOneFromDueDate(id);
    }
    
    private void dailyPopWindow(int id, TextView textView){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String textViewText = "Your activity of " + databaseHelper.getOneFromDaily(id).getEventTitle() + " is coming up";
        textView.setText(textViewText);
        databaseHelper.updateWakedStatusInDaily(id, 1);
    }
    private void cupAnimation(){
        ImageView vWinnerCup = findViewById(R.id.cupAnimation);
        vWinnerCup.setBackgroundResource(R.drawable.winner_cup_list);
        mWinnerCup = (AnimationDrawable) vWinnerCup.getBackground();
        mWinnerCup.start();
    }
}
