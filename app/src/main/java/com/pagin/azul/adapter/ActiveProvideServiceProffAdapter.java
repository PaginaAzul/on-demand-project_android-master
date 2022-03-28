package com.pagin.azul.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.MessageActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveProvideServiceProffAdapter extends RecyclerView.Adapter<ActiveProvideServiceProffAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> activeList;

    public ActiveProvideServiceProffAdapter(Context context, ArrayList<String> activeList) {
        this.context = context;
        this.activeList = activeList;
    }

    @NonNull
    @Override
    public ActiveProvideServiceProffAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_professional_worker_offers_custom,null,false);

        return new ActiveProvideServiceProffAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveProvideServiceProffAdapter.MyViewHolder holder, int position) {

        holder.tvProfileName.setText(activeList.get(position));
    }

    @Override
    public int getItemCount() {
        return activeList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;

        @BindView(R.id.ll_msg)
        LinearLayout ll_msg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            ll_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MessageActivity.class));
                }
            });

        }
    }
}
