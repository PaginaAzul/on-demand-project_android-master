package com.pagin.azul.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.adapter.PastRequireProfessionalAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PastRequireProfessionalFrag extends Fragment {
    private PastRequireProfessionalAdapter pastRequireAdapter;
    private ArrayList<String> pastList;

    @BindView(R.id.rv_past)
    RecyclerView rvPast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_require_professional, container, false);
        ButterKnife.bind(this, view);

        pastList = new ArrayList<>();

        rvPast.setLayoutManager(new LinearLayoutManager(getActivity()));
        pastRequireAdapter = new PastRequireProfessionalAdapter(getActivity(), pastList);
        rvPast.setAdapter(pastRequireAdapter);


        preParedData();

        return view;
    }


    private void preParedData() {
        pastList.add("Tax 5%");
        pastList.add("Tax 6%");
        pastList.add("Tax 7%");
        pastList.add("Tax 8%");
        pastRequireAdapter.notifyDataSetChanged();
    }


}
