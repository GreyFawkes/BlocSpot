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

    //RecyclerView variables

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



    }

    //initialize repeat Listeners
    private void initListeners() {

    }
}
