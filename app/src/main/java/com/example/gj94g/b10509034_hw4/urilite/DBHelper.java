package com.example.gj94g.b10509034_hw4.urilite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gj94HW4.db";
    private static final int VERSION = 4;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + DBContract.ClockEntry.TABLE_NAME + " (" +
                DBContract.ClockEntry._ID + " INTEGER PRIMARY KEY, " +
                DBContract.ClockEntry.COLUMN_ENABLE + " TEXT NOT NULL, " +
                DBContract.ClockEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                DBContract.ClockEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                DBContract.ClockEntry.COLUMN_WEEK + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ClockEntry.TABLE_NAME);
        onCreate(db);
    }

}
