package com.flyby_riders.SQLite_DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by KRISHNENDU MANNA  00/00/2020
 */
public class TestAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public TestAdapter(Context mContext) {
        this.mContext = mContext;
        mDbHelper = new DataBaseHelper(mContext);
    }


    public TestAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }


    public TestAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }


    public void close() {
        mDbHelper.close();
    }


    public boolean INSERT_REALTIMELOCATION(String RIDE_ID, String MEMBER_ID,
                                           String LATITUDE, String LONGITUDE,
                                           String TIMESTAMP) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("RIDE_ID", RIDE_ID);
        contentValues.put("MEMBER_ID", MEMBER_ID);
        contentValues.put("LATITUDE", LATITUDE);
        contentValues.put("LONGITUDE", LONGITUDE);
        contentValues.put("TIMESTAMP", TIMESTAMP);
        mDb.insert("REALTIMELOCATION", null, contentValues);
        return true;
    }

    public Cursor GET_REALTIMELOCATION(String RIDE_ID_ST, String MEMBER_ID_ST) {
        Cursor cursor = mDb.rawQuery("Select * from REALTIMELOCATION" + " WHERE " + "RIDE_ID='" + RIDE_ID_ST + "'" + " AND " + "MEMBER_ID='" + MEMBER_ID_ST + "'", null);
        return cursor;
    }


    public boolean INSERT_RIDE_DATA(String RIDE_ID_ST, String MEMBER_ID_ST,
                                    String TOP_SPEED, String AVG_SPEED,
                                    String TOTAL_TIME, String TRACKING_STATUS) {


        Cursor cursor = mDb.rawQuery("Select * from RIDE_DATA" + " WHERE " + "RIDE_ID='" + RIDE_ID_ST + "'" + " AND " + "MEMBER_ID='" + MEMBER_ID_ST + "'", null);

        if (cursor.getCount() != 0) {
            ContentValues args = new ContentValues();
            if (!TOP_SPEED.equalsIgnoreCase("")) {
                args.put("TOP_SPEED", TOP_SPEED);
            }

            if (!AVG_SPEED.equalsIgnoreCase("")) {
                args.put("AVG_SPEED", AVG_SPEED);
            }

            if (!TOTAL_TIME.equalsIgnoreCase("")) {
                args.put("TOTAL_TIME", TOTAL_TIME);
            }

            if (!TRACKING_STATUS.equalsIgnoreCase("")) {
                args.put("TRACKING_STATUS", TRACKING_STATUS);
            }

            return mDb.update("RIDE_DATA", args, "RIDE_ID" + "=" + RIDE_ID_ST + " AND " + "MEMBER_ID" + "=" + MEMBER_ID_ST, null) > 0;

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("RIDE_ID", RIDE_ID_ST);
            contentValues.put("MEMBER_ID", MEMBER_ID_ST);
            contentValues.put("TOP_SPEED", TOP_SPEED);
            contentValues.put("AVG_SPEED", AVG_SPEED);
            contentValues.put("TOTAL_TIME", TOTAL_TIME);
            contentValues.put("TRACKING_STATUS", TRACKING_STATUS);
            mDb.insert("RIDE_DATA", null, contentValues);
            return true;
        }


    }


    public Cursor GET_RIDE_DATA(String RIDE_ID_ST, String MEMBER_ID_ST) {
        Cursor cursor = mDb.rawQuery("Select * from RIDE_DATA" + " WHERE " + "RIDE_ID='" + RIDE_ID_ST + "'" + " AND " + "MEMBER_ID='" + MEMBER_ID_ST + "'", null);
        return cursor;
    }






}
