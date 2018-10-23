package com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartmentssocietyservices.BaseActivity;
import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.adapters.NoticeBoardHistoryAdapter;
import com.kirtanlabs.nammaapartmentssocietyservices.admin.addnotice.pojo.NoticeBoardPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kirtanlabs.nammaapartmentssocietyservices.Constants.NOTICE_BOARD_REFERENCE;

public class NoticeBoardHistoryActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<NoticeBoardPojo> nammaApartmentsNoticeList;
    private NoticeBoardHistoryAdapter noticeBoardAdapter;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notice_board_history;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.notice_board;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id for all the views*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Initialising Array List*/
        nammaApartmentsNoticeList = new ArrayList<>();

        /*Initializing the adapter*/
        noticeBoardAdapter = new NoticeBoardHistoryAdapter(nammaApartmentsNoticeList, this);

        /* Setting the Notice Board Adapter*/
        recyclerView.setAdapter(noticeBoardAdapter);

        /*Retrieving Notice Details Added By Society Service Admin*/
        retrieveNoticeDetailsFromFirebase();
    }


    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when user tries to see the notices which was added by society admin.
     */
    private void retrieveNoticeDetailsFromFirebase() {
        NOTICE_BOARD_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot noticeBoardUIDSnapshot) {
                if (!noticeBoardUIDSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.notice_unavailable);
                } else {
                    for (DataSnapshot noticeBoardDataSnapshot : noticeBoardUIDSnapshot.getChildren()) {
                        String noticeBoardUID = noticeBoardDataSnapshot.getKey();
                        DatabaseReference noticeBoardUIDReference = NOTICE_BOARD_REFERENCE.child(noticeBoardUID);
                        noticeBoardUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot noticeBoardUIDDataSnapshot) {
                                NoticeBoardPojo nammaApartmentsNotice = noticeBoardUIDDataSnapshot.getValue(NoticeBoardPojo.class);
                                nammaApartmentsNoticeList.add(index++, nammaApartmentsNotice);
                                if (noticeBoardUIDSnapshot.getChildrenCount() == nammaApartmentsNoticeList.size()) {
                                    hideProgressIndicator();
                                    reverseNoticeList(nammaApartmentsNoticeList);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method gets invoked to reverse the list coming from firebase
     *
     * @param nammaApartmentsNoticeList contains the list of notices added by society admin.
     */
    private void reverseNoticeList(List<NoticeBoardPojo> nammaApartmentsNoticeList) {
        Collections.reverse(nammaApartmentsNoticeList);
        noticeBoardAdapter.notifyDataSetChanged();
    }
}