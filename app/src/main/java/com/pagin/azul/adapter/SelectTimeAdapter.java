package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.SelectTimeBean;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.HourHolder> {
    private Context context;
    //private CopyOnWriteArrayList<String> dataData;
    private String[] dataData;
    public CallBack callBack;
    private ArrayList<SelectTimeBean> selectTimeList;

    /*public SelectTimeAdapter(Context context, CopyOnWriteArrayList<String> dataData) {
        this.context = context;
        this.dataData = dataData;
    }*/

    public SelectTimeAdapter(Context context, String[] dataData) {
        this.context = context;
        this.dataData = dataData;
    }

    public SelectTimeAdapter(Context context, ArrayList<SelectTimeBean> selectTimeList) {
        this.context = context;
        this.selectTimeList = selectTimeList;
    }

    @NonNull
    @Override
    public HourHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_select_tym_list_layout, null, false);
        return new HourHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourHolder hourHolder, int position) {
        //hourHolder.tvTime.setText(dataData[position]);
        hourHolder.tvTime.setText(selectTimeList.get(position).getTime());
        if(selectTimeList.get(position).isSelected())
            hourHolder.tvTime.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        else
            hourHolder.tvTime.setTextColor(context.getResources().getColor(R.color.blacklight));
    }

    public void onClickAtAdapter(CallBack callBack) {
        this.callBack = callBack;

    }

    public interface CallBack {
      public void onClickAtPosition(int adapterPosition);
    }

    @Override
    public int getItemCount() {
        return selectTimeList.size();
    }

    public class HourHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_timeList)
        TextView tvTime;

        public HourHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.tv_timeList, R.id.l_container})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_timeList:
                    //tvTime.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    callBack.onClickAtPosition(getAdapterPosition());
                    for(int i=0;i<selectTimeList.size();i++){
                        selectTimeList.get(i).setSelected(false);
                    }
                    selectTimeList.get(getAdapterPosition()).setSelected(true);
                    notifyDataSetChanged();
                    break;

            }
        }

    }

}
