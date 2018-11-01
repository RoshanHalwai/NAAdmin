package com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.foodcollections.pojo.DonateFoodPojo;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_COLLECTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FOOD_DONATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;

public class FoodCollectionsAdapter extends RecyclerView.Adapter<FoodCollectionsAdapter.FoodCollectionHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<DonateFoodPojo> foodCollectionDataList;
    private final int screenTitle;
    private final BaseActivity baseActivity;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public FoodCollectionsAdapter(Context mCtx, List<DonateFoodPojo> foodCollectionDataList, int screenTitle) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.foodCollectionDataList = foodCollectionDataList;
        this.screenTitle = screenTitle;
    }

    /* ------------------------------------------------------------- *
     * Overriding Recycler View Adapter Object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public FoodCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_food_collections_and_scrap_collections, parent, false);
        return new FoodCollectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCollectionHolder holder, int position) {
        DonateFoodPojo donateFoodPojo = foodCollectionDataList.get(position);
        holder.textResidentNameValue.setText(donateFoodPojo.getNaUser().getPersonalDetails().getFullName());
        holder.textApartmentValue.setText(donateFoodPojo.getNaUser().getFlatDetails().getApartmentName());
        holder.textFlatNumberValue.setText(donateFoodPojo.getNaUser().getFlatDetails().getFlatNumber());
        holder.textFoodTypeAndScrapTypeValue.setText(donateFoodPojo.getFoodType());
        holder.textQuantityValue.setText(donateFoodPojo.getFoodQuantity());
        holder.textStatusValue.setText(donateFoodPojo.getStatus());

        switch (screenTitle) {
            case R.string.history:
                holder.buttonUpdateStatus.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return foodCollectionDataList.size();
    }


    /*------------------------------------------------------------------
     *Private Methods
     *------------------------------------------------------------------*/

    /**
     * This method updates the status of food donations raised by user .
     *
     * @param position       of card view whose food donations details need to be removed
     * @param donateFoodPojo contains instance of that particular UID of that particular donation.
     */
    private void updateFoodDonationStatus(int position, DonateFoodPojo donateFoodPojo) {
        String foodDonationUID = donateFoodPojo.getUid();
        foodCollectionDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, foodCollectionDataList.size());
        DatabaseReference foodDonationReference = FOOD_DONATION_REFERENCE.child(foodDonationUID).child(FIREBASE_CHILD_STATUS);
        foodDonationReference.setValue(FIREBASE_CHILD_COLLECTED).addOnSuccessListener(aVoid -> {
            baseActivity.showNotificationDialog(mCtx.getString(R.string.status_updated), mCtx.getString(R.string.user_notified), null);
            /*This is to ensure when user deletes the last item in the list a blank screen is not shown
             * instead feature unavailable layout is shown*/
            if (foodCollectionDataList.isEmpty()) {
                baseActivity.showFeatureUnavailableLayout(R.string.food_collection_unavailable);
            }
        });
    }
    /* ------------------------------------------------------------- *
     * Food Collection Holder Class
     * ------------------------------------------------------------- */

    class FoodCollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textResidentName;
        private final TextView textApartment;
        private final TextView textFlatNumber;
        private final TextView textFoodTypeAndScrapType;
        private final TextView textQuantity;
        private final TextView textStatus;
        private final TextView textResidentNameValue;
        private final TextView textApartmentValue;
        private final TextView textFlatNumberValue;
        private final TextView textFoodTypeAndScrapTypeValue;
        private final TextView textQuantityValue;
        private final TextView textStatusValue;
        private final Button buttonUpdateStatus;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        FoodCollectionHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textResidentName = itemView.findViewById(R.id.textResidentName);
            textApartment = itemView.findViewById(R.id.textApartment);
            textFlatNumber = itemView.findViewById(R.id.textFlatNumber);
            textFoodTypeAndScrapType = itemView.findViewById(R.id.textFoodTypeAndScrapType);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textStatus = itemView.findViewById(R.id.textStatus);
            textResidentNameValue = itemView.findViewById(R.id.textResidentNameValue);
            textApartmentValue = itemView.findViewById(R.id.textApartmentValue);
            textFlatNumberValue = itemView.findViewById(R.id.textFlatNumberValue);
            textFoodTypeAndScrapTypeValue = itemView.findViewById(R.id.textFoodTypeAndScrapTypeValue);
            textQuantityValue = itemView.findViewById(R.id.textQuantityValue);
            textStatusValue = itemView.findViewById(R.id.textStatusValue);
            buttonUpdateStatus = itemView.findViewById(R.id.buttonUpdateStatus);

            /*Setting fonts for all the view*/
            textResidentName.setTypeface(setLatoRegularFont(mCtx));
            textApartment.setTypeface(setLatoRegularFont(mCtx));
            textFlatNumber.setTypeface(setLatoRegularFont(mCtx));
            textFoodTypeAndScrapType.setTypeface(setLatoRegularFont(mCtx));
            textQuantity.setTypeface(setLatoRegularFont(mCtx));
            textStatus.setTypeface(setLatoRegularFont(mCtx));
            textResidentNameValue.setTypeface(setLatoBoldFont(mCtx));
            textApartmentValue.setTypeface(setLatoBoldFont(mCtx));
            textFlatNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textFoodTypeAndScrapTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textQuantityValue.setTypeface(setLatoBoldFont(mCtx));
            textStatusValue.setTypeface(setLatoBoldFont(mCtx));
            buttonUpdateStatus.setTypeface(setLatoLightFont(mCtx));

            /*Setting on click listeners to the views*/
            buttonUpdateStatus.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding On Click Listeners
         * ------------------------------------------------------------- */

        @Override
        public void onClick(View v) {
            final int position = getLayoutPosition();
            updateFoodDonationStatus(position, foodCollectionDataList.get(position));
        }
    }
}
