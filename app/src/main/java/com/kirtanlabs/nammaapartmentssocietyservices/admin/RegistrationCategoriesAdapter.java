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


public class RegistrationCategoriesAdapter extends BaseAdapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private String[] stringRegisterCategories;
    private int[] icons;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    RegistrationCategoriesAdapter(Context mCtx, String[] stringRegisterCategories, int[] icons) {
        this.mCtx = mCtx;
        this.stringRegisterCategories = stringRegisterCategories;
        this.icons = icons;
    }

    /* ------------------------------------------------------------- *
     * Overriding BaseAdapter Objects
     * ------------------------------------------------------------- */

    @Override
    public int getCount() {
        return stringRegisterCategories.length;
    }

    @Override
    public Object getItem(int position) {
        return stringRegisterCategories[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                listView = layoutInflater.inflate(R.layout.layout_register_type, parent, false);
            }

            /*Getting Id's for all the views*/
            ImageView imageRegisterType = listView.findViewById(R.id.imageRegisterType);
            TextView textSocietyServiceType = listView.findViewById(R.id.textSocietyServiceType);

            /*Setting fonts for all the views*/
            textSocietyServiceType.setTypeface(Constants.setLatoRegularFont(mCtx));

            /*Setting values for all the views*/
            imageRegisterType.setImageResource(icons[position]);
            textSocietyServiceType.setText(stringRegisterCategories[position]);
        }
        return listView;
    }
}