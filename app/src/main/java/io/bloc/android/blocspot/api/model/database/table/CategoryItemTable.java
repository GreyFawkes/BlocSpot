package io.bloc.android.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 11/1/2015.
 */
public class CategoryItemTable extends Table {

    //static final variables
    private static final String TABLE_NAME = "category_items";
    private static final String COLUMN_NAME = "name";


    public static class Builder implements Table.Builder {

        //value for the table
        ContentValues values = new ContentValues();

        //set the category name
        public Builder setCatagoryName(String catagoryName) {
            values.put(COLUMN_NAME, catagoryName);
            return this;
        }

        //insert the table into the db
        @Override
        public long insert(SQLiteDatabase writableDB) {
            return writableDB.insert(TABLE_NAME, null, values);
        }
    }

    public static Cursor fetchAllCategories(SQLiteDatabase readOnlyDatabase) {
        return readOnlyDatabase.rawQuery(
                "SELECT * FROM " + TABLE_NAME + "ORDER BY " + COLUMN_NAME,
                new String[]{COLUMN_NAME});
    }

        //------getters for this table name
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

        //The table creation statement for the CategoryItems
    @Override
    public String getCreateStatement() {
        return "CREATE TABLE " + getTableName()
                + " ("
                + COLUMN_ID + "INTEGER PRIMARY KEY"
                + COLUMN_NAME + "TEXT"
                + ")";
    }

        //public static methods for getting values from a Cursor
    public static String getCategoryName(Cursor cursor) {
        return getString(cursor, COLUMN_NAME);
    }




}
