package com.example.duelt.popWindows;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;
import com.example.duelt.fragments.TreatmentFragment;

import org.w3c.dom.Text;

public class Congratulation_Window extends AppCompatActivity {
    AnimationDrawable mWinnerCup;
    private TextView mTextViewDuration;
    private TextView mTextViewReward;
    private TextView mTextViewRewardExp;
    boolean mWinnerCupIsRunning;
    long mTimer = TreatmentFragment.getmStartTimeInMillis();
    DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);
    //testcode//**********************************************************************************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation__window);

        PetModel petmodel = petDatabaseHelper.getCurrentStat();

        ImageView vWinnerCup = (ImageView)findViewById(R.id.img_winner_cup);
        vWinnerCup.setBackgroundResource(R.drawable.winner_cup_list);
        mWinnerCup = (AnimationDrawable) vWinnerCup.getBackground();
        mWinnerCup.start();
        mTextViewDuration = findViewById(R.id.textview_duration);
        mTextViewReward = findViewById(R.id.textview_reward2);
        mTextViewRewardExp = findViewById(R.id.textview_reward);

        mTextViewDuration.setText(mTimer+" min");

        mTextViewReward.setText("you gain " + currencyGain(mTimer) + " dollar");

        mTextViewRewardExp.setText("you gain " + ExpGain(mTimer) + " exp");

        addCurrency();
        addExp();
    }


    private void addExp(){
        PetModel petModel = petDatabaseHelper.getCurrentStat();
        int gotExp = ExpGain(mTimer);
        petModel.expPlus(this, gotExp);
        petDatabaseHelper.updateData(petModel);
    }

    private int ExpGain(long mTimer){
        int expGain = (int)mTimer/6;
        return expGain;
    }

    private void addCurrency(){
        int totalCurrency = petDatabaseHelper.getCurrency();
        int moneyGain = currencyGain(mTimer);

        totalCurrency = moneyGain + totalCurrency;

        petDatabaseHelper.updateCurrency(totalCurrency);

    }


    private int currencyGain(long mTimer){
        int moneyGain = (int)mTimer/ 6;
        return moneyGain;
    }
}


//    private void getCurrentStat(){{

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