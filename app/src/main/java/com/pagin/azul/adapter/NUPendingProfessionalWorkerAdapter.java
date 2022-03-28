package com.pagin.azul.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pagin.azul.Constant.Constants.kCategoryName;

public class NUPendingProfessionalWorkerAdapter extends RecyclerView.Adapter<NUPendingProfessionalWorkerAdapter.NUPendingProfWorkViewHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> pendingList;
    private NUPendingProfessionalWorkerAdapter.ViewOffersInterface listener;

    public NUPendingProfessionalWorkerAdapter(Context context, ArrayList<NormalUserPendingOrderInner> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }

    @NonNull
    @Override
    public NUPendingProfWorkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.nu_pending_professional_worker_custom, viewGroup, false);

        return new NUPendingProfessionalWorkerAdapter.NUPendingProfWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NUPendingProfWorkViewHolder holder, int position) {

        String getDate = pendingList.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = sdf.parse(server_format);
            System.out.println(date);
            //String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
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
            holder.tv_time.setText(String.valueOf(splitted[1]+" "+splitted[2]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }

        if (pendingList.get(position).getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
            //holder.order_detail.setText(pendingList.get(position).getSeletTime());
            String selectedTime = pendingList.get(position).getSeletTime();
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                if(selectedTime.equalsIgnoreCase("Daqui a 1 hora"))
                    holder.order_detail.setText("In 1 hr");
                else if(selectedTime.equalsIgnoreCase("Daqui a 2 hora"))
                    holder.order_detail.setText("In 2 hr");
                else if(selectedTime.equalsIgnoreCase("Daqui a 3 hora"))
                    holder.order_detail.setText("In 3 hr");
                else if(selectedTime.equalsIgnoreCase("Acima a 5 hora"))
                    holder.order_detail.setText("In 5+ hr");
                else if(selectedTime.equalsIgnoreCase("Daqui a 1 dia"))
                    holder.order_detail.setText("In 1 day");
                else if(selectedTime.equalsIgnoreCase("Daqui a 2 dias"))
                    holder.order_detail.setText("In 2 days");
                else if(selectedTime.equalsIgnoreCase("Daqui a 3 dias"))
                    holder.order_detail.setText("In 3 days");
                else
                    holder.order_detail.setText(selectedTime);
            }else{
                if(selectedTime.equalsIgnoreCase("In 1 hr"))
                    holder.order_detail.setText("Daqui a 1 hora");
                else if(selectedTime.equalsIgnoreCase("In 2 hr"))
                    holder.order_detail.setText("Daqui a 2 hora");
                else if(selectedTime.equalsIgnoreCase("In 3 hr"))
                    holder.order_detail.setText("Daqui a 3 hora");
                else if(selectedTime.equalsIgnoreCase("In 5+ hr"))
                    holder.order_detail.setText("Acima a 5 hora");
                else if(selectedTime.equalsIgnoreCase("In 1 day"))
                    holder.order_detail.setText("Daqui a 1 dia");
                else if(selectedTime.equalsIgnoreCase("In 2 days"))
                    holder.order_detail.setText("Daqui a 2 dias");
                else if(selectedTime.equalsIgnoreCase("In 3 days"))
                    holder.order_detail.setText("Daqui a 3 dias");
                else
                    holder.order_detail.setText(selectedTime);
                //holder.msg.setText(context.getString(R.string.require)+" "+activeList.get(position).getSeletTime());
            }
            holder.tv_orderid.setText(pendingList.get(position).getOrderNumber());
            holder.tv_orderDetail.setText(pendingList.get(position).getOrderDetails());
            //holder.tv_phn.setText(pendingList.get(position).getMobileNumber());
            holder.tvAllOffers.setText(context.getString(R.string.view_all_offers)+" (" + pendingList.get(position).getTotalOffer() + ")");
            holder.tv_add_detail.setText(pendingList.get(position).getPickupLocation());
            //holder.dropLoc.setText("DropOff Location- " + pendingList.get(position).getDropOffLocation());
            /*double time = Double.parseDouble(pendingList.get(position).getCurrentToPicupLocation());
            DecimalFormat df = new DecimalFormat("#.##");
            time = Double.parseDouble(df.format(time));
            holder.tvDistance.setText(time + "km");*/
            holder.tvDistance.setText(pendingList.get(position).getCurrentToPicupLocation() + " KM");
            holder.prf_wrk.setText(R.string.me);
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                holder.tvCategoryName.setText(pendingList.get(position).getSelectCategoryName());
                //holder.tvSubCategoryName.setText(pendingList.get(position).getSelectSubCategoryName());
                String subCategoryName = pendingList.get(position).getSelectSubCategoryName();
                if(subCategoryName!=null && !subCategoryName.equals("")){
                    holder.tvSubCategoryName.setText(subCategoryName);
                    holder.clSubCategory.setVisibility(View.VISIBLE);
                }
                else{
                    holder.clSubCategory.setVisibility(View.GONE);
                    //holder.tvSubCategoryName.setText(R.string.no_sub_category_found);
                }
            }else{
                holder.tvCategoryName.setText(pendingList.get(position).getPortugueseCategoryName());
                //holder.tvSubCategoryName.setText(pendingList.get(position).getSelectSubCategoryName());
                String subCategoryName = pendingList.get(position).getPortugueseSubCategoryName();
                if(subCategoryName!=null && !subCategoryName.equals("")){
                    holder.tvSubCategoryName.setText(subCategoryName);
                    holder.clSubCategory.setVisibility(View.VISIBLE);
                }
                else{
                    holder.clSubCategory.setVisibility(View.GONE);
                    //holder.tvSubCategoryName.setText(R.string.no_sub_category_found);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public void setListener(NUPendingProfessionalWorkerAdapter.ViewOffersInterface listener) {
        this.listener = listener;
    }


    public interface ViewOffersInterface {

        void onAllOfferClick(View v, int pos, String id, String userId);

        void onEditClick(View v, int pos);

        void onCancelClick(NormalUserPendingOrderInner getData);


    }

    public class NUPendingProfWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.tv_all_offers)
        TextView tvAllOffers;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.prf_wrk)
        TextView prf_wrk;
        @BindView(R.id.tv_orderid)
        TextView tv_orderid;
        @BindView(R.id.tv_orderDetail)
        TextView tv_orderDetail;
        @BindView(R.id.tv_add_detail)
        TextView tv_add_detail;
