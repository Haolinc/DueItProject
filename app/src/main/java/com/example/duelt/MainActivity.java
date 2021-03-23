package com.example.duelt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpToMemo(View v){
        Intent i = new Intent(this, MemoActivity.class);
        startActivity(i);
    }


//testing
    
    public void jumpToMini(View v){
        Intent i = new Intent(this, MiniActivity.class);
        startActivity(i);
    }

    public void jumpToTP(View v){
        Intent i = new Intent(this, treatmentActivity.class);
        startActivity(i);
    }

    //use ScrollView as parent and call linearlayout for action, change ScrollView values in xml files
    //Orientation is set in xml file
    public void createCheckBoxInDueDate(View v) {
        CheckBox cb = new CheckBox(this);
        LinearLayout dueDate = findViewById(R.id.dueDate_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText("new");
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){      //when checked
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("alert");
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dueDate.removeView(cb);   //click to remove checkbox view
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cb.setChecked(false);
                                }
                            });
                    alertDialog.show();
                }
            }
        });
        dueDate.addView(cb);

    }

    public void deleteCheckBoxInDueDate(View v){

    }

    public void deleteOnCheck(View v){

    }

    public void createCheckBoxInReminder(View v) {
        CheckBox cb = new CheckBox(this);
        LinearLayout dueDate = findViewById(R.id.reminder_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);  //wrap_content

        cb.setText("new");
        cb.setLayoutParams(lp);
        cb.setGravity(Gravity.CENTER_VERTICAL);
        dueDate.addView(cb);
    }
}