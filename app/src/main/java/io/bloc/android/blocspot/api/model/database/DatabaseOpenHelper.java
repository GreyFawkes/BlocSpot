package io.bloc.android.blocspot.api.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.bloc.android.blocspot.api.model.database.table.Table;

/**
 * Created by Administrator on 11/1/2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

        //database name and version
    private static final String NAME = "blocspot_db";
    private static final int VERSION = 1;

    private Table[] mTables;

    public DatabaseOpenHelper(Context context, Table... tables) {
        super(context, NAME, null, VERSION);
        mTables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Table table : mTables) {
            db.execSQL(table.getCreateStatement());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(Table table : mTables) {
            table.onUpgrade(db, oldVersion, newVersion);
        }
    }

    public String getDatabaseName() {
        return NAME;
    }

}
