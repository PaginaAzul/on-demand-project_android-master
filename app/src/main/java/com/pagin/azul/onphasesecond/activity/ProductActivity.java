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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.ClearCartResponse;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.ProductAdapter;
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

public class ProductActivity extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    @BindView(R.id.cartLayout)
    ConstraintLayout cartLayout;

    @BindView(R.id.countTv)
    TextView countTv;
    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private String token;
    private String userId;
    private String langCode;
    private ProductAdapter adapter;
    private String resAndStoreId;
    private String subCategoryName;
    private String orderType;
    String productIds;
    int positions;
    int quantitys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getString(R.string.product));
        cartLayout.setVisibility(View.VISIBLE);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        addOnTextChangeListener();
        getIntentData();
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

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            resAndStoreId = getIntent().getStringExtra(ParamEnum.RES_AND_STORE_ID.theValue());
            subCategoryName = getIntent().getStringExtra(ParamEnum.SUB_CATEGORY_NAME.theValue());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceGetProductData(resAndStoreId, subCategoryName);
    }

    private void setUpRecyclerView(ArrayList<RestaurantResponse> productList) {
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, this, productList);
        rvProduct.setAdapter(adapter);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ivCart) {
            dispatchAddToCart();
        }
        return super.onOptionsItemSelected(item);
    }*/

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

    @Override
    public void onProductClick(int position, ArrayList<RestaurantResponse> data) {
        Intent intent = new Intent(this, ProductDetails.class);
        intent.putExtra(ParamEnum.ID.theValue(), data.get(position).get_id());
        startActivity(intent);
    }

    private void serviceGetProductData(String resAndStoreId, String subCategoryName) {
        try {
            MyDialog.getInstance(this).showDialog(this);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            if(userId!=null && !userId.isEmpty())
                jsonObject.put("userId", userId);
            jsonObject.put("langCode", langCode);
            jsonObject.put("resAndStoreId", resAndStoreId);
            jsonObject.put("subCategoryName", subCategoryName);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<RestaurantModel> beanCall = apiInterface.getProductData(token, body);

            beanCall.enqueue(new Callback<RestaurantModel>() {
                @Override
                public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                    MyDialog.getInstance(ProductActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        RestaurantModel restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                            if(restaurantModel.getData().getProductList().size()>0)
                            {
                                tvNoData.setVisibility(View.GONE);
                                setUpRecyclerView(restaurantModel.getData().getProductList());
                                getCartCount();

                            }
                            else
                            {
                                tvNoData.setVisibility(View.VISIBLE);
                            }

                        } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(ProductActivity.this);
                        } else {
                            Toast.makeText(ProductActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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
                adapter.getFilter().filter(editable);
            }
        });
    }

    @Override
    public void onAddItem(int position, ArrayList<RestaurantResponse> menuDataList) {
        if (token != null
                && !token.equalsIgnoreCase("")) {
            RestaurantResponse cartData = menuDataList.get(position).getCartData();
            if (cartData != null) {
                serviceUpdateCart(cartData.getProductId(), position, cartData.getQuantity() + 1, menuDataList);
            } else {
                // add cart if cart data is null or blank
                serviceAddToCart(menuDataList.get(position).get_id());
            }
        } else {
            showLoginDialog(this);
        }

    }

    private void serviceUpdateCarts(String productId, int position, int quantity,ArrayList<RestaurantResponse> menuDataList) {


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
                        MyDialog.getInstance(ProductActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {


                                //serviceGetMenuData(cuisine,resAndStoreId);



                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ProductActivity.this);
                            } else {
                                showDialog(ProductActivity.this,"2",menuDataList);
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
                        MyDialog.getInstance(ProductActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                Intent send = new Intent(ProductActivity.this, MyCartActivity.class);
                                startActivity(send);

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ProductActivity.this);
                            } else {

                                showDialog(ProductActivity.this,"1",null);


                                //   Toast.makeText(ProductActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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
                        MyDialog.getInstance(ProductActivity.this).hideDialog();
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



                                //   Toast.makeText(ProductActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onRemoveItem(int position, ArrayList<RestaurantResponse> menuDataList) {
        RestaurantResponse cartData = menuDataList.get(position).getCartData();
        if (cartData != null && cartData.getQuantity() != 0) {
            serviceUpdateCart(cartData.getProductId(), position, cartData.getQuantity() - 1, menuDataList);
        }
    }

    private void serviceUpdateCart(String productId, int position, int quantity, ArrayList<RestaurantResponse> menuDataList) {
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
                        MyDialog.getInstance(ProductActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                RestaurantResponse restaurantResponse = menuDataList.get(position);
                                restaurantResponse.getCartData().setQuantity(quantity);
                                adapter.update(menuDataList);
                                getCartCount();
                                Intent send = new Intent(ProductActivity.this, ScheduleMyCart.class);
                                startActivity(send);

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ProductActivity.this);
                            } else {
                                showDialog(ProductActivity.this,"2",menuDataList);
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
            productIds=productId;

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
                        MyDialog.getInstance(ProductActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                serviceGetProductData(resAndStoreId, subCategoryName);

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ProductActivity.this);
                            } else {
                                showDialog(ProductActivity.this,"1",null);
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

    private void getCartCount() {
        try {
            //MyDialog.getInstance(this).showDialog(this);

            if (!token.isEmpty()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                String token = SharedPreferenceWriter.getInstance(ProductActivity.this).getString(kToken);
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
                                CommonUtility.showDialog1(ProductActivity.this);
                            } else {
                                Toast.makeText(ProductActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
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