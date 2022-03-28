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

import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PendingProfessionalWorkerDashboardAdapter extends RecyclerView.Adapter<PendingProfessionalWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pendingList;
    private OnContentClickPendingPro onContentClickPendingPro;

    public PendingProfessionalWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pendingList, OnContentClickPendingPro onContentClickPendingPro) {
        this.context = context;
        this.pendingList = pendingList;
        this.onContentClickPendingPro = onContentClickPendingPro;

    }

    @NonNull
    @Override
    public PendingProfessionalWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pending_professional_worker_dashboard, null, false);

        return new PendingProfessionalWorkerDashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingProfessionalWorkerDashboardAdapter.MyViewHolder holder, int position) {

        //holder.tvProfileName.setText(pendingList.get(position));
        String getDate = pendingList.get(position).getCreatedAt();
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
        holder.order_no.setText(pendingList.get(position).getOrderNumber());
        holder.tvProfileName.setText(pendingList.get(position).getName());
        holder.offer_amt.setText(pendingList.get(position).getMinimumOffer() + " SAR");
        holder.offermsg.setText(pendingList.get(position).getMessage());
        holder.delivery_time.setText(pendingList.get(position).getApprxTime());
        holder.dropOff.setText("DropOff Location: " + pendingList.get(position).getPickupLocation());
        holder.mobile_no.setText(pendingList.get(position).getMobileNumber());
        holder.item_detail.setText(pendingList.get(position).getOrderDetails() + " and " + pendingList.get(position).getSeletTime());
        holder.delivery_offer.setText(pendingList.get(position).getDeliveryOffer() + " SAR");
        holder.tax.setText(pendingList.get(position).getTax() + " SAR");
        holder.total.setText(pendingList.get(position).getTotal() + " SAR");
        holder.mylocToDropOff.setText(pendingList.get(position).getCurrentToDrLocation() + "km");
        holder.tv_view_comments.setText("("+pendingList.get(position).getTotalRating() + " Ratings View all)");
        holder.tv_rat_num.setText(pendingList.get(position).getAvgRating());
        if (!pendingList.get(position).getProfilePic().equalsIgnoreCase("")) {

            Picasso.with(context).load(pendingList.get(position).getProfilePic()).placeholder(context.getResources().getDrawable(R.drawable.default_p)).error(context.getResources().getDrawable(R.drawable.default_p)).into(holder.user_pic);

        }
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public interface OnContentClickPendingPro {
        void onReviewClick(NormalUserPendingOrderInner data);

        void onReportOrderClick(NormalUserPendingOrderInner getReportData);

        void onCancelClick(NormalUserPendingOrderInner getData);

        void onContactAdminClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_pic)
        ImageView user_pic;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.tvReportOrder)
        TextView tvMakeNewOfferPro;
        @BindView(R.id.tvCancelOrderPro)
        TextView tvCancelOrderPro;
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.mylocToDropOff)
        TextView mylocToDropOff;
        @BindView(R.id.offer_amt)
        TextView offer_amt;
        @BindView(R.id.rl_mainRate)
        RelativeLayout rl_mainRate;
        @BindView(R.id.offermsg)
        TextView offermsg;
        @BindView(R.id.delivery_time)
        TextView delivery_time;
        @BindView(R.id.tv_add_details)
        TextView tv_add_details;
        @BindView(R.id.mobile_no)
        TextView mobile_no;
        @BindView(R.id.item_detail)
        TextView item_detail;
        @BindView(R.id.item_category)
        TextView item_category;
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
        @BindView(R.id.dropOff)
        TextView dropOff;
        @BindView(R.id.tvContactAdmin)
        TextView tvContactAdmin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            rl_mainRate.setOnClickListener(this);
            tvMakeNewOfferPro.setOnClickListener(this);
            tvCancelOrderPro.setOnClickListener(this);
            tvContactAdmin.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_mainRate:
                    onContentClickPendingPro.onReviewClick(pendingList.get(getAdapterPosition()));
                    break;
                case R.id.tvReportOrder:
                    onContentClickPendingPro.onReportOrderClick(pendingList.get(getAdapterPosition()));
                    break;
                case R.id.tvCancelOrderPro:
                    onContentClickPendingPro.onCancelClick(pendingList.get(getAdapterPosition()));
                    break;
                case R.id.tvContactAdmin:
                    onContentClickPendingPro.onContactAdminClick(getAdapterPosition());
                    break;
            }
        }
    }
}
