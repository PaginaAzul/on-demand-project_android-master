package com.pagin.azul.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.adapter.NewProvideServiceProffAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProvideServiceProffFrag extends Fragment {
    private NewProvideServiceProffAdapter newAdapter;
    private ArrayList<String> newList;


    @BindView(R.id.rv_new)
    RecyclerView rvNew;


    public NewProvideServiceProffFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_provide_service_proff, container, false);
        ButterKnife.bind(this, view);

        newList = new ArrayList<>();

        rvNew.setLayoutManager(new LinearLayoutManager(getActivity()));
        newAdapter = new NewProvideServiceProffAdapter(getActivity(), newList);
        rvNew.setAdapter(newAdapter);

        preParedData();


        return view;

    }

    private void preParedData() {
        newList.add("Mr. Abdul");
        newList.add("Mr. Rajiv");
        newList.add("Mr. Arjun");
        newList.add("Mr. Chandan");
        newAdapter.notifyDataSetChanged();
    }

}
