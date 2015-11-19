package io.bloc.android.blocspot.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.model.LocationItem;

/**
 * Created by Administrator on 11/19/2015.
 */
public class BlocSpotMapFragment extends MapFragment {

    private static final String TAG = "MapFragment";

    List<LocationItem> mLocationItems = new ArrayList<>();

    public static BlocSpotMapFragment newInstance() {
        return new BlocSpotMapFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        //updateLocationMarkers();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }

    @Override
    public void getMapAsync(OnMapReadyCallback callback) {
        super.getMapAsync(callback);
    }

    private void updateLocationMarkers() {
        BlocSpotApplication.getSharedDataSource().fetchLocationItems(new DataSource.Callback<List<LocationItem>>() {
            @Override
            public void onSuccess(List<LocationItem> locationItems) {
                mLocationItems =locationItems;
                Log.i(TAG, "updateLocationMarkers complete");
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}
