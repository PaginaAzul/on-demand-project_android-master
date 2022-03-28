package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.bean.AddressList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyCommentHolder> {
    private Context context;
    private List<AddressList> addressList;
    AddressList list;
    Listener listener;

    public AddressListAdapter(Context context, List<AddressList> addressList, Listener listener) {
        this.context = context;
        this.addressList = addressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressListAdapter.MyCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_address_list, null, false);
        return new AddressListAdapter.MyCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.MyCommentHolder holder, int position) {
        list = addressList.get(position);

        holder.bindContent(list);

        if (!list.getLandmark().isEmpty()) {
            holder.addresss.setText(list.getBuildingAndApart()+" "+list.getAddress());
            holder.title.setText(list.getLandmark());
        } else {
            holder.ll_main.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }


        if (list.isSelect()) {
            holder.right_click.setVisibility(View.VISIBLE);

        } else {
            holder.right_click.setVisibility(View.GONE);

        }


    }

    public interface Listener {
        void itemClick(AddressList addressList);

    }

    //remove item
    public void removeitem(int position) {
        addressList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class MyCommentHolder extends RecyclerView.ViewHolder {
        private AddressList listItem;

        @BindView(R.id.addresss)
        TextView addresss;
        @BindView(R.id.right_click)
        ImageView right_click;

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.ll_main)
        RelativeLayout ll_main;
        @BindView(R.id.view)
        View view;


        public MyCommentHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.ll_main})
        void onclick(View view) {
            switch (view.getId()) {
                case R.id.ll_main:


                    listener.itemClick( listItem);


                    if (listItem.isSelect()) {
                        clearList();
                        listItem.setSelect(false);
                        right_click.setVisibility(View.VISIBLE);

                    } else {
                        clearList();
                        listItem.setSelect(true);
                        right_click.setVisibility(View.GONE);

                    }
                    notifyDataSetChanged();


                    break;

            }


        }

        void clearList() {
            for (AddressList payment : addressList) {
                payment.setSelect(false);
            }
        }

        void bindContent(AddressList listItem) {
            this.listItem = listItem;
        }

    }

}
