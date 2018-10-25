package com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.pojo.DonateFoodPojo;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class FoodCollectionsAdapter extends RecyclerView.Adapter<FoodCollectionsAdapter.FoodCollectionHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<DonateFoodPojo> foodCollectionDataList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public FoodCollectionsAdapter(Context mCtx, List<DonateFoodPojo> foodCollectionDataList) {
        this.mCtx = mCtx;
        this.foodCollectionDataList = foodCollectionDataList;
    }

    /* ------------------------------------------------------------- *
     * Overriding Recycler View Adapter Object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public FoodCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_food_collections, parent, false);
        return new FoodCollectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCollectionHolder holder, int position) {
        DonateFoodPojo donateFoodPojo = foodCollectionDataList.get(position);
        holder.textResidentNameValue.setText(donateFoodPojo.getNaUser().getPersonalDetails().getFullName());
        holder.textApartmentValue.setText(donateFoodPojo.getNaUser().getFlatDetails().getApartmentName());
        holder.textFlatNumberValue.setText(donateFoodPojo.getNaUser().getFlatDetails().getFlatNumber());
        holder.textFoodTypeValue.setText(donateFoodPojo.getFoodType());
        holder.textQuantityValue.setText(donateFoodPojo.getFoodQuantity());
    }

    @Override
    public int getItemCount() {
        return foodCollectionDataList.size();
    }

    /* ------------------------------------------------------------- *
     * Food Collection Holder Class
     * ------------------------------------------------------------- */

    class FoodCollectionHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textResidentName;
        private final TextView textApartment;
        private final TextView textFlatNumber;
        private final TextView textFoodType;
        private final TextView textQuantity;
        private final TextView textResidentNameValue;
        private final TextView textApartmentValue;
        private final TextView textFlatNumberValue;
        private final TextView textFoodTypeValue;
        private final TextView textQuantityValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        FoodCollectionHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textResidentName = itemView.findViewById(R.id.textResidentName);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textFoodType = itemView.findViewById(R.id.textFoodType);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textFoodTypeValue = itemView.findViewById(R.id.textFoodTypeValue);
            textQuantityValue = itemView.findViewById(R.id.textQuantityValue);

            /*Setting font Id's for the view*/
            textResidentName.setTypeface(setLatoRegularFont(mCtx));
            textApartment.setTypeface(setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textFoodType.setTypeface(setLatoRegularFont(mCtx));
            textQuantity.setTypeface(setLatoRegularFont(mCtx));
            textResidentNameValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textFoodTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textQuantityValue.setTypeface(setLatoBoldFont(mCtx));
        }
    }
}
