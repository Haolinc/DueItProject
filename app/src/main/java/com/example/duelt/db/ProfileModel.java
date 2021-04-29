package com.example.duelt.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileModel {
    int memoCount;
    int dailyCount;
    int treatmentCount;
    int numberOfTheWeek;
    int weekStart;

    Calendar cal = Calendar.getInstance();
    int currentWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);


    //
    public boolean checkWeekMatch(int weekStart){
        if(currentWeekOfYear != weekStart){
            return true;
        } else { return false;}
    }

    public int getMemoCount(){return memoCount;}
    public int getDailyCount(){return dailyCount;}
    public int getTreatmentCount(){return treatmentCount;}
    public int getNumberOfTheWeek(){return numberOfTheWeek;}
    public int getWeekStart(){return weekStart;}

    public void setMemoCount(int memoCount){this.memoCount = memoCount;}
    public void setDailyCount(int dailyCount){this.dailyCount = dailyCount;}
    public void setTreatmentCount(int treatmentCount){this.treatmentCount=treatmentCount;}
    public void setNumberOfTheWeek(int numberOfTheWeek){this.numberOfTheWeek = numberOfTheWeek;}
    public void setWeekStart(int weekStart){this.weekStart = weekStart;}
}