//        @BindView(R.id.tv_phn)
//        TextView tv_phn;
        @BindView(R.id.order_detail)
        TextView order_detail;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;
        @BindView(R.id.tvSubCategoryName)
        TextView tvSubCategoryName;
        @BindView(R.id.clSubCategory)
        ConstraintLayout clSubCategory;


        public NUPendingProfWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvAllOffers.setOnClickListener(this);
            tvEdit.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_all_offers:
                    if (listener != null) {
                        if (pendingList.get(getAdapterPosition()).getTotalOffer().equalsIgnoreCase("0")) {
                            showDialog();
                        } else {
                            listener.onAllOfferClick(v, getAdapterPosition(), pendingList.get(getAdapterPosition()).get_id(), pendingList.get(getAdapterPosition()).getUserId());
                            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                                SharedPreferenceWriter.getInstance(context).writeStringValue(kCategoryName, pendingList.get(getAdapterPosition()).getSelectCategoryName());
                            }else {
                                SharedPreferenceWriter.getInstance(context).writeStringValue(kCategoryName, pendingList.get(getAdapterPosition()).getPortugueseCategoryName());
                            }
                        }
                    }
                    break;

                case R.id.tv_edit:
                    if (listener != null) {
                        listener.onEditClick(v, getAdapterPosition());
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


        textView.setText(R.string.no_offer_found_for_this_request_at_this_time);
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
