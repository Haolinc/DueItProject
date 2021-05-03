package com.example.duelt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duelt.Minigame;
import com.example.duelt.ShopPage;
import com.example.duelt.db.PetModel;
import com.example.duelt.R;
import com.example.duelt.StatesView;
import com.example.duelt.db.DatabaseHelper;

public class MiniFragment extends Fragment {
    private StatesView hungrinessState;
    private StatesView moodState;
    private StatesView expState;
    private TextView petLvNum;
    private TextView petName;
    private TextView currency;
    private Button states_pup_up_cancel_btn;
    private DatabaseHelper petDatabaseHelper;
    private PetModel petmodel;

    public MiniFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_mini,container,false);

        petDatabaseHelper = new DatabaseHelper(rootView.getContext());
        petmodel = petDatabaseHelper.getCurrentStat();

        initState(rootView);
        //Buttons in Fragments should be written here
        // ↓↓↓↓↓↓↓↓

        //dataTesting button function
        Button btn_dataTesting = (Button) rootView.findViewById(R.id.data_testing);
        btn_dataTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Minigame.class);
                startActivity(i);
            }
        });

        Button btn_shoppage = rootView.findViewById(R.id.mini_shop_button);
        btn_shoppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createShopDialog(rootView.getContext());
            }
        });


        //state_button function
//        Button btn_state_button = (Button) rootView.findViewById(R.id.states_button);
//        btn_state_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createStateDialog(rootView);
//            }
//        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateState();
    }

    private void updateState() {
        petDatabaseHelper = new DatabaseHelper(getContext());
        petmodel = petDatabaseHelper.getCurrentStat();

        //update hungriness state
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //update mood state
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        //update level text
        int level_text = petmodel.getLv();
        petLvNum.setText("LEVEL:  "+level_text);
        //update exp state
        int currentExp = petmodel.getExp();
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level_text));
        expState.setCurrentCount(currentExp);
        //update name
        petName.setText(petmodel.getName());
        //update currency
        currency.setText("Currency: " + petDatabaseHelper.getCurrency());
    }

    private void initState(View view) {

        petDatabaseHelper = new DatabaseHelper(view.getContext());
        petmodel = petDatabaseHelper.getCurrentStat();

        //init  HungrinessState bar
        hungrinessState = view.findViewById(R.id.hungrinessState);
        hungrinessState.setMaxCount(100);
        hungrinessState.setColor(Color.RED);
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //init MoodState bar
        moodState = view.findViewById(R.id.moodState);
        moodState.setMaxCount(100);
        moodState.setColor(Color.BLUE);
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        petLvNum = view.findViewById(R.id.mini_level_text);
        int level_text = petmodel.getLv();
        petLvNum.setText("LEVEL:  "+level_text);

        //init MoodState bar
        expState = view.findViewById(R.id.expState);
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level_text));
        expState.setColor(Color.GREEN);
        int currentExp = petmodel.getExp();
        expState.setCurrentCount(currentExp);

        petName = view.findViewById(R.id.mini_petname);
        petName.setText(petmodel.getName());

        currency = view.findViewById(R.id.mini_level_currency);
        currency.setText("Currency: " + petDatabaseHelper.getCurrency());
    }


    //create shop pup up and define their functions
    private void createShopDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                currency.setText("Currency: " + newCurrency);
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
            Toast.makeText(view.getContext(), "You do not have enough currency", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(view.getContext(), "You do not have enough currency", Toast.LENGTH_SHORT).show();
        }
        return totalCurrency;
    }
}
