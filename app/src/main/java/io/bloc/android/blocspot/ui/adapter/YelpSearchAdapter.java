package io.bloc.android.blocspot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.model.LocationItem;

/**
 * Created by Administrator on 12/6/2015.
 */
public class YelpSearchAdapter extends RecyclerView.Adapter<YelpSearchAdapter.ViewHolder> {

    public interface Delegate {

    }

        //this is going to change
    public interface DataSource {
        LocationItem getSearchItem(YelpSearchAdapter searchAdapter, int position);
        int getItemCount(YelpSearchAdapter searchAdapter);
    }

    //--------interface variables--------
    private WeakReference<Delegate> delegate;
    private WeakReference<DataSource> dataSource;

    //--------delegate methods----------
    public Delegate getDelegate() {
        if(delegate == null) return null;
        return delegate.get();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }

    //-------dataSource methods--------------
    public DataSource getDataSource() {
        if(dataSource == null) return null;
        return dataSource.get();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = new WeakReference<DataSource>(dataSource);
    }

    //dummy data
    private String storedData[];

    //Constructor for the Adapter
    public YelpSearchAdapter(Context context) {

    }


    //The View Holder
    /*
    provides a reference to each of the widgets in the holder view
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);

            //wire up the elements in the View
            initUI(view);

            //set up listeners
            initListeners();

        }

        //-----private methods for viewHolder Class-----

        //method to wire up the holder elements
        private void initUI(View view) {
            mTextView = (TextView) view.findViewById(R.id.tv_yelp_location_name);
        }

        //implement the holder listeners
        private void initListeners() {

        }

        private void updateHolder(LocationItem location) {
            mTextView.setText(location.getLocationName());
        }
    }

    //create the ViewHolder in the Adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //inflate the correct view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.yelp_location_item, viewGroup, false);

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

        if(dataSource == null) return;

        LocationItem item = getDataSource().getSearchItem(this, i);
        viewHolder.updateHolder(item);

    }

    //get the size if the dataset
    @Override
    public int getItemCount() {
        if(getDataSource() == null) return 0;
        return getDataSource().getItemCount(this);
    }


}
