package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviousWorkAdapter extends RecyclerView.Adapter<PreviousWorkAdapter.PreviousWorkViewHolder> {
    private Context context;
    ArrayList<String> workImages;
    CommonListener commonListener;

    public PreviousWorkAdapter(Context context, ArrayList<String> workImages,CommonListener commonListener) {
        this.context = context;
        this.workImages = workImages;
        this.commonListener = commonListener;
    }

    @NonNull
    @Override
    public PreviousWorkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_previous_work_item, viewGroup, false);
        return new PreviousWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousWorkViewHolder previousWorkViewHolder, int i) {
        Glide.with(context)
                .load(workImages.get(i))
                .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                        .error(R.drawable.imgaa))
                .into(previousWorkViewHolder.ivPreviousWork);
    }

    @Override
    public int getItemCount() {
        return workImages!=null?workImages.size():0;
    }

    public class PreviousWorkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPreviousWork)
        ImageView ivPreviousWork;
        public PreviousWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.ivPreviousWork})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.ivPreviousWork:
                    commonListener.onImageClick(workImages.get(getAdapterPosition()));
                    break;
            }
        }
    }
}
