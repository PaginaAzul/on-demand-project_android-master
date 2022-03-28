package com.pagin.azul.onphasesecond.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.ReviewAdapter;
import com.pagin.azul.onphasesecond.model.ProductDetailsResponse;
import com.pagin.azul.onphasesecond.model.RatingResponse;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.utils.CommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingAndReviewActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvRating)
    TextView tvRating;

    @BindView(R.id.tvRatingValue)
    TextView tvRatingValue;

    @BindView(R.id.tvTotalReviews)
    TextView tvTotalReviews;

    @BindView(R.id.rating_recycle)
    RecyclerView rating_recycle;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_and_review);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.rating_reviews));
        getIntentData();
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            Object obj = getIntent().getParcelableExtra(ParamEnum.DATA.theValue());
            if(obj instanceof RestaurantResponse) {
                RestaurantResponse details = (RestaurantResponse) obj;
                String avgRating = details.getAvgRating();
                try{
                    float f = Float.parseFloat(avgRating);
                    //tvRating.setText(Math.round(f)+getString(R.string.ratings));
                    tvRatingValue.setText(avgRating);
                    ratingBar.setRating(f);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                tvTotalReviews.setText(getString(R.string.restaurant_latest_reviews)+"("+details.getTotalRating()+")");
            }else if (obj instanceof ProductDetailsResponse){
                ProductDetailsResponse details = (ProductDetailsResponse) obj;
                String avgRating = details.getAvgRating();
                try{
                    float f = Float.parseFloat(avgRating);
                    //tvRating.setText(Math.round(f)+getString(R.string.ratings));
                    tvRatingValue.setText(avgRating);
                    ratingBar.setRating(f);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                tvTotalReviews.setText(getString(R.string.restaurant_latest_reviews)+"("+details.getTotalRating()+")");
            }

            ArrayList<RatingResponse> ratingList = getIntent().getParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue());
            setUpRecyclerView(ratingList);
        }
    }

    private void setUpRecyclerView(ArrayList<RatingResponse> ratingList) {
        rating_recycle.setLayoutManager(new LinearLayoutManager(this));
        rating_recycle.setAdapter(new ReviewAdapter(this,ratingList));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                }else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            }else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        }
        else {
            super.attachBaseContext(newBase);
        }
    }
}