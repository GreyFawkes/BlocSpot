package io.bloc.android.blocspot.api.model;

import android.location.Location;

/**
 * Created by Administrator on 11/1/2015.
 */
public class LocationItem extends Model {

    private String mLocationName;
    private String mLocationNotes;

    private boolean mHasVisitedLocation, mIsSearchedLocation;

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
        mIsSearchedLocation = false;

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
        mIsSearchedLocation = false;

    }

        //a basic location item for testing
    public LocationItem(String locationName, boolean isSearchedLocation) {
        super(0l);
        mLocationName = locationName;
        mCategoryId = 0l;
        mLocationNotes = "";
        mHasVisitedLocation = false;
        mLocation = new Location("");
        mLocation.setLatitude(0.0d);
        mLocation.setLatitude(0.0d);
        mLocation.setAltitude(0.0d);
        mIsSearchedLocation = isSearchedLocation;

    }

        //-----------setters------------

    public void setIsSearchedLocation(boolean isSearchedLocation) {
        mIsSearchedLocation = isSearchedLocation;
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

    public boolean isSearchedLocation() {
        return mIsSearchedLocation;
    }

    public Location getLocation() {
        return mLocation;
    }
}
