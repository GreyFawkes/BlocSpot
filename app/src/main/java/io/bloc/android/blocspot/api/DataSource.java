package io.bloc.android.blocspot.api;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.BuildConfig;
import io.bloc.android.blocspot.api.model.CategoryItem;
import io.bloc.android.blocspot.api.model.LocationItem;
import io.bloc.android.blocspot.api.model.database.DatabaseOpenHelper;
import io.bloc.android.blocspot.api.model.database.table.CategoryItemTable;
import io.bloc.android.blocspot.api.model.database.table.LocationItemTable;

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
    private LocationItemTable mLocationItemTable;

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
        mLocationItemTable = new LocationItemTable();

            //init ExecutorService
        mExecutorService = Executors.newSingleThreadExecutor();

            //init DatabaseOpenHelper with the needed tables
        mDatabaseOpenHelper = new DatabaseOpenHelper(
                BlocSpotApplication.getSharedInstance(),
                mCategoryItemTable, mLocationItemTable);
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

                //Table Builders go here - Categories
            new CategoryItemTable.Builder()
                    .setCategoryName("Cat Z")
                    .setIsChecked(false)
                    .insert(writeableDatabase);
            new CategoryItemTable.Builder()
                    .setCategoryName("Cat B")
                    .setIsChecked(true)
                    .insert(writeableDatabase);
            new CategoryItemTable.Builder()
                    .setCategoryName("Cat C")
                    .insert(writeableDatabase);

                //Table Builders - Locations
            new LocationItemTable.Builder()
                    .setLocationName("Location A")
                    .setNotes("This is Location A")
                    .setHasVisited(true)
                    .setCategory(getCategoryItemByTitle("Cat Z").getRowId())
                    .insert(writeableDatabase);
            new LocationItemTable.Builder()
                    .setLocationName("Location C")
                    .setNotes("This is Location C")
                    .setHasVisited(false)
                    .setCategory(getCategoryItemByTitle("Cat C").getRowId())
                    .insert(writeableDatabase);
            new LocationItemTable.Builder()
                    .setLocationName("Location T")
                    .setNotes("This is Location T")
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
                if (cursor.moveToFirst()) {
                    do {
                        categoryItems.add(categoryItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                callbackThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(categoryItems);
                    }
                });
            }
        });

    }

    public void fetchLocationItems(final Callback<List<LocationItem>> callback) {

            //create a new handler
        final Handler callbackThreadHander = new Handler();

            //submit the task of getting a cursor to build a list
        submitTask(new Runnable() {
            @Override
            public void run() {
                    //create a list
                final List<LocationItem> locationItems = new ArrayList<LocationItem>();

                    //get the cursor from the locationItemTable
                Cursor cursor = LocationItemTable.fetchAllLocations(
                        mDatabaseOpenHelper.getReadableDatabase()
                );

                    //start building the list from the items in the cursor
                if(cursor.moveToFirst()) {
                    do {
                        locationItems.add(locationItemFromCursor(cursor));
                    } while(cursor.moveToNext());
                }
                    //close the cursor
                cursor.close();
                    //post the list through a separate thread
                callbackThreadHander.post(new Runnable() {
                    @Override
                    public void run() {
                            //post it in onSuccess
                        callback.onSuccess(locationItems);
                    }
                });
            }
        });
    }

        //add a new category to the database table
    public void addCategoryItem(String categoryTitle) {
        new CategoryItemTable.Builder()
                .setCategoryName(categoryTitle)
                .insert(mDatabaseOpenHelper.getWritableDatabase());
    }

        //returns a CategoryItem from the Table
    public CategoryItem getCategoryItemByTitle(String categoryTitle) {
        Cursor cursor;
        String tableName = CategoryItemTable.TABLE_NAME;
        String columnName = CategoryItemTable.COLUMN_NAME;
        String[] selectionArgs = new String[]{categoryTitle};
            //get the cursor needed to build the CategoryItem
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cursor = mDatabaseOpenHelper.getReadableDatabase().query(
                    true,tableName,null,columnName+" = ?",selectionArgs,null,null,null,null
            );
        } else {
            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + "= ?";
            cursor = mDatabaseOpenHelper.getReadableDatabase().rawQuery(
                    query, selectionArgs
            );
        }

        if(cursor.moveToFirst()) {
            return categoryItemFromCursorAndCloseCursor(cursor);
        }
        cursor.close();
        return null;

    }

        //getter methods for categoryItem from cursor
    private CategoryItem categoryItemFromCursor(Cursor cursor) {
        return new CategoryItem(
                CategoryItemTable.getRowId(cursor),
                CategoryItemTable.getCategoryName(cursor),
                CategoryItemTable.getIsChecked(cursor)
        );
    }
    private CategoryItem categoryItemFromCursorAndCloseCursor(Cursor cursor) {
        CategoryItem item = categoryItemFromCursor(cursor);
        cursor.close();
        return item;
    }

        //getter methods for LocationItems from cursor
    private LocationItem locationItemFromCursor(Cursor cursor) {
        return new LocationItem(
                LocationItemTable.getRowId(cursor),
                LocationItemTable.getLocationName(cursor),
                LocationItemTable.getCategoryId(cursor),
                LocationItemTable.getNotes(cursor),
                LocationItemTable.getHasVisited(cursor)
        );
    }
    private LocationItem locationItemFromCursorAndCloseCursor(Cursor cursor) {
        LocationItem item = locationItemFromCursor(cursor);
        cursor.close();
        return item;
    }

    private void submitTask(Runnable task) {
        if(mExecutorService.isShutdown() || mExecutorService.isTerminated()) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        mExecutorService.submit(task);
    }

}
