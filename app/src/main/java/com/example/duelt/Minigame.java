package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                updateText3();
            }
        });

        mLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText4();
            }
        });
    }

    private void updateText1(){
        DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);
        PetModel petmodel = new PetModel(50, 50, 0, 1, "Boo");
        petDatabaseHelper.addOne(petmodel);
        String petmodelText = "New Pet Set";
        petHungry.setText(petmodelText);
    }
    private void updateText2(){
        DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);
        PetModel petmodel = petDatabaseHelper.getCurrentStat();
        String petmodelText = "From Database: " + " Name: "+ petmodel.getName() + " Hungriness: " + petmodel.getHungriness()
                + " Exp: " + petmodel.getExp()+ " Level: " + petmodel.getLv() + " Mood: " + petmodel.getMood();
        petMood.setText(petmodelText);
    }
    private void updateText3(){
        DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);
        PetModel oldPetmodel = petDatabaseHelper.getCurrentStat();
        PetModel newPetModel = new PetModel(5, oldPetmodel.getMood(), oldPetmodel.getExp(), oldPetmodel.getExp(), oldPetmodel.getName());
        petDatabaseHelper.updateData(newPetModel);
        String petmodelText = "Made hungriness to 5 from database";
        petexp.setText(petmodelText);
    }
    private void updateText4(){
        DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);
        petDatabaseHelper.killPet();
        petlv.setText("Dropped table");
    }

}