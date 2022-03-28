package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private Context context;
    private ArrayList<MenuItem> list;
    private CommonListener commonListener;

    public FilterAdapter(Context context, ArrayList<MenuItem> list, CommonListener commonListener) {
        this.context = context;
        this.list = list;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_filter, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        if (list.get(position).isCheck()) {
            holder.tvItem.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvItem.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        } else {
            holder.tvItem.setTextColor(ContextCompat.getColor(context, R.color.filter_grey));
            holder.tvItem.setBackgroundColor(context.getResources().getColor(R.color.filter_grey2));
        }

        holder.tvItem.setText(list.get(position).getMenu());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItem)
        TextView tvItem;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick({R.id.tvItem})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvItem:
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setCheck(false);
                    }
                    list.get(getAdapterPosition()).setCheck(true);
                    commonListener.onFilterItemClick(getAdapterPosition(),list);
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
