package com.pagin.azul.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.activities.IssuesMyOrderActivity;
import com.pagin.azul.adapter.ActiveReqiureProfessionalAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveRequireProfessionalFrag extends Fragment {
    private ActiveReqiureProfessionalAdapter activeReqiureAdapter;
    private ArrayList<String> activeList;

    @BindView(R.id.rv_active)
    RecyclerView rvActive;


    public ActiveRequireProfessionalFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_require_professional, container, false);
        ButterKnife.bind(this, view);

        activeList = new ArrayList<>();

        rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));
        activeReqiureAdapter = new ActiveReqiureProfessionalAdapter(getActivity(), activeList);
        rvActive.setAdapter(activeReqiureAdapter);

        preParedData();
        findCallback();
        return view;

    }

    private void preParedData() {
        activeList.add("Mr. Abdul");
        activeList.add("Mr. Rajiv");
        activeList.add("Mr. Arjun");
        activeList.add("Mr. Chandan");
        activeReqiureAdapter.notifyDataSetChanged();
    }


    private void findCallback() {

        activeReqiureAdapter.setListener(new ActiveReqiureProfessionalAdapter.ActiveRequireProff() {
            @Override
            public void onReportOrder(View v, int pos) {
                startActivity(IssuesMyOrderActivity.getIntent(getActivity()));
            }

            @Override
            public void onCancelOrder(View v, int pos) {
                //startActivity(CancellationActivity.getIntent(getActivity()));
            }
        });

    }

}
