package com.example.duelt;

import android.content.DialogInterface;
import android.content.Intent;
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
    public static boolean testMode =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ArrayList<String> list_items = new ArrayList<>();
        list_items.add("Reset All");
        list_items.add("Testing Mode");
        ListView listView = findViewById(R.id.setting_list);
        setting_list_items ad = new setting_list_items(this, list_items);

        listView.setAdapter(ad);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set each setting item function base on position
                switch(position) {
                    case 0:
                        resetAll();
                        break;
                    case 1:
                        if (!testMode) {
                            Toast.makeText(Setting.this, "Test Mode Open!", Toast.LENGTH_SHORT).show();
                            testMode = true;
                        }
                        else {
                            Toast.makeText(Setting.this, "Test Mode Close!", Toast.LENGTH_SHORT).show();
                            testMode = false;
                        }
                        break;
                    default: Toast.makeText(Setting.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Resetting database function
    private void resetAll(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reset Database");
        alertDialog.setMessage("Are you sure to reset all the data?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
                        databaseHelper.upgrade();
                        alertDialog.dismiss();
                        Toast.makeText(Setting.this, "Reset!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getBaseContext(), LoadingActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
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