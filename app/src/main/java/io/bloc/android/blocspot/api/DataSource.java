package io.bloc.android.blocspot.api;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.BuildConfig;
import io.bloc.android.blocspot.api.model.CategoryItem;
import io.bloc.android.blocspot.api.model.database.DatabaseOpenHelper;
import io.bloc.android.blocspot.api.model.database.table.CategoryItemTable;

/**
 * Created by Administrator on 11/1/2015.
 */
public class DataSource {

    public interface Callback<Result> {
        void onSuccess(Result result);
        void onError(String errorMessage);
    }

    private DatabaseOpenHelper mDatabaseOpenHelper;
    private CategoryItemTable mCategoryItemTable;

    private ExecutorService mExecutorService;

    public DataSource() {

            //initialize variables
        initMemberVariables();
            //debug operations
        debugOperations();

    }

        //void method for initializing Tables and other member variables
    private void initMemberVariables() {
            //init Tables
        mCategoryItemTable = new CategoryItemTable();

            //init ExecutorService
        mExecutorService = Executors.newSingleThreadExecutor();

            //init DatabaseOpenHelper with the needed tables
        mDatabaseOpenHelper = new DatabaseOpenHelper(
                BlocSpotApplication.getSharedInstance(),
                mCategoryItemTable);
    }

        //method that deletes the old database and
        // resets the database with initial data
    private void debugOperations() {
        if(BuildConfig.DEBUG && true) {
                //delete any database that currently exists in memory
            BlocSpotApplication.getSharedInstance().deleteDatabase(
                    mDatabaseOpenHelper.getDatabaseName()
            );
                //get a writable database for any pre-entered data
            SQLiteDatabase writeableDatabase =
                    mDatabaseOpenHelper.getWritableDatabase();

                //Table Builders go here
            new CategoryItemTable.Builder()
                    .setCatagoryName("Cat 1")
                    .insert(writeableDatabase);
            new CategoryItemTable.Builder()
                    .setCatagoryName("Cat 2")
                    .insert(writeableDatabase);
            new CategoryItemTable.Builder()
                    .setCatagoryName("Cat 3")
                    .insert(writeableDatabase);

        }
    }


        //fetches all of the CategoryItems in the database
        //this will probably only be used in the filter dialog
    public void fetchCategoryItems(final Callback<List<CategoryItem>> callback) {
            //create a Handler to handler the callbacks interface methods
        final Handler callbackThreadHandler = new Handler();

            //begin the task of getting all of the categories in the
            //database tables
        submitTask(new Runnable() {
            @Override
            public void run() {
                    //create an arraylist for the categoryItems
                final List<CategoryItem> categoryItems = new ArrayList<CategoryItem>();

                    //get the cursor with the table information
                Cursor cursor = CategoryItemTable.fetchAllCategories(
                        mDatabaseOpenHelper.getReadableDatabase());

                    //check if the cursor is empty
                    //start adding the items to the list
                    //close the cursor
                if(cursor.moveToFirst()) {
                    do {
                        categoryItems.add(categoryItemFromCursor(cursor));
                    }while (cursor.moveToNext());
                    cursor.close();
                }
                callbackThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(categoryItems);
                    }
                });
            }
        });

    }

    private CategoryItem categoryItemFromCursor(Cursor cursor) {
        return new CategoryItem(
                CategoryItemTable.getRowId(cursor),
                CategoryItemTable.getCategoryName(cursor)
        );
    }

    private void submitTask(Runnable task) {
        if(mExecutorService.isShutdown() || mExecutorService.isTerminated()) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        mExecutorService.submit(task);
    }

}
