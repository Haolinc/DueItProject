package com.example.duelt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import java.sql.ParameterMetaData;

public class HintHelper {
    ImageButton btn_hint;

    //
    public void checkFirstTime(Context context, String key, ImageButton btn_hint) {
        SharedPreferences sp =   PreferenceManager.getDefaultSharedPreferences(context);

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
