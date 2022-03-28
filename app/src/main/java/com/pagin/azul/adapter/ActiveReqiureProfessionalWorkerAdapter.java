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
import com.pagin.azul.activities.UserDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveReqiureProfessionalWorkerAdapter extends RecyclerView.Adapter<ActiveReqiureProfessionalWorkerAdapter.ActiveViewHolder> {

    private Context context;
    private ArrayList<String> activeList;
    private ActiveReqiureProfessionalWorkerAdapter.ActiveRequireProff listener;

    public ActiveReqiureProfessionalWorkerAdapter(Context context, ArrayList<String> activeList) {
        this.context = context;
        this.activeList = activeList;
    }

    @NonNull
    @Override
    public ActiveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_active_professional_worker_custom, null, false);

        return new ActiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveViewHolder activeViewHolder, int i) {

        activeViewHolder.tvProfileName.setText(activeList.get(i));

    }

    @Override
    public int getItemCount() {
        return activeList.size();
    }
    public void setListener(ActiveReqiureProfessionalWorkerAdapter.ActiveRequireProff listener) {
        this.listener = listener;
    }

    public class ActiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_profile_name)
        TextView tvProfileName;
        @BindView(R.id.tv_report_order)
        TextView tvReportOrder;
        @BindView(R.id.tv_cancel_order)
        TextView tvCancelOrder;
        @BindView(R.id.tvRatingReviewAll)
        TextView tvRatingReviewAll;

        @BindView(R.id.ll_msg)
        LinearLayout llMsg;

        @BindView(R.id.ll_track)
        LinearLayout llTrack;
        public ActiveViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvReportOrder.setOnClickListener(this);
            tvCancelOrder.setOnClickListener(this);
            llMsg.setOnClickListener(this);
            llTrack.setOnClickListener(this);
            tvRatingReviewAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.tv_report_order:
                    if (listener != null) {
                        listener.onReportOrder(v, getAdapterPosition());
                    }
                    break;

                case R.id.tv_cancel_order:
                    if (listener != null) {
                        listener.onCancelOrder(v, getAdapterPosition());
                    }
                    break;
                case R.id.ll_msg:

                    context.startActivity(MessageActivity.getIntent(context));
                    break;

                case R.id.ll_track:
                    //context.startActivity(ActiveTrackingActivity.getIntent(context));
                    break;

                case R.id.tvRatingReviewAll:
                    context.startActivity(new Intent(context, UserDetailsActivity.class));
                    break;
            }

        }
    }
    public interface ActiveRequireProff {
        void onReportOrder(View v, int pos);

        void onCancelOrder(View v, int pos);

    }

}
