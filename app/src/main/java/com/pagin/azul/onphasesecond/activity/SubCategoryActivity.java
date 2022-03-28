package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.bean.CommonResponseBean;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.CategoryAdapter;
import com.pagin.azul.onphasesecond.model.GetCartCountModel;
import com.pagin.azul.onphasesecond.model.ProductModel;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

public class SubCategoryActivity extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvSubCategory)
    RecyclerView rvSubCategory;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    @BindView(R.id.cartLayout)
    ConstraintLayout cartLayout;

    @BindView(R.id.countTv)
    TextView countTv;

    private String token;
    private CategoryAdapter categoryAdapter;
    private String resAndStoreId;
    private String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ButterKnife.bind(this);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        setToolbar();
        addOnTextChangeListener();
        getIntentData();
    }

    @OnClick({R.id.cartLayout})
    public void onClick(View view) {
        switch (view.getId()) {
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

    @Override
    protected void onStart() {
        super.onStart();
        getCartCount();
    }

    private void setUpRecyclerView(ArrayList<ProductResponse> data) {
        rvSubCategory.setLayoutManager(new GridLayoutManager(this, 2));
        categoryAdapter = new CategoryAdapter(this, this, data);
        rvSubCategory.setAdapter(categoryAdapter);
    }

    private void setToolbar() {
        cartLayout.setVisibility(View.VISIBLE);
        CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getString(R.string.sub_categroy));
        /*tvTitle.setText(R.string.sub_categroy);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);*/
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            resAndStoreId = getIntent().getStringExtra(ParamEnum.RES_AND_STORE_ID.theValue());
            serviceGetSubCategoryList(getIntent().getStringExtra(ParamEnum.ID.theValue()));
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            return true;
        }/*else if (item.getItemId() == R.id.ivCart) {
            if(token!=null && !token.equals("")){
                Intent intent = new Intent(this, ScheduleMyCart.class);
                startActivity(intent);
            }else{
                CommonUtilities.showDialog(this);
            }
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCategoryClick(int position, ArrayList<ProductResponse> categoryList) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(ParamEnum.RES_AND_STORE_ID.theValue(), resAndStoreId);
        intent.putExtra(ParamEnum.SUB_CATEGORY_NAME.theValue(), categoryList.get(position).getName());
        startActivity(intent);
    }

    private void serviceGetSubCategoryList(String id) {
        try {
            MyDialog.getInstance(this).showDialog(this);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("categoryId", id);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<ProductModel> beanCall = apiInterface.getSubCategoryList(body);

            beanCall.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    MyDialog.getInstance(SubCategoryActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        ProductModel restaurantModel = response.body();
                        if (restaurantModel.getResponse_code().equalsIgnoreCase("200")) {
                            setUpRecyclerView(restaurantModel.getData());
                        } else {
                            Toast.makeText(SubCategoryActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {
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
                categoryAdapter.getFilter().filter(editable);
            }
        });
    }

    private void getCartCount() {
        try {
            //MyDialog.getInstance(this).showDialog(this);

            if (!token.isEmpty()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                String token = SharedPreferenceWriter.getInstance(SubCategoryActivity.this).getString(kToken);
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
                                CommonUtility.showDialog1(SubCategoryActivity.this);
                            } else {
                                Toast.makeText(SubCategoryActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
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