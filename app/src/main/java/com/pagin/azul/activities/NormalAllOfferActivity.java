package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.adapter.NormalViewOfferAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class NormalAllOfferActivity extends AppCompatActivity {
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.rv_view_offer)
    RecyclerView rvViewOffer;
    @BindView(R.id.no_data)
    TextView no_data;

    private NormalViewOfferAdapter viewOfferdAdapter;
    private ArrayList<NormalUserPendingOrderInner> viewOfferList;
    private String orderID;


    public static Intent getIntent(Context context,String orderID){
        Intent intent= new Intent(context,NormalAllOfferActivity.class);
        intent.putExtra("kType",orderID);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_all_offer);
        ButterKnife.bind(this);

        if(getIntent()!=null){
            orderID=(String)getIntent().getSerializableExtra("kType");
            getOfferListApi(orderID);
        }

        viewOfferList = new ArrayList<>();



    }


    private void getOfferListApi(String orderID) {
        try {
            MyDialog.getInstance(NormalAllOfferActivity.this).showDialog(NormalAllOfferActivity.this);
            String token = SharedPreferenceWriter.getInstance(NormalAllOfferActivity.this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("lat", SharedPreferenceWriter.getInstance(this).getString(kLat));
                jsonObject.put("long", SharedPreferenceWriter.getInstance(this).getString(kLong));
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getOfferList(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(NormalAllOfferActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    viewOfferList = response.body().getDataList();
                                    rvViewOffer.setLayoutManager(new LinearLayoutManager(NormalAllOfferActivity.this));
                                    viewOfferdAdapter = new NormalViewOfferAdapter(NormalAllOfferActivity.this, viewOfferList);
                                    rvViewOffer.setAdapter(viewOfferdAdapter);
                                    rvViewOffer.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                   // getDataOnClick();
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    rvViewOffer.setVisibility(View.GONE);
                                }


                                //Toast.makeText(MapProfessinalWorkerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NormalAllOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NormalUserPendingOrderResponse> call, Throwable t) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
