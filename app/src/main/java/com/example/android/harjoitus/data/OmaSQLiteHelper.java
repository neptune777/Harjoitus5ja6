package com.example.android.harjoitus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by miksa on 2/6/18.
 */

/**
 * Modified by matti on 11/5/18.
 */

public class OmaSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "akut.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + DatabaseContract.DatabaseEntry.TABLE_AKUT + "( " + DatabaseContract.DatabaseEntry._ID
            + " integer primary key autoincrement, " +
            DatabaseContract.DatabaseEntry.COLUMN_NRO
            + " text not null, "+ DatabaseContract.DatabaseEntry.COLUMN_NIMI
            + " text not null,"+
            DatabaseContract.DatabaseEntry.COLUMN_PAINOS
            + " text not null, "+ DatabaseContract.DatabaseEntry.COLUMN_HANKINTAPVM
            + " text not null);";

    public OmaSQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(OmaSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.DatabaseEntry.TABLE_AKUT);
        onCreate(db);
    }

}