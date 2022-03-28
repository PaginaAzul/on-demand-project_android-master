package com.pagin.azul.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.RatingAndRiviewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastRequireProfessionalWorkerAdapter extends RecyclerView.Adapter<PastRequireProfessionalWorkerAdapter.PastViewHolder> {
    private Context context;
    private ArrayList<String> pastList;

    public PastRequireProfessionalWorkerAdapter(Context context, ArrayList<String> pastList) {
        this.context = context;
        this.pastList = pastList;
    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.require_past_professional_work_custom,null,false);

        return new PastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastViewHolder pastViewHolder, int i) {
        pastViewHolder.tvTax.setText(pastList.get(i));
    }

    @Override
    public int getItemCount() {
        return pastList.size();
    }

    public class PastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tax)
        TextView tvTax;
        @BindView(R.id.tvGiveYourRating)
        TextView tvGiveYourRating;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            tvGiveYourRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, RatingAndRiviewActivity.class));
                }
            });

        }
    }
}
