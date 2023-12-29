package com.example.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Weather.db";
    private static final  int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "weather_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CITY = "weather_city";
    private static final String COLUMN_NOTE = "weather_note";

    public MyDatabaseHelper(@Nullable Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_NOTE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i , int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addCity(String city, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CITY, city);
        cv.put(COLUMN_NOTE, note);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1)
        {
            Toast.makeText(context, "Fail !", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Add Successfull !", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteCity(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_CITY + " = ?";
        String[] whereArgs = { city };

        int result = db.delete(TABLE_NAME, whereClause, whereArgs);

        if (result > 0) {
            Toast.makeText(context, "Delete Successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
