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
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.BlocSpotApplication;
import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.api.DataSource;
import io.bloc.android.blocspot.api.model.CategoryItem;
import io.bloc.android.blocspot.ui.adapter.CategoryAdapter;

/**
 * Created by Administrator on 10/15/2015.
 */
public class BlocSpotFilterDialogFragment extends DialogFragment
    implements CategoryAdapter.Delegate {

    //public static final Variables
    public static final String FILTER_DIALOG_ARGS = "filterDialogArgs";

    //private static final variables

        //member variables
    ImageButton mImageButton;

        //RecyclerView variables
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

        //List variables
    private List<CategoryItem> mCategoryItems = new ArrayList<CategoryItem>();

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
        initListeners();


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
        initListeners();

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

    //------------Implemented callback methods--------

    @Override
    public void whenSetCategoryToggled(boolean isChecked) {

        String message;

        message = isChecked ? "isChecked" : "is not Checked";

        Toast.makeText(getActivity(), "This category " + message, Toast.LENGTH_SHORT).show();
    }

    //------------private methods-----------

        //initialize all UI dialog elements
    private void initUI(View view) {

            //wire up ImageButton
        mImageButton = (ImageButton) view.findViewById(R.id.btn_dialog_filter_add_catagory);

            //wire up RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_filter_catagories);

            //create layout manager for RecyclerView and set
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

            //create and set adapter
        mAdapter = new CategoryAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setCallbacks(this);

    }

    private void initCategoryItems() {
        BlocSpotApplication.getSharedDataSource().fetchCategoryItems(
                new DataSource.Callback<List<CategoryItem>>() {
                    @Override
                    public void onSuccess(List<CategoryItem> categoryItems) {
                        mCategoryItems.addAll(categoryItems);
                        //add list to the recyclerview
                    }

                    @Override
                    public void onError(String errorMessage) {
                        //nada/not possible
                    }
                }
        );
    }




        //initialize listeners
    private void initListeners() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Category Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
