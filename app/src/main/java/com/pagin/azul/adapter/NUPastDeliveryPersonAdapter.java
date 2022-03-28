package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NUPastDeliveryPersonAdapter extends RecyclerView.Adapter<NUPastDeliveryPersonAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pastList;
    private OnClickContentListener onClickContentListener;

    public NUPastDeliveryPersonAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pastList, OnClickContentListener onClickContentListener) {
        this.context = context;
        this.pastList = pastList;
        this.onClickContentListener = onClickContentListener;
    }

    @NonNull
    @Override
    public NUPastDeliveryPersonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //View view = LayoutInflater.from(context).inflate(R.layout.row_nu_past_delivery_person, null, false);
        View view = LayoutInflater.from(context).inflate(R.layout.row_past_normal_prof_layout, null, false);

        return new NUPastDeliveryPersonAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUPastDeliveryPersonAdapter.MyViewHolder holder, int position) {

        String getDate = pastList.get(position).getInvoiceCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {

            Date date = sdf.parse(server_format);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here

            holder.invoice_date.setText(String.valueOf(splitted[0]));
            holder.invoice_time.setText(String.valueOf(splitted[1] + " " + splitted[2]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format ferror
        }
        String getDateI = pastList.get(position).getCreatedAt();
        String server_formatI = getDateI;    //server comes format ?
        String server_format1I = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormatI = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdfI = new SimpleDateFormat(myFormatI, Locale.US);
        sdfI.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {

            Date date = sdfI.parse(server_formatI);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here

            holder.tv_date.setText(String.valueOf(splitted[0]));
            holder.tv_time.setText(String.valueOf(splitted[1] + " " + splitted[2]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format ferror
        }

        holder.orderId.setText(pastList.get(position).getOrderNumber());
//        holder.tvOrderLocation.setText(pastList.get(position).getPickupLocation());
//        holder.tvCategory.setText(pastList.get(position).getSelectCategoryName());
//        holder.tvSubCategory.setText(pastList.get(position).getSelectSubCategoryName());
//        holder.tvOrderDetails.setText(pastList.get(position).getOrderDetails());

//        if (pastList.get(position).getMinimumOffer() != null && !pastList.get(position).getMinimumOffer().equalsIgnoreCase("")) {
//            holder.offerAmt.setText(pastList.get(position).getMinimumOffer() + " Only");
//        } else {
//            holder.offerAmt.setText("0.00" + " Only");
//        }

        if(pastList.get(position).getInvoiceSubtoatl()!=null && !pastList.get(position).getInvoiceSubtoatl().isEmpty())
            holder.tvPrfOfr.setText(pastList.get(position).getInvoiceSubtoatl() + " SAR");
        else
            holder.tvPrfOfr.setText("0 SAR");

        holder.offerAmt.setText(pastList.get(position).getDeliveryOffer() + " SAR");
        holder.tvTax.setText(pastList.get(position).getTax() + " SAR");
        holder.tvTotal.setText(pastList.get(position).getTotal() + " SAR");
        if (pastList.get(position).getRatingData() != null && pastList.get(position).getRatingData().size() > 0) {
            holder.tv_rat_num.setText(String.valueOf(pastList.get(position).getRatingData().get(0).getRate()));
            holder.tvratingtxt.setText(pastList.get(position).getRatingData().get(0).getComments());
        }


    }

    @Override
    public int getItemCount() {
        return pastList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_rat_num)
//        TextView tv_rat_num;
//        @BindView(R.id.order_no)
//        TextView order_no;
//        @BindView(R.id.tv_date)
//        TextView tv_date;
//        @BindView(R.id.item_detail)
//        TextView item_detail;
//        @BindView(R.id.tv_time)
//        TextView tv_time;
//        @BindView(R.id.order_detail)
//        TextView order_detail;
//        @BindView(R.id.mobile_no)
//        TextView mobile_no;
//        @BindView(R.id.pickup_add)
//        TextView pickup_add;
//        @BindView(R.id.drop_add)
//        TextView drop_add;
//        @BindView(R.id.offer_amt)
//        TextView offer_amt;
//        @BindView(R.id.invoice_time)
//        TextView invoice_time;
//        @BindView(R.id.invoice_date)
//        TextView invoice_date;
//        @BindView(R.id.delivey_offer)
//        TextView delivey_offer;
//        @BindView(R.id.tax)
//        TextView tax;
//        @BindView(R.id.total)
//        TextView total;
//        @BindView(R.id.rate_msg)
//        TextView rate_msg;

        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.invoice_time)
        TextView invoice_time;
        @BindView(R.id.invoice_date)
        TextView invoice_date;
        @BindView(R.id.orderId)
        TextView orderId;
        @BindView(R.id.tvPrfOfr)
        TextView tvPrfOfr;
        @BindView(R.id.tvTax)
        TextView tvTax;
        @BindView(R.id.tvTotal)
        TextView tvTotal;
        @BindView(R.id.tvratingtxt)
        TextView tvratingtxt;

        @BindView(R.id.offerAmt)
        TextView offerAmt;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
//        @BindView(R.id.tvCategory)
//        TextView tvCategory;
//        @BindView(R.id.tvSubCategory)
//        TextView tvSubCategory;
//        @BindView(R.id.tvOrderDetails)
//        TextView tvOrderDetails;
//        @BindView(R.id.tvOrderLocation)
//        TextView tvOrderLocation;
        @BindView(R.id.rlRating)
        RelativeLayout rlRating;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick({R.id.rlRating})
        void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlRating:
                    onClickContentListener.onReviewClick(pastList.get(getAdapterPosition()));
                    break;
            }
        }


    }

    public interface OnClickContentListener {
        void onReviewClick(NormalUserPendingOrderInner data);
    }

}
