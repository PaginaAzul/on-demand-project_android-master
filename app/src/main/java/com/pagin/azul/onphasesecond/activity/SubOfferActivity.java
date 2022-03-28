package com.pagin.azul.onphasesecond.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.adapters.SubOfferAdapter;
import com.pagin.azul.onphasesecond.bottomsheet.FilterBottomSheet;
import com.pagin.azul.onphasesecond.model.ProductDetailModel;
import com.pagin.azul.onphasesecond.model.ProductDetailsResponse;
import com.pagin.azul.onphasesecond.model.ProductListResponse;
import com.pagin.azul.onphasesecond.model.ProductModel;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

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

public class SubOfferActivity extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivFilter)
    ImageView ivFilter;

    @BindView(R.id.rvSubOffer)
    RecyclerView rvSubOffer;

    @BindView(R.id.edtSearchFrag)
    EditText edtSearchFrag;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private String token;
    private String userId;
    private String langCode;
    private SubOfferAdapter subOfferAdapter;
    private ArrayList<ProductDetailsResponse> data;

    private ArrayList<ProductDetailsResponse> datas;


    private int radioButtonId;
    private String validityDate;
    private GPSTracker gpsTracker;
    private String storeCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_offer);
        ButterKnife.bind(this);
        ivFilter.setVisibility(View.VISIBLE);
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        getIntentData();
        addOnTextChangeListener();
    }

    @OnClick({R.id.ivFilter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFilter:
                FilterBottomSheet bottomSheet = new FilterBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putInt(ParamEnum.ID.theValue(),radioButtonId);
                bundle.putString(ParamEnum.DATA.theValue(),validityDate);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "");
                bottomSheet.setOnCommonListener(this);
                break;
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getIntent().getStringExtra(ParamEnum.TITLE.theValue()) + " " + getString(R.string.offers));
            gpsTracker = new GPSTracker(this, this);
            storeCategoryId = getIntent().getStringExtra(ParamEnum.ID.theValue());

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceGetOfferProductList(storeCategoryId, gpsTracker.getLatitude(), gpsTracker.getLongitude());
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
                subOfferAdapter.getFilter().filter(editable);
            }
        });
    }

    private void setUpRecyclerView(ArrayList<ProductDetailsResponse> data) {
        this.data = data;
        rvSubOffer.setLayoutManager(new LinearLayoutManager(this));
        subOfferAdapter = new SubOfferAdapter(this, data, this);
        rvSubOffer.setAdapter(subOfferAdapter);
    }

    private void serviceGetOfferProductList(String id, double latitude, double longitude) {
        try {
            MyDialog.getInstance(this).showDialog(this);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            if(userId!=null && !userId.isEmpty())
                jsonObject.put("userId", userId);
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("storeCategoryId", id);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<ProductListResponse> beanCall = apiInterface.getOfferProductList(body);

            beanCall.enqueue(new Callback<ProductListResponse>() {
                @Override
                public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                    MyDialog.getInstance(SubOfferActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        ProductListResponse restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                            if(restaurantModel.getData().size()>0)
                            {
                                tvNoData.setVisibility(View.GONE);
                                setUpRecyclerView(restaurantModel.getData());


                            }
                            else

                            {
                                tvNoData.setVisibility(View.VISIBLE);

                            }
                        } else {
                            Toast.makeText(SubOfferActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductListResponse> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddCart(int position, ArrayList<ProductDetailsResponse> menuDataList) {
        if (token != null && !token.equals("")) {
            RestaurantResponse cartData = menuDataList.get(position).getCartData();
            if (cartData != null) {
                serviceUpdateCart(cartData.getProductId(), cartData.getQuantity() + 1);
            } else {
                // add cart if cart data is null or blank
                serviceAddToCart(menuDataList.get(position).get_id());
            }
        } else {
            CommonUtilities.showDialog(this);
        }
    }

    private void serviceAddToCart(String productId) {
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
                        MyDialog.getInstance(SubOfferActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                dispatchAddToCart();

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(SubOfferActivity.this);
                            } else {
                                Toast.makeText(SubOfferActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void serviceUpdateCart(String productId, int quantity) {
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
                        MyDialog.getInstance(SubOfferActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                dispatchAddToCart();

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(SubOfferActivity.this);
                            } else {
                                Toast.makeText(SubOfferActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void dispatchAddToCart() {
        Intent intent = new Intent(SubOfferActivity.this, ScheduleMyCart.class);
        intent.putExtra(ParamEnum.TYPE.theValue(), SubOfferActivity.class.getSimpleName());
        startActivity(intent);
    }

    @Override
    public void onApplyFilter(int radioButtonId, String validityDate) {
        this.radioButtonId = radioButtonId;
        this.validityDate = validityDate;
        if(radioButtonId != -1 && validityDate.isEmpty()){
            if (radioButtonId == R.id.rbHigh) // filter according high to low price
                Collections.sort(data, (response1, response2) -> Integer.valueOf(response2.getOfferPrice()).compareTo(Integer.valueOf(response1.getOfferPrice())));
            else // filter according low to high price
                Collections.sort(data, (response1, response2) -> Integer.valueOf(response1.getOfferPrice()).compareTo(Integer.valueOf(response2.getOfferPrice())));
            subOfferAdapter.update(data);
        }else if(!validityDate.isEmpty() && radioButtonId == -1){
            ArrayList<ProductDetailsResponse> filterList = new ArrayList<>();
            for (ProductDetailsResponse response : data) {
                if (getOutputFormat(response.getEndDate()).equals(validityDate))
                    filterList.add(response);
            }
            subOfferAdapter.update(filterList);
        }else if(radioButtonId != -1 && !validityDate.isEmpty()){
            // first filter list according to validityDate
            ArrayList<ProductDetailsResponse> filterList = new ArrayList<>();
            for (ProductDetailsResponse response : data) {
                if (getOutputFormat(response.getEndDate()).equals(validityDate))
                    filterList.add(response);
            }
            if (radioButtonId == R.id.rbHigh) // filter according high to low price
                Collections.sort(filterList, (response1, response2) -> Integer.valueOf(response2.getOfferPrice()).compareTo(Integer.valueOf(response1.getOfferPrice())));
            else // filter according low to high price
                Collections.sort(filterList, (response1, response2) -> Integer.valueOf(response1.getOfferPrice()).compareTo(Integer.valueOf(response2.getOfferPrice())));
            subOfferAdapter.update(filterList);
        }
    }

    private String getOutputFormat(String responseDate) {
        SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        SimpleDateFormat outputFormate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormate.parse(responseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormate.format(date);
        return formattedDate;
        //}
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")) {
                if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                } else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            } else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        } else {
            super.attachBaseContext(newBase);
        }
    }
}