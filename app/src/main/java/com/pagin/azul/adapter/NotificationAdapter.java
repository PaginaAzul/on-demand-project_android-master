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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.OfferHolder> {

    private Context context;
    private ArrayList<NormalUserPendingOrderInner> notiList;

    public NotificationAdapter(Context context, ArrayList<NormalUserPendingOrderInner> notiList) {
        this.context = context;
        this.notiList = notiList;
    }

    @NonNull
    @Override
    public NotificationAdapter.OfferHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notification, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        return new NotificationAdapter.OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.OfferHolder holder, int position) {

        holder.tvNotitxt.setText(notiList.get(position).getNotiMessage());


        String getDate = notiList.get(position).getCreatedAt();

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

            holder.tv_date.setText(String.valueOf(splitted[0])+" "+splitted[1]+" "+splitted[2]);
            //holder.tv_time.setText(String.valueOf(splitted[1]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }


    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    public class OfferHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_noti_txt)
        TextView tvNotitxt;
        @BindView(R.id.tv_date)
        TextView tv_date;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
