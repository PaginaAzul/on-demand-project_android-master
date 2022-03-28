package com.pagin.azul.onphasesecond.bottomsheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.adapters.FullCustomizationAdap;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Mahipal Singh on 23,May,2019
 * mahisingh1@outlook.com
 */

public class ChooseYourTasteBottomSheet extends BottomSheetDialogFragment{

    @BindView(R.id.rvChooseTaste)
    RecyclerView rvChooseTaste;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    public ChooseYourTasteBottomSheet(){

    }

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_choose_taste, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        setUpRecyclerView();
        return view;

    }

    @OnClick({R.id.iv_close, R.id.clAddToCart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.clAddToCart:

                break;
        }
    }

    private void setUpRecyclerView() {
        rvChooseTaste.setLayoutManager(new LinearLayoutManager(mContext));
        rvChooseTaste.setAdapter(new FullCustomizationAdap(mContext));
        rvChooseTaste.setNestedScrollingEnabled(false);
    }

}
