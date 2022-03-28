package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.pagin.azul.onphasesecond.model.GetCustomerStoryDataModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class CustomrerStoriesAdapter extends RecyclerView.Adapter<CustomrerStoriesAdapter.CustomerViewHolder> {

    private Context context;
    private List<GetCustomerStoryDataModel.Datum> data;

    public CustomrerStoriesAdapter(Context context, List<GetCustomerStoryDataModel.Datum> data) {
        this.context = context;
        this.data=data;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_customer_stories_items, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position)
    {

        holder.tv_name.setText(data.get(position).getUsername());
        holder.tv_review.setText(data.get(position).getStory());
        String image = data.get(position).getImage();
        if(image!=null && !image.equals("")){
            setImage(holder.progressBar,image,holder.circleImageView3);
        }else {
            holder.progressBar.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.profile_default).into(holder.circleImageView3);
        }
        //Glide.with(context).load(data.get(position).getImage()).into(holder.circleImageView3);

    }

    @Override
    public int getItemCount() {
        return (data==null?0:data.size());
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_review)
        TextView tv_review;

        @BindView(R.id.circleImageView3)
        ImageView circleImageView3;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public CustomerViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
