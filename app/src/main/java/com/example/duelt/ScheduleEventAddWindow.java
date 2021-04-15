package com.example.duelt;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class ScheduleEventAddWindow extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_add_event);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //Solve the outside part that makes the rounded corner looks bad. (不加这两行会导致窗口内部是圆角，外面是直角）
        Window window=this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getWindow().setLayout((int)(width * .8),(int)(height*.7));


    }
}
