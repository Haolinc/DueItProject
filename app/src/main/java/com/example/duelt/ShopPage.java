package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ShopPage extends AppCompatActivity {
    private Button mFood;
    private Button mToy;
    private Button mCurrency;
    private Button mSetstat;
    private Button mgetStat;
    private Button mRemoveStat;
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;
    private TextView mText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_page);

        mText1 = findViewById(R.id.textsView1);
        mText2 = findViewById(R.id.textsView2);
        mText3 = findViewById(R.id.textsView3);
        mText4 = findViewById(R.id.textsView4);
        mFood = findViewById(R.id.btn_food);
        mToy = findViewById(R.id.btn_toy);
        mCurrency = findViewById(R.id.btn_currency);
        mSetstat = findViewById(R.id.btn_setstat);
        



    }
}