package com.example.duelt.popWindows;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.R;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.EventDateModel;

public class Memo_pop_window extends AppCompatActivity {

    EditText mEditViewTitle;
    EditText mEditViewDetail;
    Button mEditTitle;
    Button mBack;
    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_pop_window);
        int intValue = getIntent().getIntExtra("EDMID", 0);
        EventDateModel edm = databaseHelper.getOneFromDueDate(intValue);
        String mDetail = edm.getEventDetail();
        String mTitle = edm.getEventTitle();



        mBack = findViewById(R.id.btn_memo_pop_back);
        mEditViewTitle = findViewById(R.id.et_title);
        mEditViewDetail = findViewById(R.id.et_detail);
        mEditTitle = findViewById(R.id.btn_edit_title);


        mEditViewTitle.setText(mTitle);
        mEditViewDetail.setText(mDetail);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEditViewTitle.getText().toString();
                String detail = mEditViewDetail.getText().toString();

                edm.setEventTitle(title);
                edm.setEventDetail(detail);

                databaseHelper.updateDuedate(edm);

                Toast.makeText(Memo_pop_window.this,"id passed in "+ intValue, Toast.LENGTH_SHORT).show();


            }
        });

    }
}