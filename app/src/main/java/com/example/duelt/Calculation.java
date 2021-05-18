package com.example.duelt;

import android.content.Context;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;

import java.util.Calendar;
import java.util.HashMap;

public class Calculation {

    //Calculation for memo duedate reward and penalty
    public static HashMap<String, Integer> calculateReward (long setDate, long dueDate, double mood){
        HashMap<String, Integer> storing = new HashMap<>();      //0 as exp 1 as  currency
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        long rewardTime = setDate + (long) ((double) Math.round((dueDate - setDate)*0.9));   //Going over 90% will be penalty
        int penalty = (int) ((rewardTime - currentTime)/3600000);
        if (penalty > 0)
            penalty= (int)((double)(penalty)*(mood/100));
        storing.put("exp", penalty);
        storing.put("currency", penalty);
        return storing;
    }

    //Calculate Hunger and Mood decrement even if the user did not start the application
    public static void calculateHungerAndMood(Context ctx) {
        DatabaseHelper databaseHelper = new DatabaseHelper(ctx);

        long currentTime = Calendar.getInstance().getTimeInMillis();
        long updatedTime = databaseHelper.getUpdatedTime();
        long timeElapseRemainder= (currentTime - updatedTime)%(1000*60*60);
        int timeElapse = (int) ((currentTime - updatedTime)/(1000*60*60));

        PetModel petmodel = databaseHelper.getCurrentStat();

        petmodel.setHungriness(petmodel.getHungriness() - timeElapse);
        int mood = petmodel.getMood() - timeElapse;
        petmodel.setMood(Math.max(mood, 0));

        databaseHelper.updateUpdatedTime(currentTime-timeElapseRemainder);
        databaseHelper.updateData(petmodel);
    }
}

