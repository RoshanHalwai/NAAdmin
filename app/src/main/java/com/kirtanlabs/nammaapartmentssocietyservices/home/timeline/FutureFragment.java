package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal.societyServiceUID;

public class FutureFragment extends Fragment {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private BaseActivity baseActivity;
    private TimeLineAdapter futureAdapter;
    private List<SocietyServiceNotification> futureNotificationDataList;

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_future, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseActivity = (BaseActivity) getActivity();

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewFuture = view.findViewById(R.id.recyclerViewFuture);
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(getActivity()));

        futureNotificationDataList = new ArrayList<>();

        /*Setting Adapter to Recycler view*/
        futureAdapter = new TimeLineAdapter(getActivity(), futureNotificationDataList);
        recyclerViewFuture.setAdapter(futureAdapter);

        /*Setting Tag of Future Fragment*/
        String futureFragmentTag = getTag();
        ((HomeViewPager) Objects.requireNonNull(getActivity())).setFutureFragmentTag(futureFragmentTag);

        /*Retrieving All Future Request Details*/
        updateUIWithFutureData();
    }

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    /**
     * This method is used to Retrieve and Update Future Request Details from firebase.
     */
    public void updateUIWithFutureData() {
        /*Removing all previous Future Request UID from the List*/
        futureNotificationDataList.clear();
        futureAdapter.notifyDataSetChanged();

        /*Retrieving all the request details which society service has received till now*/
        RetrievingNotificationData retrievingNotificationData = new RetrievingNotificationData(getActivity(), societyServiceUID);
        retrievingNotificationData.getFutureNotificationDataList(futureNotificationDataList -> {
            if (futureNotificationDataList == null) {
                Objects.requireNonNull(baseActivity).showFeatureUnavailableLayout(R.string.future_detail_message);
            } else {
                this.futureNotificationDataList.addAll(futureNotificationDataList);
                futureAdapter.notifyDataSetChanged();
            }
        });

    }

}
