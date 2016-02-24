package io.bloc.android.blocspot.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.intent.GeofenceIntentService;
import io.bloc.android.blocspot.api.model.LocationItem;
import io.bloc.android.blocspot.ui.dialog.BlocSpotFilterDialogFragment;
import io.bloc.android.blocspot.ui.dialog.BlocSpotLocationAlertDialog;
import io.bloc.android.blocspot.ui.fragment.BlocSpotLocationListFragment;
import io.bloc.android.blocspot.ui.fragment.BlocSpotMapFragment;
import io.bloc.android.blocspot.ui.fragment.BlocSpotSearchListFragment;

/**
 * Created by Administrator on 10/15/2015.
 *
 * edit Max--
 *
 *  for 
 */
public class BlocSpotActivity extends Activity
        implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    //Final Static variables go here
    private static final String TAG = "BlocSpotActivity";
    private static final String TAG_DIALOG_FRAGMENT_FILTER = "BlocSpotFilterDialogFragment";

    private static final String TAG_MAP_FRAGMENT = "BlocSpotMapFragment";

    private static final float GEOFENCE_DISTANCE_METERS = 1000f;

    //Member variables here
    Toolbar mToolbar;
    SearchView mSearchView;

    //Google Map fragment
    BlocSpotMapFragment mMapFragment;

    GoogleMap mGoogleMap;

    //get location manager for the user's current position
    LocationManager mLocationManager;

    //Activity Mode variables
    boolean mIsInMapMode;
    boolean mIsInSearchMode;

    double mCurLatitude, mCurLongitude;


    List<LocationItem> mLocationItems = new ArrayList<LocationItem>();

    GoogleApiClient mClient;

    Geofence.Builder geofenceBuilder = new Geofence.Builder();

    List<Geofence> mGeofenceList = new ArrayList<>();

    PendingIntent mGeofencePendingIntent;


    //----------Search interface---------

        //the following callback methods are for communicating with the
        // search fragment, just sending a given string to the fragment
        // for database processing
    public interface SearchCallback {
        void searchLocationsWithQuery(String currentString);
    }

    WeakReference<SearchCallback> mSearchCallback;

    public void setSearchCallback(SearchCallback searchCallback) {
        mSearchCallback = new WeakReference<SearchCallback>(searchCallback);
    }

    public SearchCallback getSearchCallback() {
        if(mSearchCallback == null) return null;
        return mSearchCallback.get();
    }

    //--------------onCreate-------------------------


    @Override
    protected void onStart() {
        super.onStart();

            //connect the API client
        mClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

            //disconnect the API client
        mClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //set the layout of the activity
        setContentView(R.layout.activity_blocspot);

            //set up the stating mode of the Activity
        mIsInMapMode = true;
        mIsInSearchMode = false;


        //wire up the interfaces with the adapters if needed here
        //->

        //Set up the toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_blocspot_activity);
        initActivityToolbar();

        //set up the searchbar and listener
        mSearchView = (SearchView) findViewById(R.id.sv_blocspot_toolbar);

        //if the search menu is dismissed remove the searchbar and
        // and return the toolbar to its original state
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toggleSearchMenu();
                return false;
            }
        });

        //text listener for the searchView widget in the toolbar
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //send the given string 'query' to a fragment that can handle it
                //only if a fragment exists that has the callback
                if (getSearchCallback() != null) {
                    getSearchCallback().searchLocationsWithQuery(query);
                } else {
                    //Log.i(TAG, "no SearchCallback set");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //build the locationItem Array
        BlocSpotApplication.getSharedDataSource().fetchLocationItems(new DataSource.Callback<List<LocationItem>>() {
            @Override
            public void onSuccess(List<LocationItem> locationItems) {
                mLocationItems = locationItems;

            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        //build the API Client
        buildAPIClient();

        //insert a mapfragment into the fragment frameLayout
        mMapFragment = BlocSpotMapFragment.newInstance();
        mMapFragment.getMapAsync(this);
        getFragmentManager().beginTransaction()
                .add(R.id.fl_activity_fragment, mMapFragment, TAG_MAP_FRAGMENT)
                .commit();

    }

    //--------------------------private methods----------------------

    //initialize the toolbar
    private void initActivityToolbar() {

        //set the dialogs menu, title, and
        mToolbar.inflateMenu(R.menu.blocspot_activity_menu);
        mToolbar.setTitle(getResources().getString(R.string.app_name));

        //toolbar onMenuItemClickListener
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    //if the filter action is clicked, show filter dialog fragment
                    case R.id.m_action_filter_locations:
                        BlocSpotFilterDialogFragment filterDialogFragment = BlocSpotFilterDialogFragment.newInstance();
                        filterDialogFragment.show(getFragmentManager(), TAG_DIALOG_FRAGMENT_FILTER);
                        break;

                    //if the Map/List action is clicked, show either the map fragment or the List fragment
                    case R.id.m_action_view_toggle_list_map:

                        //perform action for this MenuItem
                        actionListMapItem();
                        break;

                    //if the search menu item is pressed, collapse the rest of the menu
                    //and replace the menu with a search bar
                    case R.id.m_action_search_locations:

                        toggleSearchMenu();
                        break;

                    case R.id.m_action_test_location_alert:

                        BlocSpotLocationAlertDialog alertDialog =
                                BlocSpotLocationAlertDialog.newInstance();

                        alertDialog.show(getFragmentManager(),
                                BlocSpotLocationAlertDialog.TAG_ALERT_LOCATION_DIALOG);
                        break;

                    default:
                        //nothing right now
                }

                return false;
            }
        });

    }

    //changes the appearance of the MenuItem to switch between map and list
    private void toggleListMapMenuItem() {

        //toggle mIsInMapMode
        mIsInMapMode = !mIsInMapMode;

        //if the map would be visible, show "View List"
        if(mIsInMapMode) {
            mToolbar.getMenu()
                    .findItem(R.id.m_action_view_toggle_list_map)
                    .setTitle(getResources().getString(R.string.menu_item_view_list));

            //otherwise if the list is visible, show "View Map"
        } else {

            mToolbar.getMenu()
                    .findItem(R.id.m_action_view_toggle_list_map)
                    .setTitle(getResources().getString(R.string.menu_item_view_map));
        }

    }

    //disables/enables items in the toolbar based on whether the
    //user is searching for an item or not
    //replaces the current fragment with a search fragment
    private void toggleSearchMenu() {

        //first change the value for SearchMode
        mIsInSearchMode = !mIsInSearchMode;

        //if SearchMode has been turned on in the Activity
        // enable the searchbar, blank the title,
        // and disable all other items in the toolbar
        if(mIsInSearchMode) {

            //remove the title from the toolbar
            mToolbar.setTitle("");

            //change the title of the Search menuItem from 'Search' to 'Close'
            ////TODO: change the icon of this menuItem from a 'magnifying glass' to  'X'
            //disable other menuItems
            Menu toolbarMenu = mToolbar.getMenu();
            toolbarMenu.findItem(R.id.m_action_search_locations)
                    .setTitle(getResources()
                            .getString(R.string.menu_item_close_search_locations));

            toolbarMenu.findItem(R.id.m_action_filter_locations)
                    .setVisible(false)
                    .setEnabled(false);

            toolbarMenu.findItem(R.id.m_action_view_toggle_list_map)
                    .setVisible(false)
                    .setEnabled(false);

            //make the searchbar VISIBLE
            findViewById(R.id.sv_blocspot_toolbar).setVisibility(View.VISIBLE);


            //replace the current fragment with a search fragment
            //if the search fragment does not currently exist, create one
            Fragment searchFragment = getFragmentManager()
                    .findFragmentByTag(BlocSpotSearchListFragment.TAG_SEARCH_LIST_FRAGMENT);;

            //detach the current fragment in the fragment space
            getFragmentManager().beginTransaction()
                    .detach(getFragmentManager().findFragmentById(R.id.fl_activity_fragment))
                    .commit();

            //if the searchFragment does not exist create a new one
            //then add it to the fragment space
            if(searchFragment == null) {
                searchFragment = new BlocSpotSearchListFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.fl_activity_fragment,
                                searchFragment,
                                BlocSpotSearchListFragment.TAG_SEARCH_LIST_FRAGMENT)
                        .commit();

                //if the searchFragment exists
                //attach it to the fragment space
            } else {
                getFragmentManager().beginTransaction()
                        .attach(searchFragment)
                        .commit();
            }



            //if SearchMode has been turned off in the Activity
            // enable the menus in the toolbar, reassign the title,
            // disable the searchbar, and remove the search fragment and attach the old fragment
        } else {

            //reassign the title to the toolbar
            mToolbar.setTitle(getResources().getString(R.string.app_name));

            //change the title of the Search menuItem from 'Close' to 'Search'
            ////TODO: change the icon of this menuItem from a 'X' to a 'magnifying glass'
            //enable the disabled menuItems
            Menu toolbarMenu = mToolbar.getMenu();
            toolbarMenu.findItem(R.id.m_action_search_locations)
                    .setTitle(getResources()
                            .getString(R.string.menu_item_search_locations));

            toolbarMenu.findItem(R.id.m_action_filter_locations)
                    .setVisible(true)
                    .setEnabled(true);

            toolbarMenu.findItem(R.id.m_action_view_toggle_list_map)
                    .setVisible(true)
                    .setEnabled(true);

            //make the searchbar GONE
            findViewById(R.id.sv_blocspot_toolbar).setVisibility(View.GONE);

            //detach the search fragment
            getFragmentManager().beginTransaction()
                    .detach(getFragmentManager().findFragmentById(R.id.fl_activity_fragment))
                    .commit();



            //attach the previously attached fragment
            //-----
            //if the mode is in Map Mode,
            //try and find the map fragment to attach it to the fragment space
            //if the map fragment does not exists create a new one
            //and add it to the fragment space
            if(mIsInMapMode) {

                //search for the map fragment
                MapFragment mapFragment =
                        (MapFragment) getFragmentManager().findFragmentByTag(TAG_MAP_FRAGMENT);

                //if the map fragment does not exist create a new map fragment
                // and add it to the fragment space
                if(mapFragment == null) {
                    mapFragment = MapFragment.newInstance();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fl_activity_fragment, mapFragment, TAG_MAP_FRAGMENT)
                            .commit();

                    //if the map fragment exists
                    //attach the fragment to the fragment space
                } else {
                    getFragmentManager().beginTransaction()
                            .attach(mapFragment)
                            .commit();
                }


                //if the mode is in List Mode,
                //attempt to find the list fragment to attach it to the fragment space
                //if the list fragment does not exists create a new one
                //and add it to the fragment space instead
            } else {

                //check if the list fragment exists
                Fragment listFragment = getFragmentManager()
                        .findFragmentByTag(BlocSpotLocationListFragment.TAG_LOCATION_LIST_FRAGMENT);

                //if the list fragment does not exist
                //create a new list fragment
                //and add it to the fragment space
                if(listFragment == null) {
                    listFragment = new BlocSpotLocationListFragment();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fl_activity_fragment,
                                    listFragment,
                                    BlocSpotLocationListFragment.TAG_LOCATION_LIST_FRAGMENT)
                            .commit();

                    //if the list fragment exists
                    //attach it to the fragment space
                } else {

                    getFragmentManager().beginTransaction()
                            .attach(listFragment)
                            .commit();
                }

            }

        }

    }

    //This method handles the switching between the MapView fragment
    // and the location list fragment
    private void actionListMapItem() {

        //if the Application is currently in MapMode
        // switch the MapView with the RecyclerView of locations
        if(mIsInMapMode) {

            //find the locationListFragment if it exists
            Fragment locationListFragment = getFragmentManager()
                    .findFragmentByTag(BlocSpotLocationListFragment.TAG_LOCATION_LIST_FRAGMENT);

            //detach the current fragment in the fragment space
            getFragmentManager().beginTransaction()
                    .detach(getFragmentManager().findFragmentById(R.id.fl_activity_fragment))
                    .commit();


            //if the locationListFragment does not exist and the application is in MapMode
            //first create a new locationListFragment then
            // add the new locationListFragment to the fragment space
            if (locationListFragment == null) {
                locationListFragment = new BlocSpotLocationListFragment();
                getFragmentManager().beginTransaction()
                        .add(R.id.fl_activity_fragment,
                                locationListFragment,
                                BlocSpotLocationListFragment.TAG_LOCATION_LIST_FRAGMENT)
                        .commit();

                //if a locationListFragment does exist and the application is in MapMode
                //attach the locationListFragment to the fragment space
            } else {
                getFragmentManager().beginTransaction()
                        .attach(locationListFragment)
                        .commit();

            }

            //if the application is in ListMode
            //detach the locationListFragment from fragment space
            //and attach a MapFragment to the fragment space
        } else {

            //attempt to find the MapFragment
             mMapFragment =
                    (BlocSpotMapFragment)getFragmentManager().findFragmentByTag(TAG_MAP_FRAGMENT);

            //detach current fragment from the fragment space
            getFragmentManager().beginTransaction()
                    .detach(getFragmentManager().findFragmentById(R.id.fl_activity_fragment))
                    .commit();

            //if the mapFragment cannot be found create a new one
            // and add it to the fragment space
            if(mMapFragment == null) {
                mMapFragment = BlocSpotMapFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .add(R.id.fl_activity_fragment, mMapFragment, TAG_MAP_FRAGMENT)
                        .commit();

                //if the mapFragment already exists
                //attach it to the fragment space
            } else {
                getFragmentManager().beginTransaction()
                        .attach(mMapFragment)
                        .commit();
            }

            //attach listener to the mapFragment
            mMapFragment.getMapAsync(this);

        }

        //toggle the MenuItem appearance
        //toggle the MapMode
        toggleListMapMenuItem();
    }

    private void buildAPIClient() {
        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    //--------------------Geofence Methods

    //return a GeofencingRequest
    // with the trigger being ENTER
    // and the geofences being those that were entered into the method
    private GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    //rebuild the geofence list using the locations ID's
    private void rebuildGeofenceList(List<Geofence> list) {

        //clear all items in the list - for rebuild
        list.clear();

        Log.i(TAG, "Location List Size: " + String.valueOf(mLocationItems.size()) );

        //add new items to the list
        for( int i = 0; i < mLocationItems.size() ; i++) {

            //get the next location item's id
            LocationItem item = mLocationItems.get(i);
            String fenceId = "BlocSpotFence:" + item.getRowId();

            //add a new geofence item to the list using the location id as the RequestID
            list.add(new Geofence.Builder()
                    .setRequestId(fenceId)
                    .setCircularRegion(
                            item.getLocation().getLatitude(),
                            item.getLocation().getLongitude(),
                            GEOFENCE_DISTANCE_METERS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setLoiteringDelay(120000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

            Log.i(TAG, "thing: " + i);
        }


    }

    //create a pending Intent
    private PendingIntent createGeofenceIntent() {

        //build the specific intent
        Intent intent = new Intent(this, GeofenceIntentService.class);

        return PendingIntent.getService(
                this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    //add geofences and start monitoring
    private void startGeofenceMonitoring() {

        //rebuild the geofenceList
        rebuildGeofenceList(mGeofenceList);

        //add the geofenceList Intent to the LocationServices
        LocationServices.GeofencingApi.addGeofences(
                mClient,
                getGeofencingRequest(mGeofenceList),
                createGeofenceIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if(status.isSuccess()) {
                    Log.i(TAG, "yay");
                } else {
                    Log.i(TAG, "booo");
                }
            }
        });
    }

    //remove this applications' geofence monitoring
    private void stopGeofenceMonitoring() {
        LocationServices.GeofencingApi.removeGeofences(
                mClient,
                createGeofenceIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Log.i(TAG, "stop Result: " + status);
            }
        });
    }


    //-------------------------Interface Methods--------------------

    //GoogleMap onReady method -
    // everything that happens on the map needs to happen here
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        BlocSpotApplication.getSharedDataSource().fetchLocationItems(new DataSource.Callback<List<LocationItem>>() {
            @Override
            public void onSuccess(List<LocationItem> locationItems) {

                for (int i = 0; i < locationItems.size(); i++) {

                    LatLng latLng = new LatLng(
                            locationItems.get(i).getLocation().getLatitude(),
                            locationItems.get(i).getLocation().getLongitude());

                    googleMap.addMarker(new MarkerOptions()
                        .title(locationItems.get(i).getLocationName())
                        .position(latLng));
                }

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
        Log.i("MapFragment", "onMapReady");
    }


    //Connection Callbacks methods
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "connected");
            //stop any geofences that are currently working

        startGeofenceMonitoring();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //On Connection Failed listener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed");
    }

}
