package com.pagin.azul.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.adapter.PendingProvideServiceProffAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingProvideServiceProffFrag extends Fragment {
    private PendingProvideServiceProffAdapter pendingAdapter;
    private ArrayList<String> pendingList;


    @BindView(R.id.rv_pending)
    RecyclerView rvPending;


    public PendingProvideServiceProffFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_provide_service_proff, container, false);
        ButterKnife.bind(this, view);

        pendingList = new ArrayList<>();

        rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
        pendingAdapter = new PendingProvideServiceProffAdapter(getActivity(), pendingList);
        rvPending.setAdapter(pendingAdapter);


        preParedData();

        return view;


    }

    private void preParedData() {
        pendingList.add("4.18 KM");
        pendingList.add("5.18 KM");
        pendingList.add("3.08 KM");
        pendingList.add("2.08 KM");
    }

}
