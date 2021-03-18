package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.duelt.MemoActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private DatabaseOpen openHelper;
    private static volatile DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpen(context);
    }

    public static synchronized DatabaseAccess getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void close(){
        if(database != null) {
            this.database.close();
        }
    }

    public void save(MemoActivity MemoActivity) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("Memo", MemoActivity.getText());
        String date = Long.toString(MemoActivity.getTime());
        database.update(DatabaseOpen.TABLE, values, "date = ?", new String[]{date});
    }

    public List getAllMemo(){
        List memos = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * From memo ORDER BY date DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int time = cursor.getInt(0);
            String text = cursor.getString(1);
            memos.add(new MemoActivity(time, text));
            cursor.moveToNext();
        }
        return memos;
    }
}
