package io.bloc.android.blocspot;

import android.app.Application;

import io.bloc.android.blocspot.api.DataSource;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotApplication extends Application {

    //static variables here
    private static BlocSpotApplication sharedInstance;

    //DataSource variable
    private DataSource mDataSource;

    //get shared methods here
    public static BlocSpotApplication getSharedInstance() {
        return sharedInstance;
    }

    public DataSource getDataSource(){
        return mDataSource;
    }

    public static DataSource getSharedDataSource() {
        return BlocSpotApplication.getSharedInstance().getDataSource();
    }

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        sharedInstance = this;
        mDataSource = new DataSource();
    }

    //private methods

    //public methods

}
