package io.bloc.android.blocspot.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.ui.dialog.BlocSpotFilterDialogFragment;
import io.bloc.android.blocspot.ui.fragment.BlocSpotLocationListFragment;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotActivity extends Activity {

    //Final Static variables go here
    private static final String TAG_DIALOG_FRAGMENT_FILTER = "BlocSpotFilterDialogFragment";

    //Member variables here
    Toolbar mToolbar;

    boolean mIsInMapMode;

    //--------------onCreate-------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //set the layout of the activity
        setContentView(R.layout.activity_blocspot);

            //Set up the toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_blocspot_activity);
        initActivityToolbar();

        mIsInMapMode = true;
    }

    //--------------private methods-------------------

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
                    case R.id.action_filter_locations:
                        BlocSpotFilterDialogFragment filterDialogFragment = BlocSpotFilterDialogFragment.newInstance();
                        filterDialogFragment.show(getFragmentManager(), TAG_DIALOG_FRAGMENT_FILTER);
                        break;

                    //if the Map/List action is clicked, show either the map fragment or the List fragment
                    case R.id.action_view_toggle_list_map:

                            //perform action for this MenuItem
                        actionListMapItem();
                        break;

                    default:
                        //nothing right now
                }

                return false;
            }
        });

    }

    //--------------------------private methods----------------------


        //changes the appearance of the MenuItem to switch between map and list
    private void toggleListMapMenuItem() {

            //toggle mIsInMapMode
        mIsInMapMode = !mIsInMapMode;

            //if the map would be visible, show "View List"
        if(mIsInMapMode) {
            mToolbar.getMenu()
                    .findItem(R.id.action_view_toggle_list_map)
                    .setTitle(getResources().getString(R.string.menu_item_view_list));

            //otherwise if the list is visible, show "View Map"
        } else {

            mToolbar.getMenu()
                    .findItem(R.id.action_view_toggle_list_map)
                    .setTitle(getResources().getString(R.string.menu_item_view_map));
        }

    }

    private void actionListMapItem() {

        //if the Application is in MapMode
        if(mIsInMapMode) {

            Fragment locationListFragment = getFragmentManager()
                    .findFragmentByTag(BlocSpotLocationListFragment.TAG_LOCATION_LIST_FRAGMENT);

            //if this fragment does not exist and the application is in MapMode
            //detach the previous fragment (if exists) and add the new fragment
            if (locationListFragment == null) {
                locationListFragment = new BlocSpotLocationListFragment();
                getFragmentManager().beginTransaction()
                        //.detach() // for use later
                        .add(R.id.v_activity_fragment,
                                locationListFragment,
                                BlocSpotLocationListFragment.TAG_LOCATION_LIST_FRAGMENT)
                        .commit();

                //if the fragment is not null and the application is in MapMode
                //detach the current fragment in the fragment space and
            } else {

                //reattach the locationListFragment to the activity_fragment
                getFragmentManager().beginTransaction()
                        //.detach() for later use
                        .attach(locationListFragment)
                        .commit();

            }

            //if the application is in ListMode
        } else {

            //get Map fragment here

            //check if the map fragment exists
            //if not, create one, detach current fragment, 'add' this one

            //else, detach current fragment, 'attach' this one

            //dummy test
            //check if there exists a fragment in the fragment_activity
            boolean isEmpty = getFragmentManager()
                    .findFragmentById(R.id.v_activity_fragment) == null;

            //if the map fragment does not exist, create fragment then
            //if(locationMapFragment == null) {

            //(dummyTest)if there is no fragment do nothing
            if(isEmpty) {
                //the following is for later use
//                               locationMapFragment = new BlocSpotLocationMapFragment();
//                                getFragmentManager().beginTransaction()
//                                        .detach(getFragmentManager().findFragmentById(R.id.v_activity_fragment))
//                                        .add(R.id.v_activity_fragment,
//                                                locationMapFragment,
//                                                BlocSpotLocationListFragment.TAG_LOCATION_MAP_FRAGMENT)
//                                        .commit();

                //else
            } else {
                getFragmentManager().beginTransaction()
                        .detach(getFragmentManager().findFragmentById(R.id.v_activity_fragment))
                                //.attach(getFragmentManager().findFragmentByTag(TAG_LOCATION_MAP_FRAGMENT)) //for later
                        .commit();
            }

        }

        //toggle the MenuItem appearance
        toggleListMapMenuItem();
    }
}
