package io.bloc.android.blocspot.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import io.bloc.android.blocspot.R;

/**
 * Created by Administrator on 10/20/2015.
 */
public class BlocSpotLocationItemOptionsDialog extends DialogFragment {

    //public static final Variables
    public static final String FILTER_DIALOG_ARGS = "locationOptionsDialogArgs";
    public static final String TAG_LOCATION_OPTIONS_DIALOG_FRAGMENT = "locationOptionsDiaFrag";

    //private static final variables

    //member variables
    Button mButtonNavigateTo, mButtonEditNote, mButtonDelete;
    Spinner mSpinnerCategory;

    ArrayAdapter<CharSequence> mSpinnerCategoryAdapter;

    //newInstance method
    public static BlocSpotLocationItemOptionsDialog newInstance() {
        BlocSpotLocationItemOptionsDialog dialogFragment =
                new BlocSpotLocationItemOptionsDialog();

        //put argument stuff here

        return dialogFragment;
    }

    //--------------------onCreate

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //--------------------onCreateView

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the view
        View view = inflater.inflate(R.layout.dialog_fragment_location_item_options,
                container, false);

        //initialize all view elements
        initUI(view);

        //set up listeners
        initListeners();



        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //-----------------------onCreateDialog

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_fragment_location_item_options, null);

        //initialize all view elements
        initUI(view);

        //set up listeners
        initListeners();


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

    //initialize all UI dialog elements
    private void initUI(View view) {

        //wire up the buttons
        mButtonNavigateTo = (Button) view.findViewById(R.id.btn_location_item_option_navigateTo);
        mButtonDelete = (Button) view.findViewById(R.id.btn_location_item_option_delete_item);
        mButtonEditNote = (Button) view.findViewById(R.id.btn_location_item_option_edit_note);

        //wire up the spinner
        mSpinnerCategory = (Spinner) view.findViewById(R.id.sp_location_item_option_category);
        mSpinnerCategoryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.dummy_values_cat, android.R.layout.simple_spinner_item);
        mSpinnerCategoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        //set the spinner adapter
        mSpinnerCategory.setAdapter(mSpinnerCategoryAdapter);

    }

    //initialize repeat Listeners
    private void initListeners() {

        mButtonNavigateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Navigate to this location", Toast.LENGTH_SHORT).show();
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Delete this location from memory", Toast.LENGTH_SHORT).show();
            }
        });

        mButtonEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit this location's note", Toast.LENGTH_SHORT).show();
            }
        });

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String catName;

                try {
                    catName = mSpinnerCategoryAdapter.getItem(position).toString();
                } catch (NullPointerException e) {
                    catName = "null";
                }

                Toast.makeText(getActivity(),
                        "This spinner is on " + catName,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getActivity(),
                        "Nothing selected in spinner",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
