package com.pagin.azul.adapter;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

public class NUPendingDeliveryPersonAdapter extends RecyclerView.Adapter<NUPendingDeliveryPersonAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pendingList;
    private ViewOffersInterface listener;

    public NUPendingDeliveryPersonAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }

    @NonNull
    @Override
    public NUPendingDeliveryPersonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pending_nu_delivery_persong, null, false);

        return new NUPendingDeliveryPersonAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUPendingDeliveryPersonAdapter.MyViewHolder holder, int position) {

        if (pendingList.get(position).getServiceType().equalsIgnoreCase("DeliveryPersion")) {
            String getDate = pendingList.get(position).getCreatedAt();
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


            holder.ordr_tym.setText("Require " + pendingList.get(position).getSeletTime());
            holder.tv_orderid.setText(pendingList.get(position).getOrderNumber());
            holder.tvOrderdetail.setText(pendingList.get(position).getOrderDetails());

            holder.tvAllOffers.setText("View All Offered (" + pendingList.get(position).getTotalOffer() + ")");
            holder.tv_address_detail.setText(pendingList.get(position).getPickupLocation());
            holder.dropLoc.setText(pendingList.get(position).getDropOffLocation());
            holder.drop_dis.setText(pendingList.get(position).getCurrentToPicupLocation() + "km");
            holder.tv_distance.setText(pendingList.get(position).getPickupToDropLocation() + "km");
//            holder.dropLoc.setVisibility(View.GONE);
//            holder.tv_distance.setVisibility(View.GONE);
//            holder.tv_pickup.setVisibility(View.GONE);
//            holder.view3.setVisibility(View.GONE);
//            holder.tv_point3.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public void setListener(ViewOffersInterface listener) {
        this.listener = listener;
    }

    public interface ViewOffersInterface {

        void onAllOfferClick(View v, int pos, String id, String userId);

        void onEditClick(View v, int pos);

        void onCancelClick(NormalUserPendingOrderInner getData);


    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_point3)
        ImageView tv_point3;
        @BindView(R.id.view3)
        View view3;
        @BindView(R.id.tv_pickup)
        TextView tv_pickup;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_all_offers)
        TextView tvAllOffers;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_address_detail)
        TextView tv_address_detail;
        @BindView(R.id.starttopickup)
        TextView drop_dis;
        @BindView(R.id.tv_distance)
        TextView tv_distance;

        @BindView(R.id.dropLoc)
        TextView dropLoc;

        @BindView(R.id.tv_orderid)
        TextView tv_orderid;
        @BindView(R.id.tv_order)
        TextView tv_order;
        @BindView(R.id.ordr_tym)
        TextView ordr_tym;



        @BindView(R.id.order_details)
        TextView tvOrderdetail;
        @BindView(R.id.mainLayout)
        RelativeLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvAllOffers.setOnClickListener(this);
            tvEdit.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_all_offers:
                    if (listener != null) {
                        if (pendingList.get(getAdapterPosition()).getTotalOffer().equalsIgnoreCase("0")) {
                            showDialog();
                        } else {
                            listener.onAllOfferClick(view, getAdapterPosition(), pendingList.get(getAdapterPosition()).get_id(), pendingList.get(getAdapterPosition()).getUserId());

                        }
                    }
                    break;

                case R.id.tv_edit:
                    if (listener != null) {
                        listener.onEditClick(view, getAdapterPosition());
                    }
                    break;
                case R.id.tv_cancel:
                    if (listener != null) {
                        listener.onCancelClick(pendingList.get(getAdapterPosition()));
                    }
                    break;
            }
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText("No offer found for this request at this time.");
        registerNow.setVisibility(View.GONE);
        notNow.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
