package io.bloc.android.blocspot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.bloc.android.blocspot.R;

/**
 * Created by Administrator on 10/18/2015.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    //private static final variables


    //public static final variables


    //private member variables


    //interface for the adapter
    public interface Callbacks {
        void whenVisitedCheckboxToggled(boolean isChecked);
    }

    //--------interface variables--------
    private WeakReference<Callbacks> callbacks;

    //--------callbacks methods----------
    public Callbacks getCallbacks() {
        if(callbacks == null) return null;
        return callbacks.get();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = new WeakReference<Callbacks>(callbacks);
    }


    //dummy data
    private String storedData[];

    //Constructor for the Adapter
    public SearchAdapter(Context context) {

        //dummy data
        storedData = context.getResources().getStringArray(R.array.dummy_values_search);
    }


    //The View Holder
    /*
    provides a reference to each of the widgets in the holder view
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public CheckBox mCheckBox;

        public ViewHolder(View view) {
            super(view);

            //wire up the elements in the View
            initUI(view);

            //set up listeners
            initListeners();

        }

        //-----private methods for viewHolder Class-----

            //method to wire up the holder elements
        private void initUI(View view) {
            mTextView = (TextView) view.findViewById(R.id.tv_stored_location_name);
            mCheckBox = (CheckBox) view.findViewById(R.id.cb_stored_location_has_visited);
        }

            //implement the holder listeners
        private void initListeners() {
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getCallbacks().whenVisitedCheckboxToggled(isChecked);
                }
            });
        }
    }

    //create the ViewHolder in the Adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //inflate the correct view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stored_search_location, viewGroup, false);

        //additional settings here
        //....

        //create the object and return it
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    //replace any of the content of the ViewHolder per list item
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        //replace the stuff inside of the view with the stuff in data

        viewHolder.mTextView.setText(storedData[i]);

    }

    //get the size if the dataset
    @Override
    public int getItemCount() {
        return storedData.length;
    }
}
