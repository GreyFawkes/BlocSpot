package io.bloc.android.blocspot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.model.LocationItem;

/**
 * Created by Administrator on 10/15/2015.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{


    //private static final variables


    //public static final variables


    //private member variables

    //Interface methods needed here...
    public interface DataSource {
        LocationItem getLocationItem(LocationAdapter locationAdapter, int position);
        int getItemCount(LocationAdapter locationAdapter);
    }
    public interface Delegate {
        void whenOptionsButtonPressed(LocationItem locationItem);
        void whenVisitedCheckboxToggled(boolean isChecked);
    }

    //interface variables here
    private WeakReference<Delegate> delegate;
    private WeakReference<DataSource> dataSource;

    //delegate methods
    public Delegate getDelegate() {
        if(delegate == null) return null;
        return delegate.get();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = new WeakReference<Delegate>(delegate);
    }

    //dataSource methods
    public DataSource getDataSource() {
        if(dataSource == null) return null;
        return dataSource.get();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = new WeakReference<DataSource>(dataSource);
    }

    //Constructor for the Adapter
    public LocationAdapter(Context context) {

    }


    //The View Holder
    /*
    provides a reference to each of the widgets in the holder view
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        private LocationItem mLocationItem;

        public TextView mLocationName, mLocationNotes, mLocationDistance;
        public CheckBox mCheckBox;
        public Button mOptionsButton;

        public ViewHolder(View view) {
            super(view);

            //wire up the elements in the View
            initUI(view);

            //set up listeners
            initListeners();





        }

        //--------private holder class methods------------


            //this method wires up all of the UI elements into code
        private void initUI(View view) {
            mLocationName = (TextView) view.findViewById(R.id.tv_location_item_name);
            mLocationNotes = (TextView) view.findViewById(R.id.tv_location_item_notes);
            mLocationDistance = (TextView) view.findViewById(R.id.tv_location_item_distance_away);
            mCheckBox = (CheckBox) view.findViewById(R.id.cb_location_item_has_visited);
            mOptionsButton = (Button) view.findViewById(R.id.btn_location_item_options);
        }

        private void initListeners() {
            mOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        //perform the action described in BlocSpotLocationListFragment
                    getDelegate().whenOptionsButtonPressed(mLocationItem);

                }
            });

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        //perform the action described in BlocSpotLocationListFragment
                    getDelegate().whenVisitedCheckboxToggled(isChecked);
                }
            });
        }

            //update the UI based on a locationItem
        private void updateHolder(LocationItem locationItem){
            mLocationName.setText(locationItem.getLocationName());
            mLocationNotes.setText(locationItem.getLocationNotes());
            mCheckBox.setChecked(locationItem.hasVisitedLocation());
            mLocationItem = locationItem;
        }

        public LocationItem getLocationItem() {
            return mLocationItem;
        }

    }

    //create the ViewHolder in the Adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //inflate the correct view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_item, viewGroup, false);

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

        LocationItem item = getDataSource().getLocationItem(this, i);
        viewHolder.updateHolder(item);

    }

    //get the size if the data set
    @Override
    public int getItemCount() {
        if(getDataSource() == null) return 0;
        return getDataSource().getItemCount(this);
    }
}
