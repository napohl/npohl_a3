package com.csci448.npohl.npohl_a3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csci448.npohl.npohl_a3.database.PinDataBaseHelper;
import com.csci448.npohl.npohl_a3.database.PinDataCursorWrapper;
import com.csci448.npohl.npohl_a3.database.PinDataDbSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton that implements the database and stores all PinData
 */
public class AllData {
    private static AllData sAllData;
    
    // TODO: 4/11/17 got back to listing 14.7 and remove a lot of the data from AllData
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static AllData get(Context context) {
        if (sAllData == null) {
            sAllData = new AllData(context);
        }
        return sAllData;
    }

    private AllData(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PinDataBaseHelper(mContext).getWritableDatabase();
    }

    public void addData(PinData d) {
        ContentValues values = getContentValues(d);

        mDatabase.insert(PinDataDbSchema.PinDataTable.NAME, null, values);
    }

    public List<PinData> getAllPinData() {
        List<PinData> data = new ArrayList<>();

        PinDataCursorWrapper cursor = queryPinData(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                data.add(cursor.getPinData());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return data;
    }

    private static ContentValues getContentValues(PinData data) {
        ContentValues values = new ContentValues();
        values.put(PinDataDbSchema.PinDataTable.Cols.LAT, data.getLat());
        values.put(PinDataDbSchema.PinDataTable.Cols.LON, data.getLon());
        values.put(PinDataDbSchema.PinDataTable.Cols.DATE, data.getDate().getTime());
        values.put(PinDataDbSchema.PinDataTable.Cols.TEMP, data.getTemp());
        values.put(PinDataDbSchema.PinDataTable.Cols.WEATHER, data.getCondition());

        return values;
    }

    private PinDataCursorWrapper queryPinData(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PinDataDbSchema.PinDataTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new PinDataCursorWrapper(cursor);
    }

    // TODO: 4/12/2017 add delete
    public void deleteDatabase() {
        mDatabase.delete(PinDataDbSchema.PinDataTable.NAME, null, null);
        //mDatabase = new PinDataBaseHelper(mContext).getWritableDatabase();
    }
}
