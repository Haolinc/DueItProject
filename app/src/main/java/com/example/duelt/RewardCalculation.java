package com.example.duelt;

import java.util.Date;

public class RewardCalculation {

    public static int[] calculateReward (Date setDate, Date dueDate){
        int [] arr = new int[2];      //0 as exp 1 as currency
        long currentTime = java.util.Calendar.getInstance().getTime().getTime();
        long rewardTime = setDate.getTime() + (long) ((double) Math.round((dueDate.getTime() - setDate.getTime())*0.9));
        if (currentTime>rewardTime) {
            arr[0] = (int)((currentTime-rewardTime)/3600000);
            arr[1] = (int)((currentTime-rewardTime)/3600000);
        }
        else {
            arr[0] = (int)((rewardTime-currentTime)/3600000);
            arr[1] = (int)((rewardTime-currentTime)/3600000);
        }
        return arr;
    }
}

