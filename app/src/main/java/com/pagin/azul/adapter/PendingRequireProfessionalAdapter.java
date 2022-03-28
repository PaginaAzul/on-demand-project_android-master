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

public class PendingRequireProfessionalAdapter extends RecyclerView.Adapter<PendingRequireProfessionalAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> pendingList;
    private ViewOffersInterface listener;

    public PendingRequireProfessionalAdapter(Context context, ArrayList<String> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }

    @NonNull
    @Override
    public PendingRequireProfessionalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pending_require_professional, null, false);

        return new PendingRequireProfessionalAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequireProfessionalAdapter.MyViewHolder holder, int position) {

        holder.tvDistance.setText(pendingList.get(position));
    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public void setListener(ViewOffersInterface listener) {
        this.listener = listener;
    }

    public interface ViewOffersInterface {

        void onAllOfferClick(View v, int pos);

        void onEditClick(View v, int pos);

        void onCancelClick(View v, int pos);


    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.tv_all_offers)
        TextView tvAllOffers;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;

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
                        listener.onAllOfferClick(view, getAdapterPosition());
                    }
                    break;

                case R.id.tv_edit:
                    if (listener != null) {
                        listener.onEditClick(view, getAdapterPosition());
                    }
                    break;
                case R.id.tv_cancel:
                    if (listener != null) {
                        listener.onCancelClick(view, getAdapterPosition());
                    }
                    break;
            }
        }
    }
}
