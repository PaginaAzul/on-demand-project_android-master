package com.pagin.azul.adapter;

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
import com.pagin.azul.bean.CateResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapCategoriesAdapter extends RecyclerView.Adapter<MapCategoriesAdapter.CategoryHolder> {
    private SelectCategoryInterface listener;
    private Context context;
    private ArrayList<CateResponse> cateList;

    public MapCategoriesAdapter(Context context, ArrayList<CateResponse> cateList) {
        this.context = context;
        this.cateList = cateList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.map_categories_layout, null, false);
        return new CategoryHolder(view);

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

    public void setListener(SelectCategoryInterface listener) {
        this.listener = listener;
    }

    public interface SelectCategoryInterface {

        void onCategoryClick(String name);


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
                    if (listener != null) {
                        listener.onCategoryClick(cateList.get(getAdapterPosition()).getCategoriesName());
                    }
                    break;
            }
        }
    }


}
