package com.pagin.azul.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.adapter.ActiveProvideServiceProffAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveProvideServiceProffFrag extends Fragment {
    private ActiveProvideServiceProffAdapter activeAdapter;
    private ArrayList<String> activeList;

    @BindView(R.id.rv_active)
    RecyclerView rvActive;


    public ActiveProvideServiceProffFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_provide_service_proff, container, false);
        ButterKnife.bind(this, view);

        activeList = new ArrayList<>();

        rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));
        activeAdapter = new ActiveProvideServiceProffAdapter(getActivity(), activeList);
        rvActive.setAdapter(activeAdapter);


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
