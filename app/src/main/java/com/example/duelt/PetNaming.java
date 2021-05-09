package com.example.duelt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;
import com.example.duelt.fragments.TabActivity;

public class PetNaming extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView textView;
    EditText editText;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_naming);
        textView = findViewById(R.id.pet_name_text);
        editText = findViewById(R.id.pet_name_edit);
        btn = findViewById(R.id.pet_name_button);

        String text = "Please Enter Your Pet Name!";
        textView.setText(text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setError("Name can't be empty");
                }
                else
                    toMain(editText.getText().toString());
            }
        });
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    public void toMain(String name){
        PetModel pet = new PetModel(100, 100, 0,1, name);
        databaseHelper.updateData(pet);
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
        finish();
    }
}