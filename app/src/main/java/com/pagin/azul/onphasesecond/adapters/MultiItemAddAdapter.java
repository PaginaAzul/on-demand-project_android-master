package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.pagin.azul.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultiItemAddAdapter extends RecyclerView.Adapter<MultiItemAddAdapter.ChooseTasteHolder> {
    private Context context;

    private OnMutipleAddonClick listener;

    public MultiItemAddAdapter(Context context, OnMutipleAddonClick listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChooseTasteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_multi_addon, viewGroup, false);
        return new ChooseTasteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseTasteHolder holder, int i) {


        /*if (menList.get(i).isCheck())
        {
            holder.checkBox.setText(menList.get(i).getName());
            holder.tvPrice.setText(context.getResources().getString(R.string.usd) + menList.get(i).getPrice());
            holder.checkBox.setChecked(true);
            holder.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.mahroom));

        }
        else
        {
            holder.tvPrice.setText(context.getResources().getString(R.string.usd) + menList.get(i).getPrice());
            holder.checkBox.setText(menList.get(i).getName());
            holder.checkBox.setChecked(false);
            holder.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.colorGrey));
        }*/
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ChooseTasteHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkBox)
        CheckBox checkBox;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        public ChooseTasteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    /*if (isChecked) {
                        if (listener != null)
                            listener.onMultipleItemCheck(getAdapterPosition(), menList.get(getAdapterPosition()));
                            menList.get(getAdapterPosition()).setCheck(true);
                    } else {

                        if (listener != null)
                            listener.onMultiItemUncheck(getAdapterPosition(), menList.get(getAdapterPosition()));
                            menList.get(getAdapterPosition()).setCheck(false);
                    }*/


                }
            });


        }
    }

    public interface OnMutipleAddonClick {

        //void onMultipleItemCheck(int pos, AddonsEntity response);

        //void onMultiItemUncheck(int pos, AddonsEntity response);

    }


    /*public void updateList(List<AddonsEntity> menList) {

        this.menList = menList;
        notifyDataSetChanged();

    }*/

}