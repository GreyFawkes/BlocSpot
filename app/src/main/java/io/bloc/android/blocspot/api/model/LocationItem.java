package io.bloc.android.blocspot.api.model;

import android.location.Location;

/**
 * Created by Administrator on 11/1/2015.
 */
public class LocationItem extends Model {

    private String mLocationName;
    private String mLocationNotes;

    private boolean mHasVisitedLocation;

    private long mCategoryId;
    private Location mLocation;

        //----------Constructor-----------
    public LocationItem(long rowId, String locationName, long categoryId,
                        String locationNotes, boolean hasVisitedLocation,
                        Location location) {

        super(rowId);
        mLocationName = locationName;
        mCategoryId = categoryId;
        mLocationNotes = locationNotes;
        mHasVisitedLocation = hasVisitedLocation;
        mLocation = location;
    }



    public LocationItem(long rowId, String locationName, long categoryId,
                        String locationNotes, boolean hasVisitedLocation) {

        super(rowId);
        mLocationName = locationName;
        mCategoryId = categoryId;
        mLocationNotes = locationNotes;
        mHasVisitedLocation = hasVisitedLocation;
        mLocation = new Location("");
        mLocation.setLatitude(0.0d);
        mLocation.setLatitude(0.0d);
        mLocation.setAltitude(0.0d);
    }


        //----------Getters--------------
    public String getLocationName() {
        return mLocationName;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public String getLocationNotes() {
        return mLocationNotes;
    }

    public boolean hasVisitedLocation() {
        return mHasVisitedLocation;
    }

    public Location getLocation() {
        return mLocation;
    }
}
