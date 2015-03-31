package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerald on 03/30/2015.
 */
public class DataSource {
    private final SQLiteHelper dbHelper;
    private final String[] allColumns;
    private SQLiteDatabase database;

    {
        allColumns = new String[]
                {
                        SQLiteHelper.COLUMN_ID,
                        SQLiteHelper.COLUMN_FIRSTNAME,
                };
    }

    public DataSource(Context context)
    {
        dbHelper = new SQLiteHelper(context);
    }

    public void open()
            throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Profile createProfile(String comment)
    {
        final ContentValues values;
        final long          insertId;
        final Cursor        cursor;
        final Profile       newProfile;

        values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_FIRSTNAME, comment);
        insertId = database.insert(SQLiteHelper.COLUMN_FIRSTNAME,
                null,
                values);
        cursor = database.query(SQLiteHelper.COLUMN_FIRSTNAME,
                allColumns,
                SQLiteHelper.COLUMN_ID + " = " + insertId,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        newProfile = cursorToProfile(cursor);
        cursor.close();

        return (newProfile);
    }

    public void deleteProfile(final Profile comment)
    {
        final long id;

        id = comment.getId();
        System.out.println("Profile deleted with id: " + id);
        database.delete(SQLiteHelper.COLUMN_FIRSTNAME,
                SQLiteHelper.COLUMN_ID + " = " + id,
                null);
    }

    public List<Profile> getAllProfiles()
    {
        final List<Profile> comments;
        final Cursor cursor;

        comments = new ArrayList<Profile>();
        cursor   = database.query(SQLiteHelper.COLUMN_FIRSTNAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        try
        {
            cursor.moveToFirst();

            while(!(cursor.isAfterLast()))
            {
                final Profile comment;

                comment = cursorToProfile(cursor);
                comments.add(comment);
                cursor.moveToNext();
            }

        }
        finally
        {
            // make sure to close the cursor
            cursor.close();
        }

        return (comments);
    }

    private Profile cursorToProfile(final Cursor cursor)
    {
        final Profile comment;

        comment = new Profile();
        comment.setId(cursor.getLong(0));
        comment.setProfile(cursor.getString(1));

        return (comment);
    }
}
