package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpToMemo(View v) {
        Intent i = new Intent(this, MemoActivity.class);
        startActivity(i);
    }


    public void jumpToMini(View v) {
        Intent i = new Intent(this, MiniActivity.class);
        startActivity(i);
    }

    /*
    private void createDatabase() {
        SQLiteDatabase database = openOrCreateDatabase("memotexts.db", MODE_PRIVATE, null);
        database.execSQL("CREATE NEW MEMO IF NOT EXISTS(memo TEXT, memo_date, memo_duedate);");
        database.close();
    }*/
}