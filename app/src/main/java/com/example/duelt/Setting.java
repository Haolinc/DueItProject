package com.example.duelt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.db.DatabaseHelper;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ArrayList<String> list_items = new ArrayList<>();
        list_items.add("Reset All");
        list_items.add("test");
        ListView listView = findViewById(R.id.setting_list);
        setting_list_items ad = new setting_list_items(this, list_items);

        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        resetAll();
                        break;
                    case 1:
                        Toast.makeText(Setting.this, "clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default: Toast.makeText(Setting.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void resetAll(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reset Everything");
        alertDialog.setMessage("Are you sure to reset every data?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
                        databaseHelper.upgrade();
                        alertDialog.dismiss();
                        Toast.makeText(Setting.this, "Reset!", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}