package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.duelt.EventDateModel;
import com.example.duelt.PetModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "duelt.db";
    public static final int VERSION  = 1;
    //Time table
    private static final String TIME_TABLE_NAME = "EVENT_DATE_TABLE";
    private static final String TIME_FOR_ORDER_COLUMN = "TIME_FOR_ORDER";
    private static final String YEAR_COLUMN = "YEAR";
    private static final String MONTH_COLUMN = "MONTH";
    private static final String DAY_COLUMN = "DAY";
    private static final String HOUR_COLUMN = "HOUR";
    private static final String MINUTE_COLUMN = "MINUTE";
    private static final String EVENT_TITLE_COLUMN = "EVENT_TITLE";
    private static final String EVENT_DETAIL_COLUMN = "EVENT_DETAIL";
    private static final String ID_COLUMN = "ID";
    //Pet table
    private static final String PET_TABLE_NAME = "PET_STAT_TABLE";
    private static final String NAME_COLUMN = "NAME";
    private static final String HUNGRINESS_COLUMN = "HUNGRINESS";
    private static final String MOOD_COLUMN = "MOOD";
    private static final String EXP_COLUMN = "EXP";
    private static final String LEVEL_COLUMN = "LEVEL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createTimeTableStatement = "CREATE TABLE IF NOT EXISTS " + TIME_TABLE_NAME + " ( " + TIME_FOR_ORDER_COLUMN
                + " BIGINT, " + YEAR_COLUMN + " INT, " + MONTH_COLUMN + " INT, " + DAY_COLUMN +
                " INT, " + HOUR_COLUMN + " INT, " + MINUTE_COLUMN + " INT, " + EVENT_TITLE_COLUMN +
                " TEXT, " + EVENT_DETAIL_COLUMN + " TEXT, " + ID_COLUMN  + " INT UNIQUE);";
        db.execSQL(createTimeTableStatement);

        final String createPetTableStatement = "CREATE TABLE IF NOT EXISTS " + PET_TABLE_NAME + " ( "
                + NAME_COLUMN + " TEXT UNIQUE, "
                + HUNGRINESS_COLUMN + " INT, "
                + MOOD_COLUMN + " INT, "
                + EXP_COLUMN + " INT, "
                + LEVEL_COLUMN + " INT);";
        db.execSQL(createPetTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        onCreate(db);
    }

    public void updateData(PetModel pet){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + PET_TABLE_NAME + " SET "
                + HUNGRINESS_COLUMN + " = "+ pet.getHungriness() + ", "
                + MOOD_COLUMN + " = " + pet.getMood() + ", "
                + EXP_COLUMN + " = " + pet.getExp() + ", "
                + LEVEL_COLUMN + " = " + pet.getLv()
                + " WHERE " + NAME_COLUMN + " = '" + pet.getName() + "';");
        db.close();
    }

    public void addOne(PetModel pet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN, pet.getName());
        cv.put(HUNGRINESS_COLUMN, pet.getHungriness());
        cv.put(MOOD_COLUMN, pet.getMood());
        cv.put(EXP_COLUMN, pet.getExp());
        cv.put(LEVEL_COLUMN, pet.getLv());

        db.insert(PET_TABLE_NAME, null, cv);
        db.close();
    }

    public void killPet(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME + ";");
        onCreate(db);
        db.close();
    }

    public PetModel getCurrentStat(){
        SQLiteDatabase db = this.getReadableDatabase();
        PetModel petStat = new PetModel(0, 0, 0,0, "");

        Cursor cursor = db.rawQuery("SELECT * From  "+ PET_TABLE_NAME, null);

        if(cursor.moveToFirst()){

            do{
                String name = cursor.getString(0);
                int hunger = cursor.getInt(1);
                int mood = cursor.getInt(2) ;
                int exp = cursor.getInt(3);
                int level = cursor.getInt(4);


                petStat = new PetModel(hunger, mood, exp, level, name);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return petStat;
    }

    public void addOne(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TIME_FOR_ORDER_COLUMN, eventDateModel.getTimeForOrder());
        cv.put(YEAR_COLUMN, eventDateModel.getYear());
        cv.put(MONTH_COLUMN, eventDateModel.getMonth());
        cv.put(DAY_COLUMN, eventDateModel.getDay());
        cv.put(HOUR_COLUMN, eventDateModel.getHour());
        cv.put(MINUTE_COLUMN,eventDateModel.getMinute());
        cv.put(EVENT_TITLE_COLUMN, eventDateModel.getEventTitle());
        cv.put(EVENT_DETAIL_COLUMN, eventDateModel.getEventDetail());
        cv.put(ID_COLUMN, eventDateModel.getID());

        db.insert(TIME_TABLE_NAME, null, cv);
        db.close();
    }

    //delete event by its ID
    public boolean deleteOne(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
               //TABLE_NAME+ " WHERE "  + YEAR_COLUMN + " = " + eventDateModel.getYear() + ";";
                TIME_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + eventDateModel.getID() + ";" ;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean deleteOne(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
                //TABLE_NAME+ " WHERE "  + YEAR_COLUMN + " = " + eventDateModel.getYear() + ";";
                TIME_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";" ;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }


    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+TIME_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.close();
        db.close();
    }

    public List<EventDateModel> getDueDateReminder(){
        List<EventDateModel> eventDateModelsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+TIME_TABLE_NAME+" WHERE "+ TIME_FOR_ORDER_COLUMN + ">10000 " + " ORDER BY "+ TIME_FOR_ORDER_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                int year = cursor.getInt(1);
                int month = cursor.getInt(2) ;
                int day = cursor.getInt(3);
                int hour = cursor.getInt(4);
                int minute = cursor.getInt(5) ;
                String eventTitle = cursor.getString(6);
                String eventDetail = cursor.getString(7);
                int id = cursor.getInt(8);

                EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute, id);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
    }

    public List<EventDateModel> getDailyRoutine(){
        List<EventDateModel> eventDateModelsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+TIME_TABLE_NAME+" WHERE "+ TIME_FOR_ORDER_COLUMN + "<10000 " + " ORDER BY "+ TIME_FOR_ORDER_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                int year = cursor.getInt(1);
                int month = cursor.getInt(2) ;
                int day = cursor.getInt(3);
                int hour = cursor.getInt(4);
                int minute = cursor.getInt(5) ;
                String eventTitle = cursor.getString(6);
                String eventDetail = cursor.getString(7);
                int id = cursor.getInt(8);

                EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute,id);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
    }

    public List<EventDateModel> getAll(){
        List<EventDateModel> eventDateModelsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+TIME_TABLE_NAME+" ORDER BY "+ TIME_FOR_ORDER_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                int year = cursor.getInt(1);
                int month = cursor.getInt(2) ;
                int day = cursor.getInt(3);
                int hour = cursor.getInt(4);
                int minute = cursor.getInt(5) ;
                String eventTitle = cursor.getString(6);
                String eventDetail = cursor.getString(7);
                int id = cursor.getInt(8);

                EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute, id);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
    }

    public List<Integer> getIDFromDatabase(){
        List<Integer> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + ID_COLUMN + " From  "+TIME_TABLE_NAME, null);

        if(cursor.moveToFirst()){

            do{
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                idList.add(id);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return idList;
    }
}
