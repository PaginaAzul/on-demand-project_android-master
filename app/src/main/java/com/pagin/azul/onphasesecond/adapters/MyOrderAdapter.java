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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.UpcomingMenuViewHolder> {

    private Context context;
    private ArrayList<MyOrderResponse> orderList;
    private boolean isCollapsed;

    public MyOrderAdapter(Context context,ArrayList<MyOrderResponse> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public MyOrderAdapter(Context context,ArrayList<MyOrderResponse> orderList,boolean isCollapsed) {
        this.context = context;
        this.orderList = orderList;
        this.isCollapsed = isCollapsed;
    }

    @NonNull
    @Override
    public UpcomingMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_upcoming_menu_item,parent,false);
        return new UpcomingMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMenuViewHolder holder, int position) {
        MyOrderResponse productData = orderList.get(position).getProductData();
        String image = productData.getProductImage();
        if(image!=null && !image.equals("")){
            setImage(holder.progressbar,image,holder.ivImage);
        }else {
            holder.progressbar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.food_thali).override(110, 150).into(holder.ivImage);
        }
        Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show();
        holder.tvName.setText(productData.getProductName());
        holder.tvDes.setText(productData.getDescription());
        String currency = productData.getCurrency();
        String price = CommonUtilities.getPriceFormat(productData.getPrice());
        String offerPrice = productData.getOfferPrice();
        if(offerPrice!=null) {
            String formatOfferPrice = CommonUtilities.getPriceFormat(offerPrice);
            holder.tvPrice.setText(formatOfferPrice+" "+currency);
            holder.tvQuantityPrice.setText(orderList.get(position).getQuantity()+" x "+formatOfferPrice+" "+currency);
        }
        else {
            holder.tvPrice.setText(price+" "+currency);
            holder.tvQuantityPrice.setText(orderList.get(position).getQuantity()+" x "+price+" "+currency);
        }
        String productType = productData.getProductType();
        if(productType!=null){
            holder.IvDishType.setVisibility(View.VISIBLE);
            if(productType.equalsIgnoreCase(ParamEnum.VEG.theValue()))
                holder.IvDishType.setImageResource(R.drawable.veg);
            else
                holder.IvDishType.setImageResource(R.drawable.nonveg);
        }else {
            holder.IvDishType.setVisibility(GONE);
        }

    }

    @Override
    public int getItemCount() {
        if(isCollapsed){
            return Math.min(orderList.size(), 2);
        }else {
            return orderList.size();
        }
    }

    public class UpcomingMenuViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.progressbar)
        ProgressBar progressbar;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvQuantityPrice)
        TextView tvQuantityPrice;
        @BindView(R.id.IvDishType)
        ImageView IvDishType;
        public UpcomingMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
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
