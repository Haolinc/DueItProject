package com.example.duelt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TextEntering extends AppCompatActivity {
    private String eventTitle;
    private String eventDetail;
    private EditText eventTitleInput;
    private EditText eventDetailInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entering);
        eventTitleInput = findViewById(R.id.eventTitle);
        eventDetailInput = findViewById(R.id.eventDetail);
    }

    public void back(View v){
        finish();
    }

    public void saveEvent(View v){
        eventDetail = eventDetailInput.getText().toString();
        eventTitle = eventTitleInput.getText().toString();

        //testing
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(eventTitle);
        //alertDialog.setMessage(mYear + "/" + mMonth+"/"+ mDay + "  "+ mHour + ": " + mMinute +"\n" + eventDetail );
        alertDialog.setMessage(eventDetail);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                    }
                });
        alertDialog.show();
    }
}