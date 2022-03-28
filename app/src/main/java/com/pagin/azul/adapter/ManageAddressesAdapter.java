package com.pagin.azul.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.AddressPickerAct;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageAddressesAdapter extends RecyclerView.Adapter<ManageAddressesAdapter.MyCommentHolder> {
    private Context context;
    private List<AddressList> addressList;
    AddressList list;
    Listener listener;

    public ManageAddressesAdapter(Context context, List<AddressList> addressList, Listener listener) {
        this.context = context;
        this.addressList = addressList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ManageAddressesAdapter.MyCommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_manage_addresses, null, false);
        return new ManageAddressesAdapter.MyCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAddressesAdapter.MyCommentHolder holder, int position) {
        list = addressList.get(position);
        holder.tv_address.setText(list.getAddress());
        holder.tvType.setText(list.getLandmark());
        //holder.tv_landmark.setText(list.getLandmark());

    }

    public interface Listener {
        void delete(int position, String id);
        void onAddressClick(int position, List<AddressList> addressList);
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

        @BindView(R.id.tv_type)
        TextView tvType;

        @BindView(R.id.tv_address)
        TextView tv_address;

        @BindView(R.id.tv_landmark)
        TextView tv_landmark;

        public MyCommentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.iv_edit, R.id.iv_delete,R.id.rlAddress})
        void onclick(View view) {
            switch (view.getId()) {
                case R.id.iv_edit:
                    context.startActivity(AddressPickerAct.getIntent(context, addressList.get(getAdapterPosition()), "editbtn"));
                    ((Activity) context).finish();
                    break;

                case R.id.iv_delete:
                    listener.delete(getAdapterPosition(), addressList.get(getAdapterPosition()).get_id());
                    break;

                case R.id.rlAddress:
                    listener.onAddressClick(getAdapterPosition(),addressList);
                    break;
            }
        }

    }
}
