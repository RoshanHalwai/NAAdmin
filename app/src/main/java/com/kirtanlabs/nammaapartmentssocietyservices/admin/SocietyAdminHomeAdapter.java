package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.Constants;
import com.kirtanlabs.nammaapartmentssocietyservices.R;

public class SocietyAdminHomeAdapter extends BaseAdapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private String[] stringSocietyAdminServices;
    private int[] icons;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    SocietyAdminHomeAdapter(Context mCtx, String[] stringSocietyAdminServices, int[] icons) {
        this.mCtx = mCtx;
        this.stringSocietyAdminServices = stringSocietyAdminServices;
        this.icons = icons;
    }

    /* ------------------------------------------------------------- *
     * Overriding Base Adapter object
     * ------------------------------------------------------------- */

    @Override
    public int getCount() {
        return stringSocietyAdminServices.length;
    }

    @Override
    public Object getItem(int position) {
        return stringSocietyAdminServices[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                gridView = layoutInflater.inflate(R.layout.grid_layout_society_admin_home, parent, false);
            }

            /*Getting Id's for all the views*/
            ImageView imageSocietyAdminServices = gridView.findViewById(R.id.imageSocietyAdminServices);
            TextView textSocietyAdminServices = gridView.findViewById(R.id.textSocietyAdminServices);

            /*Setting font Id's for the view*/
            textSocietyAdminServices.setTypeface(Constants.setLatoRegularFont(mCtx));

            /*Setting values for all the views*/
            textSocietyAdminServices.setText(stringSocietyAdminServices[position]);
            imageSocietyAdminServices.setImageResource(icons[position]);
        }
        return gridView;
    }
}
