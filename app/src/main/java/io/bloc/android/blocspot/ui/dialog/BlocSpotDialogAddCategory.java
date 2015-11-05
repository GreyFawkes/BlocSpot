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
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import io.bloc.android.blocspot.R;

/**
 * Created by Administrator on 11/4/2015.
 * This dialog will take in a line of text for a new
 * category name
 */
public class BlocSpotDialogAddCategory extends DialogFragment {

    //public static final Variables
    public static final String ADD_CATEGORY_DIALOG_ARGS = "addCategoryDialogArgs";
    public static final String TAG_ADD_CATEGORY_DIALOG_FRAGMENT = "addCategoryDiaFrag";

    //private static final variables

    //member variables
    EditText mEditText;
    String mCategoryTitle;

    //callback interface
    public interface Callbacks {
        void onDialogOkPressed(String categoryTitle);
    }

    //callback variables
    WeakReference<Callbacks> callbacks;

    //callback getter and setter
    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = new WeakReference<Callbacks>(callbacks);
    }

    public Callbacks getCallbacks() {
        if(callbacks == null) return null;
        return callbacks.get();
    }

    //newInstance method
    public static BlocSpotDialogAddCategory newInstance() {
        BlocSpotDialogAddCategory dialogFragment =
                new BlocSpotDialogAddCategory();

        //put argument stuff here if needed

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
        View view = inflater.inflate(R.layout.dialog_fragment_add_category,
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
                .inflate(R.layout.dialog_fragment_add_category, null);

        //initialize all view elements
        initUI(view);

        //set up listeners
        initListeners();


        //if a dialog is created, show the following
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("New Category Name")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "This options dialog works", Toast.LENGTH_SHORT).show();
                        getCallbacks().onDialogOkPressed(mCategoryTitle);
                    }
                }).create();

    }

    //------------private methods-----------

    //initialize all UI dialog elements
    private void initUI(View view) {

        //wire up the editText
        mEditText = (EditText) view.findViewById(R.id.et_new_category_name);

    }

    //initialize repeat Listeners
    private void initListeners() {

            //this listener is only recording the string typed in the editText box
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //capture the string when the text is changed
                //NOTE: for some reason getText().toString is not doing the job... don't know why
                mCategoryTitle = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
