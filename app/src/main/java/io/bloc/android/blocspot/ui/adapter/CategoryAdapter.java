package io.bloc.android.blocspot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.model.CategoryItem;

/**
 * Created by Administrator on 10/15/2015.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    //private static final variables


    //public static final variables


    //private member variables

    //--------interfaces----------
    public interface DataSource {
        CategoryItem getCategoryItem(CategoryAdapter categoryAdapter, int position);
        int getItemCount(CategoryAdapter categoryAdapter);
    }

    public interface Delegate {
        void whenSetCategoryToggled(boolean isChecked);
    }

    //---------interface variables-----------
    private WeakReference<Delegate> delegate;
    private WeakReference<DataSource> dataSource;

    //--------interface methods----------
    public DataSource getDataSource() {
        if(dataSource == null) return null;
        return dataSource.get();
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new WeakReference<DataSource>(dataSource);
    }

    public Delegate getDelegate() {
        if(delegate == null) return null;
        return delegate.get();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }

        //--------dummy data-----------
    private String data[];

        //---Constructor for the Adapter----
    public CategoryAdapter(Context context) {

            //dummy data
        data = context.getResources().getStringArray(R.array.dummy_values_cat);
    }

    //--------------------------------------

    //The View Holder
    /*
    provides a reference to each of the widgets in the holder view
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);

            //wire up the elements in the View
            initUI(view);

            //set up the listeners
            initListeners();


        }

        //-----private methods for viewHolder class

            //void method to wire up the elements in each holder
        private void initUI(View view) {
            mTextView = (TextView) view.findViewById(R.id.tv_category_name);
            mCheckBox = (CheckBox) view.findViewById(R.id.cb_set_category);
        }

            //listeners for each element in the holder if needed
        private void initListeners() {
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getDelegate().whenSetCategoryToggled(isChecked);
                }
            });
        }

        private void update(CategoryItem categoryItem) {
            mTextView.setText(categoryItem.getCategoryName());
        }
    }

        //create the ViewHolder in the Adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            //inflate the correct view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_item, viewGroup, false);

        //additional settings here
        //....

            //create the object and return it
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


        //replace any of the content of the ViewHolder per list item
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

            //replace the stuff inside of the view with the stuff in data

        if(getDataSource() == null) {
            return;
        }

            //get the category item from the database
        CategoryItem categoryItem = getDataSource().getCategoryItem(this, i);
            //update the view
        viewHolder.update(categoryItem);

    }

        //get the size of the dataset
    @Override
    public int getItemCount() {
        return data.length;
    }



}
