package com.example.duelt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ShopPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);
        viewSetup();
    }

    private void viewSetup(){
        Button back_btn = findViewById(R.id.shop_back_button);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        Button buyfood_btn = findViewById(R.id.shop_buyfood_button);
        buyfood_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyFood();
            }
        });
        Button buytoy_btn = findViewById(R.id.shop_buytoy_button);
        buyfood_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyToy();
            }
        });
    }

    public void buyFood(){

    }

    public void buyToy(){

    }

    public void back(){
        finish();
    }

}