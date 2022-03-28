package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.activities.RatingAndRiviewActivity;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.activity.MyCartActivity;
import com.pagin.azul.onphasesecond.activity.RatingAndReviewActivity;
import com.pagin.azul.onphasesecond.activity.ShareReviwsActivity;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class PreviousAdapter extends RecyclerView.Adapter<PreviousAdapter.PreviousViewHolder> {

    private Context context;
    private ArrayList<MyOrderResponse> data;
    private CommonListener commonListener;

    public PreviousAdapter(Context context, ArrayList<MyOrderResponse> data, CommonListener commonListener) {
        this.context = context;
        this.data = data;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public PreviousViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_previous_adapter, parent, false);
        return new PreviousViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousViewHolder holder, int position) {
        MyOrderResponse sellerData = data.get(position).getSellerData();
        String image = sellerData.getImage();
        if(image!=null && !image.equals("")){
            setImage(holder.pbPast,image,holder.ivPreviousOrder);
        }else {
            holder.pbPast.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(holder.ivPreviousOrder);
        }
        String restaurantName = sellerData.getName();
        holder.tvPreviousOrderName.setText(restaurantName);
        holder.tvOrderIdPrevious.setText(context.getResources().getString(R.string.my_order_id)+data.get(position).getOrderNumber());
        holder.tvDatePrevious.setText(CommonUtilities.getMyOrdersOutputFormat(data.get(position).getCreatedAt()));
        holder.tvTotalPriceMyOrder.setText(context.getResources().getString(R.string.total_price)+CommonUtilities.getPriceFormat(data.get(position).getTotalPrice())+" Kz");
        holder.tvAddressMyOrders.setText(data.get(position).getAddress());
        holder.tvRestaurantNameBelow.setText(restaurantName);
        if(data.get(position).getOrderType().equalsIgnoreCase("Product"))
            holder.tvDeliverytime.setText(data.get(position).getDeliveryTimeSlot());
        else
            holder.tvDeliverytime.setText(data.get(position).getExcepetdDeliveryTime()+context.getResources().getString(R.string.mins));
        //holder.tvDeliverytime.setText(data.get(position).getExcepetdDeliveryTime()+" mins");

        ArrayList<MyOrderResponse> orderList = data.get(position).getOrderData();
        if(orderList!=null && !orderList.isEmpty()){
            holder.rvPreviousOrder.setLayoutManager(new LinearLayoutManager(context));
            holder.rvPreviousOrder.setAdapter(new MyOrderAdapter(context,orderList,true));
            if(orderList.size()<=2){
                holder.tvViewMore.setVisibility(GONE);
            }else {
                holder.tvViewMore.setVisibility(View.VISIBLE);
            }
        }else {
            holder.tvViewMore.setVisibility(GONE);
        }

        /*holder.rvPreviousOrder.setLayoutManager(new LinearLayoutManager(context));
        holder.rvPreviousOrder.setAdapter(new MyOrderAdapter(context,data.get(position).getOrderData()));*/
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    public class PreviousViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cl_previous)
        ConstraintLayout cl_previous;

        @BindView(R.id.ivPreviousDown)
        ImageView ivPreviousDown;

        @BindView(R.id.ivPreviousOrder)
        ImageView ivPreviousOrder;

        @BindView(R.id.rvPreviousOrder)
        RecyclerView rvPreviousOrder;

        @BindView(R.id.pbPast)
        ProgressBar pbPast;

        @BindView(R.id.tvPreviousOrderName)
        TextView tvPreviousOrderName;

        @BindView(R.id.tvOrderIdPrevious)
        TextView tvOrderIdPrevious;

        @BindView(R.id.tvDatePrevious)
        TextView tvDatePrevious;

        @BindView(R.id.tvTotalPriceMyOrder)
        TextView tvTotalPriceMyOrder;

        @BindView(R.id.tvAddressMyOrders)
        TextView tvAddressMyOrders;

        @BindView(R.id.tvRestaurantNameBelow)
        TextView tvRestaurantNameBelow;

        @BindView(R.id.tvDeliverytime)
        TextView tvDeliverytime;

        @BindView(R.id.tvViewMore)
        TextView tvViewMore;

        private boolean isExpanded;

        public PreviousViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.mainCl, R.id.tvRateOrder, R.id.cl_reorderPrevious, R.id.tvViewMore})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mainCl:
                    if (cl_previous.getVisibility() == View.GONE) {
                        ivPreviousDown.setImageResource(R.drawable.drop_down_ic);
                        cl_previous.setVisibility(View.VISIBLE);
                    } else {
                        ivPreviousDown.setImageResource(R.drawable.drop_down_icon);
                        cl_previous.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tvRateOrder:
                {
                    if(data.get(getAdapterPosition()).getRatingData()==null){ // if ratingData equal to null then enable rate user text
                        Intent intent = new Intent(context, RatingAndRiviewActivity.class);
                        intent.putExtra("kComming",PreviousAdapter.class.getSimpleName());
                        intent.putExtra(ParamEnum.ID.theValue(),data.get(getAdapterPosition()).getResAndStoreId());
                        intent.putExtra(ParamEnum.ORDER_ID.theValue(),data.get(getAdapterPosition()).get_id());
                        context.startActivity(intent);
                    }
                }
                    break;
                case R.id.cl_reorderPrevious:
                    commonListener.onReorderClick(getAdapterPosition(),data);
                    break;
                case R.id.tvViewMore:
                    if(isExpanded){
                        rvPreviousOrder.setLayoutManager(new LinearLayoutManager(context));
                        rvPreviousOrder.setAdapter(new MyOrderAdapter(context,data.get(getAdapterPosition()).getOrderData(),true));
                        isExpanded = false;
                    }else {
                        rvPreviousOrder.setLayoutManager(new LinearLayoutManager(context));
                        rvPreviousOrder.setAdapter(new MyOrderAdapter(context,data.get(getAdapterPosition()).getOrderData(),false));
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
}
