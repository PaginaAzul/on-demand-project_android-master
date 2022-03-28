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

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddWorkImagesAdapter extends RecyclerView.Adapter<AddWorkImagesAdapter.DocuViewHolder> {
    private Context context;
    private ArrayList<File> docList;
    private CommonListener onClickListener;

    public AddWorkImagesAdapter(Context context, ArrayList<File> docList, CommonListener onClickListener) {
        this.context = context;
        this.docList = docList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public DocuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_work_images_item,viewGroup,false);
        return new DocuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocuViewHolder docuViewHolder, int i) {

        //docuViewHolder.ivWorkImage.setImageURI(Uri.fromFile(docList.get(i).getAbsoluteFile()));

        Glide.with(context)
                .load(docList.get(i).getAbsoluteFile())
                .apply(RequestOptions.placeholderOf(R.drawable.loader)
                        .error(R.drawable.place_holder))
                .into(docuViewHolder.ivWorkImage);

    }

    @Override
    public int getItemCount() {
        return docList!=null?docList.size():0;
    }

    public class DocuViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivWorkImage)
        ImageView ivWorkImage;

        public DocuViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick({R.id.ivDeleteRecord,R.id.ivWorkImage})
        public void onClick(View view){
            switch (view.getId()){
                case R.id.ivDeleteRecord:
                    onClickListener.onDeleteClick(getAdapterPosition());
                    break;
                case R.id.ivWorkImage:
                    onClickListener.onImageClick(getAdapterPosition());
                    break;
            }
        }
    }

    public void updateList(ArrayList<File> docList){
        this.docList = docList;
        notifyDataSetChanged();
    }
}
