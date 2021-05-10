package com.example.duelt;

import android.content.Context;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;

import java.util.Calendar;
import java.util.HashMap;

public class Calculation {

    public static HashMap<String, Integer> calculateReward (long setDate, long dueDate){
        HashMap<String, Integer> storing = new HashMap<>();      //0 as exp 1as  currency
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        long rewardTime = setDate + (long) ((double) Math.round((dueDate - setDate)*0.9));
        storing.put("exp", (int)((rewardTime - currentTime)/3600000));
        storing.put("currency", (int)((rewardTime - currentTime)/3600000));
        return storing;
    }
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

