package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.model.ProductResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private Context context;
    private ArrayList<ProductResponse> list;
    private String type;
    private CommonListener commonListener;

    public TimeSlotAdapter(Context context, ArrayList<ProductResponse> list, String type, CommonListener commonListener) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_time_slot,parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        holder.tvTimeSlot.setText(list.get(position).getOpenTime() + " - " + list.get(position).getCloseTime());
        if(list.get(position).isSelected()){
            holder.tvTimeSlot.setBackgroundResource(R.drawable.bg_blue_btn);
            holder.tvTimeSlot.setTextColor(Color.WHITE);
        }else {
            holder.tvTimeSlot.setBackgroundResource(R.drawable.bg_light_radius_white);
            holder.tvTimeSlot.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTimeSlot)
        TextView tvTimeSlot;
        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.tvTimeSlot})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.tvTimeSlot:
                    if(type.equalsIgnoreCase("Morning"))
                        commonListener.onSelectMorningSlot(getAdapterPosition());
                    else if(type.equalsIgnoreCase("Afternoon"))
                        commonListener.onSelectAfternoonSlot(getAdapterPosition());
                    else if(type.equalsIgnoreCase("Evening"))
                        commonListener.onSelectEveningSlot(getAdapterPosition());
                    break;
            }
        }
    }
}
