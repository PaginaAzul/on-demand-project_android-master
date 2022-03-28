package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyCommentHolder> {
    private Context context;
    private ArrayList<NormalUserPendingOrderInner> commentList;

    public CommentsAdapter(Context context, ArrayList<NormalUserPendingOrderInner> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, null, false);


        return new MyCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentHolder myCommentHolder, int position) {

        myCommentHolder.tv_user.setText(commentList.get(position).getRatingBy1());
        myCommentHolder.tv_user_details.setText(commentList.get(position).getComments());

        String rating = String.valueOf(commentList.get(position).getRate());
        myCommentHolder.rb_ratting.setRating(Float.parseFloat(rating));

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyCommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_user)
        TextView tv_user;
        @BindView(R.id.tv_user_details)
        TextView tv_user_details;
        @BindView(R.id.rb_ratting)
        RatingBar rb_ratting;


        public MyCommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
