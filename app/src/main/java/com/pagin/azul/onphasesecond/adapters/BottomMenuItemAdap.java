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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomMenuItemAdap extends RecyclerView.Adapter<BottomMenuItemAdap.MenuItemViewHolder> {

    private Context context;

    public BottomMenuItemAdap(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_menu_items_bottom_sheet,viewGroup,false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder menuItemViewHolder, int i) {
        if(i==0){
            menuItemViewHolder.tv_memu_name.setTextColor(context.getResources().getColor(R.color.mahroom));
            menuItemViewHolder.tvItemCount.setTextColor(context.getResources().getColor(R.color.mahroom));
        }else {
            menuItemViewHolder.tv_memu_name.setTextColor(context.getResources().getColor(R.color.colorGrey));
            menuItemViewHolder.tvItemCount.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_memu_name)
        TextView tv_memu_name;

        @BindView(R.id.tvItemCount)
        TextView tvItemCount;

        @BindView(R.id.clParentMenu)
        ConstraintLayout clParentMenu;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            clParentMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //listener.onClickMenu(getAdapterPosition(),menList.get(getAdapterPosition()));
                }
            });

        }
    }
}
