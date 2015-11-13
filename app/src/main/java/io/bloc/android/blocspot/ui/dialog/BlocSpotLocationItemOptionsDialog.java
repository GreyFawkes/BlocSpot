package io.bloc.android.blocspot.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.model.CategoryItem;
import io.bloc.android.blocspot.api.model.LocationItem;

/**
 * Created by Administrator on 10/20/2015.
 */
public class BlocSpotLocationItemOptionsDialog extends DialogFragment {

    //public static final Variables
    public static final String FILTER_DIALOG_ARGS = "locationOptionsDialogArgs";
    public static final String TAG_LOCATION_OPTIONS_DIALOG_FRAGMENT = "locationOptionsDiaFrag";

    //private static final variables
    private static final String ARGS_CATEGORY_ID = "itemOptionsDialog_categoryId";
    private static final String ARGS_NOTE = "itemOptionsDialog_note";

    //member variables
    Button mButtonNavigateTo, mButtonEditNote, mButtonDelete;
    Spinner mSpinnerCategory;
    EditText mNoteEditText;

    String mLocationNote;
    long mCategoryId;

    boolean mEditMode = false;

    List<CategoryItem> mCategoryItems = new ArrayList<CategoryItem>();
    List<String> mCategoryNames = new ArrayList<String>();

    ArrayAdapter<String> mSpinnerCategoryAdapter;

    //newInstance method
    public static BlocSpotLocationItemOptionsDialog newInstance(LocationItem locationItem) {
        BlocSpotLocationItemOptionsDialog dialogFragment =
                new BlocSpotLocationItemOptionsDialog();

        //put argument stuff here
        Bundle args = new Bundle();
        args.putLong(ARGS_CATEGORY_ID, locationItem.getCategoryId());
        args.putString(ARGS_NOTE, locationItem.getLocationNotes());
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    //--------------------onCreate

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //get the necessary values from the bundle
        mLocationNote = getArguments().getString(ARGS_NOTE);



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
                .inflate(R.layout.dialog_fragment_location_item_options, null);

        initUI(view);

        //if a dialog is created, show the following
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Filter")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "This options dialog works", Toast.LENGTH_SHORT).show();
                    }
                }).create();

    }

    //------------private methods-----------

    //simplified method that takes all common implementation between a fragment and a dialog for
    // more simplified edits
    private void initUI(View view){

        // // TODO: 11/9/2015 use a method to force the initCategoryData method to complete first
            //semaphores or executor service
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
        mButtonNavigateTo = (Button) view.findViewById(R.id.btn_location_item_option_navigateTo);
        mButtonDelete = (Button) view.findViewById(R.id.btn_location_item_option_delete_item);
        mButtonEditNote = (Button) view.findViewById(R.id.btn_location_item_option_edit_note);

        //wire up EditText
        mNoteEditText = (EditText) view.findViewById(R.id.et_location_item_option_note);

        //wire up the spinner
        mSpinnerCategory = (Spinner) view.findViewById(R.id.sp_location_item_option_category);

//        mSpinnerCategoryAdapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.dummy_values_cat, android.R.layout.simple_spinner_item);
        Log.i(TAG_LOCATION_OPTIONS_DIALOG_FRAGMENT, "start init SpinnerAdapter");

        mSpinnerCategoryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mCategoryNames);

        mSpinnerCategoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        //set the spinner adapter
        mSpinnerCategory.setAdapter(mSpinnerCategoryAdapter);

    }

    //initialize repeat Listeners
    private void initListeners() {

            //action when the user clicks the NavigateTo button
        mButtonNavigateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Navigate to this location", Toast.LENGTH_SHORT).show();
            }
        });

            //action when the user clicks the Delete button
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Delete this location from memory", Toast.LENGTH_SHORT).show();
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

                        //get the current string
                    mLocationNote = mNoteEditText.getText().toString();
                        //make the view gone
                    mNoteEditText.setVisibility(View.GONE);
                        //change the button text to Edit location note
                    mButtonEditNote.setText(R.string.dialog_location_item_edit_note);
                        //mEditMode is false
                    mEditMode = false;

                    /*
                    if edit mode is false -
                        put the current note string into the editText box
                        open the editText box (make it visible)
                        change the text on the button to 'save location note'
                        change mEditMode to true
                     */
                } else {

                    if(mLocationNote == null) mLocationNote = "";
                        //set the text in the EditText to the current note string
                    mNoteEditText.setText(mLocationNote);
                        //set the textView to visible
                    mNoteEditText.setVisibility(View.VISIBLE);
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
            Log.i(TAG_LOCATION_OPTIONS_DIALOG_FRAGMENT, String.valueOf(i) + " " + mCategoryItems.get(i).getCategoryName());
            mCategoryNames.add(mCategoryItems.get(i).getCategoryName());
        }

    }

}
