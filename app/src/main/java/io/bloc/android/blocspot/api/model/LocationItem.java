package io.bloc.android.blocspot.api.model;

/**
 * Created by Administrator on 11/1/2015.
 */
public class LocationItem extends Model {

    private String mLocationName;
    private String mCategoryName;
    private String mLocationNotes;

    private boolean mHasVisitedLocation;

        //----------Constructor-----------
    public LocationItem(long rowId, String locationName, String categoryName,
                        String locationNotes, boolean hasVisitedLocation) {

        super(rowId);
        mLocationName = locationName;
        mCategoryName = categoryName;
        mLocationNotes = locationNotes;
        mHasVisitedLocation = hasVisitedLocation;
    }

        //----------Getters--------------
    public String getLocationName() {
        return mLocationName;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public String getLocationNotes() {
        return mLocationNotes;
    }

    public boolean isHasVisitedLocation() {
        return mHasVisitedLocation;
    }
}
