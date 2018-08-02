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

import java.util.Objects;

import static com.kirtanlabs.nammaapartmentssocietyservices.SocietyServiceGlobal.societyServiceUID;

public class FutureFragment extends Fragment {

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
        BaseActivity baseActivity = (BaseActivity) getActivity();

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewHistory = view.findViewById(R.id.recyclerViewFuture);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Retrieving all the request details which society service has received till now*/
        RetrievingNotificationData retrievingNotificationData = new RetrievingNotificationData(getActivity(), societyServiceUID);
        retrievingNotificationData.getFutureNotificationDataList(futureNotificationDataList -> {
            if (futureNotificationDataList == null) {
                Objects.requireNonNull(baseActivity).showFeatureUnavailableLayout(R.string.future_detail_message);
            } else {
                TimeLineAdapter adapter = new TimeLineAdapter(getActivity(), futureNotificationDataList);
                recyclerViewHistory.setAdapter(adapter);
            }
        });

    }

}
