package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WalletTranstionHistoryAdapter extends RecyclerView.Adapter<WalletTranstionHistoryAdapter.MyWalletHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> walletList;

    public WalletTranstionHistoryAdapter(Context context,ArrayList<NormalUserPendingOrderInner> walletList) {
        this.context = context;
        this.walletList = walletList;
    }

    @NonNull
    @Override
    public MyWalletHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_delivery_worker_custom, viewGroup, false);

        return new MyWalletHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWalletHolder holder, int position) {


        String getDate = walletList.get(position).getInvoiceCreatedAt();

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

            holder.tvDateAndTime.setText(String.valueOf(splitted[1]+"  "+String.valueOf(splitted[0])));
            //holder.tv_time.setText(String.valueOf(splitted[1]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
        holder.tvTotalProWorker.setText(walletList.get(position).getInvoiceTotal()+"SAR");
        holder.tvDeliveryOffersProWorker.setText(walletList.get(position).getInvoiceSubtoatl()+"SAR");
        holder.tvTaxProWorker.setText(walletList.get(position).getInvoiceTax()+"SAR");
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    public class MyWalletHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTotalProWorker)
        TextView tvTotalProWorker;
        @BindView(R.id.tvTaxProWorker)
        TextView tvTaxProWorker;
        @BindView(R.id.tvDeliveryOffersProWorker)
        TextView tvDeliveryOffersProWorker;

 @BindView(R.id.tvDateAndTime)
        TextView tvDateAndTime;


        public MyWalletHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
