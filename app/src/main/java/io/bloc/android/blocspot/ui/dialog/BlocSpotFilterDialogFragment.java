package io.bloc.android.blocspot.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.ui.adapter.CategoryAdapter;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotFilterDialogFragment extends DialogFragment {

    //public static final Variables
    public static final String FILTER_DIALOG_ARGS = "filterDialogArgs";

    //private static final variables

    //member variables

        //RecyclerView variables
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

        //newInstance method
    public static BlocSpotFilterDialogFragment newInstance() {
        BlocSpotFilterDialogFragment dialogFragment = new BlocSpotFilterDialogFragment();

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
        View view = inflater.inflate(R.layout.dialog_fragment_blocspot_filter,
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
                .inflate(R.layout.dialog_fragment_blocspot_filter, null);

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
                        Toast.makeText(getActivity(), "This dialog works", Toast.LENGTH_SHORT).show();
                    }
                }).create();

    }

    //------------private methods-----------

        //initialize all UI dialog elements
    private void initUI(View view) {

            //wire up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_filter_catagories);

            //create layout manager for RecyclerView and set
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

            //create and set adapter
        mAdapter = new CategoryAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }

        //initialize repeat Listeners
    private void initListeners() {

    }
}
