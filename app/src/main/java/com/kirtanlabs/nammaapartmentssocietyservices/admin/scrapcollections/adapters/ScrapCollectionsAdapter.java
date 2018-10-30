package com.kirtanlabs.nammaapartmentssocietyservices.admin.scrapcollections.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.scrapcollections.pojo.ScrapCollectionPojo;

import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_COLLECTED;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.setLatoRegularFont;
import static com.kirtanlabs.nammaapartmentssocietyservices.Utilities.capitalizeString;

public class ScrapCollectionsAdapter extends RecyclerView.Adapter<ScrapCollectionsAdapter.ScrapCollectionHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<ScrapCollectionPojo> scrapCollectionDataList;
    private final BaseActivity baseActivity;
    private final int screenTitle;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public ScrapCollectionsAdapter(Context mCtx, List<ScrapCollectionPojo> scrapCollectionDataList, int screenTitle) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.scrapCollectionDataList = scrapCollectionDataList;
        this.screenTitle = screenTitle;
    }

    /* ------------------------------------------------------------- *
     * Overriding Recycler View Adapter Object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public ScrapCollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_food_collections, parent, false);
        return new ScrapCollectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrapCollectionHolder holder, int position) {

        /*Setting the Scrap Type Text since we are using same layout*/
        holder.textFoodTypeAndScrapType.setText(mCtx.getString(R.string.scrap_type));
        holder.imageSocietyService.setImageResource(R.drawable.scrap_collection_na);
        ScrapCollectionPojo scrapCollectionData = scrapCollectionDataList.get(position);
        holder.textResidentNameValue.setText(scrapCollectionData.getNaUser().getPersonalDetails().getFullName());
        holder.textApartmentValue.setText(scrapCollectionData.getNaUser().getFlatDetails().getApartmentName());
        holder.textFlatNumberValue.setText(scrapCollectionData.getNaUser().getFlatDetails().getFlatNumber());
        holder.textFoodTypeAndScrapTypeValue.setText(scrapCollectionData.getScrapType());
        holder.textQuantityValue.setText(scrapCollectionData.getQuantity());
        holder.textStatusValue.setText(capitalizeString(scrapCollectionData.getStatus()));

        switch (screenTitle) {
            case R.string.history:
                holder.buttonUpdateStatus.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return scrapCollectionDataList.size();
    }

    /*------------------------------------------------------------------
     *Private Method
     *------------------------------------------------------------------*/

    /**
     * This method updates the status of scrap collections raised by user .
     *
     * @param position            of card view whose scrap collection details needs to be removed.
     * @param scrapCollectionPojo contains instance of that particular UID of that particular scrap collection.
     */
    private void updateScrapCollectionStatus(int position, ScrapCollectionPojo scrapCollectionPojo) {
        String scrapCollectionUID = scrapCollectionPojo.getNotificationUID();
        scrapCollectionDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, scrapCollectionDataList.size());
        DatabaseReference scrapCollectionReference = ALL_SOCIETYSERVICENOTIFICATION_REFERENCE.child(scrapCollectionUID).child(FIREBASE_CHILD_STATUS);
        scrapCollectionReference.setValue(FIREBASE_CHILD_COLLECTED);

        /*This is to ensure when user deletes the last item in the list a blank screen is not shown
         * instead feature unavailable layout is shown*/
        if (scrapCollectionDataList.isEmpty()) {
            baseActivity.showFeatureUnavailableLayout(R.string.scrap_collection_unavailable);
        }
    }
    /* ------------------------------------------------------------- *
     * Scrap Collection Holder Class
     * ------------------------------------------------------------- */

    class ScrapCollectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final ImageView imageSocietyService;
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

        ScrapCollectionHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            imageSocietyService = itemView.findViewById(R.id.imageSocietyService);
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

            /*Setting on click listeners on views*/
            buttonUpdateStatus.setOnClickListener(this);
        }

        /* ------------------------------------------------------------- *
         * Overriding On Click Listeners
         * ------------------------------------------------------------- */
        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            ScrapCollectionPojo scrapCollectionPojo = scrapCollectionDataList.get(position);
            switch (v.getId()) {
                case R.id.buttonUpdateStatus:
                    updateScrapCollectionStatus(position, scrapCollectionPojo);
                    break;
            }

        }
    }
}
