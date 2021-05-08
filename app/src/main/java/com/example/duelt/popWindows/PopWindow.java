package com.example.duelt.popWindows;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
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
    ImageView vWinnerCup;
    DatabaseHelper databaseHelper;
    int id;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        databaseHelper = new DatabaseHelper(this);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.5));


        textView = findViewById(R.id.textView2);
        mGreat = findViewById(R.id.btn_great);
        vWinnerCup = findViewById(R.id.cupAnimation);

        id = getIntent().getIntExtra("EDMID", 0);
        String table = getIntent().getStringExtra("Table");

        Preload p = new Preload();
        p.execute(table);

        switch(table) {
            case "Duedate":
                break;
            case "DailyReminder":
                break;
            case "DailyReward":
                break;
            case "DailyPenalty":
                break;
            case "Treatment":
                break;
            default:
                break;
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

    private String dailyPenalty(int id) {
        EventDateModel edm = databaseHelper.getOneFromDaily(id);
        int penalty = edm.getWaked();
        databaseHelper.updateWakedStatusInDaily(id, 0);
        String textViewText = "You have skipped " + edm.getEventTitle() + " " + penalty +
                " times,  therefore you have lose " + penalty*10 + " currency.";
        databaseHelper.updateCurrency(databaseHelper.getCurrency() - penalty*10);
        return textViewText;
    }

    private String treatmentPopWindow(){
        PetModel petmodel = databaseHelper.getCurrentStat();
        long mTimer = TreatmentFragment.getmStartTimeInMillis();

//        cupAnimation();

        int currency = ((int)mTimer/6);
        int exp = ((int)mTimer/6);
        databaseHelper.updateCurrency(databaseHelper.getCurrency()+currency);
        petmodel.expPlus(this, databaseHelper.getCurrency()+exp);
        databaseHelper.updateData(petmodel);

        String textViewText = "You have earned " + currency + " currency and " + exp + " exp.";
        return textViewText;
    }

    private String dueDatePopWindow(int id){
        EventDateModel eventDateModel = databaseHelper.getOneFromDueDate(id);
        HashMap<String, Integer> penalty= Calculation.calculateReward(eventDateModel.getSetDateMillis(), eventDateModel.getDueDateMillis());
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
        databaseHelper.deleteOneFromDueDate(id);
        return textViewText;
    }

    private String dailyRewardPopWindow (int id) {
        PetModel petModel = databaseHelper.getCurrentStat();
        EventDateModel edm = databaseHelper.getOneFromDaily(id);
        petModel.setExp(petModel.getExp()+10);
        databaseHelper.updateCurrency(databaseHelper.getCurrency()+10);
        edm.setWaked(0);
        edm.setWakedTime(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + Calendar.getInstance().get(Calendar.YEAR)*1000);
        databaseHelper.updateDaily(edm);
        String textViewText = "You have gain 10 exp and 10 currency!";
        return textViewText;
    }

    private String dailyReminderPopWindow(int id){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String textViewText = "Your activity of " + databaseHelper.getOneFromDaily(id).getEventTitle() + " is coming up";
        databaseHelper.updateWakedStatusInDaily(id, 1);
        return textViewText;
    }

    private void cupAnimation(){
        vWinnerCup.setBackgroundResource(R.drawable.winner_cup_list);
        mWinnerCup = (AnimationDrawable) vWinnerCup.getBackground();
        mWinnerCup.start();
    }

    private class Preload extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String ... strings){
            switch(strings[0]) {
                case "Duedate":
                    return dueDatePopWindow(id);
                case "DailyReminder":
                    return dailyReminderPopWindow(id);
                case "DailyReward":
                    return dailyRewardPopWindow(id);
                case "DailyPenalty":
                    return dailyPenalty(id);
                case "Treatment":
                    return treatmentPopWindow();
                default:
                    return null;
            }
        }
        @Override
        protected void onPostExecute(String string){
            super.onPostExecute(string);
            textView.setText(string);
            cupAnimation();
        }

    }
}

