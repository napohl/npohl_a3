package com.csci448.npohl.npohl_a3.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.csci448.npohl.npohl_a3.PinData;

import java.util.Date;

/**
 * Created by Nate on 4/12/2017.
 */

public class PinDataCursorWrapper extends CursorWrapper {
    public PinDataCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public PinData getPinData() {
        double lat = getDouble(getColumnIndex(PinDataDbSchema.PinDataTable.Cols.LAT));
        double lon = getDouble(getColumnIndex(PinDataDbSchema.PinDataTable.Cols.LON));
        long date = getLong(getColumnIndex(PinDataDbSchema.PinDataTable.Cols.DATE));
        int temp = getInt(getColumnIndex(PinDataDbSchema.PinDataTable.Cols.TEMP));
        String condition = getString(getColumnIndex(PinDataDbSchema.PinDataTable.Cols.WEATHER));

        PinData data = new PinData();
        data.setLat(lat);
        data.setLon(lon);
        data.setDate(new Date(date));
        data.setTemp(temp);
        data.setCondition(condition);

        return data;
    }
}
