package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.AddToCartViewHolder> {
    private Context context;
    private List<MenuItem> menuList;

    public MenuAdapter(Context context, List<MenuItem> menu) {
        this.context = context;
        this.menuList = menu;
    }

    @NonNull
    @Override
    public AddToCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_cart_items_horizontal, viewGroup, false);
        return new AddToCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddToCartViewHolder holder, final int position) {

        holder.tvMenuitems.setText(menuList.get(position).getMenu());


        if (menuList.get(position).isCheck()) {
            holder.tabViewLine.setVisibility(View.VISIBLE);
            holder.tvMenuitems.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tvMenuitems.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.tabViewLine.setVisibility(View.GONE);

        }

        holder.clParent.setOnClickListener(v -> {
            for (int i = 0; i < menuList.size(); i++) {
                menuList.get(i).setCheck(false);
            }
            menuList.get(position).setCheck(true);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

    public class AddToCartViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvMenuitems)
        TextView tvMenuitems;

        @BindView(R.id.clParent)
        ConstraintLayout clParent;

        @BindView(R.id.tabViewLine)
        View tabViewLine;


        public AddToCartViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }



}
