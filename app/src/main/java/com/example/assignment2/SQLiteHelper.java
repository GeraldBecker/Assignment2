package com.example.assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gerald on 03/27/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_ASSIGNMENT2;
    public static final String COLUMN_ID;
    public static final String COLUMN_FIRSTNAME;
    private static final String DATABASE_NAME;
    private static final int DATABASE_VERSION;
    private static final String DATABASE_CREATE;

    static {
        TABLE_ASSIGNMENT2 = "comments";
        COLUMN_ID        = "_id";
        COLUMN_FIRSTNAME = "comment";
        DATABASE_NAME    = "commments.db";
        DATABASE_VERSION = 1;
        DATABASE_CREATE  = "create table " +
                TABLE_ASSIGNMENT2 + "(" + COLUMN_ID +
                " integer primary key autoincrement, " + COLUMN_FIRSTNAME +
                " text not null);";

    }
    public SQLiteHelper(final Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }


    @Override
    public void onCreate(final SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db,
                          final int            oldVersion,
                          final int            newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT2);
        onCreate(db);
    }
}
