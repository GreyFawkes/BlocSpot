package io.bloc.android.blocspot.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import io.bloc.android.blocspot.ui.activity.BlocSpotActivity;
import io.bloc.android.blocspot.ui.adapter.SearchAdapter;

/**
 * Created by Administrator on 10/18/2015.
 */
public class BlocSpotSearchListFragment extends Fragment
    implements
        SearchAdapter.Delegate,
        SearchAdapter.DataSource,
        BlocSpotActivity.SearchCallback{

    //private static final variables

    //public static final variables
    public static final String TAG_SEARCH_LIST_FRAGMENT = "SearchListFragment";
    //private member variables

    RecyclerView mRecyclerView;
    SearchAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    BlocSpotActivity mActivity;

    List<LocationItem> mSearchItems = new ArrayList<LocationItem>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivity = (BlocSpotActivity)activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return;
            //do nothing
        }
            //set the searchCallback from the activity to this fragment
        mActivity.setSearchCallback(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity.setSearchCallback(null);
    }

    //-------------------onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //-------------------onCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate view and hook up UI elements
        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);
        initUI(view);


        return view;
    }


    //------------implemented Interface methods - Delegate--------

    @Override
    public void whenVisitedCheckboxToggled(LocationItem locationItem, boolean isChecked) {

        String message;

        if(isChecked) {
            message = "is Checked";
        } else {
            message = "is not Checked";
        }

        Toast.makeText(getActivity(), "This item " + message, Toast.LENGTH_SHORT).show();
    }

    //------------implemented Interface methods - DataSource--------

    @Override
    public LocationItem getSearchItem(SearchAdapter searchAdapter, int position) {
        if(mSearchItems.isEmpty()) {
            return new LocationItem(-1, "N/A", -1, "N/A", false);
        }
        return mSearchItems.get(position);
    }

    @Override
    public int getItemCount(SearchAdapter searchAdapter) {
        return mSearchItems.size();
    }

    //-------------implemented Interface methods - SearchCallback---------

    @Override
    public void searchLocationsWithQuery(String currentString) {
        updateLocationDataSet(currentString);
    }

    //------------private methods-----------

    //initialize all UI dialog elements
    private void initUI(View view) {

        //wire up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_locations_list);

        //create layout manager for RecyclerView and set
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //create and set adapter
        mAdapter = new SearchAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //for the adapter, use the callback methods implemented in this class
        mAdapter.setDelegate(this);
        mAdapter.setDataSource(this);

        mActivity.setSearchCallback(this);

    }

    //initialize repeat Listeners
    private void initListeners() {

    }

    public void updateLocationDataSet(String substring) {

        Log.i(TAG_SEARCH_LIST_FRAGMENT, "update started");

        BlocSpotApplication.getSharedDataSource().fetchLocationItemsBySubstring(substring,
                new DataSource.Callback<List<LocationItem>>() {

            @Override
            public void onSuccess(List<LocationItem> locationItems) {
                Log.i(TAG_SEARCH_LIST_FRAGMENT, "on success started");
                if(getActivity() == null) return;

                mSearchItems = locationItems;
                mAdapter.notifyDataSetChanged();
                Log.i(TAG_SEARCH_LIST_FRAGMENT, "list populated");
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }



}
