package com.kirtanlabs.nammaapartmentssocietyservices.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;


public class RegisterTypeAdapter extends BaseAdapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;
    private String[] stringRegisterType;
    private int[] icons;

    /* ------------------------------------------------------------- *
     * Constructors
     * ------------------------------------------------------------- */

    RegisterTypeAdapter(Context mCtx, String[] stringRegisterType, int[] icons) {
        this.mCtx = mCtx;
        this.stringRegisterType = stringRegisterType;
        this.icons = icons;
    }

    /* ------------------------------------------------------------- *
     * Overriding BaseAdapter Objects
     * ------------------------------------------------------------- */

    @Override
    public int getCount() {
        return stringRegisterType.length;
    }

    @Override
    public Object getItem(int position) {
        return stringRegisterType[position];
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

            /*Setting values for all the views*/
            imageRegisterType.setImageResource(icons[position]);
            textSocietyServiceType.setText(stringRegisterType[position]);
        }
        return listView;
    }
}