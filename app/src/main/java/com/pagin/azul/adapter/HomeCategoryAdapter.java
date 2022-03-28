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
import com.pagin.azul.fragment.HomeCategoryFrag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.HomeCategoryViewHolder> {
    private Context context;
    private CommonListener commonListener;
    private ArrayList<CommonResponseBean> commonResponses;
    private String type;

    public HomeCategoryAdapter(Context context, CommonListener commonListener, ArrayList<CommonResponseBean> commonResponses,String type) {
        this.context = context;
        this.commonListener = commonListener;
        this.commonResponses = commonResponses;
        this.type = type;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_category_item, viewGroup, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryViewHolder homeCategoryViewHolder, int i) {
        if(type.equalsIgnoreCase(HomeCategoryFrag.class.getSimpleName())){
            Glide.with(context)
                    .load(commonResponses.get(i).getCategoryImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                            .error(R.drawable.default_cat))
                    .into(homeCategoryViewHolder.ivCategory);
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                homeCategoryViewHolder.tvCategoryName.setText(commonResponses.get(i).getCategoryName());
            }else{
                homeCategoryViewHolder.tvCategoryName.setText(commonResponses.get(i).getPortugueseCategoryName());
            }
        }else{
            Glide.with(context)
                    .load(commonResponses.get(i).getImage())
                    .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                            .error(R.drawable.default_cat))
                    .into(homeCategoryViewHolder.ivCategory);
            if(SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                homeCategoryViewHolder.tvCategoryName.setText(commonResponses.get(i).getSubCategoryName());
            }else{
                homeCategoryViewHolder.tvCategoryName.setText(commonResponses.get(i).getPortugueseSubCategoryName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return commonResponses.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;

        @BindView(R.id.ivCategory)
        ImageView ivCategory;

        public HomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.clCategory})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clCategory:
                    commonListener.onItemClick(getAdapterPosition(),commonResponses);
                    break;
            }
        }
    }

    public void update(ArrayList<CommonResponseBean> commonResponses, String type){
        this.commonResponses = commonResponses;
        this.type = type;
        notifyDataSetChanged();
    }
}
