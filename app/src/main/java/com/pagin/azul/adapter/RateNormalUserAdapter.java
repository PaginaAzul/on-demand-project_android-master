package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateNormalUserAdapter extends RecyclerView.Adapter<RateNormalUserAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> normalUserList;

    public RateNormalUserAdapter(Context context, ArrayList<NormalUserPendingOrderInner> normalUserList) {
        this.context = context;
        this.normalUserList = normalUserList;
    }

    @NonNull
    @Override
    public RateNormalUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_my_rate, null, false);


        return new RateNormalUserAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateNormalUserAdapter.MyViewHolder holder, int position) {


        holder.tvUserName.setText(normalUserList.get(position).getRatingByName());
        holder.rat_cmmt.setText(normalUserList.get(position).getComments());
        String rating = String.valueOf(normalUserList.get(position).getRate());
        holder.rb_ratting.setRating(Float.parseFloat(rating));

    }

    @Override
    public int getItemCount() {
        return normalUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.rb_ratting)
        RatingBar rb_ratting;
        @BindView(R.id.rat_cmmt)
        TextView rat_cmmt;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
