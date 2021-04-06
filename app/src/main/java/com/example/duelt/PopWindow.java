package com.example.duelt;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.duelt.db.DatabaseHelper;

import java.util.List;

public class PopWindow extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int id= getIntent().getIntExtra("EDMID", 0);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        EventDateModel eventDateModel = databaseHelper.getOne(id);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        String textViewText = "Your activity of " + eventDateModel.getEventTitle() + " is coming up";

        getWindow().setLayout((int)(width*.8), (int)(height*.5));
        TextView textView = findViewById(R.id.textView2);
        textView.setText(textViewText);
        databaseHelper.deleteOne(id);
    }
}
