package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.model.ExclusiveOfferModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Mahipal Singh on 14,JUne,2019
 * mahisingh1@outlook.com
 */

public class HomeOfferViewPagerAdap extends PagerAdapter {

    private Context context;
    private CommonListener commonListener;
    private int parentPosition;
    private ArrayList<ExclusiveOfferModel.Datum> offerList;

    public HomeOfferViewPagerAdap(Context context, CommonListener commonListener, int parentPosition, ArrayList<ExclusiveOfferModel.Datum> offerList) {
        this.context = context;
        this.commonListener = commonListener;
        this.parentPosition = parentPosition;
        this.offerList = offerList;
    }

    @Override
    public int getCount() {
        return offerList!=null?offerList.size():0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_offer_home, container, false);
        ImageView ivOffers = itemView.findViewById(R.id.ivOffers);
        ProgressBar progressbar = itemView.findViewById(R.id.progressbar);
        if(offerList.get(position)!=null){
            String image = offerList.get(position).getImage();
            if(image!=null && !image.equals("")){
                setImage(progressbar,image,ivOffers);
            }else {
                progressbar.setVisibility(View.GONE);
                Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(ivOffers);
            }
        }else {
            progressbar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(ivOffers);
        }
        ivOffers.setOnClickListener(v -> commonListener.onItemClick(parentPosition,position));
        container.addView(itemView);
        return itemView;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    private void setImage( ProgressBar progressBar,final String imageUri,final ImageView imageView) {
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
