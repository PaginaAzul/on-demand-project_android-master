package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
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
import butterknife.OnClick;

public class NUPastProfWorkerAdapter extends RecyclerView.Adapter<NUPastProfWorkerAdapter.NUPastProfWorkerViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pastList;

    public NUPastProfWorkerAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pastList) {
        this.context = context;
        this.pastList = pastList;
    }

    @NonNull
    @Override
    public NUPastProfWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_past_normal_prof_layout, viewGroup, false);

        return new NUPastProfWorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUPastProfWorkerViewHolder holder, int position) {

        String getDate = pastList.get(position).getInvoiceCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

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
            /*String your_format;
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                your_format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(date);
            else
                your_format = new SimpleDateFormat("dd-MMMM-yyyy hh:mm a").format(date);*/
            String your_format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(date);
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
        holder.tvOrderLocation.setText(pastList.get(position).getPickupLocation());
        if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
            holder.tvCategory.setText(pastList.get(position).getSelectCategoryName());
            //holder.tvSubCategory.setText(pastList.get(position).getSelectSubCategoryName());
            String subCategoryName = pastList.get(position).getSelectSubCategoryName();
            if(subCategoryName!=null && !subCategoryName.equals("")){
                holder.tvSubCategory.setText(subCategoryName);
                holder.rlSubCategory.setVisibility(View.VISIBLE);
            }
            else{
                holder.rlSubCategory.setVisibility(View.GONE);
                //holder.tvSubCategory.setText(R.string.no_sub_category_found);
            }
        }else {
            holder.tvCategory.setText(pastList.get(position).getPortugueseCategoryName());
            //holder.tvSubCategory.setText(pastList.get(position).getSelectSubCategoryName());
            String subCategoryName = pastList.get(position).getPortugueseSubCategoryName();
            if(subCategoryName!=null && !subCategoryName.equals("")){
                holder.tvSubCategory.setText(subCategoryName);
                holder.rlSubCategory.setVisibility(View.VISIBLE);
            }
            else{
                holder.rlSubCategory.setVisibility(View.GONE);
                //holder.tvSubCategory.setText(R.string.no_sub_category_found);
            }
        }

        holder.tvOrderDetails.setText(pastList.get(position).getOrderDetails());

//        if (pastList.get(position).getMinimumOffer() != null && !pastList.get(position).getMinimumOffer().equalsIgnoreCase("")) {
//            holder.offerAmt.setText(pastList.get(position).getMinimumOffer() + " Only");
//        } else {
//            holder.offerAmt.setText("0.00" + " Only");
//        }

        String currency = pastList.get(position).getCurrency()!=null?pastList.get(position).getCurrency():"";
        if(pastList.get(position).getInvoiceSubtoatl() != null && !pastList.get(position).getInvoiceSubtoatl().equals("0")){
            holder.rlAdditionalCost.setVisibility(View.VISIBLE);
            holder.tvPrfOfr.setText(pastList.get(position).getInvoiceSubtoatl() + " "+currency);
        }else {
            holder.rlAdditionalCost.setVisibility(View.GONE);
            holder.tvPrfOfr.setText("0 "+currency);
        }

        holder.offerAmt.setText(pastList.get(position).getDeliveryOffer() + " "+currency);
        holder.tvTax.setText(pastList.get(position).getTax() + " "+currency);
        holder.tvTotal.setText(pastList.get(position).getTotal() + " "+currency);
        if (pastList.get(position).getRatingData() != null && pastList.get(position).getRatingData().size() > 0) {
            holder.tv_rat_num.setText(String.valueOf(pastList.get(position).getRatingData().get(0).getRate()));
            //holder.tv_rat_num.setText(String.valueOf(pastList.get(position).getRatingData().get(pastList.get(position).getRatingData().size()-1).getRate()));
            //holder.tvratingtxt.setText(pastList.get(position).getRatingData().get(0).getComments());
        }
    }

    @Override
    public int getItemCount() {
        return pastList.size();
    }

    public class NUPastProfWorkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.tvCategory)
        TextView tvCategory;
        @BindView(R.id.tvSubCategory)
        TextView tvSubCategory;
        @BindView(R.id.tvOrderDetails)
        TextView tvOrderDetails;
        @BindView(R.id.tvOrderLocation)
        TextView tvOrderLocation;
        @BindView(R.id.rlRating)
        RelativeLayout rlRating;
        @BindView(R.id.rlSubCategory)
        RelativeLayout rlSubCategory;
        @BindView(R.id.rlAdditionalCost)
        RelativeLayout rlAdditionalCost;


        public NUPastProfWorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.rlRating})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlRating:
                    /*if(pastList.get(getAdapterPosition()).getRatingData()==null ||  pastList.get(getAdapterPosition()).getRatingData().size()==0){
                        context.startActivity(RatingAndRiviewActivity.getIntent(context, pastList.get(getAdapterPosition()), "PastNUP"));
                    }*/
                    break;
            }
        }
    }
}
