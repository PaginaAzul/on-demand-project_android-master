package com.pagin.azul.onphasesecond.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pagin.azul.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullCustomizationAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SingleItemSelectAdapter.OnSingleAddOnClick, MultiItemAddAdapter.OnMutipleAddonClick {
    private Context context;
    SingleItemSelectAdapter singleItemSelAdapter;
    MultiItemAddAdapter multiItemAddAdapter;
    public static final int SINGLE_SELECT_ADDON = 0;
    public static final int MULTI_SELECT_ADDON = 1;

    public FullCustomizationAdap(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       // Section.ItemType currentItemType = getCurrentItemType(viewType);
        switch (viewType) {
            case SINGLE_SELECT_ADDON:
                View v1 = inflater.inflate(R.layout.layout_single_checkbox, parent, false);
                viewHolder = new SingleSelectionAddon(v1);
                break;
            case MULTI_SELECT_ADDON:
                View v2 = inflater.inflate(R.layout.layot_multicheckbox, parent, false);
                viewHolder = new MultiSelectionAddon(v2);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        switch (holder.getItemViewType()) {
            case SINGLE_SELECT_ADDON:
                SingleSelectionAddon singleSelectionHolder = (SingleSelectionAddon) holder;
                singleItemSelAdapter = new SingleItemSelectAdapter(context, this);
                singleSelectionHolder.bindSingleData(i);
                break;
            case MULTI_SELECT_ADDON:
                 MultiSelectionAddon multiSelectionAddon = (MultiSelectionAddon) holder;
                 multiItemAddAdapter = new MultiItemAddAdapter(context, this);
                 multiSelectionAddon.bindMultipleData(i);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class SingleSelectionAddon extends RecyclerView.ViewHolder {

        @BindView(R.id.TvTitle)
        TextView TvTitle;

        @BindView(R.id.rvSingleCheckBox)
        RecyclerView rvSingleCheckBox;

        public SingleSelectionAddon(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bindSingleData(final int postion) {
            rvSingleCheckBox.setLayoutManager(new LinearLayoutManager(context));
            rvSingleCheckBox.setAdapter(singleItemSelAdapter);
        }

    }


    public class MultiSelectionAddon extends RecyclerView.ViewHolder {

        @BindView(R.id.TvTitle)
        TextView TvTitle;

        @BindView(R.id.rvMultiCheckBox)
        RecyclerView rvMultiCheckBox;

        public MultiSelectionAddon(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bindMultipleData(final int postion) {
            rvMultiCheckBox.setLayoutManager(new LinearLayoutManager(context));
            rvMultiCheckBox.setAdapter(multiItemAddAdapter);
        }
    }

    public interface onCustomiseItemClic {
        //void onItemCheck(int pos, AddonsEntity response);
        //void onItemUncheck(int pos, AddonsEntity response);
    }

    @Override
    public int getItemViewType(int position) {
        /// 1 for single selection menu and 2 for mutliple selectin menu
        if (position==0)
            return SINGLE_SELECT_ADDON;
        else
            return MULTI_SELECT_ADDON;

    }

}