package com.example.duelt;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import java.sql.ParameterMetaData;

public class HintHelper {
    ImageButton btn_hint;

    //
    public void checkFirstTime(View v,String key, ImageButton btn_hint) {
        SharedPreferences sp =   PreferenceManager.getDefaultSharedPreferences(v.getContext());

        boolean isFirstTime = sp.getBoolean(key, true);
        this.btn_hint = btn_hint;
        if(!isFirstTime){
            btn_hint.setVisibility(View.INVISIBLE);
        }else {
            btn_hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_hint.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean(key, false);
                    edit.apply();
                }
            });

        }
    }
}
