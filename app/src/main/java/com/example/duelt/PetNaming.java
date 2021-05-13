package com.example.duelt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;
import com.example.duelt.fragments.TabActivity;

public class PetNaming extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText editText;
    Button btn;
    ImageView imageView;
    AnimationDrawable catAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_naming);
        editText = findViewById(R.id.pet_name_edit);
        btn = findViewById(R.id.pet_name_button);

        imageView = findViewById(R.id.pet_animations);
        imageView.setBackgroundResource(R.drawable.cat_animation_1);
        catAnimation = (AnimationDrawable) imageView.getBackground();
        catAnimation.start();

        //Condition to force user to add name for their pet
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

    //Go to main activity
    public void toMain(String name){
        PetModel pet = new PetModel(100, 100, 0,1, name);
        databaseHelper.updateData(pet);
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
        finish();
    }
}