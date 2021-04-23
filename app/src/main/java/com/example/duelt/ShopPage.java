package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.duelt.db.DatabaseHelper;

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

    DatabaseHelper itemDatabaseHelper = new DatabaseHelper(this);

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
        mgetStat = findViewById(R.id.btn_getstat);
        mRemoveStat = findViewById(R.id.btn_remove);

        mSetstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStat();
            }
        });

        mgetStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStat();
            }
        });

        mRemoveStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStat();
            }
        });

    }
    private void setStat(){
        itemDatabaseHelper.updateAllItem(50,50,100);
        String itemmodelText = "New item Set";
        mText1.setText(itemmodelText);
    }

    private void getStat(){
        String itemmodelText = "From Database: " + " Currency: "+ itemDatabaseHelper.getCurrency() + " Food: " + itemDatabaseHelper.getFood()
                + " Toy: " + itemDatabaseHelper.getToy();
        mText2.setText(itemmodelText);
    }

    private void removeStat(){
        itemDatabaseHelper.removeAllItem();
        mText3.setText("Removed");
    }






}