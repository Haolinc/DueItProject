package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "duelt.db";
    public static final int VERSION  = 1;
    //Time table common contents
    private static final String EVENT_TITLE_COLUMN = "EVENT_TITLE";
    private static final String ID_COLUMN = "ID";
    private static final String WAKED_COLUMN  = "WAKE_STATUS";
    //Due Date Table
    private static final String DUEDATE_TABLE_NAME = "DUE_DATE_TABLE";
    private static final String SET_DATE_COLUMN  = "SET_DATE";
    private static final String DUE_DATE_COLUMN  = "DUE_DATE";
    private static final String EVENT_DETAIL_COLUMN = "EVENT_DETAIL";
    //Daily Routine Table
    private static final String DAILY_TABLE_NAME = "DAILY_TABLE";
    private static final String TIME_FOR_ORDER_COLUMN = "TIME_FOR_ORDER";
    private static final String HOUR_COLUMN = "HOUR";
    private static final String MINUTE_COLUMN = "MINUTE";
    private static final String WAKED_TIME_COLUMN = "WAKED_TIME";

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

    //table for chart
    private static final String PROFILE_CHART = "PROFILE_CHART";
    private static final String NUMBER_OF_THE_WEEK = "NUMBER_WEEK";
    private static final String DATE_FOR_THE_WEEK_START = "DATE_FOR_THE_WEEK_START";
    private static final String DATE_FOR_THE_WEEK_END = "DATE_FOR_THE_WEEK_END";
    private static final String DAILY_COUNT = "DAILY_COUNT";
    private static final String MEMO_COUNT = "MEMO_COUNT";
    private static final String TREATMENT_COUNT = "TREATMENT_COUNT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createProfileCount = "CREATE TABLE IF NOT EXISTS " + PROFILE_CHART + " ( "
                + NUMBER_OF_THE_WEEK + " INT UNIQUE, "
                + DATE_FOR_THE_WEEK_START + "INT, "
                + DAILY_COUNT + " INT, "
                + MEMO_COUNT + " INT, "
                + TREATMENT_COUNT + " INT);";
        db.execSQL(createProfileCount);

        final String createDueDateTableStatement = "CREATE TABLE IF NOT EXISTS " + DUEDATE_TABLE_NAME + " ( "
                + EVENT_TITLE_COLUMN + " TEXT, "
                + EVENT_DETAIL_COLUMN + " TEXT, "
                + SET_DATE_COLUMN + " BIGINT, "
                + DUE_DATE_COLUMN + " BIGINT, "
                + ID_COLUMN  + " INT UNIQUE, "
                + WAKED_COLUMN + " INT);";
        db.execSQL(createDueDateTableStatement);

        final String createDailyTableStatement = "CREATE TABLE IF NOT EXISTS " + DAILY_TABLE_NAME + " ( " + TIME_FOR_ORDER_COLUMN
                + " BIGINT, " + HOUR_COLUMN + " INT, " + MINUTE_COLUMN + " INT, " + EVENT_TITLE_COLUMN +
                " TEXT, " + ID_COLUMN  + " INT UNIQUE, " + WAKED_TIME_COLUMN + " INT, " + WAKED_COLUMN + " INT);";
        db.execSQL(createDailyTableStatement);

        final String createPetTableStatement = "CREATE TABLE IF NOT EXISTS " + PET_TABLE_NAME + " ( "
                + NAME_COLUMN + " TEXT UNIQUE, "
                + HUNGRINESS_COLUMN + " INT, "
                + MOOD_COLUMN + " INT, "
                + EXP_COLUMN + " INT, "
                + LEVEL_COLUMN + " INT);";
        db.execSQL(createPetTableStatement);

        PetModel petmodel = new PetModel(0, 0, 0, 1, "Boo");
        initPet(petmodel, db);

        final String createItemTableStatement = "CREATE TABLE IF NOT EXISTS " + ITEM_TABLE_NAME + " ( "
                + CURRENCY_COLUMN + " INT, "
                + FOOD_COLUMN + " INT, "
                + TOY_COLUMN + " INT);";
        db.execSQL(createItemTableStatement);

        initItem(db);

        final String createWeekDayTableStatement = "CREATE TABLE IF NOT EXISTS " + WEEKLY_SCHEDULE_TABLE_NAME + "("
                + EVENT_ID_COLUMN + " INT UNIQUE, "
                + EVENT_NAME_COLUMN + " TEXT, "
                + WEEK_DAY_COLUMN + " TEXT, "  //Review this line
                + START_TIME_COLUMN + " TEXT, "
                + END_TIME_COLUMN + " TEXT, "
                + START_TIME_POSITION_COLUMN + " INT, "
                + END_TIME_POSITION_COLUMN + " INT, "
                + BACKGROUND_COLOR_COLUMN + " TEXT);";
        db.execSQL(createWeekDayTableStatement);



        createExpForLevelTable(100, 100, 0.25, db);
    }


    //profile chart upgrade ***********************************************************************************************************************
    public void updateProfileCount(int numberOfWeek, int weekStart, int dailyCount, int memoCount, int treatmentCount){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + PROFILE_CHART + " SET "
                + NUMBER_OF_THE_WEEK + " = "+ numberOfWeek + ", "
                + DATE_FOR_THE_WEEK_START + " = " + weekStart + ", "
                + DAILY_COUNT + " = " + dailyCount + ", "
                + MEMO_COUNT + " = " + memoCount + ", "
                + TREATMENT_COUNT + " = " + treatmentCount + ";");
        db.close();
    }




    private void createExpForLevelTable(int maxLevel, int expForLevel1, double increateRatio, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        //Create a new table
        final String createExpForLevelStatement = "CREATE TABLE IF NOT EXISTS " + EXP_FOR_LEVEL_TABLE + " ( "
                + EXP_FOR_EACH_LEVEL_COLUMN + " INT, "
                + E_LEVEL_COLUMN + " INT);" ;
        db.execSQL(createExpForLevelStatement);

        int currentExp = expForLevel1;
        for(int level = 0; level<100; level ++ ){

            cv.put(E_LEVEL_COLUMN, level);
            //exp calculation formula 
            currentExp = (int)(currentExp + (expForLevel1 * increateRatio)) ;
            cv.put(EXP_FOR_EACH_LEVEL_COLUMN, currentExp);
            db.insert(EXP_FOR_LEVEL_TABLE, null, cv);

        }
    }

    public int getExpForLevelUp(int level){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EXP_FOR_LEVEL_TABLE + " WHERE " + E_LEVEL_COLUMN + " = " + level + ";", null);
        cursor.moveToFirst();
        int exp = cursor.getInt(cursor.getColumnIndex(EXP_FOR_EACH_LEVEL_COLUMN));
        cursor.close();
        db.close();
        return exp;
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
        db.execSQL("DROP TABLE IF EXISTS " + DUEDATE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WEEKLY_SCHEDULE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXP_FOR_LEVEL_TABLE);
        onCreate(db);
    }

    public void upgrade() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DUEDATE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WEEKLY_SCHEDULE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EXP_FOR_LEVEL_TABLE);
        onCreate(db);
    }

    //item model updateData****************************************************************************************************
    public void updateAllItem(int currency, int food, int toy){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE_NAME + " SET "
                + CURRENCY_COLUMN + " = "+ currency + ", "
                + FOOD_COLUMN + " = " + food + ", "
                + TOY_COLUMN + " = " + toy + ";");
        db.close();
    }
    //init Item table
    private void initItem(SQLiteDatabase db){
        ContentValues cv = new ContentValues();

        cv.put(CURRENCY_COLUMN, 100);
        cv.put(FOOD_COLUMN, 0);
        cv.put(TOY_COLUMN, 0);

        db.insert(ITEM_TABLE_NAME, null, cv);
    }

    public void updateCurrency(int currency){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE_NAME + " SET "
                + CURRENCY_COLUMN + " = " + currency + ";");
        db.close();
    }

    public void updateFood(int food){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE_NAME + " SET "
                + FOOD_COLUMN + " = " + food + ";");
        db.close();
    }

    public void updateToy(int toy){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE_NAME + " SET "
                + TOY_COLUMN + " = " + toy + ";");
        db.close();
    }

    public int getCurrency() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From  "+ ITEM_TABLE_NAME, null);
        cursor.moveToFirst();
        int currency = cursor.getInt(cursor.getColumnIndex(CURRENCY_COLUMN));
        cursor.close();
        db.close();
        return currency;
    }

    public int getFood() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From  "+ ITEM_TABLE_NAME, null);
        cursor.moveToFirst();
        int food = cursor.getInt(cursor.getColumnIndex(FOOD_COLUMN));
        cursor.close();
        db.close();
        return food;
    }

    public int getToy() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From  "+ ITEM_TABLE_NAME, null);
        cursor.moveToFirst();
        int toy = cursor.getInt(cursor.getColumnIndex(TOY_COLUMN));
        cursor.close();
        db.close();
        return toy;
    }

    public void removeAllItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME + ";");
        onCreate(db);
        db.close();
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

    //init pet table
    private void initPet(PetModel pet, SQLiteDatabase db){
        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN, pet.getName());
        cv.put(HUNGRINESS_COLUMN, pet.getHungriness());
        cv.put(MOOD_COLUMN, pet.getMood());
        cv.put(EXP_COLUMN, pet.getExp());
        cv.put(LEVEL_COLUMN, pet.getLv());

        db.insert(PET_TABLE_NAME, null, cv);
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
    //DueDate table------------------------------------------------------------------------------------------------------
    public void addDate(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EVENT_TITLE_COLUMN, eventDateModel.getEventTitle());
        cv.put(EVENT_DETAIL_COLUMN, eventDateModel.getEventDetail());
        cv.put(SET_DATE_COLUMN, Calendar.getInstance().getTimeInMillis());
        cv.put(DUE_DATE_COLUMN, eventDateModel.getDueDate().getTimeInMillis());
        cv.put(ID_COLUMN, eventDateModel.getID());
        cv.put(WAKED_COLUMN, 0);

        db.insert(DUEDATE_TABLE_NAME, null, cv);
        db.close();
    }

    public EventDateModel getSetDate (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DUE_DATE_COLUMN + " WHERE " + ID_COLUMN + " = " + id + ";", null);
        cursor.moveToFirst();
        long dueDateMilli = cursor.getLong(cursor.getColumnIndex(SET_DATE_COLUMN));
        cursor.close();
        db.close();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dueDateMilli);
        return  new EventDateModel(cal);
    }

    public long getDueDate (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DUE_DATE_COLUMN + " WHERE " + ID_COLUMN + " = " + id + ";", null);
        cursor.moveToFirst();
        long setDateMilli = cursor.getLong(cursor.getColumnIndex(DUE_DATE_COLUMN));
        cursor.close();
        db.close();
        return setDateMilli;
    }

    public void deleteOneFromDueDate(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
                DUEDATE_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";" ;
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public void deleteAllFromDueDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+DUEDATE_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public List<EventDateModel> getDueDateReminder(){
        List<EventDateModel> eventDateModelsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+DUEDATE_TABLE_NAME+" ORDER BY "+ DUE_DATE_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                long setDate = cursor.getLong(cursor.getColumnIndex(SET_DATE_COLUMN));
                long dueDate = cursor.getLong(cursor.getColumnIndex(DUE_DATE_COLUMN));
                String eventTitle = cursor.getString(cursor.getColumnIndex(EVENT_TITLE_COLUMN));
                String eventDetail = cursor.getString(cursor.getColumnIndex(EVENT_DETAIL_COLUMN));
                int id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN));
                int waked = cursor.getInt(cursor.getColumnIndex(WAKED_COLUMN));

                EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, setDate, dueDate, id, waked);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
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

    //update wake status in duedate table
    public void updateWakedStatusInDueDate(int id, int wakedStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + DUEDATE_TABLE_NAME + " SET "
                + WAKED_COLUMN + " = "+ wakedStatus
                + " WHERE " + ID_COLUMN + " = " + id + ";");
        db.close();
    }

    //get one by id from duedate table
    public EventDateModel getOneFromDueDate(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        EventDateModel eventDateModel;
        Cursor cursor = db.rawQuery("SELECT * FROM " + DUEDATE_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";", null);
        cursor.moveToFirst();
        long setDate = cursor.getLong(cursor.getColumnIndex(SET_DATE_COLUMN));
        long dueDate = cursor.getLong(cursor.getColumnIndex(DUE_DATE_COLUMN));
        String eventTitle = cursor.getString(cursor.getColumnIndex(EVENT_TITLE_COLUMN));
        String eventDetail = cursor.getString(cursor.getColumnIndex(EVENT_DETAIL_COLUMN));
        int waked = cursor.getInt(cursor.getColumnIndex(WAKED_COLUMN));
        eventDateModel = new EventDateModel(eventTitle,eventDetail,setDate, dueDate,id,waked);
        cursor.close();
        db.close();
        return eventDateModel;
    }


    //Daily table ------------------------------------------------------------------------------------------------------
    public void addOneToDaily(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TIME_FOR_ORDER_COLUMN, eventDateModel.getTimeForOrder());
        cv.put(HOUR_COLUMN, eventDateModel.getHour());
        cv.put(MINUTE_COLUMN,eventDateModel.getMinute());
        cv.put(EVENT_TITLE_COLUMN, eventDateModel.getEventTitle());
        cv.put(ID_COLUMN, eventDateModel.getID());
        cv.put(WAKED_COLUMN, eventDateModel.getWaked());
        cv.put(WAKED_TIME_COLUMN, 0);

        db.insert(DAILY_TABLE_NAME, null, cv);
        db.close();
    }

    public void updateDaily(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + DAILY_TABLE_NAME + " SET "
                + WAKED_COLUMN + " = " + eventDateModel.getWaked() + ", "
                + EVENT_TITLE_COLUMN + " = '" + eventDateModel.getEventTitle() + "', "
                + HOUR_COLUMN + " = " + eventDateModel.getHour() + ", "
                + MINUTE_COLUMN + " = " + eventDateModel.getMinute() + ", "
                + WAKED_TIME_COLUMN + " = " + eventDateModel.getWakedTime() + ", "
                + TIME_FOR_ORDER_COLUMN + " = " + eventDateModel.getTimeForOrder()
                + " WHERE " + ID_COLUMN + " = " + eventDateModel.getID() + ";");
        db.close();
    }

    //delete from daily
    public void deleteOneFromDaily(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " +
                DAILY_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id + ";" ;
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    //Delete all in Daily table
    public void deleteAllFromDaily() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+DAILY_TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
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
                int wakedTime = cursor.getInt(cursor.getColumnIndex(WAKED_TIME_COLUMN));


                EventDateModel eventDateModel = new EventDateModel(eventTitle,hour,minute,id, waked, wakedTime);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
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
        int wakedTime = cursor.getInt(cursor.getColumnIndex(WAKED_TIME_COLUMN));
        eventDateModel = new EventDateModel(eventTitle,hour,minute,id,waked,wakedTime);
        cursor.close();
        db.close();
        return eventDateModel;
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


    //update wake status in daily table
    public void updateWakedStatusInDaily(int id, int wakedStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + DAILY_TABLE_NAME + " SET "
                + WAKED_COLUMN + " = "+ wakedStatus
                + " WHERE " + ID_COLUMN + " = " + id + ";");
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

        cv.put(EVENT_ID_COLUMN, weeklyScheduleModel.getId());
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

    public List<WeeklyScheduleModel> getDailySchedule(String weeday){
        List<WeeklyScheduleModel> weeklyScheduleModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+WEEKLY_SCHEDULE_TABLE_NAME + " WHERE " + WEEK_DAY_COLUMN + " LIKE '" + weeday + "' ORDER BY "+ EVENT_ID_COLUMN, null);

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
