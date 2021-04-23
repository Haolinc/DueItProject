package com.example.duelt;

import java.util.HashMap;

public class RewardCalculation {

    public static HashMap<String, Integer> calculateReward (long setDate, long dueDate){
        HashMap<String, Integer> storing = new HashMap<>();      //0 as exp 1as  currency
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        long rewardTime = setDate + (long) ((double) Math.round((dueDate - setDate)*0.9));
        storing.put("exp", (int)((rewardTime - currentTime)/3600000));
        storing.put("currency", (int)((rewardTime - currentTime)/3600000));
        return storing;
    }
}

