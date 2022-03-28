package com.pagin.azul.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.ProviderDetailsAct;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pagin.azul.Constant.Constants.kProfile;

public class ViewAllOfferAdapter extends RecyclerView.Adapter<ViewAllOfferAdapter.MyWalletHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> viewOfferList;
    private AcceptOfferInterface listener;

    public ViewAllOfferAdapter(Context context, ArrayList<NormalUserPendingOrderInner> viewOfferList) {
        this.context = context;
        this.viewOfferList = viewOfferList;
    }

    @NonNull
    @Override
    public ViewAllOfferAdapter.MyWalletHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_view_all_offers, viewGroup, false);

        return new ViewAllOfferAdapter.MyWalletHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllOfferAdapter.MyWalletHolder holder, int position) {

        if (viewOfferList.get(position).getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
            holder.tvProfileName.setText(viewOfferList.get(position).getOfferMakeByName());
            String currency = viewOfferList.get(position).getCurrency()!=null?viewOfferList.get(position).getCurrency():"";
            holder.tv_dchargs.setText(viewOfferList.get(position).getMinimumOffer() + " "+currency+" "+context.getString(R.string.only));
            holder.tv_msg.setText(viewOfferList.get(position).getMessage());
            holder.tv_dTime.setText(viewOfferList.get(position).getApprxTime());
            holder.tv_distance.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
            holder.drop_dis.setText(viewOfferList.get(position).getCurrentToPicupLocation() + "km");
            holder.tvRatingViewAllOffers.setText(viewOfferList.get(position).getTotalRating() +" "+context.getString(R.string.ratings_view_all));
            holder.tv_rat_num.setText(viewOfferList.get(position).getAvgRating());
            //holder.tvModeOfTransport.setText(viewOfferList.get(position).getTransportMode());
            String transportMode = viewOfferList.get(position).getTransportMode();
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                if(transportMode.equalsIgnoreCase("Carro próprio"))
                    holder.tvModeOfTransport.setText("Own Vehicle");
                else if(transportMode.equalsIgnoreCase("Carro da empresa"))
                    holder.tvModeOfTransport.setText("Company Vehicle");
                else if(transportMode.equalsIgnoreCase("Sem transporte"))
                    holder.tvModeOfTransport.setText("No Vehicle");
                else
                    holder.tvModeOfTransport.setText(transportMode);
            }else{
                if(transportMode.equalsIgnoreCase("Own Vehicle"))
                    holder.tvModeOfTransport.setText("Carro próprio");
                else if(transportMode.equalsIgnoreCase("Company Vehicle"))
                    holder.tvModeOfTransport.setText("Carro da empresa");
                else if(transportMode.equalsIgnoreCase("No Vehicle"))
                    holder.tvModeOfTransport.setText("Sem transporte");
                else
                    holder.tvModeOfTransport.setText(transportMode);
                //holder.msg.setText(context.getString(R.string.require)+" "+activeList.get(position).getSeletTime());
            }
            holder.delivery_charge.setText(context.getResources().getString(R.string.charge_offer));
            holder.del_msg.setText(context.getResources().getString(R.string.msg));
            holder.del_time.setText(context.getResources().getString(R.string.order_time_2));
            holder.tv_pickup.setVisibility(View.GONE);
            //holder.drop_dis.setVisibility(View.GONE);
            //holder.view3.setVisibility(View.GONE);
            holder.tv_point3.setVisibility(View.GONE);
            holder.prf_txt.setText(context.getResources().getString(R.string.prof_worker));
            holder.service_loc.setText(context.getResources().getString(R.string.service_loc));
            Glide.with(context)
                    .load(viewOfferList.get(position).getOfferAcceptedByProfilePic())
                    .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                            .error(R.drawable.profile_default))
                    .into(holder.ivProviderPic);

        } else if (viewOfferList.get(position).getServiceType().equalsIgnoreCase("DeliveryPersion")) {
            holder.tvProfileName.setText(viewOfferList.get(position).getOfferMakeByName());
            holder.tv_dchargs.setText(viewOfferList.get(position).getMinimumOffer() + " Only");
            holder.tv_msg.setText(viewOfferList.get(position).getMessage());
            holder.tv_dTime.setText(viewOfferList.get(position).getApprxTime());
            holder.tv_distance.setText(viewOfferList.get(position).getCurrentToPicupLocation() + "km");
            holder.drop_dis.setText(viewOfferList.get(position).getPickupToDropLocation() + "km");
            holder.tvRatingViewAllOffers.setText(viewOfferList.get(position).getTotalRating() + " Ratings View all");
            holder.tv_rat_num.setText(viewOfferList.get(position).getAvgRating());
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

        void onAccceptOfferClick(View v, int pos, String id,String realOrderId);
        void onRejectOfferClick(View v, int pos, String id);


    }


    public class MyWalletHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_point3)
        ImageView tv_point3;
        @BindView(R.id.view3)
        View view3;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;

        @BindView(R.id.service_loc)
        TextView service_loc;
         @BindView(R.id.del_msg)
        TextView del_msg;
         @BindView(R.id.prf_txt)
        TextView prf_txt;

        @BindView(R.id.del_time)
        TextView del_time;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.btn_accept_offer)
        Button btnAccept;
        @BindView(R.id.tv_dchargs)
        TextView tv_dchargs;
        @BindView(R.id.tv_dTime)
        TextView tv_dTime;
        @BindView(R.id.tv_msg)
        TextView tv_msg;
        @BindView(R.id.starttopickup)
        TextView drop_dis;
        @BindView(R.id.tv_distance)
        TextView tv_distance;
        @BindView(R.id.rl_mainRate)
        RelativeLayout rl_mainRate;
        @BindView(R.id.tvRatingViewAllOffers)
        TextView tvRatingViewAllOffers;
        @BindView(R.id.delivery_charge)
        TextView delivery_charge;
        @BindView(R.id.tv_pickup)
        TextView tv_pickup;
        @BindView(R.id.tvModeOfTransport)
        TextView tvModeOfTransport;
        @BindView(R.id.tvViewProfile)
        TextView tvViewProfile;
        @BindView(R.id.view2)
        View view2;
        @BindView(R.id.ivProviderPic)
        ImageView ivProviderPic;

        @BindView(R.id.btn_reject)
        Button btn_reject;


        public MyWalletHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnAccept.setOnClickListener(this);
            rl_mainRate.setOnClickListener(this);
            btn_reject.setOnClickListener(this);
            tvViewProfile.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_accept_offer:
                    if (listener != null) {
                        listener.onAccceptOfferClick(view, getAdapterPosition(), viewOfferList.get(getAdapterPosition()).get_id(),viewOfferList.get(getAdapterPosition()).getRealOrderId());
                    }
                    break;
                case R.id.btn_reject:
                    if (listener != null) {
                        listener.onRejectOfferClick(view, getAdapterPosition(), viewOfferList.get(getAdapterPosition()).get_id());
                    }
                    break;
                case R.id.rl_mainRate:
                    context.startActivity(UserDetailsActivity.getIntent(context, viewOfferList.get(getAdapterPosition()), "FromViewOffer"));
                    //context.startActivity(new Intent(context, UserDetailsActivity.class));
                    break;
                case R.id.tvViewProfile:
                    Intent intent = new Intent(context, ProviderDetailsAct.class);
                    intent.putExtra(Constants.kData,viewOfferList.get(getAdapterPosition()));
                    context.startActivity(intent);
                    break;
            }
        }
    }


}
