package com.pagin.azul.onphasesecond.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.activity.MyCartActivity;
import com.pagin.azul.onphasesecond.bottomsheet.RestroInfoBottomSheet;
import com.pagin.azul.onphasesecond.utilty.SwitchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersFrag extends Fragment {

    public MyOrdersFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.tvPast)
    TextView tvPast;

    @BindView(R.id.tvOnGoing)
    TextView tvOnGoing;

    @BindView(R.id.tvUpcoming)
    TextView tvUpcoming;

    private UpcomingOrdersFragment upcomingOrdersFragment;
    private OngoingOrdersFragment ongoingOrdersFragment;
    private PastOrdersFragment pastOrdersFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ButterKnife.bind(this,view);
        initFrag();
        return view;
    }

    @OnClick({R.id.tvPast,R.id.tvOnGoing,R.id.tvUpcoming})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPast:
                loadFragment("Past");
                break;
            case R.id.tvOnGoing:
                loadFragment("Ongoing");
                break;
            case R.id.tvUpcoming:
                loadFragment("Upcoming");
                break;
        }
    }

    private void initFrag() {
        upcomingOrdersFragment = new UpcomingOrdersFragment();
        ongoingOrdersFragment = new OngoingOrdersFragment();
        pastOrdersFragment = new PastOrdersFragment();
        loadFragment("Upcoming");
    }

    private void loadFragment(String tab) {
        if (tab.equalsIgnoreCase("Past")) {
            tvPast.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvPast.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_past));
            tvOnGoing.setBackground(null);
            tvOnGoing.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            tvUpcoming.setBackground(null);
            tvUpcoming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            getChildFragmentManager().beginTransaction().replace(R.id.frameMyOrders, pastOrdersFragment).commit();
        } else if (tab.equalsIgnoreCase("Ongoing")) {
            tvOnGoing.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvOnGoing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_ongoing));
            tvPast.setBackground(null);
            tvPast.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            tvUpcoming.setBackground(null);
            tvUpcoming.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            getChildFragmentManager().beginTransaction().replace(R.id.frameMyOrders, ongoingOrdersFragment).commit();
        } else if (tab.equalsIgnoreCase("Upcoming")) {
            tvUpcoming.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvUpcoming.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_upcoming));
            tvOnGoing.setBackground(null);
            tvOnGoing.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            tvPast.setBackground(null);
            tvPast.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
            getChildFragmentManager().beginTransaction().replace(R.id.frameMyOrders, upcomingOrdersFragment).commit();
        }
    }
}