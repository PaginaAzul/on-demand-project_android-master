package com.pagin.azul.activities;

import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.CategorySearchAdapter;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.utils.CommonUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCategoryAct extends AppCompatActivity implements CommonListener {

    @BindView(R.id.rvSearchCategory)
    RecyclerView rvSearchCategory;

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    private CategorySearchAdapter categorySearchAdapter;
    private ArrayList<CommonResponseBean> subCategoryData;
    private String categoryId = "";
    private String portugueseCategoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);
        ButterKnife.bind(this);
        setOnTouchListener();
        setUpRecyclerView();
        addOnTextChangeListener();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void setOnTouchListener() {
        edtSearch.setOnTouchListener((view, motionEvent) -> {
            int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= edtSearch.getRight() - edtSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {
                    // your action here
                    edtSearch.setText("");
                    return true;
                }
            }
            return false;
        });
    }

    private void setUpRecyclerView() {
        if(getIntent().getExtras()!=null){
            categoryId =  getIntent().getStringExtra(Constants.kCategoryId);
            portugueseCategoryName =  getIntent().getStringExtra(Constants.kPortugueseCategoryName);
            subCategoryData =  getIntent().getParcelableArrayListExtra(Constants.kData);
            //rvSearchCategory.setLayoutManager(new LinearLayoutManager(this));
            rvSearchCategory.setLayoutManager(new GridLayoutManager(this,2));
            categorySearchAdapter = new CategorySearchAdapter(this,this,subCategoryData);
            rvSearchCategory.setAdapter(categorySearchAdapter);
        }else {
            subCategoryData = new ArrayList<>();
        }
    }

    @Override
    public void onItemClick(int position, ArrayList<CommonResponseBean> commonResponses) {
        startActivity(MapProfessinalWorkerActivity.getSubCategoryIntent(this, "btn_prfsnal_worker"
                ,commonResponses.get(position).getSubCategoryName(),commonResponses.get(position).getCategoryName(),categoryId,
                commonResponses.get(position).get_id(),portugueseCategoryName,commonResponses.get(position).getPortugueseSubCategoryName()));
    }

    private void addOnTextChangeListener() {
        final ArrayList<CommonResponseBean> categoryList = new ArrayList<>();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(edtSearch.getText().toString().trim().length()>0){
                    categoryList.clear();
                    categoryList.addAll(subCategoryData);
                    List<CommonResponseBean> list = filter(edtSearch.getText().toString(), categoryList, true);
                    categoryList.clear();
                    categoryList.addAll(list);
                    categorySearchAdapter.update(categoryList);
                    /*if(main_data.size()>0){
                        tvNoData.setVisibility(View.GONE);
                    }else {
                        tvNoData.setVisibility(View.VISIBLE);
                    }*/
                }else{
                    categoryList.clear();
                    categoryList.addAll(subCategoryData);
                    categorySearchAdapter.update(categoryList);
                }
                edtSearch.requestFocus();
            }
        });
    }

    private List<CommonResponseBean> filter(String string, Iterable<CommonResponseBean> iterable, boolean b) {
        if (iterable == null)
            return new LinkedList<CommonResponseBean>();
        else {
            List<CommonResponseBean> collected = new LinkedList<CommonResponseBean>();
            Iterator<CommonResponseBean> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                CommonResponseBean item = iterator.next();
                try{
                    if (item.getSubCategoryName().toLowerCase().contains(string.toLowerCase())){
                        collected.add(item);
                    }else if (item.getPortugueseSubCategoryName().toLowerCase().contains(string.toLowerCase())){
                        collected.add(item);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return collected;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                }else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            }else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        }
        else {
            super.attachBaseContext(newBase);
        }
    }
}
