package com.csci448.npohl.npohl_a3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.csci448.npohl.npohl_a3.PinData;
import com.csci448.npohl.npohl_a3.database.PinDataDbSchema.PinDataTable;

public class PinDataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "pinDataBase.db";

    public PinDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PinDataTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                PinDataTable.Cols.LAT + ", " +
                PinDataTable.Cols.LON + ", " +
                PinDataTable.Cols.DATE + ", " +
                PinDataTable.Cols.TEMP + ", " +
                PinDataTable.Cols.WEATHER +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //intentionally left blank
    }
}
