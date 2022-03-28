package com.pagin.azul.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.ContactAdmin;
import com.pagin.azul.activities.CreateInvoiceActivity;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveProfessionalWorkerDashboardAdapter extends RecyclerView.Adapter<ActiveProfessionalWorkerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> activeList;
    private OnContentClickProActive onContentClickProActive;
    private String mobile;

    public ActiveProfessionalWorkerDashboardAdapter(Context context, ArrayList<NormalUserPendingOrderInner> activeList, OnContentClickProActive onContentClickProActive) {
        this.context = context;
        this.activeList = activeList;
        this.onContentClickProActive = onContentClickProActive;
    }

    @NonNull
    @Override
    public ActiveProfessionalWorkerDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_active_professional_worker_dashboard, null, false);

        return new ActiveProfessionalWorkerDashboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveProfessionalWorkerDashboardAdapter.MyViewHolder holder, int position) {

//        if (activeList.get(position).getGoStatus().equalsIgnoreCase("false")) {
//            holder.tvInvoice.setText("Go");
//            holder.tvInvoice.setTextColor(context.getResources().getColor(R.color.white));
//            holder.tvInvoice.setBackground(context.getResources().getDrawable(R.drawable.bg_blue_btn));
//            holder.tvInvoiceArrived.setVisibility(View.GONE);
//        } else if (activeList.get(position).getGoStatus().equalsIgnoreCase("true")) {
//            if (activeList.get(position).getArrivedStatus().equalsIgnoreCase("false")) {
//                holder.tvInvoice.setText("Go");
//                holder.tvInvoice.setTextColor(context.getResources().getColor(R.color.green));
//                holder.tvInvoiceArrived.setVisibility(View.VISIBLE);
//            } else if (activeList.get(position).getInvoiceStatus().equalsIgnoreCase("false")) {
//                holder.tvInvoiceCreated.setVisibility(View.VISIBLE);
//                holder.tvInvoiceArrived.setVisibility(View.GONE);
//                holder.tvInvoice.setVisibility(View.GONE);
//            } else {
//                holder.tvInvoice.setText(context.getResources().getString(R.string.edit_invoice));
//                holder.tvInvoice.setTextColor(context.getResources().getColor(R.color.purpalMedium));
//                holder.tvWorkDone.setVisibility(View.VISIBLE);
//                holder.tvInvoiceArrived.setVisibility(View.GONE);
//                holder.tvInvoiceCreated.setVisibility(View.GONE);
//            }
//        }

        String getDate = activeList.get(position).getCreatedAt();
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
            holder.tv_date.setText(String.valueOf(splitted[0]));
            holder.tv_time.setText(String.valueOf(splitted[1]+" "+splitted[2]));
        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
        String getDatei = activeList.get(position).getInvoiceCreatedAt();
        String server_formati = getDatei;    //server comes format ?
        String server_format1i = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormati = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdfi = new SimpleDateFormat(myFormati, Locale.US);
        sdfi.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdfi.parse(server_formati);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here
            holder.invoice_time.setText(String.valueOf(splitted[0]));
            holder.invoice_date.setText(String.valueOf(splitted[1]+" "+splitted[2]));
        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }

        mobile = activeList.get(position).getOfferAcceptedByMobileNumber();
        holder.order_no.setText(activeList.get(position).getOrderNumber());
        holder.tvProfileName.setText(activeList.get(position).getOfferAcceptedByName());
        holder.starttopickup.setText(activeList.get(position).getCurrentToDrLocation() + "km");
        holder.droptodelivered.setText(" 0 km");
        holder.pickuptodrop.setText(" 0 km");
        //holder.pickuptodrop.setText(activeList.get(position).getPickupToDropLocation() + "km");
        holder.offer_amt.setText(activeList.get(position).getMinimumOffer() + " SAR Only");
        holder.msg.setText(activeList.get(position).getMessage());
        holder.delivery_time.setText(activeList.get(position).getApprxTime());
        //holder.tv_add_details.setText("Order Details: Require " + activeList.get(position).getSeletTime());
        holder.mobile_no.setText(activeList.get(position).getMobileNumber());
        holder.delivery_offer.setText(activeList.get(position).getDeliveryOffer() + " SAR");
        holder.tax.setText(activeList.get(position).getTax() + " SAR");
        holder.total.setText(activeList.get(position).getTotal() + " SAR");
        holder.tv_rat_num.setText(activeList.get(position).getAvgRating());
        holder.tv_view_comments.setText("(" + activeList.get(position).getTotalRating() + " Ratings View all)");
        holder.item_detail.setText("Drop Off: " + activeList.get(position).getPickupLocation());
        holder.item_category.setText(activeList.get(position).getOrderDetails());
        if (!activeList.get(position).getOfferAcceptedByProfilePic().equalsIgnoreCase("")) {
            Picasso.with(context).load(activeList.get(position).getOfferAcceptedByProfilePic()).placeholder(context.getResources().getDrawable(R.drawable.default_p)).error(context.getResources().getDrawable(R.drawable.default_p)).into(holder.user_pic);
        }


    }

    @Override
    public int getItemCount() {
        return activeList.size();
    }


    public interface OnContentClickProActive {
        void onReviewClick(NormalUserPendingOrderInner data);

        void onMsgClick(NormalUserPendingOrderInner getData);

        void onWorkDoneClick(int position, String orderId, NormalUserPendingOrderInner getData);

        void onCancelOffer(NormalUserPendingOrderInner getData);

        void onTrackClick(NormalUserPendingOrderInner getTrackdata);

        void onArrivedClick(NormalUserPendingOrderInner getArrivedata);

        void onGoClick(int position, String orderID);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_pic)
        ImageView user_pic;
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.tvWorkDone)
        TextView tvWorkDone;
        @BindView(R.id.tvArrived)
        TextView tvInvoiceCreated;
        @BindView(R.id.tv_view_comments)
        TextView tv_view_comments;
        @BindView(R.id.iv_msg)
        ImageView iv_msg;
        @BindView(R.id.tvWorkDoneProAct)
        TextView tvWorkDoneProAct;
        @BindView(R.id.tvCancelOfferProAct)
        TextView tvCancelOfferProAct;
        @BindView(R.id.ivTrack)
        ImageView ivTrack;
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.rl_mainRate)
        RelativeLayout rl_mainRate;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.starttopickup)
        TextView starttopickup;
        @BindView(R.id.pickuptodrop)
        TextView pickuptodrop;
        @BindView(R.id.workertopickup)
        TextView droptodelivered;
        @BindView(R.id.tvInvoice)
        TextView tvInvoice;
        @BindView(R.id.tvInvoiceArrived)
        TextView tvInvoiceArrived;
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.tv_dropoffstatus)
        TextView tv_dropoffstatus;
        @BindView(R.id.tax)
        TextView tax;
        @BindView(R.id.delivery_offer)
        TextView delivery_offer;
        @BindView(R.id.invoice_date)
        TextView invoice_date;
        @BindView(R.id.invoice_time)
        TextView invoice_time;
        @BindView(R.id.item_category)
        TextView item_category;
        @BindView(R.id.item_detail)
        TextView item_detail;
        @BindView(R.id.mobile_no)
        TextView mobile_no;
        @BindView(R.id.tv_add_details)
        TextView tv_add_details;
        @BindView(R.id.delivery_time)
        TextView delivery_time;
        @BindView(R.id.msg)
        TextView msg;
        @BindView(R.id.offer_amt)
        TextView offer_amt;
        @BindView(R.id.tvContactAdmin)
        TextView tvContactAdmin;
        @BindView(R.id.cl_msgPrfWorker)
        ConstraintLayout cl_msgPrfWorker;
        @BindView(R.id.cl_TrackProWorker)
        ConstraintLayout cl_TrackProWorker;
        @BindView(R.id.cl_callProfWorker)
        ConstraintLayout cl_callProfWorker;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rl_mainRate.setOnClickListener(this);
            cl_msgPrfWorker.setOnClickListener(this);
            cl_TrackProWorker.setOnClickListener(this);
            cl_callProfWorker.setOnClickListener(this);
            tvWorkDoneProAct.setOnClickListener(this);
            tvCancelOfferProAct.setOnClickListener(this);
            tvInvoiceArrived.setOnClickListener(this);
            tvInvoice.setOnClickListener(this);
            tvInvoiceCreated.setOnClickListener(this);
            tvWorkDone.setOnClickListener(this);
            tvContactAdmin.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvWorkDoneProAct:
                    onContentClickProActive.onWorkDoneClick(getAdapterPosition(), activeList.get(getAdapterPosition()).get_id(), activeList.get(getAdapterPosition()));
                    break;
                case R.id.tvCancelOfferProAct:
                    onContentClickProActive.onCancelOffer(activeList.get(getAdapterPosition()));
                    break;
                case R.id.rl_mainRate:
                    onContentClickProActive.onReviewClick(activeList.get(getAdapterPosition()));
                    break;
                case R.id.cl_msgPrfWorker:
                    onContentClickProActive.onMsgClick(activeList.get(getAdapterPosition()));
                    break;
                case R.id.cl_TrackProWorker:
                    onContentClickProActive.onTrackClick(activeList.get(getAdapterPosition()));
                    break;
                case R.id.cl_callProfWorker:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+" + mobile));
                    context.startActivity(intent);
                    break;
                case R.id.tvInvoice:
                    if (onContentClickProActive != null) {
                        if (tvInvoice.getText().toString().trim().equalsIgnoreCase("Edit\nInvoice")) {
                            context.startActivity(CreateInvoiceActivity.getIntent(context, activeList.get(getAdapterPosition()), "ActivePW"));
                        } else if (tvInvoice.getText().toString().equalsIgnoreCase("Go")) {
                            if (activeList.get(getAdapterPosition()).getGoStatus().equalsIgnoreCase("false")) {
                                onContentClickProActive.onGoClick(getAdapterPosition(), activeList.get(getAdapterPosition()).get_id());
                            }
                        }
                    }
                    break;
                case R.id.tvInvoiceArrived:
                    if (onContentClickProActive != null) {
                        if (tvInvoiceArrived.getText().toString().equalsIgnoreCase("Arrived")) {
                            onContentClickProActive.onArrivedClick(activeList.get(getAdapterPosition()));
                        }
                    }
                    break;
                case R.id.tvArrived:
                    context.startActivity(CreateInvoiceActivity.getIntent(context, activeList.get(getAdapterPosition()), "ActivePW"));
                    break;
                case R.id.tvWorkDone:
                    onContentClickProActive.onWorkDoneClick(getAdapterPosition(), activeList.get(getAdapterPosition()).get_id(), activeList.get(getAdapterPosition()));
                    break;
                case R.id.tvContactAdmin:
                    context.startActivity(new Intent(context, ContactAdmin.class));
                    break;
            }
        }
    }
}
