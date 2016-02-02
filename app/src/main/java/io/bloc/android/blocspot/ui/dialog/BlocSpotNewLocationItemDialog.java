package io.bloc.android.blocspot.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.model.CategoryItem;
import io.bloc.android.blocspot.api.model.LocationItem;

//// TODO: 2/1/2016 THERE IS STILL STUFF TO BE DONE HERE, this dialog is not ready to go yet

/**
 * Created by Administrator on 10/20/2015.
 */
public class BlocSpotNewLocationItemDialog extends DialogFragment {

    //public static final Variables
    public static final String FILTER_DIALOG_ARGS = "newlocationOptionsDialogArgs";
    public static final String TAG_NEW_LOCATION_DIALOG_FRAGMENT = "newlocationOptionsDiaFrag";

    //private static final variables

    private static final String ARGS_LOCATION_ID = "itemOptionsDialog_locationId";
    private static final String ARGS_LOCATION_NAME = "itemOptionsDialog_locationName";
    private static final String ARGS_NOTE = "itemOptionsDialog_note";
    private static final String ARGS_CATEGORY_ID = "itemOptionsDialog_categoryId";
    private static final String ARGS_HAS_VISITED_LOCATION = "itemOptionsDialog_hasVisitedLocation";
    private static final String ARGS_LONGITUDE = "itemOptionsDialog_longitude";
    private static final String ARGS_LATITUDE = "itemOptionsDialog_latitude";

    //member variables
    Button mButtonEditNote;
    Spinner mSpinnerCategory;
    EditText mEditTextLocationNote, mEditTextLocationName, mEditTextLatitude, mEditTextLongitude;

    String mLocationNote, mLocationName;
    long mCategoryId, mLocationId;
    boolean mHasVisitedLocation;
    double mLatitude, mLongitude;

    boolean mEditMode = false;

    List<CategoryItem> mCategoryItems = new ArrayList<CategoryItem>();
    List<String> mCategoryNames = new ArrayList<String>();

    ArrayAdapter<String> mSpinnerCategoryAdapter;

    public interface Callback {
        void onDialogOkPressed(long locationId, String locationName, String locationNote,
                               long categoryId, boolean hasVisitedLocation,
                               double latitude, double longitude);
    }

    private WeakReference<Callback> mCallback;

    public void setCallback(Callback callback){
        mCallback = new WeakReference<Callback>(callback);
    }

    public Callback getCallback() {
        if(mCallback == null) return null;
        return mCallback.get();
    }


    //newInstance method
    public static BlocSpotNewLocationItemDialog newInstance(LocationItem locationItem) {
        BlocSpotNewLocationItemDialog dialogFragment =
                new BlocSpotNewLocationItemDialog();

        //put argument stuff here
        Bundle args = new Bundle();
        args.putLong(ARGS_LOCATION_ID, locationItem.getRowId());
        args.putString(ARGS_LOCATION_NAME, locationItem.getLocationName());
        args.putLong(ARGS_CATEGORY_ID, locationItem.getCategoryId());
        args.putString(ARGS_NOTE, locationItem.getLocationNotes());
        args.putBoolean(ARGS_HAS_VISITED_LOCATION, locationItem.hasVisitedLocation());
        args.putDouble(ARGS_LATITUDE, locationItem.getLocation().getLatitude());
        args.putDouble(ARGS_LONGITUDE, locationItem.getLocation().getLongitude());

        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    //--------------------onCreate

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            //get the necessary values from the bundle - except categoryId,
            // which is retrieved in the initCategoryData method
            mLocationNote = getArguments().getString(ARGS_NOTE);
            mLocationName = getArguments().getString(ARGS_LOCATION_NAME);
            mLocationId = getArguments().getLong(ARGS_LOCATION_ID);
            mHasVisitedLocation = getArguments().getBoolean(ARGS_HAS_VISITED_LOCATION);
            mLatitude = getArguments().getDouble(ARGS_LATITUDE);
            mLongitude =  getArguments().getDouble(ARGS_LONGITUDE);
        }

