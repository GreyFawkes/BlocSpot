package io.bloc.android.blocspot.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.model.LocationItem;
import io.bloc.android.blocspot.ui.adapter.LocationAdapter;
import io.bloc.android.blocspot.ui.dialog.BlocSpotLocationItemOptionsDialog;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotLocationListFragment extends Fragment
    implements
        LocationAdapter.Delegate,
        LocationAdapter.DataSource {

    //private static final variables

    //public static final variables
    public static final String TAG_LOCATION_LIST_FRAGMENT = "LocationListFragment";
    //private member variables

    // // TODO: 11/8/2015 maybe use a recycler to fix the problem with the views not refreshing???

    RecyclerView mRecyclerView;
    LocationAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    List<LocationItem> mLocationItems = new ArrayList<LocationItem>();

        //-------------------onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLocationDataSet();
    }


        //-------------------onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //inflate view and hook up UI elements
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        initUI(view);

        return view;
    }


    //------------Interface methods: Delegate-----------


        //performs the following when the user presses the options button
        //on any of the location item views
    @Override
    public void whenOptionsButtonPressed() { // Todo: the interface should return the LocationItem
            //create the optionsMenu dialog
        BlocSpotLocationItemOptionsDialog optionsDialog =
                BlocSpotLocationItemOptionsDialog.newInstance();

            //any additional setup for the fragment goes here

            //post the dialog as an alertDialog
        optionsDialog.show(getFragmentManager(),
                BlocSpotLocationItemOptionsDialog.TAG_LOCATION_OPTIONS_DIALOG_FRAGMENT);


    }

    @Override
    public void whenVisitedCheckboxToggled(boolean isChecked) { // // TODO: 11/8/2015 return the locationItem for database changes 

        String message;

        if(isChecked){
            message = "is Checked";
        } else {
            message = "is not Checked";
        }

        Toast.makeText(getActivity(), "location has visited checkbox is " + message , Toast.LENGTH_SHORT).show();

    }

    //------------Interface methods: DataSource-----------

    @Override
    public LocationItem getLocationItem(LocationAdapter locationAdapter, int position) {
        if(mLocationItems.size() == 0) {
            return new LocationItem(0,"N/A", 0, "N/A", false);
        }
        return mLocationItems.get(position);
    }

    @Override
    public int getItemCount(LocationAdapter locationAdapter) {
        return mLocationItems.size();
    }

    //------------private methods-----------

    //initialize all UI dialog elements
    private void initUI(View view) {

        //wire up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_search_list);

        //create layout manager for RecyclerView and set
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //create and set adapter
        mAdapter = new LocationAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //set up the callbacks
        mAdapter.setDelegate(this);
        mAdapter.setDataSource(this);

    }

    //initialize repeat Listeners
    private void initListeners() {

    }

    //initialize the Location Data from the Database
    private void initLocationDataSet() {

        BlocSpotApplication.getSharedDataSource().fetchLocationItems(new DataSource.Callback<List<LocationItem>>() {
            @Override
            public void onSuccess(List<LocationItem> locationItems) {
                if(getActivity() == null) {
                    return;
                }

                //get the list of locations
                mLocationItems = locationItems;
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String errorMessage) {
                //does not apply
            }
        });

    }
}