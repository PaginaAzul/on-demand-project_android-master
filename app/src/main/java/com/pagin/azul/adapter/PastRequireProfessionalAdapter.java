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
import butterknife.OnClick;

public class PastRequireProfessionalAdapter extends RecyclerView.Adapter<PastRequireProfessionalAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> pastList;

    public PastRequireProfessionalAdapter(Context context, ArrayList<String> pastList) {
        this.context = context;
        this.pastList = pastList;
    }

    @NonNull
    @Override
    public PastRequireProfessionalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_past_require_professional, null, false);

        return new PastRequireProfessionalAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastRequireProfessionalAdapter.MyViewHolder holder, int position) {

        holder.tvTax.setText(pastList.get(position));
    }

    @Override
    public int getItemCount() {
        return pastList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tax)
        TextView tvTax;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


        @OnClick({R.id.rl_ratingClick})
         void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_ratingClick:
                    //context.startActivity(RatingAndRiviewActivity.getIntent(context));
                    break;
            }
        }
    }
}
