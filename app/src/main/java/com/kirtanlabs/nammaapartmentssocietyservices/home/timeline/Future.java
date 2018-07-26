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

import com.kirtanlabs.nammaapartmentssocietyservices.R;
import com.kirtanlabs.nammaapartmentssocietyservices.home.PlumberServicesAdapter;

public class Future extends Fragment {

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

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewFuture = view.findViewById(R.id.recyclerViewFuture);
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(getActivity()));

        PlumberServicesAdapter adapter = new PlumberServicesAdapter(getActivity(), R.string.future);

        //Setting adapter to recycler view
        recyclerViewFuture.setAdapter(adapter);
    }
}
