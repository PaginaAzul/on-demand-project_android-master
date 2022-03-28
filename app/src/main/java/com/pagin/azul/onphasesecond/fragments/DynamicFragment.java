package com.pagin.azul.onphasesecond.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MenuFilterActivity;
import com.pagin.azul.onphasesecond.activity.RestaurantDetails;
import com.pagin.azul.onphasesecond.adapters.MenuDetailsAdapter;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.showLoginDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DynamicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicFragment extends Fragment implements CommonListener {

    @BindView(R.id.rvMenuItems)
    RecyclerView rvMenuItems;

    private Context mContext;
    private MenuDetailsAdapter menuDetailsAdapter;
    private ArrayList<RestaurantResponse> menuDataList;
    String token;
    String userID;
    String langCode;
    String cuisine;
    String resAndStoreId;

    public DynamicFragment() {
        // Required empty public constructor
    }

    public static DynamicFragment newInstance(int val) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        return fragment;
    }

    public static DynamicFragment newInstance(String cuisine, String resAndStoreId) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString("cuisine", cuisine);
        args.putString("resAndStoreId", resAndStoreId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dynamic, container, false);
        ButterKnife.bind(this,view);
        mContext = getContext();
        token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);
        userID = SharedPreferenceWriter.getInstance(mContext).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE);
        //setUpRecyclerView();
        getIntentData();
        return view;
    }

    private void getIntentData() {
        if(getArguments()!=null){
            cuisine = getArguments().getString("cuisine");
            resAndStoreId = getArguments().getString("resAndStoreId");
            serviceGetMenuData(cuisine,resAndStoreId);
        }
    }

    private void setUpRecyclerView(ArrayList<RestaurantResponse> list) {
        rvMenuItems.setLayoutManager(new LinearLayoutManager(mContext));
        menuDetailsAdapter = new MenuDetailsAdapter(mContext,list,this,DynamicFragment.class.getSimpleName(),true);
        rvMenuItems.setAdapter(menuDetailsAdapter);

    }

    private void serviceGetMenuData(String cuisine, String resAndStoreId) {
        try {
            //MyDialog.getInstance(mContext).showDialog(getActivity());
            //if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                if(!token.isEmpty()){
                    jsonObject.put("userId", userID);
                }
                jsonObject.put("langCode", langCode);
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("cuisine", cuisine);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.getMenuData(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        //MyDialog.getInstance(mContext).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                               /* menuDataList.clear();
                                menuDataList.addAll(restaurantModel.getData().getMenuList());
                                menuDetailsAdapter.notifyDataSetChanged();*/
                                menuDataList = new ArrayList<>();
                                menuDataList.addAll(restaurantModel.getData().getMenuList());

                                rvMenuItems.setLayoutManager(new LinearLayoutManager(mContext));
                                menuDetailsAdapter = new MenuDetailsAdapter(mContext,menuDataList,DynamicFragment.this,DynamicFragment.class.getSimpleName(),true);
                                rvMenuItems.setAdapter(menuDetailsAdapter);
                                ((RestaurantDetails)mContext).addMenuDetailAdapter(rvMenuItems,cuisine);

                              //  setUpRecyclerView(menuDataList);
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
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddItem(int position,ArrayList<RestaurantResponse> menuDataList)
    {
        if (SharedPreferenceWriter.getInstance(getActivity()).getString(kToken) != null
            && !SharedPreferenceWriter.getInstance(getActivity()).getString(kToken).equalsIgnoreCase(""))
        {
            RestaurantResponse cartData = menuDataList.get(position).getCartData();
            if(cartData!=null) {
                serviceUpdateCart(cartData.getProductId(),position,cartData.getQuantity()+1,menuDataList);
            }else {
                // add cart if cart data is null or blank
                serviceAddToCart(menuDataList.get(position).get_id());
            }
        }else{
            showLoginDialog(getActivity());
        }

    }

    @Override
    public void onRemoveItem(int position,ArrayList<RestaurantResponse> menuDataList) {
        RestaurantResponse cartData = menuDataList.get(position).getCartData();
        if(cartData!=null && cartData.getQuantity()!=0){
            serviceUpdateCart(cartData.getProductId(),position,cartData.getQuantity()-1,menuDataList);
        }
    }

    private void serviceUpdateCart(String productId, int position, int quantity,ArrayList<RestaurantResponse> menuDataList) {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userID);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);
                jsonObject.put("quantity", quantity);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.updateCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(mContext).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                RestaurantResponse restaurantResponse = menuDataList.get(position);
                                restaurantResponse.getCartData().setQuantity(quantity);
                                menuDetailsAdapter.updateDataList(restaurantResponse,position);
                                //menuDetailsAdapter.notifyDataSetChanged();
                                //menuDetailsAdapter.updateList(menuDataList);
                                //serviceGetMenuData(cuisine,resAndStoreId);


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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serviceAddToCart(String productId) {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userID);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.addToCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(mContext).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                serviceGetMenuData(cuisine,resAndStoreId);

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if((RestaurantDetails)getActivity()!=null)
            ((RestaurantDetails)getActivity()).setOnCommonListener(this);

        //setUpRecyclerView(menuDataList);
    }

    @Override
    public void onFilter(String newText) {
        //menuDetailsAdapter.getFilter().filter(newText);
    }

    @Override
    public void onMenuFilterClick(String cuisine) {

    }



    public MenuDetailsAdapter getMenuAdapter()
    {
        return menuDetailsAdapter;
    }

}