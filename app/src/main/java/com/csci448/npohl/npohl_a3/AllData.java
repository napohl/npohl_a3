package com.csci448.npohl.npohl_a3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.csci448.npohl.npohl_a3.database.PinDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that implements the database and stores all PinData
 */
public class AllData {
    private static AllData sAllData;

    private List<PinData> mDataSet;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private AllData(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PinDataBaseHelper(mContext).getWritableDatabase();
        mDataSet = new ArrayList<>();
    }
}
