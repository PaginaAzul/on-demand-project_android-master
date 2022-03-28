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
import com.pagin.azul.activities.RatingAndRiviewActivity;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastDeliveryWorkerDashboardAdapter extends RecyclerView.Adapter<PastDeliveryWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pastList;

    public PastDeliveryWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pastList) {
        this.context = context;
        this.pastList = pastList;
    }

    @NonNull
    @Override
    public PastDeliveryWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // View view = LayoutInflater.from(context).inflate(R.layout.row_past_dashboard_delivery_worker, null, false);
        View view = LayoutInflater.from(context).inflate(R.layout.row_past_normal_prof_layout, null, false);

        return new PastDeliveryWorkerDashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastDeliveryWorkerDashboardAdapter.MyViewHolder holder, int position) {
        holder.prf_offer.setText("Invoice Amount");

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

            holder.invoice_time.setText(String.valueOf(splitted[0]));
            holder.invoice_date.setText(String.valueOf(splitted[1] + " " + splitted[2]));

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


        holder.tvReviewrate.setText("Review and Rate.");
        holder.orderId.setText(pastList.get(position).getOrderNumber());
//         if (pastList.get(position).getMinimumOffer() != null && !pastList.get(position).getMinimumOffer().equalsIgnoreCase("")) {
//            holder.offer_amt.setText(pastList.get(position).getMinimumOffer() + context.getResources().getString(R.string.only));
//        } else {
//            holder.offer_amt.setText("0.00" + context.getResources().getString(R.string.only));
//        }

        holder.offerAmt.setText(pastList.get(position).getDeliveryOffer() + context.getResources().getString(R.string.sar));
        if(pastList.get(position).getInvoiceSubtoatl() != null) {
            holder.tvPrfOfr.setText(pastList.get(position).getInvoiceSubtoatl() + context.getResources().getString(R.string.sar));
        }else {
            holder.tvPrfOfr.setText("0" + context.getResources().getString(R.string.sar));
        }
        holder.tvTax.setText(pastList.get(position).getTax() + context.getResources().getString(R.string.sar));
        holder.tvTotal.setText(pastList.get(position).getTotal() + context.getResources().getString(R.string.sar));

        if (pastList.get(position).getRatingData() != null && pastList.get(position).getRatingData().size() > 0) {
            holder.tv_rat_num.setText(String.valueOf(pastList.get(position).getRatingData().get(0).getRate()));
            holder.tvratingtxt.setText(pastList.get(position).getRatingData().get(0).getComments());
        }
    }

    @Override
    public int getItemCount() {
        return pastList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.tvReviewrate)
        TextView tvReviewrate;
@BindView(R.id.prf_offer)
        TextView prf_offer;

        @BindView(R.id.offerAmt)
        TextView offerAmt;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.rlRating)
        RelativeLayout rlRating;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            rlRating.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_rating:
                    context.startActivity(RatingAndRiviewActivity.getIntent(context, pastList.get(getAdapterPosition()), "PastDP"));
                    break;
            }
        }
    }
}
