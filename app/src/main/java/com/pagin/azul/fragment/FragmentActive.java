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
import com.pagin.azul.adapter.ActiveDeliveryWorkerDashboardAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentActive extends Fragment {

    private ActiveDeliveryWorkerDashboardAdapter activeAdapter;
    private ArrayList<String> activeList;

    @BindView(R.id.rv_active)
    RecyclerView rvActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View  view=inflater.inflate(R.layout.fragment_active_details,container,false);
        ButterKnife.bind(this,view);


        activeList = new ArrayList<>();

//        rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));
//        activeAdapter = new ActiveDeliveryWorkerDashboardAdapter(getActivity(), activeList,this);
//        rvActive.setAdapter(activeAdapter);


        preParedData();

        return view;

    }

    private void preParedData() {
        activeList.add("Mr. Abdul");
        activeList.add("Mr. Rajiv");
        activeList.add("Mr. Arjun");
        activeList.add("Mr. Chandan");
        activeAdapter.notifyDataSetChanged();
    }


}