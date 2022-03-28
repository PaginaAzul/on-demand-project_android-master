package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDeliveryWorkerDashboardAdapter extends RecyclerView.Adapter<NewDeliveryWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> newList;
    private OnClickReview onClickReview;

    public NewDeliveryWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> newList, OnClickReview onClickReview) {
        this.context = context;
        this.newList = newList;
        this.onClickReview = onClickReview;
    }

    @NonNull
    @Override
    public NewDeliveryWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_new_dashboard_delivery_work, viewGroup, false);

        return new NewDeliveryWorkerDashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewDeliveryWorkerDashboardAdapter.MyViewHolder holder, int position) {

        String getDate = newList.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here
            holder.tv_date.setText(String.valueOf(splitted[0]));
            holder.tv_time.setText(String.valueOf(splitted[1]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }

        holder.mobile.setText(newList.get(position).getMobileNumber());
        holder.tvProfileName.setText(newList.get(position).getName());
        holder.order_no.setText(newList.get(position).getOrderNumber());
        holder.drop_dis.setText(newList.get(position).getCurrentToPicupLocation() + "km");
        holder.tv_dropOff.setText(newList.get(position).getPickupToDropLocation() + "km");
        holder.tv_order_details.setText(" - "+newList.get(position).getOrderDetails());
        holder.tv_pick_loc.setText(newList.get(position).getPickupLocation());
        holder.tv_dropoff_loc.setText( newList.get(position).getDropOffLocation());
        holder.tv_order_tym.setText(" Require " + newList.get(position).getSeletTime());
        holder.tv_view_comments.setText(newList.get(position).getTotalRating() + " Ratings View all");
        holder.tv_rat_num.setText(newList.get(position).getAvgRating());
        if (!newList.get(position).getProfilePic().equalsIgnoreCase("")) {

            Glide.with(context)
                    .load(newList.get(position).getProfilePic())
                    .apply(new RequestOptions().placeholder(R.drawable.default_p).override(200, 200))
                    .into(holder.user_pic);
        }
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.rl_mainRating)
        RelativeLayout rl_mainRating;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.mobile)
        TextView mobile;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.tv_dropOff)
        TextView tv_dropOff;
        @BindView(R.id.rl_makeOffer)
        RelativeLayout rl_makeOffer;
        @BindView(R.id.starttopickup)
        TextView drop_dis;
        @BindView(R.id.tv_order_details)
        TextView tv_order_details;
        @BindView(R.id.order_des)
        TextView order_des;
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.tv_dropoff_loc)
        TextView tv_dropoff_loc;
        @BindView(R.id.tv_pick_loc)
        TextView tv_pick_loc;
        @BindView(R.id.tv_order_tym)
        TextView tv_order_tym;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.user_pic)
        ImageView user_pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rl_mainRating.setOnClickListener(this);
            rl_makeOffer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_mainRating:
                    onClickReview.onClickReview(newList.get(getAdapterPosition()));
                    break;
                case R.id.rl_makeOffer:
                    onClickReview.onClickMakeOffer(newList.get(getAdapterPosition()));
                    break;
            }
        }
    }

    public interface OnClickReview {
        void onClickReview(NormalUserPendingOrderInner data);

        void onClickMakeOffer(NormalUserPendingOrderInner offerdata);
    }
}
