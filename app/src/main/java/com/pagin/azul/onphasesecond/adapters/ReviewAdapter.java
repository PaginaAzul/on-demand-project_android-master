package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.model.RatingResponse;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private ArrayList<RatingResponse> ratingList;

    public ReviewAdapter(Context mContext, ArrayList<RatingResponse> ratingList) {
        this.context = mContext;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_resto_info_items,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        RatingResponse userData = ratingList.get(position).getUserData();
        String image = userData.getProfilePic();
        if(image!=null && !image.equals("")){
            setImage(holder.progressBar,image,holder.ivProfilePic);
        }else {
            holder.progressBar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.profile_default).into(holder.ivProfilePic);
        }
        holder.tv_name.setText(userData.getName());
        try{
            holder.ratingbar.setRating(Float.parseFloat(ratingList.get(position).getRating()));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        holder.tv_review.setText(ratingList.get(position).getReview());

        String responseDate = ratingList.get(position).getUpdatedAt()+" "+ratingList.get(position).getUpdatedAt();
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String currentDate = mdformat.format(calendar.getTime());
        Date startDate = null,endDate = null;
        try {
            String outputDate =  CommonUtilities.getOutputFormat(responseDate);
            startDate = mdformat.parse(outputDate);
            endDate = mdformat.parse(currentDate);
            printDifference(startDate,endDate,holder.tvTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // hide view(line) at last position of adapter
        if(position==(getItemCount()-1))
            holder.view36.setVisibility(GONE);
        else
            holder.view36.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return ratingList!=null?ratingList.size():0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.ratingbar)
        RatingBar ratingbar;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tv_review)
        TextView tv_review;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.view36)
        View view36;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void setImage(ProgressBar progressBar, final String imageUri, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context.getApplicationContext())
                .load(imageUri)
                //.centerCrop()
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

    public void printDifference(Date startDate, Date endDate,TextView tvTime) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        long difference = Math.abs(startDate.getTime() - endDate.getTime());
        long diffInHours = TimeUnit.MILLISECONDS.toHours(difference);


        if((elapsedSeconds>0 && elapsedMinutes==0 && diffInHours==0 && elapsedDays==0)){
            if(elapsedSeconds>1)
                tvTime.setText(elapsedSeconds+context.getResources().getString(R.string.sec_ago));
            else
                tvTime.setText(elapsedSeconds+context.getResources().getString(R.string.seconds_ago));
        }else if((elapsedMinutes > 0 && diffInHours == 0 && elapsedDays == 0)) {
            if(elapsedMinutes==1)
                tvTime.setText(elapsedMinutes+context.getResources().getString(R.string.min_ago));
            else
                tvTime.setText(elapsedMinutes+context.getResources().getString(R.string.mins_ago));
        }else if((diffInHours>0 && elapsedSeconds==0 && elapsedMinutes==0 && elapsedDays==0) || (diffInHours>0  && elapsedMinutes>0 && elapsedDays==0))  {
            if(diffInHours==1)
                tvTime.setText(diffInHours+context.getResources().getString(R.string.hour_ago));
            else
                tvTime.setText(diffInHours+context.getResources().getString(R.string.hours_ago));
        }else if((elapsedDays>0 && elapsedSeconds==0 && elapsedMinutes==0 && diffInHours==0) || (elapsedMinutes>0 && diffInHours>0 && elapsedDays>0)) {
            if(elapsedDays==1)
                tvTime.setText(elapsedDays+context.getResources().getString(R.string.day_ago));
            else
                tvTime.setText(elapsedDays+context.getResources().getString(R.string.days_ago));
        }else {
            tvTime.setText(R.string.small_now);
        }

//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;

//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);


    }
}
