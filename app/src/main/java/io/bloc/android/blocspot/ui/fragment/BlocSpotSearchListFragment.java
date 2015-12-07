package io.bloc.android.blocspot.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.model.LocationItem;
import io.bloc.android.blocspot.api.network.yelp.Yelp;
import io.bloc.android.blocspot.ui.activity.BlocSpotActivity;
import io.bloc.android.blocspot.ui.adapter.SearchAdapter;
import io.bloc.android.blocspot.ui.adapter.YelpSearchAdapter;

/**
 * Created by Administrator on 10/18/2015.
 */
public class BlocSpotSearchListFragment extends Fragment
    implements
        SearchAdapter.Delegate,
        SearchAdapter.DataSource,
        YelpSearchAdapter.Delegate,
        YelpSearchAdapter.DataSource,
        BlocSpotActivity.SearchCallback{

    //private static final variables

    //public static final variables
    public static final String TAG_SEARCH_LIST_FRAGMENT = "SearchListFragment";
    //private member variables

    RecyclerView mRecyclerView;
    SearchAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    RecyclerView mYelpRecyclerView;
    YelpSearchAdapter mYelpSearchAdapter;
    RecyclerView.LayoutManager mYelpLayoutManager;

    BlocSpotActivity mActivity;

    List<LocationItem> mSearchItems = new ArrayList<LocationItem>();
    List<LocationItem> mYelpItems = new ArrayList<>();

    //---Yelp stuff

    LocationManager mLocationManager;
    double mCurrentLatitude, mCurrentLongitude;


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

        mLocationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLatitude = location.getLatitude();
                mCurrentLongitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });


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

    //--------------implemented Interface methods - yelp Datasource---------

    @Override
    public LocationItem getSearchItem(YelpSearchAdapter searchAdapter, int position) {
        if(mYelpItems.isEmpty()) {
            return new LocationItem("nope", true);
        }
        return mYelpItems.get(position);
    }

    @Override
    public int getItemCount(YelpSearchAdapter searchAdapter) {
        return mYelpItems.size();
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
        mYelpRecyclerView = (RecyclerView) view.findViewById(R.id.rv_yelp_locations_list);

        //create layout manager for RecyclerView and set
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //layoutManager for the yelpRecyclerView
        mYelpLayoutManager = new LinearLayoutManager(getActivity());
        mYelpRecyclerView.setLayoutManager(mYelpLayoutManager);

        //create and set adapters
        mAdapter = new SearchAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        //create yelp adapters
        mYelpSearchAdapter = new YelpSearchAdapter(getActivity());
        mYelpRecyclerView.setAdapter(mYelpSearchAdapter);

        //for the adapter, use the callback methods implemented in this class
        mAdapter.setDelegate(this);
        mAdapter.setDataSource(this);

        //set callbacks for yelp adapter
        mYelpSearchAdapter.setDelegate(this);
        mYelpSearchAdapter.setDataSource(this);

        mActivity.setSearchCallback(this);

    }

    //initialize repeat Listeners
    private void initListeners() {

    }

    public void updateLocationDataSet(final String substring) {

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

        //add Yelp search here....
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {

                String searchResponse;
                searchResponse = Yelp.getYelp(getActivity()).search(substring, mCurrentLatitude, mCurrentLongitude);
                try {
                    return processJSON(searchResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return searchResponse;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //notifiy that the location name list has been changed
                mYelpSearchAdapter.notifyDataSetChanged();


            }
        }.execute();

    }

        //get all the important bits in the JSON string for the yelpAdapter
    private String processJSON(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        JSONArray businesses = json.getJSONArray("businesses");
        ArrayList<LocationItem> businessNames = new ArrayList<>(businesses.length());
        for(int i = 0; i<businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            String locationName = business.getString("name");
            businessNames.add(new LocationItem(locationName, true));
        }

        //update the location name list
        mYelpItems = businessNames;

        String text = TextUtils.join("\n", businessNames);
        return text;
    }



}
