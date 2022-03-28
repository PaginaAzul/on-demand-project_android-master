package com.pagin.azul.onphasesecond.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.adapters.BottomMenuItemAdap;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuItemBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.menu_item_recycler)
    RecyclerView menu_item_recycler;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bootmsheet_menu_item,container,false);
        ButterKnife.bind(this,view);
        mContext = getContext();
        setUpRecyclerView();
        return view;

    }

    @OnClick({R.id.iv_close})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.iv_close:
            dismiss();
            break;
        }
    }

    private void setUpRecyclerView() {
        menu_item_recycler.setLayoutManager(new LinearLayoutManager(mContext));
        menu_item_recycler.setAdapter(new BottomMenuItemAdap(mContext));
    }

}
