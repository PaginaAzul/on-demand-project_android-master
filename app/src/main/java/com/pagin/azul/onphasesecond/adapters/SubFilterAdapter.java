package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.model.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubFilterAdapter extends RecyclerView.Adapter<SubFilterAdapter.SubFilterViewHolder> {

    private Context context;
    private ArrayList<MenuItem> list;
    private CommonListener commonListener;

    public SubFilterAdapter(Context context, ArrayList<MenuItem> list,CommonListener commonListener) {
        this.context = context;
        this.list = list;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public SubFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_sub_category,parent, false);
        return new SubFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubFilterViewHolder holder, int position) {
        if (list.get(position).isCheck()) {
            holder.checkBox.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.checkBox.setChecked(true);

        } else {
            holder.checkBox.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setText(list.get(position).getMenu());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class SubFilterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBox)
        CheckBox checkBox;

        public SubFilterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick({R.id.checkBox})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.checkBox:
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setCheck(false);
                    }
                    list.get(getAdapterPosition()).setCheck(true);
                    commonListener.onFilterSubItemClick(getAdapterPosition(),list);
                    notifyDataSetChanged();
                    break;


            }
        }
    }

    public void update(ArrayList<MenuItem> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
