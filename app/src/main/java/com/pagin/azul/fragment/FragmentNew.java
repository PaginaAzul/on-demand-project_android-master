package com.pagin.azul.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;
import com.pagin.azul.adapter.NewOfferAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentNew extends Fragment {
    @BindView(R.id.rv_new_offer)
    RecyclerView recyclerViewNewOffer;
    ArrayList<String> newOfferList;
    private NewOfferAdapter newOfferAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View  view=inflater.inflate(R.layout.fragment_new_offer,container,false);
        ButterKnife.bind(this,view);
        newOfferList=new ArrayList<>();

        recyclerViewNewOffer.setLayoutManager(new LinearLayoutManager(getActivity()));
        newOfferAdapter=new NewOfferAdapter(getActivity(),newOfferList);
        recyclerViewNewOffer.setAdapter(newOfferAdapter);
        prePareData();
        return view;
    }

    private void prePareData() {
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferList.add("A");
        newOfferAdapter.notifyDataSetChanged();


    }
}