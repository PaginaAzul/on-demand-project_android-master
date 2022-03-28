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

public class ActiveDeliveryWorkerDashboardAdapter extends RecyclerView.Adapter<ActiveDeliveryWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> activeList;
    private OnClickContentListener onClickContentListener;
    private String mobile;

    public ActiveDeliveryWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> activeList, OnClickContentListener onClickContentListener) {
        this.context = context;
        this.activeList = activeList;
        this.onClickContentListener = onClickContentListener;
    }

    @NonNull
    @Override
    public ActiveDeliveryWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_active_dashboard_delivery_worker, null, false);

        return new ActiveDeliveryWorkerDashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveDeliveryWorkerDashboardAdapter.MyViewHolder holder, int position) {


        String getDate = activeList.get(position).getCreatedAt();
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
        mobile = activeList.get(position).getOfferAcceptedByMobileNumber();
        holder.pickLoc.setText(activeList.get(position).getPickupLocation());
        holder.dropLoc.setText(activeList.get(position).getDropOffLocation());
        holder.tvProfileName.setText(activeList.get(position).getOfferAcceptedByName());
        holder.order_id.setText(activeList.get(position).getOrderNumber());
        holder.starttopickup.setText(activeList.get(position).getCurrentToPicupLocation() + "km");
        holder.pickuptodrop.setText(activeList.get(position).getPickupToDropLocation() + "km");
        holder.droptodelivered.setText("0" + "km");
       // holder.offer_amt.setText(activeList.get(position).getMinimumOffer() + " only");
        holder.tvMsg.setText(activeList.get(position).getMessage());
        //holder.dilevery_tym.setText(activeList.get(position).getApprxTime());
        //holder.tv_order_details.setText("Order Details: Require " + activeList.get(position).getSeletTime());
        holder.mobile_no.setText(activeList.get(position).getOfferAcceptedByCountryCode() + activeList.get(position).getOfferAcceptedByMobileNumber());
        //holder.item_detail.setText(activeList.get(position).getOrderDetails());
        holder.delivery_offer.setText(activeList.get(position).getDeliveryOffer() + " SAR");
        holder.tax.setText(activeList.get(position).getTax() + " SAR");
        holder.total.setText(activeList.get(position).getTotal() + " SAR");
        holder.tv_view_comments.setText(activeList.get(position).getTotalRating() + " Ratings View all");
        holder.tv_rat_num.setText(activeList.get(position).getAvgRating());
        if (!activeList.get(position).getOfferAcceptedByProfilePic().equalsIgnoreCase("")) {
            Glide.with(context).load(activeList.get(position).getOfferAcceptedByProfilePic()).apply(new RequestOptions().placeholder(R.drawable.default_p).override(200, 200)).into(holder.user_pic);
        }

//Showing Invoice Data&Time......
        String getInvoiceDate = activeList.get(position).getInvoiceCreatedAt();
        String server_formatI = getInvoiceDate;    //server comes format ?
        String server_format11 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_formatI);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here
            holder.invoice_date.setText(String.valueOf(splitted[0]));
            holder.invoice_time.setText(String.valueOf(splitted[1]));
        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
    }

    @Override
    public int getItemCount() {
        return activeList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.order_id)
        TextView order_id;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.workertopickup)
        TextView droptodelivered;
        @BindView(R.id.pickuptodrop)
        TextView pickuptodrop;
        @BindView(R.id.starttopickup)
        TextView starttopickup;
        @BindView(R.id.mobile_no)
        TextView mobile_no;
        @BindView(R.id.invoice_time)
        TextView invoice_time;
        @BindView(R.id.invoice_date)
        TextView invoice_date;
        @BindView(R.id.delivery_offer)
        TextView delivery_offer;
        @BindView(R.id.tax)
        TextView tax;
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.tvWorkDoneActive)
        TextView tvWorkDoneActive;

        @BindView(R.id.user_pic)
        ImageView user_pic;


        @BindView(R.id.rl_maiRaing)
        RelativeLayout rl_maiRaing;


        @BindView(R.id.pickLoc)
        TextView pickLoc;
        @BindView(R.id.dropLoc)
        TextView dropLoc;
        @BindView(R.id.tvMsg)
        TextView tvMsg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);




            tvWorkDoneActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentListener.onWorkDoneClick(activeList.get(getAdapterPosition()));
                }
            });



            rl_maiRaing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentListener.onReviewClick(activeList.get(getAdapterPosition()));
                }
            });



        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {


            }
        }
    }

    public interface OnClickContentListener {

        void onWorkDoneClick(NormalUserPendingOrderInner data);

//        void onClickCancelActive(NormalUserPendingOrderInner getData);
//
//        void onMsgClick(NormalUserPendingOrderInner getData);
//
//        void onTrackClick(NormalUserPendingOrderInner getTrackdata);
//
        void onReviewClick(NormalUserPendingOrderInner data);
//
//        void onClickGo(int position, String orderID);
//
//        void onArrivedClick(NormalUserPendingOrderInner getArrivedata);

    }


//      case R.id.cl_call:
//    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:+" + mobile));
//                    context.startActivity(intent);
//                    break;
}
