package com.csci448.npohl.npohl_a3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

//import com.csci448.npohl.npohl_a3.database.PinDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that implements the database and stores all PinData
 */
public class AllData {
    private static AllData sAllData;
    
    //// TODO: 4/11/17 got back to listing 14.7 and remove a lot of the data from AllData
    private List<PinData> mDataSet;
    private Context mContext;
    //private SQLiteDatabase mDatabase;

    private AllData(Context context) {
        mContext = context.getApplicationContext();
        //mDatabase = new PinDataBaseHelper(mContext).getWritableDatabase();
        mDataSet = new ArrayList<>();
    }

    // TODO: 4/11/17 add static getter to get all data (look at criminal intent) 
}
