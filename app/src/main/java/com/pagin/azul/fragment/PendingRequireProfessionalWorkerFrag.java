package com.pagin.azul.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.activities.ViewAllOffersActivity;
import com.pagin.azul.adapter.PendingRequireProfessionalWorkerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingRequireProfessionalWorkerFrag extends Fragment {

    private PendingRequireProfessionalWorkerAdapter requireAdapter;
    ArrayList<String> pendingList;

    @BindView(R.id.rv_pending)
    RecyclerView rvPending;


    public PendingRequireProfessionalWorkerFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_require_professional, container, false);
        ButterKnife.bind(this, view);

        pendingList = new ArrayList<>();


        rvPending.setLayoutManager(new LinearLayoutManager(getActivity()));
        requireAdapter = new PendingRequireProfessionalWorkerAdapter(getActivity(), pendingList);
        rvPending.setAdapter(requireAdapter);

        preParedData();
        allClick();
        return view;
    }

    private void preParedData() {
        pendingList.add("4.18 KM");
        pendingList.add("5.18 KM");
        pendingList.add("3.08 KM");
        pendingList.add("2.08 KM");
    }


    private void allClick() {

        requireAdapter.setListener(new PendingRequireProfessionalWorkerAdapter.ViewOffersInterface() {
            @Override
            public void onAllOfferClick(View v, int pos) {

                startActivity(ViewAllOffersActivity.getIntent(getActivity(),""));
            }

            @Override
            public void onEditClick(View v, int pos) {
                getActivity().finish();
            }

            @Override
            public void onCancelClick(View v, int pos) {
                //startActivity(CancellationActivity.getIntent(getActivity()));
            }
        });
    }

}
