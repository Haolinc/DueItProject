package com.example.duelt.popWindows;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.duelt.Calculation;
import com.example.duelt.LoadingActivity;
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
    int id, penaltyHolding;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        databaseHelper = new DatabaseHelper(this);

        //Pop window size
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

        mGreat.setText("Okay");
        mGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table.equals("Treatment")) {    //If the message is from treatment page then jump back to main
                    Intent i = new Intent(v.getContext(), TabActivity.class);
                    startActivity(i);
                }
                else if(table.equals("Mini")){    //This message means the pet is starved to dead
                    Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    finish();
                }
            }
        });
    }

    //From daily fragment and calculate the penalty for skipping event
    private String dailyPenalty(int id) {
        int penalty = getIntent().getIntExtra("WakedTimes", 0);
        EventDateModel edm = databaseHelper.getOneFromDaily(id);
        PetModel pet = databaseHelper.getCurrentStat();
        pet.setMood(pet.getMood()-10);
        databaseHelper.updateWakedStatusInDaily(id, 0);
        databaseHelper.updateCurrency(databaseHelper.getCurrency() - penalty*10);
        databaseHelper.updateData(pet);
        String textViewText = "You have skipped " + edm.getEventTitle() + " " + penalty +
                " times,  therefore you have lose " + penalty*10 + " currency and -10 mood of your pet.";

        return textViewText;
    }

    //From treatment activity and give user reward
    private String treatmentPopWindow(){
        PetModel petmodel = databaseHelper.getCurrentStat();
        long mTimer = TreatmentFragment.getmStartTimeInMillis();

        int currency = ((int)mTimer/6);
        int exp = ((int)mTimer/6);
        databaseHelper.updateCurrency(databaseHelper.getCurrency()+currency);
        petmodel.expPlus(this, databaseHelper.getCurrency()+exp);
        databaseHelper.updateData(petmodel);

        return "You have earned " + currency + " currency and " + exp + " exp.";
    }

    //From memo activity and calculate whether user will get reward or penalty
    private String dueDatePopWindow(int id){
        EventDateModel eventDateModel = databaseHelper.getOneFromDueDate(id);
        HashMap<String, Integer> penalty= Calculation.calculateReward(eventDateModel.getSetDateMillis(), eventDateModel.getDueDateMillis(), (double)databaseHelper.getCurrentStat().getMood());
        String textViewText = "";

        penaltyHolding = penalty.get("exp");

        if (penaltyHolding>0){
            textViewText = "You have completed " + eventDateModel.getEventTitle() + "! You have gain " + penalty.get("exp") + " exp and " +
                    penalty.get("currency") + " currency!";
            PetModel pm = new PetModel(this);
            pm.expPlus(this, penaltyHolding);
            databaseHelper.updateData(pm);
            databaseHelper.updateCurrency(databaseHelper.getCurrency()+penaltyHolding);
        }

        else if (penaltyHolding<0){
            textViewText = "Your duedate of " + eventDateModel.getEventTitle() + " has passed or you have passed above 90% of the time towards duedate! " +
                    "You have loss " + penalty.get("currency") + " currency!";
            databaseHelper.updateCurrency(databaseHelper.getCurrency()-penaltyHolding);
        }
        else {
            textViewText = "You have reached 90% of the time towards duedate! You did not gain anything or lose anything.";
        }
        databaseHelper.deleteOneFromDueDate(id);
        return textViewText;
    }

    //From daily fragment and grant user reward
    private String dailyRewardPopWindow (int id) {
        PetModel petModel = databaseHelper.getCurrentStat();
        EventDateModel edm = databaseHelper.getOneFromDaily(id);
        petModel.setExp(petModel.getExp()+10);
        databaseHelper.updateCurrency(databaseHelper.getCurrency()+10);
        edm.setWaked(0);
        edm.setWakedTime(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + Calendar.getInstance().get(Calendar.YEAR)*1000);
        databaseHelper.updateDaily(edm);
        return "You have gain 10 exp and 10 currency!";
    }

    //From AlarmReceiver to remind user to do their event
    private String dailyReminderPopWindow(int id){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String textViewText = "Your activity of " + databaseHelper.getOneFromDaily(id).getEventTitle() + " is coming up";
        databaseHelper.updateWakedStatusInDaily(id, 1);
        return textViewText;
    }

    //Use for avoiding repetitive code
    private void cupAnimation(){
        Glide.with(this)
                .asGif()
                .load(R.drawable.trophy)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        return false;
                    }
                })
                .fitCenter()
                .into(vWinnerCup);
    }

    private void dueDateAnimation(int penalty){
        if (penalty > 0) {
            cupAnimation();
        }
        else {
            vWinnerCup.setBackgroundResource(R.drawable.saltyfish_logo);
        }

    }

    //Use to make the activity to start quicker
    private class Preload extends AsyncTask<String, String, String[]> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String[] doInBackground(String ... strings){
            String[] str = new String[2];
            str[0] = strings[0];
            //Base on sent from which class to perform different action
            switch(strings[0]) {
                case "Duedate":
                    str[1]=dueDatePopWindow(id);
                    return str;
                case "DailyReminder":
                    str[1] = dailyReminderPopWindow(id);
                    return str;
                case "DailyReward":
                    str[1] = dailyRewardPopWindow(id);
                    return str;
                case "DailyPenalty":
                    str[1] = dailyPenalty(id);
                    return str;
                case "Treatment":
                    str[1] = treatmentPopWindow();
                    return str;
                case "Mini":
                   databaseHelper.killPet();
                   str[1] = "your pet is dead.";
                   return str;
                default:
                    return null;
            }
        }
        @Override
        protected void onPostExecute(String [] string){
            super.onPostExecute(string);
            textView.setText(string[1]);

            switch(string[0]) {
                case "Duedate":
                    dueDateAnimation(penaltyHolding);
                    return;
                case "Treatment":
                case "DailyReward":
                    cupAnimation();
                    return;
                case "DailyReminder":
                    vWinnerCup.setBackgroundResource(R.drawable.warning_sign);

                    return;
                case "DailyPenalty":
                case "Mini":
                    vWinnerCup.setBackgroundResource(R.drawable.saltyfish_logo);
                    return;
                default:
            }
        }
    }
}

