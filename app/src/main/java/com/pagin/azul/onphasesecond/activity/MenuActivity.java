package com.pagin.azul.onphasesecond.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.SignUpOptions;
import com.pagin.azul.bean.CateResponse;
import com.pagin.azul.bean.ClearCartResponse;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.MenuDetailsAdapter;
import com.pagin.azul.onphasesecond.fragments.DynamicFragment;
import com.pagin.azul.onphasesecond.model.GetCartCountModel;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.showLoginDialog;

public class MenuActivity extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    @BindView(R.id.cartLayout)
    ConstraintLayout cartLayout;

    @BindView(R.id.countTv)
    TextView countTv;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;

    private String token;
    private String userId;
    private String langCode;
    private String cuisine;
    private String resAndStoreId;
    private MenuDetailsAdapter menuDetailsAdapter;
    private String orderType="";
    String productIds;
    int positions;
    int quantitys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        cartLayout.setVisibility(View.VISIBLE);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        getIntentData();
        addOnTextChangeListener();
    }

    @OnClick({R.id.tvAddToCart, R.id.cartLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddToCart:
            case R.id.cartLayout:
                dispatchAddToCart();
                break;
        }
    }

    private void dispatchAddToCart() {
        if (token != null && !token.equals("")) {
            Intent intent;
            if (orderType.equalsIgnoreCase(ParamEnum.PRODUCT.theValue()))
                intent = new Intent(this, ScheduleMyCart.class);
            else
                intent = new Intent(this, MyCartActivity.class);
            startActivity(intent);
        } else {
            CommonUtilities.showDialog(this);
        }
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            cuisine = getIntent().getStringExtra("cuisine");
            CommonUtilities.setToolbar(this, mainToolbar, tvTitle, cuisine);
            resAndStoreId = getIntent().getStringExtra("resAndStoreId");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceGetMenuData(cuisine,resAndStoreId);
    }

    private void serviceGetMenuData(String cuisine, String resAndStoreId) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            //if (!token.isEmpty()) {
            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            if(!token.isEmpty()){
                jsonObject.put("userId", userId);
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
                    MyDialog.getInstance(MenuActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        RestaurantModel restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                            ArrayList<RestaurantResponse> menuList = restaurantModel.getData().getMenuList();
                            if(menuList!=null && !menuList.isEmpty()){
                                tvNoData.setVisibility(View.GONE);
                                rvMenu.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                                menuDetailsAdapter = new MenuDetailsAdapter(MenuActivity.this,menuList,
                                        MenuActivity.this,DynamicFragment.class.getSimpleName(),true);
                                rvMenu.setAdapter(menuDetailsAdapter);
                            }else {
                                tvNoData.setVisibility(View.VISIBLE);
                            }
                            getCartCount();

                        } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(MenuActivity.this);
                        } else {
                            Toast.makeText(MenuActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void getCartCount() {
        try {
            //MyDialog.getInstance(this).showDialog(this);

            if (!token.isEmpty()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                String token = SharedPreferenceWriter.getInstance(MenuActivity.this).getString(kToken);
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<GetCartCountModel> beanCall = apiInterface.getCartCount(token, body);

                beanCall.enqueue(new Callback<GetCartCountModel>() {
                    @Override
                    public void onResponse(Call<GetCartCountModel> call, Response<GetCartCountModel> response) {
                        //MyDialog.getInstance(SubCategoryActivity.this).hideDialog();
                        if (response.isSuccessful()) {

                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getData() <= 0) {
                                    countTv.setVisibility(View.GONE);
                                } else {
                                    countTv.setVisibility(View.VISIBLE);
                                    countTv.setText(response.body().getData() + "");
                                }

                                orderType = response.body().getType();


                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MenuActivity.this);
                            } else {
                                Toast.makeText(MenuActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCartCountModel> call, Throwable t) {
                    }
                });
            }

            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddItem(int position,ArrayList<RestaurantResponse> menuDataList)
    {
        if (token != null
                && !token.equalsIgnoreCase(""))
        {
            RestaurantResponse cartData = menuDataList.get(position).getCartData();
            if(cartData!=null) {
                serviceUpdateCart(cartData.getProductId(),position,cartData.getQuantity()+1,menuDataList);
            }else {
                // add cart if cart data is null or blank
                serviceAddToCart(menuDataList.get(position).get_id());
            }
        }else{
            showLoginDialog(this);
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

        productIds=productId;
        quantitys=quantity;
        positions=position;
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);
                jsonObject.put("quantity", quantity);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.updateCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(MenuActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                RestaurantResponse restaurantResponse = menuDataList.get(position);
                                restaurantResponse.getCartData().setQuantity(quantity);
                                //menuDetailsAdapter.updateDataList(restaurantResponse,position);
                                //menuDetailsAdapter.notifyDataSetChanged();
                                menuDetailsAdapter.updateList(menuDataList);
                                Intent send = new Intent(MenuActivity.this, ScheduleMyCart.class);
                                startActivity(send);
                                //serviceGetMenuData(cuisine,resAndStoreId);


                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MenuActivity.this);
                            } else {
                              showDialog(MenuActivity.this,"2",menuDataList);
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
        productIds=productId;

        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.addToCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(MenuActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                serviceGetMenuData(cuisine,resAndStoreId);

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MenuActivity.this);
                            } else {

                                showDialog(MenuActivity.this,"1",null);


                             //   Toast.makeText(MenuActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void serviceUpdateCarts(String productId, int position, int quantity,ArrayList<RestaurantResponse> menuDataList) {

        productIds=productId;
        quantitys=quantity;
        positions=position;
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);
                jsonObject.put("quantity", quantity);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.updateCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(MenuActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                RestaurantResponse restaurantResponse = menuDataList.get(position);
                                restaurantResponse.getCartData().setQuantity(quantity);
                                //menuDetailsAdapter.updateDataList(restaurantResponse,position);
                                //menuDetailsAdapter.notifyDataSetChanged();
                                menuDetailsAdapter.updateList(menuDataList);
                                //serviceGetMenuData(cuisine,resAndStoreId);
//                                Intent send = new Intent(MenuActivity.this, ScheduleMyCart.class);
//                                startActivity(send);


                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MenuActivity.this);
                            } else {
                                showDialog(MenuActivity.this,"2",menuDataList);
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

    private void serviceAddToCarts(String productId) {
        productIds=productId;

        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.addToCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(MenuActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                serviceGetMenuData(cuisine,resAndStoreId);
                                Intent send = new Intent(MenuActivity.this, ScheduleMyCart.class);
                                startActivity(send);

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MenuActivity.this);
                            } else {

                                showDialog(MenuActivity.this,"1",null);


                                //   Toast.makeText(MenuActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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


    public void showDialog(Context context,String data,ArrayList<RestaurantResponse> menuDataList) {

        final Dialog dialogLockDetails = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        dialogLockDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLockDetails.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogLockDetails.setContentView(R.layout.dialog_clear_cart);
        dialogLockDetails.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogLockDetails.setCanceledOnTouchOutside(true);



        TextView tvOkay = dialogLockDetails.findViewById(R.id.tvOkay);

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCart(data,menuDataList);

                dialogLockDetails.dismiss();
            }
        });



        dialogLockDetails.show();
    }


   public   void ClearCart(String data,ArrayList<RestaurantResponse> menuDataList) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ClearCartResponse> beanCall = apiInterface.clearCart(token, body);

                beanCall.enqueue(new Callback<ClearCartResponse>() {
                    @Override
                    public void onResponse(Call<ClearCartResponse> call, Response<ClearCartResponse> response) {
                        MyDialog.getInstance(MenuActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ClearCartResponse restaurantModel = response.body();
                            if (restaurantModel.getStatus().equals("SUCCESS")) {
                                if(data.equals("1"))
                                {
                                    serviceAddToCarts(productIds);

                                }
                                else
                                {
                                    serviceUpdateCarts (productIds,positions,quantitys,menuDataList);

                                }


                             //   serviceGetMenuData(cuisine,resAndStoreId);

                            }  else {



                                //   Toast.makeText(MenuActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ClearCartResponse> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void addOnTextChangeListener() {
        edtSearchFrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(menuDetailsAdapter!=null)
                    menuDetailsAdapter.getFilter().filter(editable);
            }
        });
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