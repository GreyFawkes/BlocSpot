package io.bloc.android.blocspot.api.model;

/**
 * Created by Administrator on 11/1/2015.
 */
public class CategoryItem extends Model {

        //----Member variables---
    private String mCategoryName;

        //---Constructor----
    public CategoryItem(long rowId, String categoryName) {
        super(rowId);
        mCategoryName = categoryName;
    }
        //---Getters--------
    public String getCategoryName() {
        return mCategoryName;
    }
    
}
