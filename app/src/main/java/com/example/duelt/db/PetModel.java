package com.example.duelt.db;

import android.content.Context;

public class PetModel {
    private int hungriness;
    private int  mood;
    private int exp;
    private int level = 1;
    private String name;

    /*private int currency;
    private int food;
    private int toy;*/

    public PetModel(int hungriness, int mood, int exp, int level, String name){
        this.hungriness = hungriness;
        this.mood = mood;
        this.exp = exp;
        this.level = level;
        this.name= name;
    }

    public PetModel (Context ctx){
        DatabaseHelper db = new DatabaseHelper(ctx);
        PetModel pm = db.getCurrentStat();
        this.hungriness = pm.getHungriness();
        this.mood = pm.getMood();
        this.exp = pm.getExp();
        this.level = pm.getLv();
        this.name = pm.getName();
    }

    /*public ShopModel(int currency, int food, int toy) {
        this.currency = currency;
        this.food = food;
        this.toy = toy;
    }*/
    //feed the pet, hungriness will increase by 5
    public void feed(){
        hungriness += 5;
        if (hungriness> 100){
            hungriness = 100;
        }
    }
    //when use a toy, pet mood will increase by 5
    public void play(){
        mood += 5;
        if (mood > 100) {
            mood = 100;
        }
    }
    //if complete a task, pet will gain exp
    public void expPlus(Context context, int gotExp){
        exp += gotExp;
        lvUp(context);
    }
    //if exp is bigger than 100, pet will level up
    public void lvUp(Context context){
        DatabaseHelper db = new DatabaseHelper(context);
        int expForLevelUp = db.getExpForLevelUp(level);
        while (exp> expForLevelUp ){
            exp = exp - expForLevelUp;
            level += 1;
            expForLevelUp = db.getExpForLevelUp(level);
        }
    }

    //time pass states decrease
    public void passTime(int num){
        hungriness-= num;
        mood-= num;
        if (hungriness<0){
            hungriness = 0;
        }
        if(mood < 0){
            mood = 0;
        }
    }


    //getter
    public int getHungriness(){
        return hungriness;
    }

    public int getMood(){
        return mood;
    }

    public int getLv(){
        return level;
    }

    public int getExp() { return exp; }

    public String getName() {return name;}
    /*//shop getter
    public int getFood(){return food; }

    public int getToy(){return toy; }

    public int getCurrency(){return currency; }*/
    //setter
    public void setHungriness(int hungriness){
        this.hungriness = hungriness;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setLv(int level) {
        this.level = level;
    }

    public void setExp(int Exp) {
        this.exp = exp;
    }
    /*//shop setter
    public void  setCurrency(int currency) { this.currency = currency; }

    public void setFood(int food) { this.food = food; }

    public void setToy(int toy) {this.toy = toy;}*/
}
