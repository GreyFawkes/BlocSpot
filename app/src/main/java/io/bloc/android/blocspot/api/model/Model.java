package io.bloc.android.blocspot.api.model;

/**
 * Created by Administrator on 11/1/2015.
 */
public abstract class Model {

    private final long mRowId;

    public Model(long rowId) {
        mRowId = rowId;
    }

    public long getRowId() {
        return mRowId;
    }

}
