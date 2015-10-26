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

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.ui.adapter.LocationAdapter;
import io.bloc.android.blocspot.ui.dialog.BlocSpotLocationItemOptionsDialog;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotLocationListFragment extends Fragment
    implements LocationAdapter.Callbacks{

    //private static final variables

    //public static final variables
    public static final String TAG_LOCATION_LIST_FRAGMENT = "LocationListFragment";
    //private member variables

    RecyclerView mRecyclerView;
    LocationAdapter mAdapter;
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
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        initUI(view);


        return view;
    }


    //------------Interface methods-----------


        //performs the following when the user presses the options button
        //on any of the location item views
    @Override
    public void whenOptionsButtonPressed() {
            //create the optionsMenu dialog
        BlocSpotLocationItemOptionsDialog optionsDialog =
                BlocSpotLocationItemOptionsDialog.newInstance();

            //any additional setup for the fragment goes here

            //post the dialog as an alertDialog
        optionsDialog.show(getFragmentManager(),
                BlocSpotLocationItemOptionsDialog.TAG_LOCATION_OPTIONS_DIALOG_FRAGMENT);


    }

    @Override
    public void whenVisitedCheckboxToggled(boolean isChecked) {

        String message;

        if(isChecked){
            message = "is Checked";
        } else {
            message = "is not Checked";
        }

        Toast.makeText(getActivity(), "location has visited checkbox is " + message , Toast.LENGTH_SHORT).show();

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
        mAdapter.setCallbacks(this);

    }

    //initialize repeat Listeners
    private void initListeners() {

    }
}
