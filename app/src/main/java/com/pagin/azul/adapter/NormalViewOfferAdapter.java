package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NormalViewOfferAdapter extends RecyclerView.Adapter<NormalViewOfferAdapter.MyWalletHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> viewOfferList;
    private AcceptOfferInterface listener;

    public NormalViewOfferAdapter(Context context, ArrayList<NormalUserPendingOrderInner> viewOfferList) {
        this.context = context;
        this.viewOfferList = viewOfferList;
    }

    @NonNull
    @Override
    public NormalViewOfferAdapter.MyWalletHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_normal_view_offer, viewGroup, false);

        return new NormalViewOfferAdapter.MyWalletHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NormalViewOfferAdapter.MyWalletHolder holder, int position) {

        holder.tv_profile_name.setText(viewOfferList.get(position).getOfferAcceptedByName());
        holder.offer_amt.setText(viewOfferList.get(position).getMinimumOffer()+" SAR");
        holder.msg.setText(viewOfferList.get(position).getMessage());
        holder.workertopickup.setText(viewOfferList.get(position).getCurrentToPicupLocation());
        holder.pickuptodrop.setText(viewOfferList.get(0).getPickupToDropLocation());
        holder.dilevery_tym.setText(viewOfferList.get(0).getApprxTime());
        Glide.with(context).load(viewOfferList.get(position).getOfferAcceptedByProfilePic()).into(holder.user_pic);

        holder.tv_view_comments.setText("("+viewOfferList.get(position).getTotalRating() + " Ratings View all)");
        holder.tv_rat_num.setText(viewOfferList.get(position).getAvgRating());


        if (viewOfferList.get(position).getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
            holder.workertopickup.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
            holder.imageView10.setVisibility(View.GONE);
            holder.droptodelivered4.setVisibility(View.GONE);
            holder.droptodelivered3.setText(context.getString(R.string.prof_worker));
            holder.droptodelivered5.setText(context.getString(R.string.service_loc));
            holder.pickuptodrop.setText(viewOfferList.get(0).getCurrentToPicupLocation()+" KM");



        }

    }

    @Override
    public int getItemCount() {
        return viewOfferList.size();
    }


    public void setListener(AcceptOfferInterface listener) {
        this.listener = listener;
    }

    public interface AcceptOfferInterface {

        void onAccceptOfferClick(View v, int pos, String id);

        void onRejectOfferClick(View v, int pos, String id);


    }


    public class MyWalletHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_pic)
        ImageView user_pic;
        @BindView(R.id.imageView10)
        ImageView imageView10;
        @BindView(R.id.tv_profile_name)
        TextView tv_profile_name;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.offer_amt)
        TextView offer_amt;
        @BindView(R.id.msg)
        TextView msg;
        @BindView(R.id.dilevery_tym)
        TextView dilevery_tym;
        @BindView(R.id.tv_msg)
        TextView tv_msg;
        @BindView(R.id.workertopickup)
        TextView workertopickup;
         @BindView(R.id.droptodelivered5)
        TextView droptodelivered5;

        @BindView(R.id.droptodelivered4)
        TextView droptodelivered4;
        @BindView(R.id.pickuptodrop)
        TextView pickuptodrop;
        @BindView(R.id.droptodelivered3)
        TextView droptodelivered3;
        @BindView(R.id.btn_reject)
        Button btn_reject;
        @BindView(R.id.btn_accept)
        Button btn_accept;
        @BindView(R.id.view2)
        View view2;


        public MyWalletHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btn_accept.setOnClickListener(this);
            btn_reject.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_accept:
                    if (listener != null) {
                        listener.onAccceptOfferClick(view, getAdapterPosition(), viewOfferList.get(getAdapterPosition()).get_id());
                    }
                    break;
                case R.id.btn_reject:
                    if (listener != null) {
                        listener.onRejectOfferClick(view, getAdapterPosition(), viewOfferList.get(getAdapterPosition()).get_id());
                    }
                    break;
            }
        }
    }

}
