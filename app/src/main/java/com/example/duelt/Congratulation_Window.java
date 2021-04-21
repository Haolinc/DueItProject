package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;

public class Congratulation_Window extends AppCompatActivity {
    AnimationDrawable mWinnerCup;
    private TextView mTextViewDuration;
    boolean mWinnerCupIsRunning;
    long mTimer = TreatmentFragment.getmStartTimeInMillis();
    DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);
    //testcode//**********************************************************************************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation__window);

        ImageView vWinnerCup = (ImageView)findViewById(R.id.img_winner_cup);
        vWinnerCup.setBackgroundResource(R.drawable.winner_cup_list);
        mWinnerCup = (AnimationDrawable) vWinnerCup.getBackground();
        mWinnerCup.start();
        mTextViewDuration = findViewById(R.id.textview_duration);

        mTextViewDuration.setText(mTimer+" min");

    }
}


//    private void getCurrentStat(){
//        PetModel petmodel = petDatabaseHelper.getCurrentStat();
//        String petmodelText = "From Database: " + " Name: "+ petmodel.getName() + " Hungriness: " + petmodel.getHungriness()
//                + " Exp: " + petmodel.getExp()+ " Level: " + petmodel.getLv() + " Mood: " + petmodel.getMood();
//        Toast.makeText(this, petmodelText, Toast.LENGTH_SHORT).show();
//    }

//    private void getCurrentStat2(){
//        ItemModel itemModel = petDatabaseHelper.getItemStat();
//        String itemmodelText = "From Database: " + " Name: "+ ItemModel.getName() + " Hungriness: " + ItemModel.getHungriness()
//                + " Exp: " + ItemModel.getExp();
//        Toast.makeText(this, itemmodelText, Toast.LENGTH_SHORT).show();
//    }

//    private void gainRewards(){
//        PetModel oldPetmodel = petDatabaseHelper.getCurrentStat();
//        //ItemModel oldItemModel = petDatabaseHelper.getCurrentStat2();;
//        //get old pet's hungriness
//        //int currentExp = oldPetmodel.getHungriness();
//        //int currentCurrency =
//        //feed pet function
//        //currentHungry = feed(currentHungry);
//        //set hungry bar state
//        //hungrinessState.setCurrentCount(currentHungry);
//       //oldPetmodel.setHungriness(currentHungry);
//        //update pet sqlite
//        //petDatabaseHelper.updateData(oldPetmodel);
//        //String petmodelText = "add 5 hungriness to the pet";
//        //Toast.makeText(this, petmodelText, Toast.LENGTH_SHORT).show();
//
//    }