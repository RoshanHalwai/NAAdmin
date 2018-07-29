package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;

class ServiceTypeAdapter extends BaseAdapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private String[] stringServicesList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    ServiceTypeAdapter(Context mCtx, String[] stringServicesList) {
        this.mCtx = mCtx;
        this.stringServicesList = stringServicesList;
    }

    /* ------------------------------------------------------------- *
     * Overriding BaseAdapter Objects
     * ------------------------------------------------------------- */

    @Override
    public int getCount() {
        return stringServicesList.length;
    }

    @Override
    public Object getItem(int position) {
        return stringServicesList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View spinnerView = convertView;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                spinnerView = layoutInflater.inflate(R.layout.layout_service_list, parent, false);
            }

            /*Getting Id's for all the views*/
            TextView textServiceName = spinnerView.findViewById(R.id.textServiceName);
            CheckBox checkboxSelectedService = spinnerView.findViewById(R.id.checkboxSelectedService);

            /*Setting values for all the views*/
            textServiceName.setText(stringServicesList[position]);

            if (position == 0) {
                checkboxSelectedService.setVisibility(View.INVISIBLE);
            }
        }
        return spinnerView;
    }
}
