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

import java.util.ArrayList;

public class Minigame extends AppCompatActivity {
    private TextView petHungry;
    private TextView petMood;
    private TextView petexp;
    private TextView petlv;
    private Button setPet;
    private Button getCurrentStat;
    private Button mExp;
    private Button mDelete;
    private Button mPlay;
    private Button feed;
    private StatesView hungrinessState;
    private StatesView moodState;
    private StatesView expState;
    private TextView petLvNum;
    //TESTING Purpose;
    private Button showEXP_Table;
    DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);

        petHungry = findViewById(R.id.pet_hungriness);
        petMood = findViewById(R.id.pet_mood);
        petexp = findViewById(R.id.pet_exp);
        petlv = findViewById(R.id.pet_lv);
        setPet = findViewById(R.id.btn_set_pet);
        getCurrentStat = findViewById(R.id.btn_getCurrentStat);
        mExp = findViewById(R.id.btn_exp_puls);
        mDelete = findViewById(R.id.btn_delete);
        mPlay = findViewById(R.id.btn_play);
        petLvNum = findViewById(R.id.pet_lv_number);
        feed = findViewById(R.id.btn_feed);
        //TESTING Purpose;
        showEXP_Table = findViewById(R.id.SHOW_EXP_TABLE);

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

        int level = petmodel.getLv();
        petLvNum.setText(level + " ");

        //init MoodState bar
        expState = findViewById(R.id.expState);
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level));
        expState.setColor(Color.GREEN);
        int currentExp = petmodel.getExp();
        moodState.setCurrentCount(currentExp);

        setPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPet();
            }
        });

        mExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expPlus(5);
            }
        });

        getCurrentStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentStat();
            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               feedPet();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killPet();
            }
        });

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toyPet();
            }
        });

        //TESTING Purpose;
        showEXP_Table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showExpTable();
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //update Pet StatesBar
        updatePetStates();
    }

    private void updatePetStates() {
        PetModel petmodel = petDatabaseHelper.getCurrentStat();

        moodState.setCurrentCount(petmodel.getMood());
        hungrinessState.setCurrentCount(petmodel.getHungriness());
        int level = petmodel.getLv();
        petLvNum.setText(level + " ");
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level));
        expState.setCurrentCount(petmodel.getExp());


    }

    private void showExpTable() {
        ArrayList<Integer> expTable = petDatabaseHelper.getExpForLevelTable();
        Toast.makeText(this, expTable.toString(), Toast.LENGTH_SHORT).show();
    }

    private void setPet(){
        PetModel petmodel = new PetModel(0, 0, 0, 1, "Boo");
        petDatabaseHelper.addOne(petmodel);
        String petmodelText = "New Pet Set";
        Toast.makeText(this, petmodelText, Toast.LENGTH_SHORT).show();
    }
    private void getCurrentStat(){
        PetModel petmodel = petDatabaseHelper.getCurrentStat();
        String petmodelText = "From Database: " + " Name: "+ petmodel.getName() + " Hungriness: " + petmodel.getHungriness()
                + " Exp: " + petmodel.getExp()+ " Level: " + petmodel.getLv() + " Mood: " + petmodel.getMood();
        Toast.makeText(this, petmodelText, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, petmodelText, Toast.LENGTH_SHORT).show();

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
        Toast.makeText(this, petmodelText, Toast.LENGTH_SHORT).show();

    }

    //feed the pet, hungriness will increase by 5
    public int feed(int hungriness){
        hungriness += 5;
        if (hungriness > 100){
            hungriness = 100;
        }
        return hungriness;
    }

    private void killPet(){
        petDatabaseHelper.killPet();
        petlv.setText("Dropped table");
    }


    public void openShop(View v){
        /*Intent i = new Intent(this, ShopPage.class);
        startActivity(i);*/
        startActivity(new Intent(this,ShopPage.class));
    }

    private void expPlus(int gotExp) {
        PetModel petModel = petDatabaseHelper.getCurrentStat();
        petModel.expPlus(this, gotExp);
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(petModel.getLv()));
        expState.setCurrentCount(petModel.getExp());
        petLvNum.setText(petModel.getLv() + " ");
        petDatabaseHelper.updateData(petModel);

    }
}
