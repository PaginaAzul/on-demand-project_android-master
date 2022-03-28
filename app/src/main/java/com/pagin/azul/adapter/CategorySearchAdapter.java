package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.CategoryVoewHolder> {
    private Context context;
    private CommonListener commonListener;
    private ArrayList<CommonResponseBean> subCategoryData;

    public CategorySearchAdapter(Context context, CommonListener commonListener, ArrayList<CommonResponseBean> subCategoryData) {
        this.context = context;
        this.commonListener = commonListener;
        this.subCategoryData = subCategoryData;
    }

    @NonNull
    @Override
    public CategoryVoewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_item, viewGroup, false);
        return new CategoryVoewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVoewHolder categoryVoewHolder, int i) {
        if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
            categoryVoewHolder.tvCategorySearch.setText(subCategoryData.get(i).getSubCategoryName());
        }else{
            categoryVoewHolder.tvCategorySearch.setText(subCategoryData.get(i).getPortugueseSubCategoryName());
        }
        Glide.with(context)
                .load(subCategoryData.get(i).getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                        .error(R.drawable.img))
                .into(categoryVoewHolder.ivCategorySearch);
    }

    @Override
    public int getItemCount() {
        return subCategoryData.size();
    }

    public class CategoryVoewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCategoryName)
        TextView tvCategorySearch;
        @BindView(R.id.ivCategory)
        ImageView ivCategorySearch;
        public CategoryVoewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.clCategory})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clCategory:
                    commonListener.onItemClick(getAdapterPosition(),subCategoryData);
                    break;
            }
        }
    }

    public void update(ArrayList<CommonResponseBean> list){
        this.subCategoryData = list;
        notifyDataSetChanged();
    }
}
