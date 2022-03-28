package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.model.ExclusiveOfferModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeAdapter extends RecyclerView.Adapter {

    public Context context;
    public static final int OFFER_VIEW = 0;
    public static final int RESTRO_VIEW = 1;
    public static final int GROCERY_VIEW = 2;
    private CommonListener commonListener;
    int currentPage = 0;
    private ArrayList<ExclusiveOfferModel.Datum> offerList;
    private ArrayList<RestaurantResponse> nearByRestroList;
    private ArrayList<RestaurantResponse> nearByStoreList;

    public HomeAdapter(Context context, CommonListener commonListener, ArrayList<ExclusiveOfferModel.Datum> offerList, ArrayList<RestaurantResponse> nearByRestroList, ArrayList<RestaurantResponse> nearByStoreList) {
        this.context = context;
        this.commonListener = commonListener;
        this.offerList = offerList;
        this.nearByRestroList = nearByRestroList;
        this.nearByStoreList = nearByStoreList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == OFFER_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_offer_home_item, parent, false);
            return new OfferViewHolder(view);
        } else if (viewType == RESTRO_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_restaurant_adapter, parent, false);
            return new RestroListViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_home_restaurant_adapter, parent, false);
            return new RestroListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case OFFER_VIEW:
                if (offerList != null && offerList.size() > 0) {
                    HomeOfferViewPagerAdap homeOfferViewPagerAdap = new HomeOfferViewPagerAdap(context, commonListener, position, offerList);
                    ((OfferViewHolder) holder).vpOffer.setAdapter(homeOfferViewPagerAdap);
                    //setAutoScroll(homeOfferViewPagerAdap, ((OfferViewHolder) holder).vpOffer);
                } else {
                    ((OfferViewHolder) holder).tvAvailableOffers.setVisibility(View.GONE);
                    ((OfferViewHolder) holder).vpOffer.setVisibility(View.GONE);
                    ((OfferViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                    ((OfferViewHolder) holder).view.setVisibility(View.GONE);
                }
                break;
            case RESTRO_VIEW:
                if (nearByRestroList != null && nearByRestroList.size() > 0) {
                    HomeViewPagerRestroAdap homeViewPagerRestroAdap = new HomeViewPagerRestroAdap(context, commonListener, position, nearByRestroList);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setAdapter(homeViewPagerRestroAdap);
                    ((RestroListViewHolder) holder).tvRestaurant.setText(context.getString(R.string.near_by_restaurants));
                } else {
                    ((RestroListViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).tvRestaurant.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).view.setVisibility(View.GONE);
                }
                break;
            case GROCERY_VIEW:
                if(nearByStoreList!=null && nearByStoreList.size()>0){
                    HomeViewPagerRestroAdap homeViewPagerStoreAdap = new HomeViewPagerRestroAdap(context,commonListener,position,nearByStoreList);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setAdapter(homeViewPagerStoreAdap);
                    ((RestroListViewHolder) holder).tvRestaurant.setText(R.string.grocery_stores);
                }else {
                    ((RestroListViewHolder) holder).tvViewMore.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).vpHomeRestaurants.setVisibility(View.GONE);
                    ((RestroListViewHolder) holder).tvRestaurant.setVisibility(View.GONE);
                }
                ((RestroListViewHolder) holder).view.setVisibility(View.GONE);
                break;
        }
    }

    private void setAutoScroll(HomeOfferViewPagerAdap homeOfferViewPagerAdap, ViewPager vpOffer) {
        Timer timer;
        final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == homeOfferViewPagerAdap.getCount()) {
                currentPage = 0;
            }
            try{
                vpOffer.setCurrentItem(currentPage++, true);
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public int getItemCount() {
        if (nearByRestroList != null && nearByRestroList.size() > 0) {
            return 3;
        } else if (nearByStoreList != null && nearByStoreList.size() > 0) {
            return 3;
        } else if (offerList != null && offerList.size() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return OFFER_VIEW;
        if (position == 1)
            return RESTRO_VIEW;
        else
            return GROCERY_VIEW;

    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpOffer)
        ViewPager vpOffer;
        @BindView(R.id.tvAvailableOffers)
        TextView tvAvailableOffers;
        @BindView(R.id.tvViewMore)
        TextView tvViewMore;
        @BindView(R.id.view)
        View view;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            vpOffer.setClipToPadding(false);
            vpOffer.setPadding(30, 0, 80, 0);
            tvViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    class RestroListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vpHomeRestaurants)
        ViewPager vpHomeRestaurants;
        @BindView(R.id.tvRestaurant)
        TextView tvRestaurant;
        @BindView(R.id.tvViewMore)
        TextView tvViewMore;
        @BindView(R.id.view)
        View view;

        public RestroListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            vpHomeRestaurants.setClipToPadding(false);
            vpHomeRestaurants.setPadding(30, 0, 80, 0);
            tvViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonListener.onViewMoreClick(getAdapterPosition());
                }
            });
        }
    }

    public void updateList(ArrayList<ExclusiveOfferModel.Datum> offerList, ArrayList<RestaurantResponse> nearByRestroList, ArrayList<RestaurantResponse> nearByStoreList) {
        this.offerList = offerList;
        this.nearByRestroList = nearByRestroList;
        this.nearByStoreList = nearByStoreList;
        notifyDataSetChanged();
    }

}
