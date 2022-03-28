package com.pagin.azul.onphasesecond.adapters;

import android.util.Log;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.fragments.DynamicFragment;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;

import java.util.ArrayList;

public class MenuPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private ArrayList<RestaurantResponse> data;
    private String resAndStoreId;

    public MenuPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<RestaurantResponse> data, String resAndStoreId) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.data = data;
        this.resAndStoreId = resAndStoreId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=DynamicFragment.newInstance(data.get(position).getCuisine(),resAndStoreId);
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }



}
