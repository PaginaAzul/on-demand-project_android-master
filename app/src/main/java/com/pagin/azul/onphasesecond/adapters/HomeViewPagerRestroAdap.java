package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;

public class HomeViewPagerRestroAdap extends PagerAdapter {
    private Context context;
    private CommonListener commonListener;
    private int parentPosition;
    private ArrayList<RestaurantResponse> list;

    public HomeViewPagerRestroAdap(Context context, CommonListener commonListener, int parentPosition,ArrayList<RestaurantResponse> list) {
        this.context = context;
        this.commonListener = commonListener;
        this.parentPosition = parentPosition;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_view_pager_restaurants, container, false);
        TextView tvRestaurantNameHome = (TextView) itemView.findViewById(R.id.tvRestaurantNameHome);
        CardView cvPrentView = (CardView) itemView.findViewById(R.id.cvPrentView);
        TextView tvRestauAddressHome = (TextView) itemView.findViewById(R.id.tvRestauAddressHome);
        TextView tvRatingHome = (TextView) itemView.findViewById(R.id.tvRatingHome);
        ImageView ivLikeHome = (ImageView) itemView.findViewById(R.id.ivLikeHome);
        TextView tvReview = (TextView) itemView.findViewById(R.id.tvReview);
        RatingBar ivRatinngs = (RatingBar) itemView.findViewById(R.id.ivRatinngs);
        ImageView ivRestaurants = (ImageView) itemView.findViewById(R.id.ivRestaurants);
        ProgressBar progressbar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        TextView tvdistance = (TextView) itemView.findViewById(R.id.tvdistance);
        LinearLayout llFoodType = (LinearLayout) itemView.findViewById(R.id.llFoodType);
        ImageView ivdestLocation = (ImageView) itemView.findViewById(R.id.ivdestLocation);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) itemView.findViewById(R.id.lottieAnimationView);
        /*if(parentPosition==2){
            llFoodType.setVisibility(View.GONE);
        }else{
            llFoodType.setVisibility(View.VISIBLE);
        }*/

        tvRestaurantNameHome.setText(list.get(position).getName());
        tvRestauAddressHome.setText(list.get(position).getAddress());
        String avgRating = list.get(position).getAvgRating();
        tvRatingHome.setText(avgRating);
        try{
            ivRatinngs.setRating(Float.parseFloat(avgRating));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        tvReview.setText("("+list.get(position).getTotalRating()+context.getResources().getString(R.string.small_reviews)+")");
        if(list.get(position).isFav())
            ivLikeHome.setImageResource(R.drawable.fav);
        else
            ivLikeHome.setImageResource(R.drawable.fav_un);

        String image = list.get(position).getImage();
        if(image!=null && !image.equals("")){
            setImage(progressbar,image,ivRestaurants);
        }else {
            progressbar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(ivRestaurants);
        }

        ArrayList<RestaurantResponse> cuisineList = list.get(position).getCuisineData();
        if(cuisineList!=null && !cuisineList.isEmpty()){
            for (int i = 0; i < cuisineList.size(); i++) {
                TextView textView = new TextView(context);
                textView.setText(cuisineList.get(i).getName());
                textView.setCompoundDrawablePadding(10);
                textView.setTextSize(11);
                //textView.setTextColor(ContextCompat.getColor(context, R.color.light_grey));
                textView.setPadding(0, 0, 15, 0);
                textView.setMaxLines(1);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bg_dot_circle, 0, 0, 0);
                llFoodType.addView(textView);
            }
        }else {
            if(parentPosition==1){
                TextView textView = new TextView(context);
                textView.setText(R.string.no_cuisine);
                textView.setTextSize(11);
                textView.setPadding(0, 0, 15, 0);
                textView.setMaxLines(1);
                llFoodType.addView(textView);
            }
        }

        try {
            /*double time = distance(Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(kLat)),
                    Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(kLong)),
                    Double.parseDouble(list.get(position).getLatitude()),
                    Double.parseDouble(list.get(position).getLongitude()));
            DecimalFormat df = new DecimalFormat("#.##");
            time = Double.parseDouble(df.format(time));*/
            //double distanceInKm = 1.60934  * list.get(position).getDist().getCalculated();
            double distanceInKm = 0.001  * list.get(position).getDist().getCalculated();
            //DecimalFormat df = new DecimalFormat("#.##");
            //tvdistance.setText(df.format(distanceInKm) + "km");
            tvdistance.setText(distanceInKm+"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        cvPrentView.setOnClickListener(v -> commonListener.onItemClick(parentPosition,position));
        ivdestLocation.setOnClickListener(v -> CommonUtilities.dispatchGoogleMap(context,list.get(position).getLatitude(),
                list.get(position).getLongitude()));

        ivLikeHome.setOnClickListener(v -> {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.setAnimation("layer.json");
            lottieAnimationView.playAnimation();
            lottieAnimationView.loop(true);

            commonListener.onFavClick(position,parentPosition,list.get(position));

            //lottieAnimationView.setVisibility(GONE);
        });

        container.addView(itemView);
        return itemView;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        ButterKnife.bind(this, view);
        return view == ((ConstraintLayout) o);
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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
