package com.example.duelt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class setting_list_items extends ArrayAdapter<String> {
    private Context context;

    public setting_list_items(Activity context, ArrayList<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_setting_list_items, parent, false);
        }

        String settingName = getItem(position);

        TextView stringText = listItemView.findViewById(R.id.setting_string);
        stringText.setText(settingName);
        return listItemView;
    }
}