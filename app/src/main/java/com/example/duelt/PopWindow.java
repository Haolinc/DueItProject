package com.example.duelt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.duelt.db.DatabaseHelper;

import java.util.HashMap;
import java.util.List;

public class PopWindow extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int id= getIntent().getIntExtra("EDMID", -1);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        EventDateModel eventDateModel = databaseHelper.getOne(id);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        String textViewText = "Your activity of " + eventDateModel.getEventTitle() + " is coming up";

        getWindow().setLayout((int)(width*.8), (int)(height*.5));
        TextView textView = findViewById(R.id.textView2);
        textView.setText(textViewText);
        eventDateModel.setWaked(1);
        databaseHelper.updateWakedStatus(eventDateModel);
        //databaseHelper.deleteOne(id);
    }


}
