package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ArrayList<String> list_items = new ArrayList<>();
        list_items.add("a");
        list_items.add("b");
        list_items.add("c");
        list_items.add("d");
        ListView listView = findViewById(R.id.setting_list);
        setting_list_items ad = new setting_list_items(this, list_items);

        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0: return;
                    default: Toast.makeText(Setting.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}