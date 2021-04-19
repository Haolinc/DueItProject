package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

public class Minigame extends AppCompatActivity {
    private TextView petHungry;
    private TextView petMood;
    private TextView petexp;
    private TextView petlv;
    private Button mHungry;
    private Button mMood;
    private Button mExp;
    private Button mLevel;
    private Button mPlay;

    private StatesView hungrinessState;
    private StatesView moodState;
    private StatesView expState;

    DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);

        petHungry = findViewById(R.id.pet_hungriness);
        petMood = findViewById(R.id.pet_mood);
        petexp = findViewById(R.id.pet_exp);
        petlv = findViewById(R.id.pet_lv);
        mHungry = findViewById(R.id.btn_hungry);
        mMood = findViewById(R.id.btn_mood);
        mExp = findViewById(R.id.btn_exp);
        mLevel = findViewById(R.id.btn_lv);
        mPlay = findViewById(R.id.btn_play);

        PetModel petmodel = petDatabaseHelper.getCurrentStat();

        //init  HungrinessState bar
        hungrinessState = findViewById(R.id.hungrinessState);
        hungrinessState.setMaxCount(100);
        hungrinessState.setColor(Color.RED);
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //init MoodState bar
        moodState = findViewById(R.id.moodState);
        moodState.setMaxCount(100);
        moodState.setColor(Color.BLUE);
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        //init MoodState bar
        expState = findViewById(R.id.expState);

        mHungry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText1();
            }
        });

        mMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText2();
            }
        });

        mExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               feedPet();
            }
        });

        mLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText4();
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toyPet();
            }
        });
    }

    private void updateText1(){
        PetModel petmodel = new PetModel(50, 50, 0, 1, "Boo");
        petDatabaseHelper.addOne(petmodel);
        String petmodelText = "New Pet Set";
        petHungry.setText(petmodelText);
    }
    private void updateText2(){
        PetModel petmodel = petDatabaseHelper.getCurrentStat();
        String petmodelText = "From Database: " + " Name: "+ petmodel.getName() + " Hungriness: " + petmodel.getHungriness()
                + " Exp: " + petmodel.getExp()+ " Level: " + petmodel.getLv() + " Mood: " + petmodel.getMood();
        petMood.setText(petmodelText);
    }


    private void toyPet(){
        PetModel oldPetmodel = petDatabaseHelper.getCurrentStat();
        int currentMood = oldPetmodel.getMood();
        currentMood = toy(currentMood);
        //update mood state bar
        moodState.setCurrentCount(currentMood);
        //check your pet's current mood
        if(currentMood <= 40){
            Toast.makeText(this, "your pet is sad!", Toast.LENGTH_LONG).show();
        } else if (currentMood >= 40 && currentMood<=60){
            Toast.makeText(this, "your pet is happy!", Toast.LENGTH_LONG).show();
        } else if (currentMood >= 80) {
            Toast.makeText(this, "your pet is excited!", Toast.LENGTH_LONG).show();
        }
        oldPetmodel.setMood(currentMood);
        petDatabaseHelper.updateData(oldPetmodel);
        String petmodelText = "add 5 mood to the pet";
        petMood.setText(petmodelText);
    }
    //play with pet, mood will increase by 5
    private int toy(int mood) {
        mood += 5;
        if(mood > 100){
            mood = 100;
        }

        return mood;
    }



    private void feedPet(){
        PetModel oldPetmodel = petDatabaseHelper.getCurrentStat();
        //get old pet's hungriness
        int currentHungry = oldPetmodel.getHungriness();
        //feed pet function
        currentHungry = feed(currentHungry);
        //set hungry bar state
        hungrinessState.setCurrentCount(currentHungry);
        oldPetmodel.setHungriness(currentHungry);
        //update pet sqlite
        petDatabaseHelper.updateData(oldPetmodel);
        String petmodelText = "add 5 hungriness to the pet";
        petexp.setText(petmodelText);
    }

    //feed the pet, hungriness will increase by 5
    public int feed(int hungriness){
        hungriness += 5;
        if (hungriness > 100){
            hungriness = 100;
        }
        return hungriness;
    }

    private void updateText4(){
        petDatabaseHelper.killPet();
        petlv.setText("Dropped table");
    }


    public void openShop(View v){
        /*Intent i = new Intent(this, ShopPage.class);
        startActivity(i);*/
        startActivity(new Intent(this,ShopPage.class));
    }
}
