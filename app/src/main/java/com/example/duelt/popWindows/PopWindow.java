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
import com.example.duelt.db.PetModel;
import com.example.duelt.fragments.TreatmentFragment;

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
        int id;
        String table = getIntent().getStringExtra("Table");
        switch(table) {
            case "Duedate":
                id= getIntent().getIntExtra("EDMID", -1);
                dueDatePopWindow(id, textView);
                break;
            case "Daily":
                id= getIntent().getIntExtra("EDMID", -1);
                dailyPopWindow(id, textView);
                break;
            case "Treatment":
                treatmentPopWindow(textView);

            default: System.out.println("error");
        }
    }

    private void treatmentPopWindow(TextView textView){
        DatabaseHelper DatabaseHelper = new DatabaseHelper(this);
        PetModel petmodel = DatabaseHelper.getCurrentStat();
        long mTimer = TreatmentFragment.getmStartTimeInMillis();

        ImageView vWinnerCup = findViewById(R.id.cupAnimation);
        vWinnerCup.setBackgroundResource(R.drawable.winner_cup_list);
        mWinnerCup = (AnimationDrawable) vWinnerCup.getBackground();
        mWinnerCup.start();

        int currency = ((int)mTimer/6);
        int exp = ((int)mTimer/6);
        DatabaseHelper.updateCurrency(DatabaseHelper.getCurrency()+currency);
        petmodel.expPlus(this, DatabaseHelper.getCurrency()+exp);
        DatabaseHelper.updateData(petmodel);

        String textViewText = "You have earned " + currency + " currency and " + exp + " exp.";
        textView.setText(textViewText);
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
