package com.pagin.azul.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.adapter.PastDeliveryWorkerDashboardAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPast extends Fragment {
    private PastDeliveryWorkerDashboardAdapter pastAdapter;

    private ArrayList<String> pastList;

    @BindView(R.id.rv_past)
    RecyclerView rvPast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_past_details, container, false);
        ButterKnife.bind(this, view);


        pastList = new ArrayList<>();

//        rvPast.setLayoutManager(new LinearLayoutManager(getActivity()));
//        pastAdapter = new PastDeliveryWorkerDashboardAdapter(getActivity(), pastList);
//        rvPast.setAdapter(pastAdapter);


        preParedData();
        return view;
    }

    private void preParedData() {
        pastList.add("Tax 5%");
        pastList.add("Tax 6%");
        pastList.add("Tax 7%");
        pastList.add("Tax 8%");
        pastAdapter.notifyDataSetChanged();
    }

}
