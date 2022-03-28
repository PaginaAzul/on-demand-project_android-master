package com.pagin.azul.onphasesecond.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.adapters.FilterAdapter;
import com.pagin.azul.onphasesecond.adapters.SubFilterAdapter;
import com.pagin.azul.onphasesecond.model.MenuItem;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterActivity extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvCount)
    TextView tvCount;

    @BindView(R.id.rvTypeCategrory)
    RecyclerView rvTypeCategrory;

    @BindView(R.id.rvSubCategory)
    RecyclerView rvSubCategory;

    @BindView(R.id.rbGrocery)
    RadioButton rbGrocery;

    private FilterAdapter filterAdapter;
    private SubFilterAdapter subFilterAdapter;
    private String filterItem;
    private String filterSubItem;
    private ArrayList<MenuItem> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.filter));
        getIntentData();
    }

    @OnClick({R.id.rbGrocery, R.id.rbFood, R.id.btnApply})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbGrocery:
                filterAdapter.update(getGroceryType());
                subFilterAdapter.update(getSubGroceryType());
                break;
            case R.id.rbFood:
                filterAdapter.update(getFoodType());
                subFilterAdapter.update(getSubFoodTaste());
                break;
            case R.id.btnApply:
                dispatchSearch();
                break;
        }
    }

    private void dispatchSearch() {
        Intent intent = new Intent();
        intent.putExtra(ParamEnum.FILTER_ITEM.theValue(),filterItem);
        intent.putExtra(ParamEnum.FILTER_SUB_ITEM.theValue(),filterSubItem);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            //String type = getIntent().getStringExtra(ParamEnum.TYPE.theValue());
            tvCount.setText(getIntent().getIntExtra(ParamEnum.COUNT.theValue(),0)+getString(R.string.product_found));
            /*if (type != null) {
                if(type.equalsIgnoreCase(SubOfferActivity.class.getSimpleName()))
                    rbGrocery.setText(R.string.shop);
                setUpRecylerView(type);
            }else {*/
            //}
        }
        setUpRecylerView();
    }

    private void setUpRecylerView(/*String type*/) {
        filterList = new ArrayList<>();
        rvTypeCategrory.setLayoutManager(new LinearLayoutManager(this));
        filterAdapter = new FilterAdapter(this,getGroceryType(),this);
        rvTypeCategrory.setAdapter(filterAdapter);

        rvSubCategory.setLayoutManager(new LinearLayoutManager(this));
        /*if(type.equalsIgnoreCase(SubOfferActivity.class.getSimpleName()))
            subFilterAdapter = new SubFilterAdapter(this,getSubShopType(),this);
        else*/
            subFilterAdapter = new SubFilterAdapter(this,getSubGroceryType(),this);
        rvSubCategory.setAdapter(subFilterAdapter);
    }

    // when we select Grocery radio button then get grocery type filter
    private ArrayList<MenuItem> getGroceryType(){
        ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<3;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Type");
                menuItem.setCheck(true);
                filterItem = "Type";
            }else if(i==1){
                menuItem.setMenu("Distance");
                menuItem.setCheck(false);
            }else{
                menuItem.setMenu("Preference");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    // when we select Food radio button then get food type filter
    private ArrayList<MenuItem> getFoodType(){
        ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<4;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Taste");
                menuItem.setCheck(true);
            }else if(i==1){
                menuItem.setMenu("Type");
                menuItem.setCheck(false);
            }else if(i==2){
                menuItem.setMenu("Eat type");
                menuItem.setCheck(false);
            }else{
                menuItem.setMenu("Ratings");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    private ArrayList<MenuItem> getSubGroceryType(){
        filterList.clear();
        //ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<4;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Staples");
                menuItem.setCheck(true);
                filterSubItem = "Staples";
            }else if(i==1){
                menuItem.setMenu("Snacks");
                menuItem.setCheck(false);
            }else if(i==2){
                menuItem.setMenu("Dairy");
                menuItem.setCheck(false);
            }else{
                menuItem.setMenu("HouseHold");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    private ArrayList<MenuItem> getSubShopType(){
        ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<4;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Liquor Stores");
                menuItem.setCheck(true);
            }else if(i==1){
                menuItem.setMenu("Pharmacy");
                menuItem.setCheck(false);
            }else if(i==2){
                menuItem.setMenu("Grocery");
                menuItem.setCheck(false);
            }else{
                menuItem.setMenu("HouseHold");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    private ArrayList<MenuItem> getSubFoodTaste(){
        filterList.clear();
        //ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<3;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Sweet");
                menuItem.setCheck(true);
            }else if(i==1){
                menuItem.setMenu("Salty");
                menuItem.setCheck(false);
            }else{
                menuItem.setMenu("Mixed");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    private ArrayList<MenuItem> getDistance(){
        filterList.clear();
        //ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<5;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Under 2 KM");
                menuItem.setCheck(true);
            }else if(i==1){
                menuItem.setMenu("Under 5 KM");
                menuItem.setCheck(false);
            }else if(i==2){
                menuItem.setMenu("Under 10 KM");
                menuItem.setCheck(false);
            }else if(i==3){
                menuItem.setMenu("Under 20 KM");
                menuItem.setCheck(false);
            }else{
                menuItem.setMenu("Under 45 KM");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    private ArrayList<MenuItem> getEatType(){
        filterList.clear();
        //ArrayList<MenuItem> filterList = new ArrayList<>();
        for(int i=0;i<2;i++){
            MenuItem menuItem = new MenuItem();
            if(i==0){
                menuItem.setMenu("Veg");
                menuItem.setCheck(true);
            }else {
                menuItem.setMenu("Non Veg");
                menuItem.setCheck(false);
            }
            filterList.add(menuItem);
        }
        return filterList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.menuClear) {
            for (int i=0;i<filterList.size();i++){
                filterList.get(i).setCheck(false);
            }
            subFilterAdapter.update(filterList);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFilterItemClick(int position, ArrayList<MenuItem> list) {
        filterItem = list.get(position).getMenu();
        switch (filterItem){
            case "Type":
                subFilterAdapter.update(getSubGroceryType());
                break;
            case "Distance":
                subFilterAdapter.update(getDistance());
                break;
            case "Taste":
                subFilterAdapter.update(getSubFoodTaste());
                break;
            case "Eat type":
                subFilterAdapter.update(getEatType());
                break;
        }
    }

    @Override
    public void onFilterSubItemClick(int position, ArrayList<MenuItem> list) {
        filterSubItem = list.get(position).getMenu();
    }
}