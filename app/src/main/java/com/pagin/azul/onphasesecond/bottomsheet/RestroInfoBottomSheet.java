package com.pagin.azul.onphasesecond.bottomsheet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.activity.RatingAndReviewActivity;
import com.pagin.azul.onphasesecond.activity.RestaurantDetails;
import com.pagin.azul.onphasesecond.adapters.ReviewAdapter;
import com.pagin.azul.onphasesecond.model.RatingResponse;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestroInfoBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.restro_info_recycler)
    RecyclerView restro_info_recycler;

    @BindView(R.id.tv_description)
    TextView tv_description;

    @BindView(R.id.tv_timing)
    TextView tv_timing;

    @BindView(R.id.tv_restro_heading)
    TextView tv_restro_heading;

    private Context mContext;
    private RestaurantResponse details;
    private ArrayList<RatingResponse> ratingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_restau_info,container,false);
        ButterKnife.bind(this,view);
        mContext = getContext();
        getIntentData();
        return view;
    }

    @OnClick({R.id.iv_close,R.id.tv_viewAll})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_viewAll:
                if(ratingList!=null && !ratingList.isEmpty())
                    dispatchRatingReview();
                break;
        }
    }

    private void dispatchRatingReview() {
        Intent intent = new Intent(mContext, RatingAndReviewActivity.class);
        intent.putExtra(ParamEnum.DATA.theValue(),details);
        intent.putParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue(),ratingList);
        startActivity(intent);
    }

    private void getIntentData() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            details = bundle.getParcelable(ParamEnum.DATA.theValue());
            if (details != null) {
                tv_description.setText(details.getDescription());
                String openingTime = details.getOpeningTime();
                String closingTime = details.getClosingTime();
                tv_timing.setText((openingTime!=null?openingTime:"")+" - "+(closingTime!=null?closingTime:""));
                String storeType = details.getStoreType();
                // change header according to store type
                if(storeType.equalsIgnoreCase(ParamEnum.GROCERY.theValue()))
                    tv_restro_heading.setText(R.string.store_info);
                else
                    tv_restro_heading.setText(R.string.restaurant_info);
            }
            ratingList = bundle.getParcelableArrayList(ParamEnum.DATA_LIST.theValue());
            setUpRecyclerView(ratingList);
        }
    }

    private void setUpRecyclerView(ArrayList<RatingResponse> ratingList) {
        restro_info_recycler.setLayoutManager(new LinearLayoutManager(mContext));
        restro_info_recycler.setAdapter(new ReviewAdapter(mContext,ratingList));
    }
}
