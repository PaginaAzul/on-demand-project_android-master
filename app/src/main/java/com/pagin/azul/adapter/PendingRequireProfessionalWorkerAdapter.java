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

public class PendingRequireProfessionalWorkerAdapter extends RecyclerView.Adapter<PendingRequireProfessionalWorkerAdapter.PendingViewHolder> {

    private Context context;
    private ArrayList<String> pendingList;
    private PendingRequireProfessionalWorkerAdapter.ViewOffersInterface listener;

    public PendingRequireProfessionalWorkerAdapter(Context context, ArrayList<String> pendingList) {
        this.context = context;
        this.pendingList = pendingList;
    }

    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pending_professional_worker_custom, viewGroup, false);

        return new PendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder pendingViewHolder, int i) {
        pendingViewHolder.tvDistance.setText(pendingList.get(i));

    }

    @Override
    public int getItemCount() {
        return pendingList.size();
    }

    public void setListener(PendingRequireProfessionalWorkerAdapter.ViewOffersInterface listener) {
        this.listener = listener;
    }

    public interface ViewOffersInterface {

        void onAllOfferClick(View v, int pos);

        void onEditClick(View v, int pos);

        void onCancelClick(View v, int pos);


    }

    public class PendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.tv_all_offers)
        TextView tvAllOffers;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        public PendingViewHolder(@NonNull View itemView) {
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
                        listener.onAllOfferClick(v, getAdapterPosition());
                    }
                    break;

                case R.id.tv_edit:
                    if (listener != null) {
                        listener.onEditClick(v, getAdapterPosition());
                    }
                    break;
                case R.id.tv_cancel:
                    if (listener != null) {
                        listener.onCancelClick(v, getAdapterPosition());
                    }
                    break;
            }

        }
    }
}
