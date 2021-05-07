package com.example.duelt.popWindows;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.Calculation;
import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;
import com.example.duelt.db.PetModel;
import com.example.duelt.fragments.TabActivity;
import com.example.duelt.fragments.TreatmentFragment;

import java.util.Calendar;
import java.util.HashMap;

public class PopWindow extends AppCompatActivity {
    AnimationDrawable mWinnerCup;
    Button mGreat;

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
        mGreat = findViewById(R.id.btn_great);

        int id;
        String table = getIntent().getStringExtra("Table");
        if (table!=null) {
            switch(table) {
                case "Duedate":
                    id= getIntent().getIntExtra("EDMID", -1);
                    dueDatePopWindow(id, textView);
                    break;
                case "DailyReminder":
                    id= getIntent().getIntExtra("EDMID", -1);
                    dailyReminderPopWindow(id, textView);
                    break;
                case "DailyReward":
                    id= getIntent().getIntExtra("EDMID", -1);
                    dailyRewardPopWindow(id, textView);
                    break;
                case "DailyPenalty":
                    id= getIntent().getIntExtra("EDMID", -1);
                    dailyPenalty(id, textView);
                    break;
                case "Treatment":
                    treatmentPopWindow(textView);
                    break;

                default:
                    System.out.println("error");
                    break;
        }

        }

        mGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table.equals("Treatment")) {
                    Intent i = new Intent(v.getContext(), TabActivity.class);
                    startActivity(i);
                }
                else {
                    finish();
                }

            }
        });
    }

    private void dailyPenalty(int id, TextView textView) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        EventDateModel edm = databaseHelper.getOneFromDaily(id);
        int penalty = edm.getWaked();
        databaseHelper.updateWakedStatusInDaily(id, 0);
        String textViewText = "You have skipped " + edm.getEventTitle() + " " + penalty +
                " times,  therefore you have lose " + penalty*10 + " currency.";
        textView.setText(textViewText);
        databaseHelper.updateCurrency(databaseHelper.getCurrency() - penalty*10);
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
        HashMap<String, Integer> penalty= Calculation.calculateReward(eventDateModel.getSetDateMillis(), eventDateModel.getDueDateMillis());
        cupAnimation();
        String textViewText = "";

        if (penalty.get("exp")>0){
            textViewText = "You have completed " + eventDateModel.getEventTitle() + "! You have gain " + penalty.get("exp") + " exp and " +
                    penalty.get("currency") + " currency!";
            PetModel pm = new PetModel(this);
            pm.expPlus(this, penalty.get("exp"));
            databaseHelper.updateData(pm);
            databaseHelper.updateCurrency(databaseHelper.getCurrency()+penalty.get("currency"));
        }

        else if (penalty.get("exp")<0){
            textViewText = "Your duedate of " + eventDateModel.getEventTitle() + " has passed or you have passed above 90% of the time towards duedate! " +
                    "You have loss " + penalty.get("currency") + " currency!";
            databaseHelper.updateCurrency(databaseHelper.getCurrency()-penalty.get("currency"));
        }
        else {
            textViewText = "You have reached 90% of the time towards duedate! You did not gain anything or lose anything.";
        }

        textView.setText(textViewText);
        databaseHelper.deleteOneFromDueDate(id);
    }

    private void dailyRewardPopWindow (int id, TextView textView) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        PetModel petModel = databaseHelper.getCurrentStat();
        EventDateModel edm = databaseHelper.getOneFromDaily(id);
        petModel.setExp(petModel.getExp()+10);
        databaseHelper.updateCurrency(databaseHelper.getCurrency()+10);
        edm.setWaked(0);
        edm.setWakedTime(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + Calendar.getInstance().get(Calendar.YEAR)*1000);
        databaseHelper.updateDaily(edm);
        String textViewText = "You have gain 10 exp and 10 currency!";
        textView.setText(textViewText);
    }

    private void dailyReminderPopWindow(int id, TextView textView){
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
