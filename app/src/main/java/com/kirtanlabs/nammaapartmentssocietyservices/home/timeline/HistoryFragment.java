package com.kirtanlabs.nammaapartmentssocietyservices.home.timeline;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.home.HomeViewPager;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.NammaApartmentUser.NAUser;
import com.kirtanlabs.nammaapartmentssocietyservices.pojo.SocietyServiceNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_HISTORY;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.SOCIETY_SERVICES_REFERENCE;

public class HistoryFragment extends Fragment {

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*TODO: Show Progress Indicator while we fetch History Data from firebase*/
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.showProgressIndicator();

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Retrieving all the request details which society service has received till now*/
        RetrievingNotificationData retrievingNotificationData = new RetrievingNotificationData();
        retrievingNotificationData.getHistoryUIDMap(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                historyUIDMap -> {
                    retrievingNotificationData.getNotificationDataList(
                            new ArrayList<>(historyUIDMap.keySet()),
                            societyServiceNotificationList -> {
                                List<SocietyServiceNotification> AllRequestDetailsList = new ArrayList<>();
                                for (SocietyServiceNotification societyServiceNotification : societyServiceNotificationList) {
                                    retrievingNotificationData.getUserData(societyServiceNotification.getUserUID(), new RetrievingNotificationData.UserDataCallback() {
                                        @Override
                                        public void onCallBack(NAUser NAUser) {
                                            societyServiceNotification.setNaUser(NAUser);
                                            societyServiceNotification.setSocietyServiceResponse(historyUIDMap.get(societyServiceNotification.getNotificationUID()));
                                            AllRequestDetailsList.add(societyServiceNotification);

                                            if (societyServiceNotificationList.size() == AllRequestDetailsList.size()) {
                                                baseActivity.hideProgressIndicator();

                                                HomeAdapter adapter = new HomeAdapter(getActivity(), AllRequestDetailsList, R.string.history);
                                                recyclerViewHistory.setAdapter(adapter);
                                            }
                                        }
                                    });

                                }
                            });
                }
        );
    }

}
