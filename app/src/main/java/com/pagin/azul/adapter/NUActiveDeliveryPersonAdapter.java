package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.UserDetailsActivity;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NUActiveDeliveryPersonAdapter extends RecyclerView.Adapter<NUActiveDeliveryPersonAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> activeList;
    private ActiveRequireProff listener;
    private String mobile;


    public NUActiveDeliveryPersonAdapter(Context context, ArrayList<NormalUserPendingOrderInner> activeList) {
        this.context = context;
        this.activeList = activeList;
    }

    @NonNull
    @Override
    public NUActiveDeliveryPersonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_nu_active_delivery_person, null, false);

        return new NUActiveDeliveryPersonAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUActiveDeliveryPersonAdapter.MyViewHolder holder, int position) {

        if (activeList.get(position).getServiceType().equalsIgnoreCase("DeliveryPersion")) {
//            if (activeList.get(position).getInvoiceStatus().equalsIgnoreCase("true")) {
//                holder.tvReviewInvoice.setText(R.string.review_invoice);
//                holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.purpalMedium));
//                holder.tvInvoiceCreated.setVisibility(View.VISIBLE);
//            } else {
//                holder.tvReviewInvoice.setText("Go");
//                holder.tvReviewInvoice.setTextColor(context.getResources().getColor(R.color.green));
//            }

            String getDate = activeList.get(position).getCreatedAt();
            String server_format = getDate;    //server comes format ?
            String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
            String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            try {

                String[] splitted = getFormattedDateAndTime(activeList.get(position).getCreatedAt());
                holder.tv_date.setText(String.valueOf(splitted[0]));
                holder.tv_time.setText(String.valueOf(splitted[1]+" "+splitted[2]));

                String[] splittedInvoice = getFormattedDateAndTime(activeList.get(position).getInvoiceCreatedAt());
                holder.invoice_date.setText(String.valueOf(splittedInvoice[0]));
                holder.invoice_time.setText(String.valueOf(splittedInvoice[1]+" "+splittedInvoice[2]));


            } catch (Exception e) {
                System.out.println(e.toString()); //date format error
            }
            mobile = activeList.get(position).getOfferAcceptedOfMobileNumber();
            holder.order_no.setText(activeList.get(position).getOrderNumber());
            holder.tv_profile_name.setText(activeList.get(position).getOfferAcceptedOfName());
            holder.starttopickup.setText(activeList.get(position).getCurrentToPicupLocation() + "km");
            holder.pickuptodrop.setText(activeList.get(position).getPickupToDropLocation() + "km");
            holder.change_offer.setText(activeList.get(position).getMinimumOffer()+" SAR");
            holder.delivery_msg.setText(activeList.get(position).getMessage());
            holder.delivery_time.setText(activeList.get(position).getApprxTime());
            holder.mobile_no.setText(activeList.get(position).getOrderDetails());

            holder.mobile_no.setText(activeList.get(position).getMobileNumber());
            holder.item_detail.setText(activeList.get(position).getOrderDetails());
            holder.delivery_offer.setText(activeList.get(position).getDeliveryOffer() + " SAR");
            holder.tax.setText(activeList.get(position).getTax() + " SAR");
            holder.total.setText(activeList.get(position).getTotal() + " SAR");
            holder.tv_rat_num.setText(activeList.get(position).getAvgRating());
            holder.tvReviewAll.setText("("+activeList.get(position).getTotalRating() + " Ratings View all)");
            if (!activeList.get(position).getOfferAcceptedOfProfilePic().equalsIgnoreCase("")) {
                Picasso.with(context).load(activeList.get(position).getOfferAcceptedOfProfilePic()).placeholder(R.drawable.default_p).into(holder.user_pic);
            }

        }

    }

    @Override
    public int getItemCount() {
        return activeList.size();
    }

    public void setListener(ActiveRequireProff listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        TextView tvArrived;
        @BindView(R.id.tvReviewInvoice)
        TextView tvReviewInvoice;
        @BindView(R.id.tvArrived)
        TextView tvInvoiceCreated;
        @BindView(R.id.tv_profile_name)
        TextView tv_profile_name;
        @BindView(R.id.tv_rat_num)
        TextView tv_rat_num;
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
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.rl_mainRating)
        RelativeLayout rl_mainRating;

        @BindView(R.id.tvMsdTrack)
        TextView tvMsdTrack;



        @BindView(R.id.webUrl)
        WebView webUrl;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            rl_mainRating.setOnClickListener(this);
            tvReviewInvoice.setOnClickListener(this);
            tvMsdTrack.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvMsdTrack:
                    if(listener !=null) {
                        listener.onMessageTrack(activeList.get(getAdapterPosition()));
                    }

                    break;

//                case R.id.ll_msg:
//                    context.startActivity(NUMessageDeliveryPersonActivity.getIntent(context,activeList.get(getAdapterPosition()),"FromNormal"));
//                    break;
//                case R.id.ll_track:
//                    context.startActivity(ActiveTrackingActivity.getIntent(context, activeList.get(getAdapterPosition()), "FromNUActive"));
//                    break;
                case R.id.rl_mainRating:
                    context.startActivity(UserDetailsActivity.getIntent(context, activeList.get(getAdapterPosition()), "FromNUActive"));
                    break;
                case R.id.tvReviewInvoice:
                    if (tvReviewInvoice.getText().toString().equalsIgnoreCase("Review\nInvoice")) {
                        webUrl.getSettings().setJavaScriptEnabled(true);
                        webUrl.loadUrl("http://docs.google.com/gview?embedded=true&url=" + activeList.get(getAdapterPosition()).getInvoicePdf());
                    }
                    break;
//                case R.id.ll_call:
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:"+mobile));
//                    context.startActivity(intent);
//                    break;
            }
        }
    }

    public interface ActiveRequireProff {
        void onReportOrder(View v, int pos);

        void onMessageTrack(NormalUserPendingOrderInner getData);

    }


    private String[] getDateTime(String getDate) {

        String server_format = getDate;    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date date = null;
        try {
            date = sdf.parse(server_format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        String your_format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        System.out.println(your_format);
        String[] splitted = your_format.split(" ");
        System.out.println(splitted[1]);    //The second part of the splitted string, i.e time

        return splitted;
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
            String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
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
