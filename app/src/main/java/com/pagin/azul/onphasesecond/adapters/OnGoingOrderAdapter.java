package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class OnGoingOrderAdapter extends RecyclerView.Adapter<OnGoingOrderAdapter.OnGoingViewHolder> {

    private Context context;
    private ArrayList<MyOrderResponse> data;
    private CommonListener commonListener;

    public OnGoingOrderAdapter(Context context, ArrayList<MyOrderResponse> data, CommonListener commonListener) {
        this.context = context;
        this.data = data;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ongoing_items,parent,false);
        return new OnGoingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingViewHolder holder, int position) {
        MyOrderResponse sellerData = data.get(position).getSellerData();
        String image = sellerData.getImage();
        if(image!=null && !image.equals("")){
            setImage(holder.progressBarOrder,image,holder.ivOrderLogo);
        }else {
            holder.progressBarOrder.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(holder.ivOrderLogo);
        }
        String restaurantName = sellerData.getName();
        holder.tvRestaurantName.setText(restaurantName);
        holder.tvOrderId.setText(context.getResources().getString(R.string.my_order_id)+data.get(position).getOrderNumber());
        holder.tvDateAndTime.setText(CommonUtilities.getMyOrdersOutputFormat(data.get(position).getCreatedAt()));

        holder.recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerMyOrders.setAdapter(new MyOrderAdapter(context,data.get(position).getOrderData()));

        holder.tvTotalPriceMyOrder.setText(context.getResources().getString(R.string.total_price)+CommonUtilities.getPriceFormat(data.get(position).getTotalPrice())+" Kz");
        holder.tvAddressMyOrders.setText(data.get(position).getAddress());
        holder.pickUpRestroName.setText(restaurantName);
        if(data.get(position).getOrderType().equalsIgnoreCase("Product"))
            holder.tvExpectedTime.setText(data.get(position).getDeliveryTimeSlot());
        else
            holder.tvExpectedTime.setText(data.get(position).getExcepetdDeliveryTime()+context.getResources().getString(R.string.mins));
        //holder.tvExpectedTime.setText(data.get(position).getExcepetdDeliveryTime()+" mins");
        MyOrderResponse driverData = data.get(position).getDriverData();
        holder.tvDeliveryPerson.setText(driverData.getName());
        holder.tvPhoneNumber.setText(driverData.getCountryCode()+driverData.getMobileNumber());

        String status = data.get(position).getStatus();
        switch (status){
            case "Accept":
            case "In process":
                orderStatus(holder, "In the Kitchen");
                break;
            case "Out for delivery":
                orderStatus(holder, "Out for Delivery");
                break;
        }

        /*if (!data.get(position).getCancelStatus()) {
            holder.tvTimeOrder.setVisibility(View.VISIBLE);
            holder.tvCancelOrder.setVisibility(View.VISIBLE);
            try {
                new CountDownTimer(300000, 1000) {
                    @Override
                    public void onFinish() {
                    *//*holder.tvTimeOrder.setVisibility(View.GONE);
                    holder.tvCancelOrder.setVisibility(View.GONE);*//*
                        commonListener.onTimerFinish(position,data);
                    }
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //int hrs = (int) TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                        int min = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                        //holder.tvTimeOrder.setText(min + ":" + sec);
                        holder.tvTimeOrder.setText(String.format(Locale.US, "%02d:%02d", min, sec));
                    }
                }.start();
            } catch (Exception e) {
                System.out.println("NumberFormatException: " + e.getMessage());
            }
        }else {
            holder.tvTimeOrder.setVisibility(View.GONE);
            holder.tvCancelOrder.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    public class OnGoingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_myOrders)
        ConstraintLayout cl_myOrders;

        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;

        @BindView(R.id.ivDropDownOrder)
        ImageView ivDropDownOrder;

        @BindView(R.id.recyclerMyOrders)
        RecyclerView recyclerMyOrders;

        @BindView(R.id.stepCounter)
        StepperIndicator stepCounter;

        @BindView(R.id.ivOrderLogo)
        ImageView ivOrderLogo;

        @BindView(R.id.progressBarOrder)
        ProgressBar progressBarOrder;

        @BindView(R.id.tvRestaurantName)
        TextView tvRestaurantName;

        @BindView(R.id.tvOrderId)
        TextView tvOrderId;

        @BindView(R.id.tvTotalPriceMyOrder)
        TextView tvTotalPriceMyOrder;

        @BindView(R.id.tvAddressMyOrders)
        TextView tvAddressMyOrders;

        @BindView(R.id.tvRestaurantNameBelow)
        TextView pickUpRestroName;

        @BindView(R.id.tvDateAndTime)
        TextView tvDateAndTime;

        @BindView(R.id.tvTimeOrder)
        TextView tvTimeOrder;

        @BindView(R.id.tvCancelOrder)
        TextView tvCancelOrder;

        @BindView(R.id.tvExpectedTime)
        TextView tvExpectedTime;

        @BindView(R.id.tvDeliveryPerson)
        TextView tvDeliveryPerson;

        @BindView(R.id.tvPhoneNumber)
        TextView tvPhoneNumber;

        public OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.mainCl,R.id.tvCancelOrder,R.id.tvAddressMyOrders,R.id.edStatus})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.mainCl:
                    if(cl_myOrders.getVisibility()==View.GONE){
                        ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
                        constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
                        cl_myOrders.setVisibility(View.VISIBLE);
                    }else {
                        ivDropDownOrder.setImageResource(R.drawable.drop_down_icon);
                        constraintLayout.setBackground(context.getDrawable(R.drawable.bg_collapse));
                        cl_myOrders.setVisibility(View.GONE);
                    }
                    break;

                case R.id.tvCancelOrder:
                    commonListener.onCancelClick(getAdapterPosition(),data);
                    break;

                case R.id.tvAddressMyOrders:
                    CommonUtilities.dispatchGoogleMap(context,data.get(getAdapterPosition()).getLatitude(),
                            data.get(getAdapterPosition()).getLongitude());
                    break;
            }
        }
    }

    private void orderStatus(OnGoingViewHolder viewHolder,String status){
        CharSequence[] labelArray=new String[4];
        labelArray[0] = context.getResources().getString(R.string.confirmed);
        labelArray[1] = context.getResources().getString(R.string.in_the_kitchen);
        labelArray[2] = context.getResources().getString(R.string.out_for_delivery);
        labelArray[3] = context.getResources().getString(R.string.delivered);
        viewHolder.stepCounter.setLabels(labelArray);
        switch (status) {
            case "Confirmed": viewHolder.stepCounter.setCurrentStep(1); break;
            case "In the Kitchen": viewHolder.stepCounter.setCurrentStep(2); break;
            case "Out for Delivery": viewHolder.stepCounter.setCurrentStep(3); break;
            case "Delivered": viewHolder.stepCounter.setCurrentStep(4); break;
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

    public void update(ArrayList<MyOrderResponse> data){
        this.data = data;
        notifyDataSetChanged();
    }
}
