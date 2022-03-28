package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pagin.azul.R;
import com.pagin.azul.adapter.HomeCategoryAdapter;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable {

    private Context context;
    private CommonListener commonListener;
    private ArrayList<ProductResponse> list;
    private ArrayList<ProductResponse> mainList;

    public CategoryAdapter(Context context,CommonListener commonListener) {
        this.context = context;
        this.commonListener = commonListener;
    }

    public CategoryAdapter(Context context, CommonListener commonListener, ArrayList<ProductResponse> list) {
        this.context = context;
        this.commonListener = commonListener;
        this.list = list;
        this.mainList = list;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String image = list.get(position).getImage();
        if(image!=null && !image.equals("")){
            setImage(holder.progressBar,image,holder.ivCategory);
        }else {
            holder.progressBar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.default_cat).override(110, 150).into(holder.ivCategory);
        }
        holder.tvCategoryName.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults results=new FilterResults();
                final ArrayList<ProductResponse> tempFilterData=new ArrayList<>();
                if(constraint==null||constraint.toString().trim().length()==0)
                {
                    results.values=mainList;
                }else {
                    String constrainString=constraint.toString().toLowerCase();
                    for(ProductResponse data:mainList)
                    {
                        if(data.getName().toLowerCase().contains(constrainString))
                        {
                            tempFilterData.add(data);
                            Log.w("charSeq",constrainString);
                        }
                        Log.w("charSeq",constrainString);

                    }
                    results.count=tempFilterData.size();
                    results.values=tempFilterData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                if(results.values!=null)
                {
                    list=(ArrayList<ProductResponse>)results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivCategory)
        ImageView ivCategory;
        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick({R.id.clCategory})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.clCategory:
                    commonListener.onCategoryClick(getAdapterPosition(),list);
                    break;
            }
        }
    }

    private void setImage(ProgressBar progressBar, final String imageUri, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context.getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                }).into(imageView);
    }
}
