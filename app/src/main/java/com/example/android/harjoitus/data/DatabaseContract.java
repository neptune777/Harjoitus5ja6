package com.example.android.harjoitus.data;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static final class DatabaseEntry implements BaseColumns {

        public static final String TABLE_AKUT = "taskukirjat";
        public static final String COLUMN_NRO = "nro";
        public static final String COLUMN_NIMI = "nimi";
        public static final String COLUMN_PAINOS = "painos";
        public static final String COLUMN_HANKINTAPVM = "hankintapvm";

    }
}
