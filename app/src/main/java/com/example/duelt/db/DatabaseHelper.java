package com.example.duelt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.duelt.EventDateModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "duelt.db";
    public static final int VERSION  = 1;
    private static final String TABLE_NAME = "EVENT_DATE_TABLE";
    private static final String TIME_FOR_ORDER_COLUMN = "TIME_FOR_ORDER";
    private static final String YEAR_COLUMN = "YEAR";
    private static final String MONTH_COLUMN = "MONTH";
    private static final String DAY_COLUMN = "DAY";
    private static final String HOUR_COLUMN = "HOUR";
    private static final String MINUTE_COLUMN = "MINUTE";
    private static final String EVENT_TITLE_COLUMN = "EVENT_TITLE";
    private static final String EVENT_DETAIL_COLUMN = "EVENT_DETAIL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ( " + TIME_FOR_ORDER_COLUMN
                + " BIGINT, " + YEAR_COLUMN + " INT, " + MONTH_COLUMN + " INT, " + DAY_COLUMN +
                " INT, " + HOUR_COLUMN + " INT, " + MINUTE_COLUMN + " INT, " + EVENT_TITLE_COLUMN +
                " TEXT, " + EVENT_DETAIL_COLUMN + " TEXT);";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    //delete event by its date
    public boolean deleteOne(EventDateModel eventDateModel){
        SQLiteDatabase db = this.getWritableDatabase();
        String quertyString = "DELETE FROM " +
               //TABLE_NAME+ " WHERE "  + YEAR_COLUMN + " = " + eventDateModel.getYear() + ";";
                TABLE_NAME + " WHERE " + TIME_FOR_ORDER_COLUMN + " = " + eventDateModel.getTimeForOrder() + ";" ;

        Cursor cursor = db.rawQuery(quertyString, null);


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
        String queryString = "DELETE FROM "+TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(queryString, null);
        cursor.close();
        db.close();
    }

    public List<EventDateModel> getAll(){
        List<EventDateModel> eventDateModelsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * From  "+TABLE_NAME+" ORDER BY "+ TIME_FOR_ORDER_COLUMN, null);

        if(cursor.moveToFirst()){

            do{
                int year = cursor.getInt(1);
                int month = cursor.getInt(2) ;
                int day = cursor.getInt(3);
                int hour = cursor.getInt(4);
                int minute = cursor.getInt(5) ;
                String eventTitle = cursor.getString(6);
                String eventDetail = cursor.getString(7);

                EventDateModel eventDateModel = new EventDateModel(eventTitle, eventDetail, year,month,day,hour,minute);
                eventDateModelsList.add(eventDateModel);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventDateModelsList;
    }
}
