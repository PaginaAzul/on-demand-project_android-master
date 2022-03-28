package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.getOutputFormats;

public class OnUpcomingAdapter extends RecyclerView.Adapter<OnUpcomingAdapter.OnUpcomingViewHolder> {

    private Context context;
    private ArrayList<MyOrderResponse> data;
    private CommonListener commonListener;

    public OnUpcomingAdapter(Context context, ArrayList<MyOrderResponse> data,CommonListener commonListener) {
        this.context = context;
        this.data = data;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public OnUpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_upcoming_item, parent, false);
        return new OnUpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnUpcomingViewHolder holder, int position) {
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
        String createdAt = CommonUtilities.getMyOrdersOutputFormat(data.get(position).getCreatedAt());
        holder.tvDateAndTime.setText(createdAt);

        ArrayList<MyOrderResponse> orderList = data.get(position).getOrderData();
        if(orderList!=null && !orderList.isEmpty()){
            holder.recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerMyOrders.setAdapter(new MyOrderAdapter(context,orderList,true));
            if(orderList.size()<=2){
                holder.tvViewMore.setVisibility(GONE);
            }else {
                holder.tvViewMore.setVisibility(View.VISIBLE);
            }
        }else {
            holder.tvViewMore.setVisibility(GONE);
        }
        /*holder.recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerMyOrders.setAdapter(new MyOrderAdapter(context,data.get(position).getOrderData());*/

        holder.tvTotalPriceMyOrder.setText(context.getResources().getString(R.string.total_price)+CommonUtilities.getPriceFormat(data.get(position).getTotalPrice())+" Kz");
        holder.tvAddressMyOrders.setText(data.get(position).getAddress());
        holder.pickUpRestroName.setText(restaurantName);

        if (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
        {
            holder.tvpickStatus.setText(data.get(position).getStatus());

        }
        else
        {
            if(data.get(position).getStatus().equals("Pending"))
            {
                holder.tvpickStatus.setText("Pendente");

            }
            else if(data.get(position).getStatus().equals("Confirmed"))
            {
                holder.tvpickStatus.setText("Confirmado");

            }
            else if(data.get(position).getStatus().equals("Out for Delivery"))
            {
                holder.tvpickStatus.setText("A Caminho");

            }
            else if(data.get(position).getStatus().equals("Delivered"))
            {
                holder.tvpickStatus.setText("Entregue");

            }
            else if(data.get(position).getStatus().equals("Cancelled"))
            {
                holder.tvpickStatus.setText("Cancelado");

            }
            else
            {
                holder.tvpickStatus.setText(data.get(position).getStatus());

            }

        }
        holder.tvpickupDate.setText(data.get(position).getDeliveryDate());
        if(data.get(position).getOrderType().equalsIgnoreCase("Product"))
            holder.tvPickUpTime.setText(data.get(position).getDeliveryTimeSlot());
        else
            holder.tvPickUpTime.setText(data.get(position).getExcepetdDeliveryTime()+context.getResources().getString(R.string.mins));

        /*get millisecond time between current
        date and order created
        date for showing timer */
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.ENGLISH);
        long timeInMillis = 0;
        try {
            Date orderTime = sdf.parse(createdAt);
            Date now = new Date();
            timeInMillis = now.getTime() - orderTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // check if time difference is 5 minutes and cancel status false then show time otherwise hide
        if (timeInMillis<=300000 && !data.get(position).getCancelStatus()) {
            holder.tvTimeOrder.setVisibility(View.VISIBLE);
            holder.tvCancelOrder.setVisibility(View.VISIBLE);
            try {
                if (holder.timer != null) {
                    holder.timer.cancel();
                }
                holder.timer = new CountDownTimer(300000 - timeInMillis, 1000) {
                    @Override
                    public void onFinish() {
                    /*holder.tvTimeOrder.setVisibility(View.GONE);
                    holder.tvCancelOrder.setVisibility(View.GONE);*/
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
        }

    }

    @Override
    public int getItemCount() {
        return  data!=null?data.size():0;
    }

    public class OnUpcomingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_myOrders)
        ConstraintLayout cl_myOrders;

        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;

        @BindView(R.id.ivDropDownOrder)
        ImageView ivDropDownOrder;

        @BindView(R.id.ivOrderLogo)
        ImageView ivOrderLogo;

        @BindView(R.id.recyclerMyOrders)
        RecyclerView recyclerMyOrders;

        @BindView(R.id.pickUpLayout)
        View pickUpLayout;

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

        @BindView(R.id.pickUpRestroName)
        TextView pickUpRestroName;

        @BindView(R.id.tvpickStatus)
        TextView tvpickStatus;

        @BindView(R.id.tvDateAndTime)
        TextView tvDateAndTime;

        @BindView(R.id.tvpickupDate)
        TextView tvpickupDate;

        @BindView(R.id.tvPickUpTime)
        TextView tvPickUpTime;

        @BindView(R.id.tvTimeOrder)
        TextView tvTimeOrder;

        @BindView(R.id.tvCancelOrder)
        TextView tvCancelOrder;

        @BindView(R.id.tvViewMore)
        TextView tvViewMore;

        private boolean isExpanded;
        private CountDownTimer timer;

        public OnUpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            pickUpLayout.setVisibility(View.VISIBLE);
        }

        @OnClick({R.id.ivDropDownOrder, R.id.tvCancelOrder,R.id.tvAddressMyOrders,R.id.tvViewMore})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivDropDownOrder:
                    if (cl_myOrders.getVisibility() == View.GONE) {
                        ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
                        constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
                        cl_myOrders.setVisibility(View.VISIBLE);
                    } else {
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

                case R.id.tvViewMore:
                    if(isExpanded){
                        recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
                        recyclerMyOrders.setAdapter(new MyOrderAdapter(context,data.get(getAdapterPosition()).getOrderData(),true));
                        isExpanded = false;
                    }else {
                        recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
                        recyclerMyOrders.setAdapter(new MyOrderAdapter(context,data.get(getAdapterPosition()).getOrderData(),false));
                        isExpanded = true;
                    }

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

    public void update(ArrayList<MyOrderResponse> data){
        this.data = data;
        notifyDataSetChanged();
    }

}
