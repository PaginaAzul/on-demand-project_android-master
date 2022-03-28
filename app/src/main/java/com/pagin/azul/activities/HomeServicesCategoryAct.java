package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.HomeCategoryAdapter;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.utils.CommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeServicesCategoryAct extends AppCompatActivity implements CommonListener {

    @BindView(R.id.rvHomeServices)
    RecyclerView rvHomeServices;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private HomeCategoryAdapter homeCategoryAdapter;
    private ArrayList<CommonResponseBean> subCategoryData;
    private String categoryId = "";
    private String portugueseCategoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_services_category);
        ButterKnife.bind(this);
        setToolbar();
        setUpRecyclerView();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpRecyclerView() {
        if(getIntent().getExtras()!=null){
            categoryId =  getIntent().getStringExtra(Constants.kCategoryId);
            portugueseCategoryName =  getIntent().getStringExtra(Constants.kPortugueseCategoryName);
            subCategoryData =  getIntent().getParcelableArrayListExtra(Constants.kData);
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                tv_title.setText(subCategoryData.get(0).getCategoryName());
            }else {
                tv_title.setText(portugueseCategoryName);
            }
            rvHomeServices.setLayoutManager(new GridLayoutManager(this,2));
            homeCategoryAdapter = new HomeCategoryAdapter(this,this, subCategoryData,HomeServicesCategoryAct.class.getSimpleName());
            rvHomeServices.setAdapter(homeCategoryAdapter);
        }else{
            subCategoryData = new ArrayList<>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ivSearch) {
            Intent intent = new Intent(this,SearchCategoryAct.class);
            intent.putParcelableArrayListExtra(Constants.kData,subCategoryData);
            intent.putExtra(Constants.kCategoryId,categoryId);
            intent.putExtra(Constants.kPortugueseCategoryName,portugueseCategoryName);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position, ArrayList<CommonResponseBean> commonResponses) {
        startActivity(MapProfessinalWorkerActivity.getSubCategoryIntent(this, "btn_prfsnal_worker"
                ,commonResponses.get(position).getSubCategoryName(),commonResponses.get(position).getCategoryName(),
                categoryId,commonResponses.get(position).get_id(),portugueseCategoryName,commonResponses.get(position).getPortugueseSubCategoryName()));
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
