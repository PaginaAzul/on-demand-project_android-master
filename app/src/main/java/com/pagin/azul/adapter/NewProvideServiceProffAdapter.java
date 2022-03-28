package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProvideServiceProffAdapter extends RecyclerView.Adapter<NewProvideServiceProffAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> newList;

    public NewProvideServiceProffAdapter(Context context, ArrayList<String> newList) {
        this.context = context;
        this.newList = newList;
    }

    @NonNull
    @Override
    public NewProvideServiceProffAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_new_professional_worker_dashboard,null,false);

        return new NewProvideServiceProffAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewProvideServiceProffAdapter.MyViewHolder holder, int position) {

        holder.tvProfileName.setText(newList.get(position));
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
