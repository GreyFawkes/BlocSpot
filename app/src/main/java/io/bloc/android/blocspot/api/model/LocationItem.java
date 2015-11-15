package io.bloc.android.blocspot.api.model;

/**
 * Created by Administrator on 11/1/2015.
 */
public class LocationItem extends Model {

    private String mLocationName;
    private String mLocationNotes;

    private boolean mHasVisitedLocation;

    private long mCategoryId;

        //----------Constructor-----------
    public LocationItem(long rowId, String locationName, long categoryId,
                        String locationNotes, boolean hasVisitedLocation) {

        super(rowId);
        mLocationName = locationName;
        mCategoryId = categoryId;
        mLocationNotes = locationNotes;
        mHasVisitedLocation = hasVisitedLocation;
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
}
