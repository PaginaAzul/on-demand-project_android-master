package com.pagin.azul.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pagin.azul.R;
import com.pagin.azul.activities.MapCategoriesShowActivity;
import com.pagin.azul.bean.CateResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapCategoriesList extends RecyclerView.Adapter<MapCategoriesList.CategoryHolder> {

    private Context context;
    private ArrayList<CateResponse> cateList;

    public MapCategoriesList(Context context, ArrayList<CateResponse> cateList) {
        this.context = context;
        this.cateList = cateList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.map_categories_layout, null, false);
        return new MapCategoriesList.CategoryHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int postion) {

        holder.cate_txt.setText(cateList.get(postion).getCategoriesName());

        Glide.with(context).load(cateList.get(postion).getCategoriesImg()).into(holder.cate_img);


    }

    @Override
    public int getItemCount() {
        return cateList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cate_txt)
        TextView cate_txt;
        @BindView(R.id.cate_img)
        ImageView cate_img;
        @BindView(R.id.rlMain)
        ConstraintLayout rlMain;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rlMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rlMain:
                    ((Activity) context).startActivityForResult(MapCategoriesShowActivity.getIntent(context, cateList.get(getAdapterPosition()).getCategoriesName()), 111);
                    break;
            }
        }
    }
}
