package com.example.duelt.popWindows;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;

public class Memo_pop_window extends AppCompatActivity {

    TextView mTextViewDetail;
    TextView mTextViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_pop_window);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);


        Intent i = getIntent();
        int intValue = i.getIntExtra("emdID", 0);
        EventDateModel edm = databaseHelper.getOneFromDueDate(intValue);

        String mDetail = edm.getEventDetail();
        String mTitle = edm.getEventTitle();

        mTextViewTitle = findViewById(R.id.tv_title);
        mTextViewDetail = findViewById(R.id.tv_detail);

        mTextViewTitle.setText(mTitle);
        mTextViewDetail.setText(mDetail);
    }
}