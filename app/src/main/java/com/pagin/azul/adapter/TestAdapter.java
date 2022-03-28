package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pagin.azul.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> listaa;

    public TestAdapter(Context context, ArrayList<String> listaa) {
        this.context = context;
        this.listaa = listaa;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_map_categories_list_layout, null, false);
        return new TestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return listaa.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
