package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;

import java.util.ArrayList;

public class Minigame extends AppCompatActivity {


    private Button getCurrentStat;
    private Button btn_exp_puls;
    private Button mPlay;
    private Button feed;
    private StatesView hungrinessState;
    private StatesView moodState;
    private StatesView expState;
    private TextView petLvNum;

    private Button btn_states_pup_up_cancel;
    private Button btn_open_state;
    private Button btn_open_shop;

    //TESTING Purpose;
    private Button showEXP_Table;
    DatabaseHelper petDatabaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame);


        getCurrentStat = findViewById(R.id.btn_getCurrentStat);
        btn_exp_puls = findViewById(R.id.btn_exp_puls);
        mPlay = findViewById(R.id.btn_play);
        petLvNum = findViewById(R.id.pet_lv_number);
        feed = findViewById(R.id.btn_feed);
        btn_open_state = findViewById(R.id.btn_open_state);
        btn_open_shop = findViewById(R.id.btn_shop);
        //TESTING Purpose;
        showEXP_Table = findViewById(R.id.SHOW_EXP_TABLE);

        PetModel petmodel = petDatabaseHelper.getCurrentStat();


        btn_exp_puls.setOnClickListener(new View.OnClickListener() {
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


        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toyPet();
            }
        });

        btn_open_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStateDialog();
            }
        });

        btn_open_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createShopDialog();
            }
        });

        //TESTING Purpose;
        showEXP_Table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createStateDialog();
            }

        });

    }





    private void showExpTable() {
        ArrayList<Integer> expTable = petDatabaseHelper.getExpForLevelTable();
        Toast.makeText(this, expTable.toString(), Toast.LENGTH_SHORT).show();
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



    public void openShop(View v){
        /*Intent i = new Intent(this, ShopPage.class);
        startActivity(i);*/
        startActivity(new Intent(this,ShopPage.class));
    }

    private void expPlus(int gotExp) {
        PetModel petModel = petDatabaseHelper.getCurrentStat();
        petModel.expPlus(this, gotExp);
        petDatabaseHelper.updateData(petModel);

    }

    private void createStateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View states_pop_up_view = getLayoutInflater().inflate(R.layout.states_pup_up, null);

        PetModel petmodel = petDatabaseHelper.getCurrentStat();

        //init  HungrinessState bar
        hungrinessState = states_pop_up_view.findViewById(R.id.hungrinessState);
        hungrinessState.setMaxCount(100);
        hungrinessState.setColor(Color.RED);
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //init MoodState bar
        moodState = states_pop_up_view.findViewById(R.id.moodState);
        moodState.setMaxCount(100);
        moodState.setColor(Color.BLUE);
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        petLvNum = states_pop_up_view.findViewById(R.id.pet_lv_number);
        int level_text = petmodel.getLv();
        petLvNum.setText(level_text + "  ");

        //init exp bar
        expState = states_pop_up_view.findViewById(R.id.expState);
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level_text));
        expState.setColor(Color.GREEN);
        int currentExp = petmodel.getExp();
        expState.setCurrentCount(currentExp);


        btn_states_pup_up_cancel = states_pop_up_view.findViewById(R.id.close_button);

        builder.setView(states_pop_up_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_states_pup_up_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void createShopDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View shop_pop_up_view = getLayoutInflater().inflate(R.layout.shop_pup_up, null);

        Button btn_shop_pop_up_cancel = shop_pop_up_view.findViewById(R.id.btn_close_shop);
        Button btn_buy_food = shop_pop_up_view.findViewById(R.id.btn_buy_food);
        Button btn_buy_toy = shop_pop_up_view.findViewById(R.id.btn_buy_toy);

        TextView text_currency = shop_pop_up_view.findViewById(R.id.textView_currency);
        TextView text_toy_price = shop_pop_up_view.findViewById(R.id.textView_foodPrice);
        TextView text_food_price = shop_pop_up_view.findViewById(R.id.textView_toyPrice);
        TextView text_foodNum = shop_pop_up_view.findViewById(R.id.textView_food_num);
        TextView text_toyNum = shop_pop_up_view.findViewById(R.id.textView_toy_num);


        int totalCurrency = petDatabaseHelper.getCurrency();
        int numOfFood = petDatabaseHelper.getFood();
        int numOfToy  = petDatabaseHelper.getToy();
        int foodPrice = 10;
        int toyPrice = 10;
        text_currency.setText(totalCurrency + "");
        text_food_price.setText(foodPrice + "");
        text_toy_price.setText(toyPrice + "");
        text_foodNum.setText(numOfFood + "");
        text_toyNum.setText(numOfToy + "");

        btn_buy_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCurrency = buyFood(foodPrice, shop_pop_up_view);
                text_currency.setText(newCurrency + "");
                int newNumFood = petDatabaseHelper.getFood();
                text_foodNum.setText(newNumFood + "");
            }
        });
        btn_buy_toy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCurrency = buyToy(toyPrice, shop_pop_up_view);
                text_currency.setText(newCurrency + "");
                int newNumToy = petDatabaseHelper.getToy();
                text_toyNum.setText(newNumToy + "");
            }
        });
        builder.setView(shop_pop_up_view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_shop_pop_up_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //got the food price and view of shop, return the rest of currency
    private int buyFood(int foodPrice, View view) {
        int totalCurrency = petDatabaseHelper.getCurrency();
        int numOfFood = petDatabaseHelper.getFood();
        if(totalCurrency >= foodPrice){
             totalCurrency = totalCurrency - foodPrice;
             numOfFood ++;
            petDatabaseHelper.updateCurrency(totalCurrency);
            petDatabaseHelper.updateFood(numOfFood);
        }else{
            Toast.makeText(this, "You do not have enough currency", Toast.LENGTH_SHORT).show();
        }
        return totalCurrency;
    }

    //got the food price and view of shop, return the rest of currency
    private int buyToy(int toyPrice, View view) {
        int totalCurrency = petDatabaseHelper.getCurrency();
        int numOfToy = petDatabaseHelper.getToy();
        if(totalCurrency >= toyPrice){
            totalCurrency = totalCurrency - toyPrice;
            numOfToy ++;
            petDatabaseHelper.updateCurrency(totalCurrency);
            petDatabaseHelper.updateToy(numOfToy);
        }else{
            Toast.makeText(this, "You do not have enough currency", Toast.LENGTH_SHORT).show();
        }
        return totalCurrency;
    }
}
