package io.bloc.android.blocspot.api.model;

/**
 * Created by Administrator on 11/1/2015.
 */
public class CategoryItem extends Model {

    // TODO: 11/5/2015 add a is_checked variable to the item and database for filter implementation

        //----Member variables---
    private String mCategoryName;
    private boolean mIsChecked;

        //---Constructor----
    public CategoryItem(long rowId, String categoryName, boolean isChecked) {
        super(rowId);
        mCategoryName = categoryName;
        mIsChecked = isChecked;
    }

    public CategoryItem(long rowId, String categoryName) {
        super(rowId);
        mCategoryName = categoryName;
        mIsChecked = false;
    }
        //---Getters--------
    public String getCategoryName() {
        return mCategoryName;
    }

    public boolean getIsChecked() { return mIsChecked; }
    
}
