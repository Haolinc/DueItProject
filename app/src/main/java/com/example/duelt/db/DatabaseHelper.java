package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.duelt.EventDateModel;
import com.example.duelt.ItemModel;
import com.example.duelt.PetModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "duelt.db";
    public static final int VERSION  = 1;
    //Time table
    private static final String TIME_TABLE_NAME = "EVENT_DATE_TABLE";        //remove old data
    private static final String DUEDATE_TABLE_NAME = "DUE_DATE_TABLE";
    private static final String DAILY_TABLE_NAME = "DAILY_TABLE";
    private static final String TIME_FOR_ORDER_COLUMN = "TIME_FOR_ORDER";
    private static final String YEAR_COLUMN = "YEAR";
    private static final String MONTH_COLUMN = "MONTH";
    private static final String DAY_COLUMN = "DAY";
    private static final String HOUR_COLUMN = "HOUR";
    private static final String MINUTE_COLUMN = "MINUTE";
    private static final String EVENT_TITLE_COLUMN = "EVENT_TITLE";
    private static final String EVENT_DETAIL_COLUMN = "EVENT_DETAIL";
    private static final String ID_COLUMN = "ID";
    private static final String WAKED_COLUMN  = "WAKE_STATUS";
    //Pet table
    private static final String PET_TABLE_NAME = "PET_STAT_TABLE";
    private static final String NAME_COLUMN = "NAME";
    private static final String HUNGRINESS_COLUMN = "HUNGRINESS";
    private static final String MOOD_COLUMN = "MOOD";
    private static final String EXP_COLUMN = "EXP";
    private static final String LEVEL_COLUMN = "LEVEL";

    //shop table
    private static final String ITEM_TABLE_NAME = "ITEM_TABLE";
    private static final String CURRENCY_COLUMN = "CURRENCY";
    private static final String FOOD_COLUMN = "FOOD";
    private static final String TOY_COLUMN = "TOY";



    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createDueDateTableStatement = "CREATE TABLE IF NOT EXISTS " + DUEDATE_TABLE_NAME + " ( " + TIME_FOR_ORDER_COLUMN
                + " BIGINT, " + YEAR_COLUMN + " INT, " + MONTH_COLUMN + " INT, " + DAY_COLUMN +
                " INT, " + HOUR_COLUMN + " INT, " + MINUTE_COLUMN + " INT, " + EVENT_TITLE_COLUMN +
                " TEXT, " + EVENT_DETAIL_COLUMN + " TEXT, " + ID_COLUMN  + " INT UNIQUE, " + WAKED_COLUMN + " INT);";
        db.execSQL(createDueDateTableStatement);

        final String createDailyTableStatement = "CREATE TABLE IF NOT EXISTS " + DAILY_TABLE_NAME + " ( " + TIME_FOR_ORDER_COLUMN
                + " BIGINT, " + HOUR_COLUMN + " INT, " + MINUTE_COLUMN + " INT, " + EVENT_TITLE_COLUMN +
                " TEXT, " + ID_COLUMN  + " INT UNIQUE, " + WAKED_COLUMN + " INT);";
        db.execSQL(createDailyTableStatement);

        final String createPetTableStatement = "CREATE TABLE IF NOT EXISTS " + PET_TABLE_NAME + " ( "
                + NAME_COLUMN + " TEXT UNIQUE, "
                + HUNGRINESS_COLUMN + " INT, "
                + MOOD_COLUMN + " INT, "
                + EXP_COLUMN + " INT, "
                + LEVEL_COLUMN + " INT);";
        db.execSQL(createPetTableStatement);

        final String createItemTableStatement = "CREATE TABLE IF NOT EXISTS " + ITEM_TABLE_NAME + " ( "
                + CURRENCY_COLUMN + " INT, "
                + FOOD_COLUMN + " INT, "
                + TOY_COLUMN + " INT);";
        db.execSQL(createItemTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        onCreate(db);
    }

    public void upgrade() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        onCreate(db);
    }

    //item model updateData****************************************************************************************************
    public void updateItem(ItemModel item){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE_NAME + " SET "
                + CURRENCY_COLUMN + " = "+ item.getCurrency() + ", "
                + FOOD_COLUMN + " = " + item.getFood() + ", "
                + TOY_COLUMN + " = " + item.getToy() + ";");
        db.close();
    }

    public void addItem(ItemModel item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CURRENCY_COLUMN, item.getCurrency());
        cv.put(FOOD_COLUMN, item.getFood());
        cv.put(TOY_COLUMN, item.getToy());

        db.insert(ITEM_TABLE_NAME, null, cv);
        db.close();
    }

    public void removeItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME + ";");
        onCreate(db);
        db.close();
    }

    public ItemModel getItemStat(){
        SQLiteDatabase db = this.getReadableDatabase();
        ItemModel itemStat = new ItemModel(0, 0, 0);

        Cursor cursor = db.rawQuery("SELECT * From  "+ ITEM_TABLE_NAME, null);

        if(cursor.moveToFirst()){

            do{
                int currency = cursor.getInt(cursor.getColumnIndex(CURRENCY_COLUMN));
                int food = cursor.getInt(cursor.getColumnIndex(FOOD_COLUMN)) ;
                int toy = cursor.getInt(cursor.getColumnIndex(TOY_COLUMN));

                itemStat = new ItemModel(currency,food,toy);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemStat;
    }


    //pet model updateData***************************************************************************************************
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


    //Time table methods**********************************************************************************************
    //DueDate table adding
    public void addOneToDueDate(EventDateModel eventDateModel){
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
        cv.put(WAKED_COLUMN, eventDateModel.getWaked());

        db.insert(DUEDATE_TABLE_NAME, null, cv);
        db.close();
    }

    //Daily table adding
    public void addOneToDaily(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TIME_FOR_ORDER_COLUMN, eventDateModel.getTimeForOrder());
        cv.put(HOUR_COLUMN, eventDateModel.getHour());
        cv.put(MINUTE_COLUMN,eventDateModel.getMinute());
        cv.put(EVENT_TITLE_COLUMN, eventDateModel.getEventTitle());
        cv.put(ID_COLUMN, eventDateModel.getID());
        cv.put(WAKED_COLUMN, eventDateModel.getWaked());

        db.insert(DAILY_TABLE_NAME, null, cv);
        db.close();
    }

    //delete from duedate
    public boolean deleteOneFromDueDate(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
                DUEDATE_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";" ;
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

    //delete from daily
    public boolean deleteOneFromDaily(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
                DAILY_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";" ;
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


    //Delete all in DueDate table
    public void deleteAllFromDueDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+DUEDATE_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.close();
        db.close();
    }

    //Delete all in Daily table
    public void deleteAllFromDaily() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+DAILY_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.close();
        db.close();
    }

    public List<EventDateModel> getDueDateReminder(){
        List<EventDateModel> eventDateModelsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+DUEDATE_TABLE_NAME+" ORDER BY "+ TIME_FOR_ORDER_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                int year = cursor.getInt(cursor.getColumnIndex(YEAR_COLUMN));
                int month = cursor.getInt(cursor.getColumnIndex(MONTH_COLUMN)) ;
                int day = cursor.getInt(cursor.getColumnIndex(DAY_COLUMN));
                int hour = cursor.getInt(cursor.getColumnIndex(HOUR_COLUMN));
                int minute = cursor.getInt(cursor.getColumnIndex(MINUTE_COLUMN)) ;
                String eventTitle = cursor.getString(cursor.getColumnIndex(EVENT_TITLE_COLUMN));
                String eventDetail = cursor.getString(cursor.getColumnIndex(EVENT_DETAIL_COLUMN));
                int id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                int waked = cursor.getInt(cursor.getColumnIndex(WAKED_COLUMN));

                EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute, id, waked);
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

        Cursor cursor = db.rawQuery("SELECT * From  "+DAILY_TABLE_NAME + " ORDER BY "+ TIME_FOR_ORDER_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                int hour = cursor.getInt(cursor.getColumnIndex(HOUR_COLUMN));
                int minute = cursor.getInt(cursor.getColumnIndex(MINUTE_COLUMN)) ;
                String eventTitle = cursor.getString(cursor.getColumnIndex(EVENT_TITLE_COLUMN));
                int id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                int waked = cursor.getInt(cursor.getColumnIndex(WAKED_COLUMN));


                EventDateModel eventDateModel = new EventDateModel(eventTitle,hour,minute,id, waked);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
    }

    //get one by id from duedate table
    public EventDateModel getOneFromDueDate(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        EventDateModel eventDateModel;
        Cursor cursor = db.rawQuery("SELECT * FROM " + DUEDATE_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";", null);
        cursor.moveToFirst();
                int year = cursor.getInt(cursor.getColumnIndex(YEAR_COLUMN));
                int month = cursor.getInt(cursor.getColumnIndex(MONTH_COLUMN)) ;
                int day = cursor.getInt(cursor.getColumnIndex(DAY_COLUMN));
                int hour = cursor.getInt(cursor.getColumnIndex(HOUR_COLUMN));
                int minute = cursor.getInt(cursor.getColumnIndex(MINUTE_COLUMN)) ;
                String eventTitle = cursor.getString(cursor.getColumnIndex(EVENT_TITLE_COLUMN));
                String eventDetail = cursor.getString(cursor.getColumnIndex(EVENT_DETAIL_COLUMN));
                int waked = cursor.getInt(cursor.getColumnIndex(WAKED_COLUMN));
                eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute,id,waked);
        cursor.close();
        db.close();
        return eventDateModel;
    }

    //get one by id from daily table
    public EventDateModel getOneFromDaily(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        EventDateModel eventDateModel;
        Cursor cursor = db.rawQuery("SELECT * FROM " + DAILY_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";", null);
        cursor.moveToFirst();
        int hour = cursor.getInt(cursor.getColumnIndex(HOUR_COLUMN));
        int minute = cursor.getInt(cursor.getColumnIndex(MINUTE_COLUMN)) ;
        String eventTitle = cursor.getString(cursor.getColumnIndex(EVENT_TITLE_COLUMN));
        int waked = cursor.getInt(cursor.getColumnIndex(WAKED_COLUMN));
        eventDateModel = new EventDateModel(eventTitle,hour,minute,id,waked);
        cursor.close();
        db.close();
        return eventDateModel;
    }

    //Get list of id from duedate table
    public List<Integer> getIDFromDueDate(){
        List<Integer> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + ID_COLUMN + " From  "+DUEDATE_TABLE_NAME, null);

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

    //Get list of id from daily table
    public List<Integer> getIDFromDaily(){
        List<Integer> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + ID_COLUMN + " From  "+DAILY_TABLE_NAME, null);

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

    //update wake status in duedate table
    public void updateWakedStatusInDueDate(EventDateModel edm){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + DUEDATE_TABLE_NAME + " SET "
                + WAKED_COLUMN + " = "+ edm.getWaked()
                + " WHERE " + ID_COLUMN + " = " + edm.getID() + ";");
        db.close();
    }

    //update wake status in daily table
    public void updateWakedStatusInDaily(EventDateModel edm){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + DAILY_TABLE_NAME + " SET "
                + WAKED_COLUMN + " = "+ edm.getWaked()
                + " WHERE " + ID_COLUMN + " = " + edm.getID() + ";");
        db.close();
    }


}
