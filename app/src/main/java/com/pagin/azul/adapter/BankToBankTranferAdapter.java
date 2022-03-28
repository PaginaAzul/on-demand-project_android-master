package com.pagin.azul.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankToBankTranferAdapter extends RecyclerView.Adapter<BankToBankTranferAdapter.MyBanktoBankHolder> {
    private Context context;
    private ArrayList<String> bankTransferList;

    public BankToBankTranferAdapter(Context context, ArrayList<String> bankTransferList) {
        this.context = context;
        this.bankTransferList = bankTransferList;
    }

    @NonNull
    @Override
    public MyBanktoBankHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_bank_to_bank_transfer, null, false);

        return new MyBanktoBankHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBanktoBankHolder holder, int position) {

        holder.tvAccountNo.setText(bankTransferList.get(position));

    }

    @Override
    public int getItemCount() {
        return bankTransferList.size();
    }

    public class MyBanktoBankHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_accountNo)
        TextView tvAccountNo;

        public MyBanktoBankHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
