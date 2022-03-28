package com.pagin.azul.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.CancellationActivity;
import com.pagin.azul.activities.ContactAdmin;
import com.pagin.azul.activities.ReportOrderActivity;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pagin.azul.Constant.Constants.kOrderReportId;

public class PendingDeliveryWorkerDashboardAdapter extends RecyclerView.Adapter<PendingDeliveryWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pendingList;
    private OnReviewClickPending onReviewClickPending;

    public PendingDeliveryWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pendingList, OnReviewClickPending onReviewClickPending) {
        this.context = context;
        this.pendingList = pendingList;
        this.onReviewClickPending = onReviewClickPending;
    }

    @NonNull
    @Override
    public PendingDeliveryWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pending_dashboard_delivery_worker, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingDeliveryWorkerDashboardAdapter.MyViewHolder holder, int position) {

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
            holder.datetop.setText(String.valueOf(splitted[0]));
            holder.timetop.setText(String.valueOf(splitted[1]));
        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
        holder.order_no.setText(pendingList.get(position).getOrderNumber());
        holder.tvProfileName.setText(pendingList.get(position).getName());
        holder.tv_offer_amt.setText(pendingList.get(position).getMinimumOffer() + " SAR");
        holder.msg.setText(pendingList.get(position).getMessage());
        holder.delivery_tym.setText(pendingList.get(position).getApprxTime());
        holder.tv_pickup.setText("Pickup Location: " + pendingList.get(position).getPickupLocation());
        holder.dropOff.setText("DropOff Location: " + pendingList.get(position).getDropOffLocation());
        holder.tv_mobile.setText(pendingList.get(position).getMobileNumber());
        holder.tv_itemdetail.setText(pendingList.get(position).getOrderDetails() + " and " + pendingList.get(position).getSeletTime());
        holder.tax.setText(pendingList.get(position).getTax() + " SAR");
        holder.total.setText(pendingList.get(position).getTotal() + " SAR");
        holder.pick_dis.setText(pendingList.get(position).getCurrentToPicupLocation() + "km");
        holder.drop_dis.setText(pendingList.get(position).getPickupToDropLocation() + "km");
        holder.tv_view_comments.setText(pendingList.get(position).getTotalRating() + " Ratings View all");
        holder.tv_rat_num.setText(pendingList.get(position).getAvgRating());
        if (!pendingList.get(position).getProfilePic().equalsIgnoreCase("")) {
            Picasso.with(context).load(pendingList.get(position).getProfilePic()).into(holder.user_pic);
        }
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.datetop)
        TextView datetop;
        @BindView(R.id.user_pic)
        ImageView user_pic;
        @BindView(R.id.timetop)
        TextView timetop;
        @BindView(R.id.pick_dis)
        TextView pick_dis;
        @BindView(R.id.starttopickup)
        TextView drop_dis;
        @BindView(R.id.tvContactAdmin)
        TextView tvContactAdmin;
        @BindView(R.id.tv_mobile)
        TextView tv_mobile;
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.tv_offer_amt)
        TextView tv_offer_amt;
        @BindView(R.id.msg)
        TextView msg;
        @BindView(R.id.delivery_tym)
        TextView delivery_tym;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.tv_itemdetail)
        TextView tv_itemdetail;
        @BindView(R.id.tv_offer_pending)
        TextView tv_offer_pending;
        @BindView(R.id.tv_delivery_amt)
        TextView tv_delivery_amt;
        @BindView(R.id.tv_pickup)
        TextView tv_pickup;
        @BindView(R.id.dropOff)
        TextView dropOff;
        @BindView(R.id.tax)
        TextView tax;
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.reportOrder)
        TextView reportOrder;
        @BindView(R.id.tvCancelOrderOffer)
        TextView tvCancelOrderOffer;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.rlmainRating)
        RelativeLayout rlmainRating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            reportOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(ReportOrderActivity.getIntent(context, pendingList.get(getAdapterPosition()), "Pending"));
                    SharedPreferenceWriter.getInstance(context).writeStringValue(kOrderReportId, pendingList.get(getAdapterPosition()).get_id());
                }
            });

            tvCancelOrderOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(CancellationActivity.getIntent(context, pendingList.get(getAdapterPosition())));
                }
            });

            rlmainRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReviewClickPending.onReviewClick(pendingList.get(getAdapterPosition()));
                }
            });
            tvContactAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ContactAdmin.class));
                }
            });

        }

        private void showMakeNewOfferDialog() {
            final Dialog dialog = new Dialog(context, R.style.ThemeDialogCustom);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_make_new_offer_dialog);
            TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
            TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);

            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }


    }

    public interface OnReviewClickPending {
        void onReviewClick(NormalUserPendingOrderInner getRatingData);
    }
}
