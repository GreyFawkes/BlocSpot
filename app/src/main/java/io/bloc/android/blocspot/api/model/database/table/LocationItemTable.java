package io.bloc.android.blocspot.api.model.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

/**
 * Created by Administrator on 11/6/2015.
 */
public class LocationItemTable extends Table {

    //static final variables
    public static final String TABLE_NAME = "location_items";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_HAS_VISITED = "has_visited";


    public static class Builder implements Table.Builder {

        //value for the table
        ContentValues values = new ContentValues();

        //set the category name
        public Builder setLocationName(String locationName) {
            values.put(COLUMN_NAME, locationName);
            return this;
        }

        //set the category ID
        public Builder setCategory(long categoryId) {
            values.put(COLUMN_CATEGORY_ID, categoryId);
            return this;
        }


        public Builder setNotes(String locationNotes) {
            values.put(COLUMN_NOTES, locationNotes);
            return this;
        }

        public Builder setHasVisited(boolean hasVisited) {
            values.put(COLUMN_HAS_VISITED, hasVisited);
            return this;
        }

        //insert the table into the db
        @Override
        public long insert(SQLiteDatabase writableDB) {
            return writableDB.insert(TABLE_NAME, null, values);
        }
    }

    //fetch all Category Items in Ascending order in a cursor
    public static Cursor fetchAllLocations(SQLiteDatabase readOnlyDatabase) {

        //this method is the SKD is JellyBean or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return readOnlyDatabase.query(
                    true,
                    TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    COLUMN_NAME,
                    null,
                    null);

        } else { // otherwise use a raw query

            return readOnlyDatabase.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME + " ASC", null);
        }

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
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CATEGORY_ID + " INTEGER,"
                + COLUMN_NOTES + " TEXT,"
                + COLUMN_HAS_VISITED + " INTEGER DEFAULT 0"
                + ")";
    }

    //public static methods for getting values from a Cursor
    public static String getLocationName(Cursor cursor) {
        return getString(cursor, COLUMN_NAME);
    }
    public static long getCategoryId(Cursor cursor) { return getLong(cursor, COLUMN_CATEGORY_ID); }
    public static String getNotes(Cursor cursor) { return getString(cursor, COLUMN_NOTES); }
    public static boolean getHasVisited(Cursor cursor) {return getBoolean(cursor, COLUMN_HAS_VISITED); }

}
