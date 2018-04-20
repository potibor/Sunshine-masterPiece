package com.hasanozanal.sunshine.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.hasanozanal.sunshine.Model.Weather;

/**
 * Created by ozanal on 27/03/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weatherDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "dailyweather";
    public static final String CITY_LAT = "lat";
    public static final String CITY_LON = "lon";
    public static final String KEY_ID = "_id";

    private ArrayList<Weather> weatherArrayList = new ArrayList<>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_LAT + " DOUBLE, " + CITY_LON + " DOUBLE)";
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addWeather(Double lat, Double lon){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CITY_LAT,lat);
        values.put(CITY_LON,lon);
        Log.v("lat",""+ lat);
        Log.v("lon",""+ lon);
        database.insert(TABLE_NAME,null,values);
        database.close();
    }
    public ArrayList<Weather> getWeather(){
        String SelectQuery = " SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,new String[]{CITY_LAT,CITY_LON,KEY_ID},null,null,null,null,null);

        if (cursor.moveToFirst()){
            do {
                Weather weather = new Weather();
                weather.setLat(cursor.getDouble(cursor.getColumnIndex(CITY_LAT)));
                weather.setLon(cursor.getDouble(cursor.getColumnIndex(CITY_LON)));
                weather.setItemId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));

                weatherArrayList.add(weather);
            }while (cursor.moveToNext());
        }
        return weatherArrayList;
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME +";");
    }
    public void deleteItem(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME , KEY_ID + " =? ",new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        Log.v("success","deleted");
    }
}
