package io.bloc.android.blocspot.api.model.database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 11/1/2015.
 */
public abstract class Table {

    public interface Builder {
        long insert(SQLiteDatabase writableDB);
    }

        //name for the column id
    protected static final String COLUMN_ID = "id";

        //abstract methods
    public abstract String getTableName();
    public abstract String getCreateStatement();

        //onUpgrade Method for the table
    public void onUpgrade(SQLiteDatabase readOnlyDatabase,
                          int oldVersion, int newVersion) {

        //nothing for now - used when upgrading the table in the database
    }

        //method for fetching a specific row in the database with the specific id
    public Cursor fetchRow(SQLiteDatabase readOnlyDatabase, long rowId) {
        return readOnlyDatabase.query(
                true,
                getTableName(),
                null,
                COLUMN_ID + " = ?",
                new String[] {String.valueOf(rowId)},
                null,
                null,
                null,
                null);
    }

    public static long getRowId(Cursor cursor) {
        return getLong(cursor, COLUMN_ID);
    }

    protected static String getString(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if(columnIndex == -1){
            return "";
        }
        return cursor.getString(columnIndex);
    }

    protected static long getLong(Cursor cursor, String column) {
        int columnIndex = cursor.getColumnIndex(column);
        if(columnIndex == -1) {
            return -1l;
        }
        return cursor.getLong(columnIndex);
    }

    protected static boolean getBoolean(Cursor cursor, String column) {
        return getLong(cursor, column) == 1l;
    }

}
