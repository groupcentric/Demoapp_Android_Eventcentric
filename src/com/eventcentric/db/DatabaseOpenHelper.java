package com.eventcentric.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.eventcentric.helper.L;

class DatabaseOpenHelper extends android.database.sqlite.SQLiteOpenHelper implements DB {
    // Whenever we change the DB structure, increment DATABASE_VERSION
    //- note it's only used for upgrades; if it's a new install,
    // onUpgrade WONT be called and everything is done by onCreate instead  (So new structure is modifed both places)
    //
    //Everytime we need to change the structure of the table
    // write code accordingly for the onCreate() and onUpdate() methods
    private final static int DATABASE_VERSION = 2;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        setupDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  
    }

    


    static private void startTable(StringBuilder query, String tableName) {
        query.setLength(0);
        query.append(CREATE_TABLE_QUERY);
        query.append(tableName);

        query.append(START_TABLE);
        query.append(COLUMN_ID);
        query.append(TYPE_ID);
        query.append(SEPARATE_COLUMN);
    }

    static private void alterTable(StringBuilder query, String tableName) {
        query.setLength(0);
        query.append(ALTER_TABLE_QUERY);
        query.append(tableName);
    }

    static private void appendColumn(StringBuilder query, String columnName, String columnType) {
        query.append(columnName);
        query.append(columnType);
        query.append(SEPARATE_COLUMN);
    }

    static private void addColumn(StringBuilder query, String columnName, String columnType) {
        query.append(ADD_COLUMN);
        query.append(columnName);
        query.append(columnType);
        query.append(";");
    }

    static private void commitTable(SQLiteDatabase db, StringBuilder query) {
        int L = query.length();
        int s = SEPARATE_COLUMN.length();
        if ((query.lastIndexOf(SEPARATE_COLUMN)) == L - s)
            query.replace(L - s, L, "");
        query.append(END_TABLE);
        //System.out.println(query.toString());
        db.execSQL(query.toString());
    }

    static private void commitAlterTable(SQLiteDatabase db, StringBuilder query) {
        db.execSQL(query.toString());
    }

    private void setupDB(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            StringBuilder query = new StringBuilder();
            startTable(query, TABLE_SETTINGS);
            appendColumn(query, COLUMN_SETTINGS_KEY, TYPE_TEXT);
            appendColumn(query, COLUMN_SETTINGS_VALUE, TYPE_TEXT);
            commitTable(db, query);

            startTable(query, TABLE_TOWNS);
            appendColumn(query, COLUMN_TOWNNAME, TYPE_TEXT);
            appendColumn(query, COLUMN_LATITUDE, TYPE_TEXT);
            appendColumn(query, COLUMN_LONGITUDE, TYPE_TEXT);
            commitTable(db, query);

          

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
