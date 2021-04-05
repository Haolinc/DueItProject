package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.duelt.PetModel;

import java.util.ArrayList;
import java.util.List;

public class PetDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "duelt.db";
    public static final int VERSION  = 1;
    private static final String TABLE_NAME = "PET_STAT_TABLE";
    private static final String NAME_COLUMN = "NAME";
    private static final String HUNGRINESS_COLUMN = "HUNGRINESS";
    private static final String MOOD_COLUMN = "MOOD";
    private static final String EXP_COLUMN = "EXP";
    private static final String LEVEL_COLUMN = "LEVEL";

    public PetDatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ( " + NAME_COLUMN + " TEXT, " + HUNGRINESS_COLUMN + " INT, "+ MOOD_COLUMN + " INT,"
        + EXP_COLUMN + " INT," + LEVEL_COLUMN + " INT);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addOne(PetModel pet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(HUNGRINESS_COLUMN, pet.getHungriness());
        cv.put(MOOD_COLUMN, pet.getMood());
        cv.put(EXP_COLUMN, pet.getExp());
        cv.put(LEVEL_COLUMN, pet.getLv());
        cv.put(NAME_COLUMN, pet.getLv());

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }


    public void killPet(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.close();
    }

    public List<PetModel> getCurrentStat(){
        List<PetModel> petStatList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+TABLE_NAME, null);

        if(cursor.moveToFirst()){

            do{
                int hunger = cursor.getInt(1);
                int mood = cursor.getInt(2) ;
                int exp = cursor.getInt(3);
                int level = cursor.getInt(4);
                String name = cursor.getString(5);

        PetModel petStat = new PetModel(hunger, mood, exp, level, name);
        petStatList.add(petStat);

    }while (cursor.moveToNext());
}

        cursor.close();
        db.close();
        return petStatList;
    }
}