        // // TODO: 11/14/2015 add the title of the location name somewhere


    }

    //--------------------onCreateView

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the view
        View view = inflater.inflate(R.layout.dialog_fragment_location_item_options,
                container, false);

        //initUI(view);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //-----------------------onCreateDialog

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_fragment_new_location_item, null);

        initUI(view);

        //if a dialog is created, show the following
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCallback().onDialogOkPressed(
                                mLocationId,mLocationName,mLocationNote,
                                mCategoryId,mHasVisitedLocation,
                                mLatitude, mLongitude);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancel the order, exit the dialog without doing anything
                    }
                }).create();

    }

    //------------private methods-----------

    //simplified method that takes all common implementation between a fragment and a dialog for
    // more simplified edits
    private void initUI(View view){

        //initialize
        initCategoryData();

        //initialize all view elements
        initUIElements(view);

        //set up listeners
        initListeners();

    }

    //initialize all UI dialog elements
    private void initUIElements(View view) {

        //wire up the buttons
        mButtonEditNote = (Button) view.findViewById(R.id.btn_new_location_item_option_edit_note);

        //wire up EditText
        mEditTextLocationNote = (EditText) view.findViewById(R.id.et_new_location_item_option_note);
        mEditTextLocationName = (EditText) view.findViewById(R.id.et_new_location_item_title);

        mEditTextLatitude = (EditText) view.findViewById(R.id.et_new_location_latitude);
        mEditTextLatitude.setText(String.valueOf(mLatitude));
        mEditTextLongitude = (EditText) view.findViewById(R.id.et_new_location_longitude);
        mEditTextLongitude.setText(String.valueOf(mLongitude));

        //wire up the spinner
        mSpinnerCategory = (Spinner) view.findViewById(R.id.sp_new_location_item_option_category);

//        mSpinnerCategoryAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.dummy_values_cat, android.R.layout.simple_spinner_item);

        mSpinnerCategoryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mCategoryNames);

        mSpinnerCategoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        //set the spinner adapter
        mSpinnerCategory.setAdapter(mSpinnerCategoryAdapter);

    }

    //initialize repeat Listeners
    private void initListeners() {

            //when the user modifies the title of the location save those changes
        mEditTextLocationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLocationName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //not needed
            }
        });

        mEditTextLatitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double latitude;
                try {
                    latitude = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    latitude = 0d;
                }
                if(latitude < -90d) {
                    latitude = -90d;
                    mEditTextLongitude.setText("-90");
                }
                if(latitude > 90d) {
                    latitude = 90d;
                    mEditTextLongitude.setText("90");
                }
                mLatitude = latitude;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditTextLongitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double longitude;
                try {
                    longitude = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    longitude = 0d;
                }
                if(longitude < -180d) {
                    longitude = -180d;
                    mEditTextLongitude.setText("-180");
                }
                if(longitude > 180d) {
                    longitude = 180d;
                    mEditTextLongitude.setText("180");
                }
                mLongitude = longitude;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //action when the user clicks on the EditNote button
        mButtonEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit this location's note", Toast.LENGTH_SHORT).show();

                //if edit mode is true -
                //save the current string in the editText box
                //close the editText box (make it gone)
                //change the text on the button to 'edit location note'
                //change mEditMode to false
                if(mEditMode) {

                    //mEditMode is false
                    mEditMode = false;
                    //get the current Note string and set it as the current note
                    mLocationNote = mEditTextLocationNote.getText().toString();
                    //make the view gone
                    mEditTextLocationNote.setVisibility(View.GONE);
                    //change the button text to Edit location note
                    mButtonEditNote.setText(R.string.dialog_location_item_edit_note);

                    /*
                    if edit mode is false -
                        put the current note string into the editText box
                        open the editText box (make it visible)
                        change the text on the button to 'save location note'
                        change mEditMode to true
                     */
                } else {

                    if(mLocationNote == null) mLocationNote = "";

                    mEditMode = true;
                    //set the text in the EditText to the current note string
                    mEditTextLocationNote.setText(mLocationNote);
                    //set the textView to visible
                    mEditTextLocationNote.setVisibility(View.VISIBLE);
                    //change the text on the button to 'Save location Note'
                    mButtonEditNote.setText(R.string.dialog_location_item_save_note);
                    //set mEditMode to true

                }
            }
        });

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String catName = null;

                try {
                    catName = mSpinnerCategoryAdapter.getItem(position).toString();
                } catch (NullPointerException e) {
                    //do nothing
                }

                if(catName != null) {
                    for(int i = 0; i < mCategoryItems.size(); i++) {
                        if(mCategoryItems.get(i).getCategoryName().equals(catName)) {
                            //assign this item's category id to the matched category
                            mCategoryId = mCategoryItems.get(i).getRowId();
                            //break out of the for loop
                            break;
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getActivity(),
                        "Nothing selected in spinner",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    //initialize the CategoryItem data from the db
    private void initCategoryData() {

        //because getting info from the db will take a bit of time
        // adding one item to the list will make sure the spinner inflates
        // properly. If this step is not done the spinner will 'hide' from
        // the user because it is embarrassed it does not have items in its list
        String defaultCategory = getResources().getString(R.string.dialog_location_item_defaultCategory);
        mCategoryNames.add(defaultCategory);

        BlocSpotApplication.getSharedDataSource().fetchCategoryItems(new DataSource.Callback<List<CategoryItem>>() {
            @Override
            public void onSuccess(List<CategoryItem> categoryItems) {

                mCategoryItems = categoryItems;
                initArrays();

                //assign the category Id for the locationItem
                mCategoryId = getArguments().getLong(ARGS_CATEGORY_ID, -1l);

                //if the categoryId is not -1
                if(mCategoryId != -1l) {

                    //removing the ugly first item
                    mCategoryNames.remove(0);
                    //notify data set
                    mSpinnerCategoryAdapter.notifyDataSetChanged();

                    //find the currently selected category for the LocationItem
                    // and select it in the spinner
                    for (int i = 0; i < mCategoryItems.size(); i++) {
                        if (mCategoryItems.get(i).getRowId() == mCategoryId)
                            mSpinnerCategory.setSelection(i);
                    }

                    //otherwise just notify that the dataset has changed
                } else {
                    mSpinnerCategoryAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

    //initialize any arrays - currently only used in the initCategoryData method above
    private void initArrays() {

        for(int i = 0; i < mCategoryItems.size(); i++) {
            mCategoryNames.add(mCategoryItems.get(i).getCategoryName());
        }

    }

}
