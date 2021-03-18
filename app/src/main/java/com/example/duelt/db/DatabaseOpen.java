package com.example.duelt.db;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseOpen extends SQLiteOpenHelper{
    public static final String DATABASE = "duelt.db";
    public static final String TABLE = "memo";
    public static final int VERSION  = 1;

    public DatabaseOpen(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table memo(date primary key, Memo Text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
