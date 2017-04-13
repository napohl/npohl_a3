package com.csci448.npohl.npohl_a3.database;

public class PinDataDbSchema {
    public static final class PinDataTable {
        public static final String NAME = "pindata";

        public static final class Cols {
            public static final String LAT = "latitude";
            public static final String LON = "longitude";
            public static final String DATE = "date";
            public static final String TEMP = "temperature";
            public static final String WEATHER = "weather";
        }
    }
}

