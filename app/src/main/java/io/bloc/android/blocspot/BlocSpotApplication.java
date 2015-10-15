package io.bloc.android.blocspot;

import android.app.Application;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotApplication extends Application {

    //static variables here
    private static BlocSpotApplication sharedInstance;

    //get shared methods here
    public static BlocSpotApplication getSharedInstance() {
        return sharedInstance;
    }

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //private methods

    //public methods

}
