package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.activity.MyFavoritesActivity;
import com.pagin.azul.onphasesecond.activity.RestaurantDetails;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class RestroListingAdapter extends RecyclerView.Adapter<RestroListingAdapter.MyFevViewHolder> {
    private Context context;
    private CommonListener commonListener;
    private ArrayList<RestaurantResponse> restaurantList;

    public RestroListingAdapter(Context context, CommonListener commonListener) {
        this.context = context;
        this.commonListener = commonListener;
    }

    public RestroListingAdapter(Context context, CommonListener commonListener, ArrayList<RestaurantResponse> restaurantList) {
        this.context = context;
        this.commonListener = commonListener;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public MyFevViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_pager_restaurants, viewGroup, false);
        return new MyFevViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFevViewHolder myFevViewHolder, int position) {
        String image = restaurantList.get(position).getSellerData().getImage();
        if (image != null && !image.equals("")) {
            setImage(myFevViewHolder.progressbar, image, myFevViewHolder.ivRestaurants);
        } else {
            myFevViewHolder.progressbar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(myFevViewHolder.ivRestaurants);
        }

        myFevViewHolder.tvRestaurantNameHome.setText(restaurantList.get(position).getSellerData().getName());
        myFevViewHolder.tvRestauAddressHome.setText(restaurantList.get(position).getSellerData().getAddress());
        String avgRating = restaurantList.get(position).getSellerData().getAvgRating();
        myFevViewHolder.tvRatingHome.setText(avgRating);
        try {
            myFevViewHolder.ivRatinngs.setRating(Float.parseFloat(avgRating));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        myFevViewHolder.tvReview.setText("(" + restaurantList.get(position).getSellerData().getTotalRating() + context.getResources().getString(R.string.small_reviews) + ")");

        myFevViewHolder.ivLikeHome.setImageResource(R.drawable.fav);

        for (int i = 0; i < restaurantList.get(position).getSellerData().getCuisinesName().size(); i++) {
            TextView textView = new TextView(context);
            textView.setText(restaurantList.get(position).getSellerData().getCuisinesName().get(i));
            textView.setCompoundDrawablePadding(10);
            textView.setTextSize(11);
            //textView.setTextColor(ContextCompat.getColor(context, R.color.light_grey));
            textView.setPadding(0, 0, 15, 0);
            textView.setMaxLines(1);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_dot_circle, 0, 0, 0);
            myFevViewHolder.llFoodType.addView(textView);
        }

        myFevViewHolder.tvdistance.setText(restaurantList.get(position).getDistance());
    }

    @Override
    public int getItemCount() {
        return restaurantList != null ? restaurantList.size() : 0;
    }

    public class MyFevViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivRestaurants)
        ImageView ivRestaurants;
        @BindView(R.id.tvRestaurantNameHome)
        TextView tvRestaurantNameHome;
        @BindView(R.id.tvRestauAddressHome)
        TextView tvRestauAddressHome;
        @BindView(R.id.tvRatingHome)
        TextView tvRatingHome;
        @BindView(R.id.ivRatinngs)
        RatingBar ivRatinngs;
        @BindView(R.id.tvReview)
        TextView tvReview;
        @BindView(R.id.progressbar)
        ProgressBar progressbar;
        @BindView(R.id.ivLikeHome)
        ImageView ivLikeHome;
        @BindView(R.id.llFoodType)
        LinearLayout llFoodType;
        @BindView(R.id.ivdestLocation)
        ImageView ivdestLocation;
        @BindView(R.id.lottieAnimationView)
        LottieAnimationView lottieAnimationView;
        @BindView(R.id.tvdistance)
        TextView tvdistance;

        public MyFevViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick({R.id.cvPrentView, R.id.ivdestLocation, R.id.ivLikeHome})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cvPrentView:
                    commonListener.onItemClick(getAdapterPosition());
                    break;
                case R.id.ivdestLocation:
                    CommonUtilities.dispatchGoogleMap(context, restaurantList.get(getAdapterPosition()).getSellerData().getLatitude(),
                            restaurantList.get(getAdapterPosition()).getSellerData().getLongitude());
                    break;
                case R.id.ivLikeHome:
                    commonListener.onFavClick(getAdapterPosition());
                    break;
            }
        }
    }

    private void setImage(ProgressBar progressBar, final String imageUri, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context.getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }

}