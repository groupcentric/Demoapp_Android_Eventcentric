package com.eventcentric.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import com.eventcentric.helper.Town;
import com.eventcentric.helper.Util;

public class DatabaseManager implements DB {

    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        DatabaseOpenHelper sqlCreator = new DatabaseOpenHelper(context);
        db = sqlCreator.getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public boolean putString(String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SETTINGS_KEY, key);
        cv.put(COLUMN_SETTINGS_VALUE, value);
        boolean res = db.update(TABLE_SETTINGS, cv, COLUMN_SETTINGS_KEY + " = '" + key + '\'', null) > 0; 
        if (!res) res = db.insert(TABLE_SETTINGS, COLUMN_SETTINGS_KEY, cv) > -1;
        return res;
    }

    public boolean putInt(String key, int value) {
        return putString(key, String.valueOf(value));
    }

    public boolean putLong(String key, long value) {
        return putString(key, String.valueOf(value));
    }

    public boolean putBoolean(String key, boolean value) {
        return putString(key, String.valueOf(value ? 1 : 0));
    }

    public String getString(String key) {
        String res = getString(key, null);
        if (res == null)
            throw new NullPointerException();
        return res;
    }

    public String getString(String key, String defaultValue) {
        Cursor c = db.query(TABLE_SETTINGS, new String[] { COLUMN_SETTINGS_VALUE }, COLUMN_SETTINGS_KEY + " = '" + key + '\'', null, null, null, null);
        String res = c.moveToFirst() ? c.getString(0) : defaultValue;
        c.close();
        return res;
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key, null));
    }

    public int getInt(String key, int defaultValue) {
        String s = getString(key, null);
        if (s == null)
            return defaultValue;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key) {
        return getInt(key) > 0;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String s = getString(key, null);
        if (s == null)
            return defaultValue;
        try {
            return Integer.parseInt(s) > 0;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public long getLong(String key) {
        return Long.parseLong(getString(key, null));
    }

    public long getLong(String key, long defaultValue) {
        String s = getString(key, null);
        if (s == null)
            return defaultValue;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

   



    public void saveTown(Town town) {
        ContentValues cv = new ContentValues();
        //cv.put(COLUMN_ID, 1);//town.getId());
        cv.put(COLUMN_TOWNNAME, town.getName());
        cv.put(COLUMN_LATITUDE, "");//town.getLatitude());
        cv.put(COLUMN_LONGITUDE, "");//town.getLongitude());
        if (db.update(TABLE_TOWNS, cv, COLUMN_TOWNNAME + " = '" + town.getName()+"'", null) < 1) {
            db.insert(TABLE_TOWNS, null, cv);
        }
    }
    public boolean removeTown(String townname) {
        return db.delete(TABLE_TOWNS, DB.COLUMN_TOWNNAME + " = ?" , new String[] {townname}) == 1;
    }

    public Cursor getTownsCursor() {
        return db.query(DB.TABLE_TOWNS, null, null, null, null, null, DB.COLUMN_TOWNNAME);
    }

    public Cursor getCursor(String strTable,String strFields) {
      	/*
      	*   Author: Brian La Chat
      	*   Date: 03/01/2012
      	*
      	*   Input: strTable  = Table to query
      	*   	   strFields = Fields to pull back
      	*   Ouput:
      	*
      	*   Notes:
      	*	Revisions:
      	*/
        return db.query(strTable, new String[] {strFields}, null, null, null, null, null);
    }

    

    

    public long getTownId(String town) {
        Cursor c = db.query(TABLE_TOWNS, new String[] {COLUMN_ID}, COLUMN_TOWNNAME + " = '" + town + '\'', null, null, null, null); 
        long res = -1;
        if (c.moveToFirst())
            res = c.getLong(0);
        c.close();
        return res;
    }

    public String getTownLat(String town) {
        Cursor c = db.query(TABLE_TOWNS, new String[] {COLUMN_LATITUDE}, COLUMN_TOWNNAME + " = '" + town + '\'', null, null, null, null);
        String res = "";
        if (c.moveToFirst())
            res = c.getString(0);
        c.close();
        return res;
    }

    public String getTownLon(String town) {
        Cursor c = db.query(TABLE_TOWNS, new String[] {COLUMN_LONGITUDE}, COLUMN_TOWNNAME + " = '" + town + '\'', null, null, null, null);
        String res = "";
        if (c.moveToFirst())
            res = c.getString(0);
        c.close();
        return res;
    }

   
}
