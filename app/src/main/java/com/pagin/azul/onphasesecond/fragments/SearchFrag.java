package com.pagin.azul.onphasesecond.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.AddressPickerAct;
import com.pagin.azul.activities.MapProfessinalWorkerActivity;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.MyProfileFrag;
import com.pagin.azul.onphasesecond.activity.FilterActivity;
import com.pagin.azul.onphasesecond.activity.FoodAndGroceryActivity;
import com.pagin.azul.onphasesecond.activity.GroceryDetails;
import com.pagin.azul.onphasesecond.activity.MyFavoritesActivity;
import com.pagin.azul.onphasesecond.activity.RestaurantDetails;
import com.pagin.azul.onphasesecond.adapters.HomeAdapter;
import com.pagin.azul.onphasesecond.adapters.RestroListingAdapter;
import com.pagin.azul.onphasesecond.adapters.SearchRestAndGroceryAdapter;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class SearchFrag extends Fragment implements CommonListener, SwipeRefreshLayout.OnRefreshListener {

    public SearchFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.recyclerSearch)
    RecyclerView recyclerSearch;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    @BindView(R.id.rbRest)
    RadioButton rbRest;

    @BindView(R.id.rbGrocery)
    RadioButton rbGrocery;

    @BindView(R.id.rgSearchType)
    RadioGroup rgSearchType;

    @BindView(R.id.swipeSearch)
    SwipeRefreshLayout swipeSearch;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.clSearch)
    ConstraintLayout clSearch;

    private Context mContext;
    private int count;
    private static final int FILTER_REQ = 11;
    private ArrayList<RestaurantResponse> searchList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);
        setupUI(clSearch);
        mContext = getContext();
        searchList = new ArrayList<>();
        rbRest.performClick();
        swipeSearch.setOnRefreshListener(this);
        return view;
    }

    @OnClick({R.id.rbRest, R.id.rbGrocery})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbRest:
                edtSearchFrag.setHint(R.string.search_by_restaurants_dishes_cuisines);
                serviceGetRestaurantAndStoreDataForSearch(ParamEnum.RESTAURANT.theValue(),true);
                break;
            case R.id.rbGrocery:
                edtSearchFrag.setHint(R.string.search_by_grocery_shops);
                serviceGetRestaurantAndStoreDataForSearch(ParamEnum.GROCERY.theValue(),true);
                break;
        }
    }

    @Override
    public void onResAndStoreClick(int position, ArrayList<RestaurantResponse> restAndStoreList) {
        int id = rgSearchType.getCheckedRadioButtonId();
        if(id == R.id.rbRest) {
            Intent intent = new Intent(mContext, RestaurantDetails.class);
            intent.putExtra(ParamEnum.DATA.theValue(),restAndStoreList.get(position));
            startActivity(intent);
        }else {
            Intent intent = new Intent(mContext, GroceryDetails.class);
            intent.putExtra(ParamEnum.DATA.theValue(),restAndStoreList.get(position));
            startActivity(intent);
        }
    }

    private void serviceGetRestaurantAndStoreDataForSearch(String type,boolean isLoader) {
        try {
            if(isLoader){
                MyDialog.getInstance(mContext).showDialog(getActivity());
            }
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId",( SharedPreferenceWriter.getInstance(mContext).getString(kUserId).trim().isEmpty()?
                        null: SharedPreferenceWriter.getInstance(mContext).getString(kUserId)));
                jsonObject.put("latitude", SharedPreferenceWriter.getInstance(mContext).getString(kLat));
                jsonObject.put("longitude", SharedPreferenceWriter.getInstance(mContext).getString(kLong));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE));
                jsonObject.put("type", type);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.getRestaurantAndStoreDataForSearch(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(mContext).hideDialog();
                        swipeSearch.setRefreshing(false);
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                ArrayList<RestaurantResponse> list = restaurantModel.getData().getRestaurantList();
                                if(list!=null && !list.isEmpty()) {
                                    tvNoData.setVisibility(View.GONE);
                                    setData(list);
                                }else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                }

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                Toast.makeText(mContext, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<RestaurantModel> call, Throwable t) {
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(ArrayList<RestaurantResponse> restaurantList) {
        count = restaurantList.size();
        searchList.clear();
        searchList.addAll(restaurantList);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchRestAndGroceryAdapter searchRestAndGroceryAdapter = new SearchRestAndGroceryAdapter(mContext,this,restaurantList);
        recyclerSearch.setAdapter(searchRestAndGroceryAdapter);

        edtSearchFrag.setText("");
        edtSearchFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(searchRestAndGroceryAdapter!=null)
                    searchRestAndGroceryAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onFavClick(int position, ArrayList<RestaurantResponse> list) {
        try {
            //MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(mContext).getString(kUserId));
                jsonObject.put("resAndStoreId", list.get(position).get_id());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.addToFavourite(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        //MyDialog.getInstance(mContext).hideDialog();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {
                                RestaurantResponse restaurantResponse = list.get(position);
                                if(restaurantResponse.isFav()){
                                    restaurantResponse.setFav(false);
                                }else{
                                    restaurantResponse.setFav(true);
                                }
                                setData(list);

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FavoriteModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        int id = rgSearchType.getCheckedRadioButtonId();
        if(id == R.id.rbRest)
            serviceGetRestaurantAndStoreDataForSearch(ParamEnum.RESTAURANT.theValue(),false);
        else
            serviceGetRestaurantAndStoreDataForSearch(ParamEnum.GROCERY.theValue(),false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((FoodAndGroceryActivity)getActivity()).setOnFilterClickListener(this);
    }

    @Override
    public void onFilterClick() {
        Intent intent = new Intent(mContext,FilterActivity.class);
        intent.putExtra(ParamEnum.COUNT.theValue(),count);
        startActivityForResult(intent,FILTER_REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_REQ && resultCode == Activity.RESULT_OK && data!=null){
            String filterItem = data.getStringExtra(ParamEnum.FILTER_ITEM.theValue());
            String filterSubItem = data.getStringExtra(ParamEnum.FILTER_SUB_ITEM.theValue());
            applyFilter(filterItem,filterSubItem);
        }
    }

    private void applyFilter(String filterItem, String filterSubItem) {
        ListIterator<RestaurantResponse> listIterator = searchList.listIterator();

        switch (filterItem){
            case "Type":

                break;
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    CommonUtility.hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}