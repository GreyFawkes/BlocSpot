package io.bloc.android.blocspot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import io.bloc.android.blocspot.R;

/**
 * Created by Administrator on 10/15/2015.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{

    //Callbacks method needed here...


    //private static final variables


    //public static final variables


    //private member variables

    //dummy data
    private String data[];



    //Constructor for the Adapter
    public LocationAdapter(Context context) {

        //dummy data
        data = context.getResources().getStringArray(R.array.dummy_values_loc);
    }


    //The View Holder
    /*
    provides a reference to each of the widgets in the holder view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public CheckBox mCheckBox;
        public Button mOptionsButton;

        public ViewHolder(View view) {
            super(view);

            //wire up the elements in the View
            mTextView = (TextView) view.findViewById(R.id.tv_location_item_name);
            mCheckBox = (CheckBox) view.findViewById(R.id.cb_location_item_has_visited);
            mOptionsButton = (Button) view.findViewById(R.id.btn_location_item_options);

            mOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //send a Callback to show a new optionsDialogFragment

                }
            });



        }
    }

    //create the ViewHolder in the Adapter
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //inflate the correct view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.location_item, viewGroup, false);

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

        viewHolder.mTextView.setText(data[i]);

    }

    //get the size if the dataset
    @Override
    public int getItemCount() {
        return data.length;
    }
}
