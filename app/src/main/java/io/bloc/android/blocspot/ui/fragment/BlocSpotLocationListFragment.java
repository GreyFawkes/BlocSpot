package io.bloc.android.blocspot.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.ui.adapter.LocationAdapter;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotLocationListFragment extends Fragment{

    //private static final variables

    //public static final variables
    public static final String TAG_LOCATION_LIST_FRAGMENT = "LocationListFragment";
    //private member variables

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

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


    //------------private methods-----------

    //initialize all UI dialog elements
    private void initUI(View view) {

        //wire up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_locations_list);

        //create layout manager for RecyclerView and set
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //create and set adapter
        mAdapter = new LocationAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }

    //initialize repeat Listeners
    private void initListeners() {

    }
}
