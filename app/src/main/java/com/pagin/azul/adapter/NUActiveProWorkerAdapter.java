package com.pagin.azul.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.ActiveTrackingActivity;
import com.pagin.azul.activities.NUMessageDeliveryPersonActivity;
import com.pagin.azul.activities.ReportOrderActivity;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

    public class NUActiveProWorkerAdapter extends RecyclerView.Adapter<NUActiveProWorkerAdapter.NUProfWorkerViewHolder> {

    private Context context;
    private ArrayList<NormalUserPendingOrderInner> activeList;
    private NUActiveProWorkerAdapter.ActiveRequireProff listener;
    private String mobile;

    public NUActiveProWorkerAdapter(Context context, ArrayList<NormalUserPendingOrderInner> activeList) {
        this.context = context;
        this.activeList = activeList;
    }

    @NonNull
    @Override
    public NUProfWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.nu_active_professional_worker_custom, null, false);

        return new NUProfWorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUProfWorkerViewHolder holder, int position) {

        if (activeList.get(position).getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
//            if (activeList.get(position).getGoStatus().equalsIgnoreCase("false")) {
//                holder.tvArrived.setVisibility(View.GONE);
//
//            } else if (activeList.get(position).getGoStatus().equalsIgnoreCase("true")) {
//                if (activeList.get(position).getArrivedStatus().equalsIgnoreCase("false")) {
//                    holder.tvReviewInvoice.setText("Go");
//                    holder.tvReviewInvoice.setVisibility(View.VISIBLE);
//                    holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.green));
//
//                } else if (activeList.get(position).getInvoiceStatus().equalsIgnoreCase("false")) {
//                    holder.tvReviewInvoice.setVisibility(View.GONE);
//                    holder.tvArrived.setVisibility(View.VISIBLE);
//
//                } else {
//                    holder.tvInvoiceCreated.setVisibility(View.VISIBLE);
//                    holder.tvReviewInvoice.setVisibility(View.VISIBLE);
//                    holder.tvArrived.setVisibility(View.GONE);
//                }
//
//            }
//            if (activeList.get(position).getGoStatus().equalsIgnoreCase("false")) {
//                holder.tvReviewInvoice.setText("Go");
//                holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.white));
//                holder.tvReviewInvoice.setBackground(context.getResources().getDrawable(R.drawable.bg_blue_btn));
//                holder.tvArrived.setVisibility(View.GONE);
//            } else if (activeList.get(position).getGoStatus().equalsIgnoreCase("true")) {
//                if (activeList.get(position).getInvoiceStatus().equalsIgnoreCase("false")) {
//                    holder.tvReviewInvoice.setText("Go");
//                    holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.green));
//                    holder.tvReviewInvoice.setBackgroundColor(context.getResources().getColor(R.color.white));
//                    holder.tvReviewInvoice.setEnabled(false);
//                    holder.tvArrived.setVisibility(View.VISIBLE);
//                } else if (activeList.get(position).getInvoiceStatus().equalsIgnoreCase("true")) {
//                    holder.tvReviewInvoice.setText(context.getResources().getString(R.string.review_invoice));
//                    holder.tvReviewInvoice.setEnabled(true);
//                    holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.purpalMedium));
//                    holder.tvReviewInvoice.setVisibility(View.VISIBLE);
//                    holder.tvDone.setVisibility(View.GONE);
//                    holder.tvArrived.setVisibility(View.GONE);
//                    holder.tvInvoiceCreated.setVisibility(View.VISIBLE);
//                    if (activeList.get(position).getArrivedStatus().equalsIgnoreCase("true")) {
//                        holder.tvReviewInvoice.setText(context.getResources().getString(R.string.review_invoice));
//                        holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.purpalMedium));
//                        holder.tvReviewInvoice.setVisibility(View.VISIBLE);
//                        holder.tvDone.setVisibility(View.VISIBLE);
//                        holder.tvInvoiceCreated.setVisibility(View.GONE);
//                        holder.tvArrived.setVisibility(View.GONE);
//                    }
//                    if(activeList.get(position).getArrivedStatus().equalsIgnoreCase("false")){
//                        holder.tvReviewInvoice.setText(context.getResources().getString(R.string.review_invoice));
//                        holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.purpalMedium));
//                        holder.tvDone.setVisibility(View.GONE);
//                        holder.tvInvoiceCreated.setVisibility(View.GONE);
//                        holder.tvArrived.setVisibility(View.VISIBLE);
//                    }else if(activeList.get(position).getArrivedStatus().equalsIgnoreCase("true")) {
//                        holder.tvReviewInvoice.setText(context.getResources().getString(R.string.review_invoice));
//                        holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.purpalMedium));
//                        holder.tvDone.setVisibility(View.GONE);
//                        holder.tvInvoiceCreated.setVisibility(View.VISIBLE);
//                        holder.tvArrived.setVisibility(View.VISIBLE);
//
//                        if(activeList.get(position).getWorkDoneStatus().equalsIgnoreCase("false")){
//                            holder.tvDone.setVisibility(View.GONE);
//                        }else {
//                            holder.tvDone.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            }

            try {

                String[] splitted = getFormattedDateAndTime(activeList.get(position).getCreatedAt());
                holder.tv_date.setText(String.valueOf(splitted[0]));
                holder.tv_time.setText(String.valueOf(splitted[1]+" "+splitted[2]));

                String[] splittedInvoice = getFormattedDateAndTime(activeList.get(position).getInvoiceCreatedAt());
                holder.invoice_time.setText(String.valueOf(splittedInvoice[0]));
                holder.invoice_date.setText(String.valueOf(splittedInvoice[1]+" "+splittedInvoice[2]));


            } catch (Exception e) {
                System.out.println(e.toString()); //date format error
            }

            String currency = activeList.get(position).getCurrency()!=null?activeList.get(position).getCurrency():"";
            mobile = activeList.get(position).getOfferAcceptedOfMobileNumber();
            holder.order_no.setText(activeList.get(position).getOrderNumber());
            holder.tv_profile_name.setText(activeList.get(position).getOfferAcceptedOfName());
            holder.starttopickup.setText(activeList.get(position).getCurrentToPicupLocation() + "km");
            holder.pickuptodrop.setText("0 km");
            holder.change_offer.setText(activeList.get(position).getMinimumOffer()+" "+currency+" "+context.getString(R.string.only));
            holder.delivery_msg.setText(activeList.get(position).getMessage());
            holder.delivery_time.setText(activeList.get(position).getApprxTime());
            //holder.order_detail.setText("Order Details: Require " + activeList.get(position).getSeletTime());

            holder.mobile_no.setText(activeList.get(position).getOrderDetails());
            //holder.item_detail.setText(activeList.get(position).getOrderDetails());
            holder.delivery_offer.setText(activeList.get(position).getDeliveryOffer() + " "+currency);
            holder.tax.setText(activeList.get(position).getTax() + " "+currency);
            holder.total.setText(activeList.get(position).getTotal() + " "+currency);
            holder.tv_rat_num.setText(activeList.get(position).getAvgRating());
            holder.tvReviewAll.setText("("+activeList.get(position).getTotalRating() + " "+context.getString(R.string.ratings_view_all)+")");
            //holder.tv_dropoffstatus.setText(context.getResources().getString(R.string.prof_working));
            holder.tv_dropoffstatus.setText(context.getResources().getString(R.string.prof_workr));
            holder.tv_pickupsatatus.setText(context.getResources().getString(R.string.arrived));
            holder.tv_chargeoffer.setText(context.getResources().getString(R.string.charge_offer));
            holder.tv_del_msg.setText(context.getResources().getString(R.string.prff_msg));
            holder.del_tym.setText(context.getResources().getString(R.string.work_completion_time1));
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                holder.tvCategoryName.setText(activeList.get(position).getSelectCategoryName());
                //holder.tvSubCategoryName.setText(activeList.get(position).getSelectSubCategoryName());
                String subCategoryName = activeList.get(position).getSelectSubCategoryName();
                if(subCategoryName!=null && !subCategoryName.equals("")){
                    holder.tvSubCategoryName.setText(subCategoryName);
                    holder.rlSubCategory.setVisibility(View.VISIBLE);
                }
                else{
                    holder.rlSubCategory.setVisibility(View.GONE);
                    //holder.tvSubCategoryName.setText(R.string.no_sub_category_found);
                }
            }else {
                holder.tvCategoryName.setText(activeList.get(position).getPortugueseCategoryName());
                //holder.tvSubCategoryName.setText(activeList.get(position).getSelectSubCategoryName());
                String subCategoryName = activeList.get(position).getPortugueseSubCategoryName();
                if(subCategoryName!=null && !subCategoryName.equals("")){
                    holder.tvSubCategoryName.setText(subCategoryName);
                    holder.rlSubCategory.setVisibility(View.VISIBLE);
                }
                else{
                    holder.rlSubCategory.setVisibility(View.GONE);
                    //holder.tvSubCategoryName.setText(R.string.no_sub_category_found);
                }
            }

            Glide.with(context)
                    .load(activeList.get(position).getOfferAcceptedOfProfilePic())
                    .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                            .error(R.drawable.profile_default))
                    .into(holder.user_pic);

        }
    }

    @Override
    public int getItemCount() {
        return activeList.size();
    }

    public void setListener(NUActiveProWorkerAdapter.ActiveRequireProff listener) {
        this.listener = listener;
    }

    public class NUProfWorkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.user_pic)
        ImageView user_pic;
        @BindView(R.id.tv_dropoffstatus)
        TextView tv_dropoffstatus;
        @BindView(R.id.tv_pickupsatatus)
        TextView tv_pickupsatatus;
        @BindView(R.id.tv_chargeoffer)
        TextView tv_chargeoffer;
        @BindView(R.id.tv_del_msg)
        TextView tv_del_msg;
        @BindView(R.id.del_tym)
        TextView del_tym;
        @BindView(R.id.tvDone)
        TextView tvDone;
        @BindView(R.id.tvInvoiceCreated)
        TextView tvInvoiceCreated;
        @BindView(R.id.tvArrived)
        TextView tvArrived;
        @BindView(R.id.tv_profile_name)
        TextView tv_profile_name;
        @BindView(R.id.tvReviewInvoice)
        TextView tvReviewInvoice;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
        @BindView(R.id.tv_report_order)
        TextView tvReportOrder;
        @BindView(R.id.tv_cancel_order)
        TextView tvCancelOrder;
        @BindView(R.id.tvReviewAll)
        TextView tvReviewAll;
        @BindView(R.id.order_no)
        TextView order_no;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.item_detail)
        TextView item_detail;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.workertopickup)
        TextView droptodelivered;
        @BindView(R.id.pickuptodrop)
        TextView pickuptodrop;
        @BindView(R.id.starttopickup)
        TextView starttopickup;
        @BindView(R.id.change_offer)
        TextView change_offer;
        @BindView(R.id.delivery_msg)
        TextView delivery_msg;
        @BindView(R.id.delivery_time)
        TextView delivery_time;
        @BindView(R.id.mobile_no)
        TextView mobile_no;
        @BindView(R.id.order_detail)
        TextView order_detail;
        @BindView(R.id.invoice_time)
        TextView invoice_time;
        @BindView(R.id.invoice_date)
        TextView invoice_date;
        @BindView(R.id.delivery_offer)
        TextView delivery_offer;
        @BindView(R.id.tax)
        TextView tax;
        @BindView(R.id.tvMsdTrack)
        TextView tvMsdTrack;
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;
        @BindView(R.id.tvSubCategoryName)
        TextView tvSubCategoryName;
        @BindView(R.id.rl_mainRating)
        RelativeLayout rl_mainRating;
        @BindView(R.id.rlSubCategory)
        RelativeLayout rlSubCategory;
        @BindView(R.id.ll_msg)
        LinearLayout llMsg;
        @BindView(R.id.ll_call)
        LinearLayout ll_call;

        @BindView(R.id.webUrl)
        WebView webUrl;

        @BindView(R.id.ll_track)
        LinearLayout llTrack;

        public NUProfWorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvReportOrder.setOnClickListener(this);
            tvCancelOrder.setOnClickListener(this);
            llMsg.setOnClickListener(this);
            llTrack.setOnClickListener(this);
            rl_mainRating.setOnClickListener(this);
            tvReviewInvoice.setOnClickListener(this);
            ll_call.setOnClickListener(this);
            tvMsdTrack.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.tv_report_order:
                    context.startActivity(ReportOrderActivity.getIntent(context, activeList.get(getAdapterPosition()), "NUActive"));
                    break;
                case R.id.tv_cancel_order:
                    if (listener != null) {
                        listener.onCancelOrder(activeList.get(getAdapterPosition()));
                    }
                    break;
                case R.id.ll_msg:
                    context.startActivity(NUMessageDeliveryPersonActivity.getIntent(context, activeList.get(getAdapterPosition()), "FromNUPrf"));
                    break;
                case R.id.ll_track:
                    context.startActivity(ActiveTrackingActivity.getIntent(context, activeList.get(getAdapterPosition()), "FromNUActive"));
                    break;
                case R.id.rl_mainRating:
                    context.startActivity(UserDetailsActivity.getIntent(context, activeList.get(getAdapterPosition()), "FromNUActive"));
                    break;
                case R.id.tvReviewInvoice:
                    if (tvReviewInvoice.getText().toString().equalsIgnoreCase("Review\nInvoice")) {
                        webUrl.getSettings().setJavaScriptEnabled(true);
                        webUrl.loadUrl("http://docs.google.com/gview?embedded=true&url=" + activeList.get(getAdapterPosition()).getInvoicePdf());
                    }
                    break;
                case R.id.ll_call:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mobile));
                    context.startActivity(intent);
                    break;

                case R.id.tvMsdTrack:
                    listener.onCancelOrder(activeList.get(getAdapterPosition()));
                    break;
            }
        }
    }

    public interface ActiveRequireProff {
        void onReportOrder(View v, int pos);

        void onCancelOrder(NormalUserPendingOrderInner getData);

    }

    private String[] getFormattedDateAndTime(String serverDate){
        String server_format = serverDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        String[] splitted=null;
        try {

            Date date = sdf.parse(server_format);
            System.out.println(date);
            /*String your_format;
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                your_format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(date);
            else
                your_format = new SimpleDateFormat("dd-MMMM-yyyy hh:mm a").format(date);*/
            //String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
            String your_format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(date);
            System.out.println(your_format);
            splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }

        return splitted;
    }
}
