package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.duelt.EventDateModel;
import com.example.duelt.ItemModel;
import com.example.duelt.PetModel;
import com.example.duelt.WeeklyScheduleModel;

import java.lang.reflect.Executable;
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

    //Weekly Schedule table
    private static final String WEEKLY_SCHEDULE_TABLE_NAME = "WEEKLY_SCHEDULE_TABLE";
    private static final String EVENT_ID_COLUMN = "EVENT_ID";
    private static final String EVENT_NAME_COLUMN = "EVENT_NAME";
    private static final String WEEK_DAY_COLUMN = "WEEK_DAY";
    private static final String START_TIME_COLUMN = "START_TIME";
    private static final String END_TIME_COLUMN = "END_TIME";
    private static final String START_TIME_POSITION_COLUMN="START_POSITION";
    private static final String END_TIME_POSITION_COLUMN = "END_POSITION";
    private static final String BACKGROUND_COLOR_COLUMN = "COLOR";

    //expForLevel table
    private static final String EXP_FOR_LEVEL_TABLE = "EXP_FOR_LEVEL_TABLE"  ;
    private static final String EXP_FOR_EACH_LEVEL_COLUMN = "EXP_FOR_EACH_LEVEL";
    private static final String E_LEVEL_COLUMN = "E_LEVEL";


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

        final String createWeekDayTableStatement = "CREATE TABLE IF NOT EXISTS " + WEEKLY_SCHEDULE_TABLE_NAME + "("
                + EVENT_ID_COLUMN + " INT, "
                + EVENT_NAME_COLUMN + " TEXT, "
                + WEEK_DAY_COLUMN + " TEXT, "  //Review this line
                + START_TIME_COLUMN + " TEXT, "
                + END_TIME_COLUMN + " TEXT, "
                + START_TIME_POSITION_COLUMN + " INT, "
                + END_TIME_POSITION_COLUMN + " INT, "
                + BACKGROUND_COLOR_COLUMN + " TEXT);";
        db.execSQL(createWeekDayTableStatement);

        createExpForLevelTable(100, 100, 0.25 );
    }

    private void createExpForLevelTable(int maxLevel, int expForLevel1, double increateRatio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //DELETE OLD TABLE BEFORE CREATE A NEW ONE
        db.execSQL("DROP TABLE IF EXISTS " + EXP_FOR_LEVEL_TABLE);
        //Create a new table
        final String createExpForLevelStatement = "CREATE TABLE IF NOT EXISTS " + EXP_FOR_LEVEL_TABLE + " ( "
                + EXP_FOR_EACH_LEVEL_COLUMN + " INT, "
                + E_LEVEL_COLUMN + " INT);" ;
        db.execSQL(createExpForLevelStatement);

        int currentExp = expForLevel1;
        for(int level = 0; level<100; level ++ ){

            cv.put(E_LEVEL_COLUMN, level);
            //exp calculation formula 
            currentExp = currentExp + expForLevel1 ;
            cv.put(EXP_FOR_EACH_LEVEL_COLUMN, currentExp);
            db.insert(EXP_FOR_LEVEL_TABLE, null, cv);

        }
        db.close();
    }

    public ArrayList<Integer> getExpForLevelTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Integer> table = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT * From  "+ EXP_FOR_LEVEL_TABLE, null);

        if(cursor.moveToFirst()){

            do{
                int exp = cursor.getInt(cursor.getColumnIndex(EXP_FOR_EACH_LEVEL_COLUMN));
                table.add(exp);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return table;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXP_FOR_LEVEL_TABLE);
        onCreate(db);
    }

    public void upgrade() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WEEKLY_SCHEDULE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXP_FOR_LEVEL_TABLE);
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

    //All WeeklySchedule table functions goes here
    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    //Get list of id from WeeklySchedule table
    public List<Integer> getIDFromWeeklySchedule(){
        List<Integer> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + EVENT_ID_COLUMN + " FROM " + WEEKLY_SCHEDULE_TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(EVENT_ID_COLUMN));
                idList.add(id);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return idList;
    }

    //Delete one data from WeeklySchedule table
    public boolean deleteOneFromWeeklySchedule(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
                WEEKLY_SCHEDULE_TABLE_NAME + " WHERE " + EVENT_ID_COLUMN + " = " + id + ";" ;
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

    //Add event to WeeklyScheduleTable
    public void addOneToWeeklySchedule(WeeklyScheduleModel weeklyScheduleModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

//        cv.put(EVENT_ID_COLUMN, weeklyScheduleModel.getId());
        cv.put(EVENT_NAME_COLUMN, weeklyScheduleModel.getEventName());
        cv.put(WEEK_DAY_COLUMN,weeklyScheduleModel.getWeekDay());
        cv.put(START_TIME_COLUMN, weeklyScheduleModel.getStartTime());
        cv.put(END_TIME_COLUMN, weeklyScheduleModel.getEndTime());
        cv.put(START_TIME_POSITION_COLUMN, weeklyScheduleModel.getStartPosition());
        cv.put(END_TIME_POSITION_COLUMN,weeklyScheduleModel.getEndPosition());
        cv.put(BACKGROUND_COLOR_COLUMN,weeklyScheduleModel.getColor());

        db.insert(WEEKLY_SCHEDULE_TABLE_NAME, null, cv);
        db.close();
    }

    //Get WeeklySchedule list
    public List<WeeklyScheduleModel> getWeeklySchedule(){
        List<WeeklyScheduleModel> weeklyScheduleModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+WEEKLY_SCHEDULE_TABLE_NAME + " ORDER BY "+ EVENT_ID_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                String eventName = cursor.getString(cursor.getColumnIndex(EVENT_NAME_COLUMN));
                String color = cursor.getString(cursor.getColumnIndex(BACKGROUND_COLOR_COLUMN));
                String weekDay = cursor.getString(cursor.getColumnIndex(WEEK_DAY_COLUMN));
                String startTime = cursor.getString(cursor.getColumnIndex(START_TIME_COLUMN));
                String endTime = cursor.getString(cursor.getColumnIndex(END_TIME_COLUMN));
                int startPosition = cursor.getInt(cursor.getColumnIndex(START_TIME_POSITION_COLUMN));
                int endPosition = cursor.getInt(cursor.getColumnIndex(END_TIME_POSITION_COLUMN));
                int id  = cursor.getInt(cursor.getColumnIndex(EVENT_ID_COLUMN));

                WeeklyScheduleModel weeklyScheduleModel = new WeeklyScheduleModel(eventName, color, weekDay, startTime, endTime, startPosition, endPosition, id);
                weeklyScheduleModelList.add(weeklyScheduleModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return weeklyScheduleModelList;
    }

}
