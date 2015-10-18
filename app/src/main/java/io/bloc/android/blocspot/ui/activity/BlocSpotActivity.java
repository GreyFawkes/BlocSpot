package io.bloc.android.blocspot.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.ui.dialog.BlocSpotFilterDialogFragment;
import io.bloc.android.blocspot.ui.fragment.BlocSpotLocationListFragment;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotActivity extends Activity {

    //Final Static variables go here
    private static final String TAG = "BlocSpotActivity";
    private static final String TAG_DIALOG_FRAGMENT_FILTER = "BlocSpotFilterDialogFragment";

    //Member variables here
    Toolbar mToolbar;
    SearchView mSearchView;

    //Activity Mode variables
    boolean mIsInMapMode;
    boolean mIsInSearchMode;

    //--------------onCreate-------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //set the layout of the activity
        setContentView(R.layout.activity_blocspot);

            //set up the stating mode of the Activity
        mIsInMapMode = true;
        mIsInSearchMode = false;

            //Set up the toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_blocspot_activity);
        initActivityToolbar();

            //set up the searchbar and listener
        mSearchView = (SearchView) findViewById(R.id.sv_blocspot_toolbar);
            //if the search menu is dismissed dismiss the searchbar and
            // and return the toolbar to its original state
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                toggleSearchMenu();
                return false;
            }
        });
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
                    .findItem(R.id.m_action_view_toggle_list_map)
                    .setTitle(getResources().getString(R.string.menu_item_view_list));

            //otherwise if the list is visible, show "View Map"
        } else {

            mToolbar.getMenu()
                    .findItem(R.id.m_action_view_toggle_list_map)
                    .setTitle(getResources().getString(R.string.menu_item_view_map));
        }

    }

        //disables/enables items in the toolbar base on whether the
        //user is searching for an item or not
    private void toggleSearchMenu() {

        //first change the value for SearchMode
        mIsInSearchMode = !mIsInSearchMode;

        //if SearchMode has been turned on in the Activity
        // enable the searchbar, blank the title,
        // and disable all other items in the toolbar
        if(mIsInSearchMode) {

                //remove the title from the toolbar
            mToolbar.setTitle("");
                //change the title of the SearchItem to Close
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

            //if SearchMode has been turn off in the Activity
            // enable the menus in the toolbar, reassign the title,
            // and disable the searchbar
        } else {

                //reassign the title to the toolbar
            mToolbar.setTitle(getResources().getString(R.string.app_name));
                //change the title of the SearchItem to Search
                //disable other menuItems
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

        }

    }


        //This method handles the switching between the MapView fragment
        // and the RecyclerView fragment
    private void actionListMapItem() {

        //if the Application is in MapMode
        // switch the MapView with the RecyclerView of locations
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
            // switch the RecyclerView with the MapView
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
