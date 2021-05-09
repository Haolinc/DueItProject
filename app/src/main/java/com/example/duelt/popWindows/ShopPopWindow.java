package com.example.duelt.popWindows;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;


public class ShopPopWindow extends AppCompatActivity {

    DatabaseHelper petDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_pup_up);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.45));

        Button btn_shop_pop_up_cancel = findViewById(R.id.btn_close_shop);
        Button btn_buy_food = findViewById(R.id.btn_buy_food);
        Button btn_buy_toy = findViewById(R.id.btn_buy_toy);
        TextView text_currency = findViewById(R.id.textView_currency);
        TextView text_toy_price = findViewById(R.id.textView_foodPrice);
        TextView text_food_price = findViewById(R.id.textView_toyPrice);
        TextView text_foodNum = findViewById(R.id.textView_food_num);
        TextView text_toyNum = findViewById(R.id.textView_toy_num);

        petDatabaseHelper = new DatabaseHelper(this);


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

        btn_buy_toy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCurrency = buyToy(toyPrice);
                text_currency.setText(newCurrency + "");
                int newNumToy = petDatabaseHelper.getToy();
                text_toyNum.setText(newNumToy + "");
            }
        });

        btn_buy_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCurrency = buyFood(foodPrice);
                text_currency.setText(newCurrency + "");
                int newNumFood = petDatabaseHelper.getFood();
                text_foodNum.setText(newNumFood + "");
            }
        });

        btn_shop_pop_up_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //got the food price and view of shop, return the rest of currency
    private int buyFood(int foodPrice) {
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
    private int buyToy(int toyPrice) {
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
