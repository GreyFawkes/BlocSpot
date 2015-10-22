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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import io.bloc.android.blocspot.R;

/**
 * Created by Administrator on 10/22/2015.
 */
public class BlocSpotLocationAlertDialog extends DialogFragment{

    //public static final Variables
    public static final String ALERT_LOCATION_DIALOG_ARGS = "filterDialogArgs";
    public static final String TAG_ALERT_LOCATION_DIALOG = "alertLocationDialog";

    //private static final variables

    //member variables
    private CheckBox mCheckBoxHasVisited;
    private Button mButtonCatagory, mButtonNavigateTo, mButtonShare, mButtonDelete;
    private TextView mTextViewLocationName, mTextViewLocationNotes;


    //newInstance method
    public static BlocSpotLocationAlertDialog newInstance() {
        BlocSpotLocationAlertDialog dialogFragment = new BlocSpotLocationAlertDialog();

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
        View view = inflater.inflate(R.layout.dialog_fragment_location_alert,
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
                .inflate(R.layout.dialog_fragment_location_alert, null);

        //initialize all view elements
        initUI(view);


        //set up listeners



        //if a dialog is created, show the following
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Location Alert")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "This dialog works", Toast.LENGTH_SHORT).show();
                    }
                }).create();

    }

    //------------private methods-----------

    //initialize all UI dialog elements
    private void initUI(View view) {

            //wire up the buttons
        mButtonCatagory = (Button) view.findViewById(R.id.btn_alert_dialog_location_category);
        mButtonDelete = (Button) view.findViewById(R.id.btn_alert_dialog_delete);
        mButtonNavigateTo = (Button) view.findViewById(R.id.btn_alert_dialog_navigateTo);
        mButtonShare = (Button) view.findViewById(R.id.btn_alert_dialog_share);

            //wire up the checkbox
        mCheckBoxHasVisited = (CheckBox) view.findViewById(R.id.cb_alert_dialog_has_visited);

            //wire up the textViews
        mTextViewLocationName = (TextView) view.findViewById(R.id.tv_alert_dialog_location_name);
        mTextViewLocationNotes = (TextView) view.findViewById(R.id.tv_alert_dialog_location_notes);

    }

    //initialize repeat Listeners
    private void initListeners() {

    }
}
