package com.example.duelt;

public class petModel {
    private int hungriness;
    private int  mood;
    private int exp;
    private int level = 1;

    public petModel(int hungriness, int mood, int exp, int level){
        this.hungriness = hungriness;
        this.mood = mood;
        this.exp = exp;
        this.level = level;
    }
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
    public void experience(){
        exp += 5;
    }
    //if exp is bigger than 100, pet will level up
    public void lvUp(){
        if (exp>100){
            level += 1;
        }
    }

    //time pass states decrease
    public void passTime(){
        hungriness--;
        mood--;
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
}
