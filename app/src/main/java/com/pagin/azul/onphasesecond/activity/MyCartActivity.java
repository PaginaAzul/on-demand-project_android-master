package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.ManageAddressActivity;
import com.pagin.azul.activities.SignUpOptions;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.adapters.MenuDetailsAdapter;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.getOutputFormats;

public class MyCartActivity extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvRestroAddress)
    TextView tvRestroAddress;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.tvDeliveryCharges)
    TextView tvDeliveryCharges;

    @BindView(R.id.tvTotalAmountPaid)
    TextView tvTotalAmountPaid;

    @BindView(R.id.recyclerMyCart)
    RecyclerView recyclerMyCart;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.tvPay)
    TextView tvPay;

    private String token;
    private String userId;
    private String langCode;
    private MenuDetailsAdapter menuDetailsAdapter;
    private ArrayList<RestaurantResponse> menuDataList;
    private String latitude;
    private String longitude;
    private String deliveryLat;
    private String deliveryLon;
    private String landmark;
    private String buildingAndApart;
    private String excepetdDeliveryTime;
    private String deli_charge="0";
    private String currency="";
    private String minimumValue="";
    private String address="";
    private static final int ADDRESS_REQ = 31;
    private double totalAmount;
    private double total;
    private String lat="",lon="";
    String check="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.my_cart));
        tvPay.setVisibility(View.VISIBLE);

        GPSTracker gpsTracker = new GPSTracker(MyCartActivity.this,MyCartActivity.this);
        lat = String.valueOf(gpsTracker.getLatitude());
        lon = String.valueOf(gpsTracker.getLongitude());
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        setUpRecyclerView();
        serviceGetCartItem();


      //  checktime();
    }

    private String getReminingTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    @OnClick({R.id.tvChange,R.id.tvPay,R.id.ivDistance})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tvChange:
                dispatchManageAddress();
                break;
            case R.id.tvPay:
                if(        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime).isEmpty()|| SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime)==null)
                {
                    if(validation())
                        servicePlaceOrder();
                }
                else
                {
                    checktime();

                    if(validations())
                        servicePlaceOrder();
                }

                break;
            case R.id.ivDistance:
                CommonUtilities.dispatchGoogleMap(this,latitude,longitude);
                break;
        }
    }

    private boolean validation(){
        if(minimumValue==null || minimumValue.isEmpty()){
            //  Toast.makeText(this,getString(R.string.minimum_value_paid_value)+minimumValue,Toast.LENGTH_SHORT).show();
            return false;
        }else if(totalAmount < Double.parseDouble(minimumValue)){
            Toast.makeText(this,getString(R.string.minimum_value_paid_value)+minimumValue,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validations(){
        if(minimumValue==null || minimumValue.isEmpty()){
            //  Toast.makeText(this,getString(R.string.minimum_value_paid_value)+minimumValue,Toast.LENGTH_SHORT).show();
            return false;
        }else if(totalAmount < Double.parseDouble(minimumValue)){
            Toast.makeText(this,getString(R.string.minimum_value_paid_value)+minimumValue,Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!check.equals("1"))
        {
            Toast.makeText(this,"time slots are not valid",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void dispatchManageAddress() {
        Intent intent = new Intent(this, ManageAddressActivity.class);
        startActivityForResult(intent,ADDRESS_REQ);
    }

    private void getIntentData() {
        /*if(getIntent().getExtras()!=null){
            RestaurantResponse details = getIntent().getParcelableExtra(ParamEnum.DATA.theValue());
            if (details != null) {
                tvRestroAddress.setText(details.getAddress());
                latitude = details.getLatitude();
                longitude = details.getLongitude();
                String resAndStoreId = details.get_id();
                serviceGetDeliveryCharge(resAndStoreId);
            }else {
                // coming from past order
                tvRestroAddress.setText(getIntent().getStringExtra(ParamEnum.ADDRESS.theValue()));
                latitude = getIntent().getStringExtra(ParamEnum.LAT.theValue());
                longitude = getIntent().getStringExtra(ParamEnum.LONG.theValue());
                String resAndStoreId = getIntent().getStringExtra(ParamEnum.ID.theValue());
                serviceGetDeliveryCharge(resAndStoreId);
            }
        }*/
        try {
            deliveryLat = SharedPreferenceWriter.getInstance(this).getString(kLat);
            deliveryLon = SharedPreferenceWriter.getInstance(this).getString(kLong);
            double lat = Double.parseDouble(deliveryLat);
            double lon = Double.parseDouble(deliveryLon);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            tvAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerView() {
        menuDataList = new ArrayList<>();
        recyclerMyCart.setLayoutManager(new LinearLayoutManager(this));
        menuDetailsAdapter = new MenuDetailsAdapter(this,menuDataList,this,MyCartActivity.class.getSimpleName(),false);
        recyclerMyCart.setAdapter(menuDetailsAdapter);
    }

    public void showOrderPlacedDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_order_placed);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvOkay = dialog.findViewById(R.id.tvOkay);

        tvOkay.setOnClickListener(v -> {
            startActivity(new Intent(this, FoodAndGroceryActivity.class));
            finish();
        });

        dialog.show();
    }

    private void serviceGetCartItem(){
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.getCartItem(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        MyDialog.getInstance(MyCartActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                menuDataList.clear();
                                menuDataList.addAll(model.getData());
                                menuDetailsAdapter.notifyDataSetChanged();



                                if(menuDataList!=null && !menuDataList.isEmpty()){
                                    showNoDataText(View.VISIBLE,View.GONE);
                                    getIntentData();
                                    RestaurantResponse details = menuDataList.get(0);
                                    String resAndStoreId = details.getResAndStoreId();
                                    serviceGetDeliveryCharge(resAndStoreId);

                                    RestaurantResponse sellerData = menuDataList.get(0).getSellerData();
                                    tvRestroAddress.setText(sellerData.getAddress());
                                    latitude = sellerData.getLatitude();
                                    longitude = sellerData.getLongitude();
                                    excepetdDeliveryTime = sellerData.getDeliveryTime();
                                }else {
                                    showNoDataText(View.GONE,View.VISIBLE);
                                }

                                setBillData();

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MyCartActivity.this);
                            } else {
                                Toast.makeText(MyCartActivity.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
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
    private void serviceResAndStoreDetail(String resAndStoreId) {
        Toast.makeText(this, ""+resAndStoreId, Toast.LENGTH_SHORT).show();
        try {

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", (userId.trim().isEmpty()?null:userId));
            jsonObject.put("resAndStoreId", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.rest));
            jsonObject.put("langCode", langCode);
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lon);

            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<RestaurantModel> beanCall = apiInterface.resAndStoreDetail(token, body);

            beanCall.enqueue(new Callback<RestaurantModel>() {
                @Override
                public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                    if (response.isSuccessful()) {
                        RestaurantModel restaurantModel = response.body();
                        if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                            Toast.makeText(MyCartActivity.this, ""+restaurantModel.getData().getOpeningTime()+restaurantModel.getData().getClosingTime(), Toast.LENGTH_SHORT).show();
                        } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                          //  CommonUtility.showDialog1(MyCartActivity.this);
                            Toast.makeText(MyCartActivity.this, "yesss", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MyCartActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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




    private void showNoDataText(int scrollViewVisibility, int visibleNotData) {
        scrollView.setVisibility(scrollViewVisibility);
        tvNoData.setVisibility(visibleNotData);
        tvPay.setVisibility(scrollViewVisibility);
    }

    @Override
    public void onAddItem(int position,ArrayList<RestaurantResponse> menuDataList) {
        int quantity = menuDataList.get(position).getQuantity();
        /*if(quantity!=0){
            serviceUpdateCart(menuDataList.get(position).getProductId(),position,quantity+1);
        }else {
            serviceAddToCart(menuDataList.get(position).getProductId());
        }*/
        serviceUpdateCart(menuDataList.get(position).getProductId(),position,quantity+1);
    }

    @Override
    public void onRemoveItem(int position,ArrayList<RestaurantResponse> menuDataList) {
        int quantity = menuDataList.get(position).getQuantity();
        /*if(quantity!=0){
            serviceUpdateCart(menuDataList.get(position).getProductId(),position,quantity-1);
        }*/
        serviceUpdateCart(menuDataList.get(position).getProductId(),position,quantity-1);
    }

    private void serviceUpdateCart(String productId, int position, int quantity) {
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
                        MyDialog.getInstance(MyCartActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if(quantity==0)
                                    menuDataList.remove(menuDataList.get(position));
                                else
                                    menuDataList.get(position).setQuantity(quantity);

                                menuDetailsAdapter.notifyDataSetChanged();

                                if(!menuDataList.isEmpty()) {
                                    showNoDataText(View.VISIBLE,View.GONE);
                                    setBillData();
                                }else {
                                    showNoDataText(View.GONE,View.VISIBLE);
                                }

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {

                               CommonUtility.showDialog1(MyCartActivity.this);

                            } else {
                             //   showDialogClearCart();

                              Toast.makeText(MyCartActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void showDialogClearCart() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_clear_cart, null, false);
        dialog.setContentView(view);

        TextView tvOkay = view.findViewById(R.id.tvOkay);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
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
                        MyDialog.getInstance(MyCartActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                serviceGetCartItem();


                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MyCartActivity.this);
                            } else {
                                Toast.makeText(MyCartActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void serviceGetDeliveryCharge(String resAndStoreId) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("resAndStoreId", resAndStoreId);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.getDeliveryCharge(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(MyCartActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                deli_charge=restaurantModel.getData().getDeliveryCharge();
                                currency=restaurantModel.getData().getCurrency();


                                minimumValue=restaurantModel.getData().getMinimumValue();
                                setBillData();



                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MyCartActivity.this);
                            } else {
                                Toast.makeText(MyCartActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void setBillData()
    {
        total=0;
        if(menuDataList!=null && !menuDataList.isEmpty())
        for(RestaurantResponse temp:menuDataList){
            String offerPrice = temp.getProductData().getOfferPrice();

            if(temp.getSellerData()!=null)
            {
                if(temp.getSellerData().getOfferStatus().equals("Active"))
                {
                    if(temp.getProductData().getoStatus().equals("Active"))
                    {
                        if(offerPrice!=null)
                        {
                            Date c = Calendar.getInstance().getTime();

                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);

                            try{

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


                                String str1 = formattedDate;
                                Date date1 = formatter.parse(str1);

                                String str2 = getOutputFormats(temp.getProductData().getEndDate());
                                Date date2 = formatter.parse(str2);

                                if (date2.compareTo(date1)>=0)
                                {

                                    total += temp.getQuantity() * Double.parseDouble(offerPrice);

                                }
                                else
                                {

                                    total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());


                                }

                            }catch (ParseException e1){
                                e1.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

                    }
                }
                else
                {
                    total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

                }
            }




            else
            {
                total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

            }

        }
        try{
            //double totalPrice = Double.parseDouble(CommonUtilities.getPriceFormat(String.valueOf(total)));
            if(currency!=null && !currency.isEmpty()) {
                tvTotalPrice.setText(CommonUtilities.getPriceFormat(total)+" "+currency);
                String deliveryCharge = CommonUtilities.getPriceFormat(deli_charge);
                tvDeliveryCharges.setText(deliveryCharge + " " + currency);
                deliveryCharge=  deliveryCharge.replace(",","");

                totalAmount = total + Double.parseDouble(deliveryCharge);
                tvTotalAmountPaid.setText(CommonUtilities.getPriceFormat(totalAmount) + " " + currency);
            }else {
                tvTotalPrice.setText(CommonUtilities.getPriceFormat(total)+" Kz");
                String deliveryCharge = CommonUtilities.getPriceFormat(deli_charge);
                tvDeliveryCharges.setText(deliveryCharge + " Kz");
                deliveryCharge=  deliveryCharge.replace(",","");

                totalAmount = total + Double.parseDouble(deliveryCharge);
                tvTotalAmountPaid.setText(CommonUtilities.getPriceFormat(totalAmount) + " Kz");
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDRESS_REQ && resultCode == Activity.RESULT_OK && data!=null){
            AddressList addressList = (AddressList) data.getSerializableExtra(ParamEnum.DATA.theValue());
            if (addressList != null) {
                address = addressList.getAddress();
                deliveryLat = addressList.getLat();
                deliveryLon = addressList.getLongs();
                landmark = addressList.getLandmark();
                buildingAndApart = addressList.getBuildingAndApart();
                tvAddress.setText(landmark+" "+buildingAndApart+", "+address);
            }
        }
    }

    private void servicePlaceOrder() {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("deliveryDate", getCurrentDate());
                jsonObject.put("deliveryCharge", deli_charge);
                jsonObject.put("totalPrice", totalAmount);
                jsonObject.put("price", total);
                jsonObject.put("orderType", "Menu");
                jsonObject.put("offerApplicable", false);
                jsonObject.put("offerAmount", "0");
                jsonObject.put("address", address);
                jsonObject.put("latitude", deliveryLat);
                jsonObject.put("longitude", deliveryLon);
                jsonObject.put("landmark", landmark);
                jsonObject.put("buildingAndApart", buildingAndApart);
                jsonObject.put("excepetdDeliveryTime", excepetdDeliveryTime);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.placeOrder(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(MyCartActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                showOrderPlacedDialog();

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MyCartActivity.this);
                            } else {
                                tvPay.setVisibility(View.GONE);

                                Toast.makeText(MyCartActivity.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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


    void checktime()

    {

        String string3= SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime);
        String string8= SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.ctime);

        if(!(string3.contains("AM")||string3.contains("PM")))
        {
            try {


                Date mToday = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String curTime = sdf.format(mToday);


                Date time1 = new SimpleDateFormat("HH:mm").parse(string3);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                Date time2 = new SimpleDateFormat("HH:mm").parse(string8);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);

                String someRandomTime = curTime;
                Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();
                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                    check="1";
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    System.out.println(true);
                }
                else
                {
                    check="0";

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else

        {
            try {



                String string1 = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime);

                Date time1 = new SimpleDateFormat("HH:mm aaa").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                String string2 = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.ctime);
                Date time2 = new SimpleDateFormat("HH:mm aaa").parse(string2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm aaa");
                Calendar calander = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aaa");


                String someRandomTime = simpleDateFormat.format(calander.getTime()).toString();
                Date d = new SimpleDateFormat("HH:mm aaa").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();


                try {
                    Date mToday = new Date();

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String curTime = sdf.format(mToday);
                    Date start = sdf.parse(string1);
                    Date end = sdf.parse(string2);
                    Date userDate = sdf.parse(curTime);

                    if(end.before(start))
                    {
                        Calendar mCal = Calendar.getInstance();
                        mCal.setTime(end);
                        mCal.add(Calendar.DAY_OF_YEAR, 1);
                        end.setTime(mCal.getTimeInMillis());
                    }

                    Log.d("curTime", userDate.toString());
                    Log.d("start", start.toString());
                    Log.d("end", end.toString());


                    if (userDate.after(start) && userDate.before(end)) {
                        check="1";
                    }
                    else{
                        check="0";
                    }
                } catch (ParseException e) {
                    // Invalid date was entered
                }






//                if(string1.contains("AM")&&string2.contains("AM"))
//                {
//
//                    String[] timeArray = string1.split(":");
//                    String[] timeArray1 = string2.split(":");
//                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
//
//
//                    int HH = Integer.parseInt(timeArray[0]);
//                    int HH1 = Integer.parseInt(timeArray1[0]);
//                    int HH2 = Integer.parseInt(timeArray2[0]);
//                    if(simpleDateFormat.format(calander.getTime()).toString().contains("PM"))
//                    {
//                        check="0";
//                    }
//                    else if(HH<=HH2&&HH1>=HH2)                    {
//                        check="1";
//                    }
//                    else
//                    {
//                        check="0";
//
//                    }
//                }
//                else if(string1.contains("PM")&&string2.contains("PM"))
//                {
//                    String[] timeArray = string1.split(":");
//                    String[] timeArray1 = string2.split(":");
//                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
//
//
//                    int HH = Integer.parseInt(timeArray[0]);
//                    int HH1 = Integer.parseInt(timeArray1[0]);
//                    int HH2 = Integer.parseInt(timeArray2[0]);
//                    Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();
//
//
//                    if(simpleDateFormat.format(calander.getTime()).toString().contains("AM"))
//                    {
//                        check="0";
//                    }
//
//
//                   else  if(HH<=HH2&&HH1>=HH2)                    {
//                        check="1";
//                    }
//                    else
//                    {
//                        check="0";
//
//                    }
//                }
//                else
//                {
//
//                    Toast.makeText(context, "yessss", Toast.LENGTH_SHORT).show();
//
//
//                    try {
//                        Date mToday = new Date();
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
//                        String curTime = sdf.format(mToday);
//                        Date start = sdf.parse(string1);
//                        Date end = sdf.parse(string2);
//                        Date userDate = sdf.parse(curTime);
//
//                        if(end.before(start))
//                        {
//                            Calendar mCal = Calendar.getInstance();
//                            mCal.setTime(end);
//                            mCal.add(Calendar.DAY_OF_YEAR, 1);
//                            end.setTime(mCal.getTimeInMillis());
//                        }
//
//                        Log.d("curTime", userDate.toString());
//                        Log.d("start", start.toString());
//                        Log.d("end", end.toString());
//
//
//                        if (userDate.after(start) && userDate.before(end)) {
//                            Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//                            Log.d("result", "falls between start and end , go to screen 1 ");
//                        }
//                        else{
//                            Toast.makeText(context, "nooo", Toast.LENGTH_SHORT).show();
//
//                            Log.d("result", "does not fall between start and end , go to screen 2 ");
//                        }
//                    } catch (ParseException e) {
//                        // Invalid date was entered
//                    }
////                    Toast.makeText(context, "third", Toast.LENGTH_SHORT).show();
////
////                    String[] timeArray = string1.split(":");
////                    String[] timeArray1 = string2.split(":");
////                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
////
////
////                    int HH = Integer.parseInt(timeArray[0]);
////                    int HH1 = Integer.parseInt(timeArray1[0]);
////                    int HH2 = Integer.parseInt(timeArray2[0]);
////
////
////                    if(simpleDateFormat.format(calander.getTime()).toString().contains("AM")&&HH<=HH2)
////                    {
////                        check="1";
////                        Toast.makeText(context, "f", Toast.LENGTH_SHORT).show();
////                    }
////                    else if(simpleDateFormat.format(calander.getTime()).toString().contains("PM")&&HH1>=HH2)
////                    {
////                        check="1";
////
////                    }
////
////                    else   if(HH<=HH2&&HH1>=HH2)                    {
////                        check="1";
////                    }
////
////                    else
////                    {
////
////                        check="0";
////
////                    }
//                }
            } catch (ParseException e) {


                e.printStackTrace();
            }
        }

    }
    private String getReminingTimes() {
        String delegate = "hh:mm";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }


    private String getCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = mdformat.format(calendar.getTime());

        return currentDate;
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