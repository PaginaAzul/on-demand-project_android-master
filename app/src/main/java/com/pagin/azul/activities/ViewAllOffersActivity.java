package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.ViewAllOfferAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.bean.ViewAllOfferResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.NUActiveDeliveryPersonFrag;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
import static com.pagin.azul.Constant.Constants.kOrderId;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;


public class ViewAllOffersActivity extends AppCompatActivity {
    @BindView(R.id.rv_view_offer)
    RecyclerView rvViewOffer;
    @BindView(R.id.no_data)
    TextView no_data;
    private ViewAllOfferAdapter viewOfferdAdapter;
    private ArrayList<NormalUserPendingOrderInner> viewOfferList;
    private NUActiveDeliveryPersonFrag activeFrag;
    private String realOrderId = "";
    private String commingFrom = "";
    private String orderID = "";


    //  BROADCAST RECEIVER CLASS Object : TO tap
    private BroadcastReceiver pushNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                Bundle bundle = intent.getExtras();

                String p_id = bundle.getString("offerAvailable");

                getOfferListApi();
            }

            Log.w(DeliveryMakeOfferActivity.class.getSimpleName(), "offerAvailable");
        }
    };


    public static Intent getIntent(Context context, String commingFrom) {
        Intent intent = new Intent(context, ViewAllOffersActivity.class);
        intent.putExtra("ktype", commingFrom);
        return intent;
    }

    public static Intent getIntent(Context context, String commingFrom, String orderID) {
        Intent intent = new Intent(context, ViewAllOffersActivity.class);
        intent.putExtra("ktype", commingFrom);
        intent.putExtra("orderID", orderID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_offers);
        ButterKnife.bind(this);

        viewOfferList = new ArrayList<>();
        activeFrag = new NUActiveDeliveryPersonFrag();
        if (getIntent() != null) {
            commingFrom = (String) getIntent().getStringExtra("ktype");
            orderID = (String) getIntent().getStringExtra("orderID");
        }

        getOfferListApi();
    }

    @OnClick({R.id.iv_back})
    void onCliclk(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void getDataOnClick() {
        viewOfferdAdapter.setListener(new ViewAllOfferAdapter.AcceptOfferInterface() {
            @Override
            public void onAccceptOfferClick(View v, int pos, String id, String rOrderId) {
                realOrderId = rOrderId;
                showDialogA(id, "Accept");
            }

            @Override
            public void onRejectOfferClick(View v, int pos, String id) {
                showDialogA(id, "Reject");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //      REGISTER LocalBroadcastManager TO HANDLE PUSH
        registerReceiver(pushNotifyReceiver,
                new IntentFilter("Tap Successful"));
    }

    private void getOfferListApi() {
        try {
            MyDialog.getInstance(ViewAllOffersActivity.this).showDialog(ViewAllOffersActivity.this);
            String token = SharedPreferenceWriter.getInstance(ViewAllOffersActivity.this).getString(kToken);
            if (!token.isEmpty()) {
                String orderId = null;
                if (orderID != null && !orderID.isEmpty()) {
                    orderId = orderID;
                } else {
                    orderId = SharedPreferenceWriter.getInstance(this).getString(kOrderId);
                }
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("lat", SharedPreferenceWriter.getInstance(this).getString(kLat));
                jsonObject.put("long", SharedPreferenceWriter.getInstance(this).getString(kLong));
                jsonObject.put("orderId", orderId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getOfferList(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(ViewAllOffersActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    viewOfferList = response.body().getDataList();
                                    rvViewOffer.setLayoutManager(new LinearLayoutManager(ViewAllOffersActivity.this));
                                    viewOfferdAdapter = new ViewAllOfferAdapter(ViewAllOffersActivity.this, viewOfferList);
                                    rvViewOffer.setAdapter(viewOfferdAdapter);
                                    rvViewOffer.setVisibility(View.VISIBLE);
                                    no_data.setVisibility(View.GONE);
                                    getDataOnClick();
                                    Toast.makeText(ViewAllOffersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    //no_data.setVisibility(View.VISIBLE);
                                    //rvViewOffer.setVisibility(View.GONE);
                                    viewOfferList = response.body().getDataList();
                                    finish();
                                }

                                //Toast.makeText(ViewAllOffersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ViewAllOffersActivity.this);
                            } else {
                                Toast.makeText(ViewAllOffersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void callAcceptOfferApi(String offerId) {
        try {
            MyDialog.getInstance(ViewAllOffersActivity.this).showDialog(ViewAllOffersActivity.this);
            String token = SharedPreferenceWriter.getInstance(ViewAllOffersActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("offerId", offerId);
                jsonObject.put("orderId", realOrderId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<ViewAllOfferResponse> beanCall = apiInterface.acceptOffer(token, body);
                beanCall.enqueue(new Callback<ViewAllOfferResponse>() {
                    @Override
                    public void onResponse(Call<ViewAllOfferResponse> call, Response<ViewAllOfferResponse> response) {
                        MyDialog.getInstance(ViewAllOffersActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ViewAllOfferResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                //dialogSubmitSuccess(getString(R.string.offer_accept_sccussfully));

                                if (commingFrom.equalsIgnoreCase("FromNUDP")) {

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("kFromCat", "Delivery");
                                    hashMap.put("kFrom", "Active");

                                    startActivity(HomeMainActivity.getIntent(ViewAllOffersActivity.this, hashMap));
                                    finish();
                                    //startActivity(NUDeliveryPersonDashboardActivity.getIntent(ViewAllOffersActivity.this, "VIEWOFFER"));
                                } else if (commingFrom.equalsIgnoreCase("FromNUPW")) {

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("kFromCat", "Professional");
                                    hashMap.put("kFrom", "Active");

                                    startActivity(HomeMainActivity.getIntent(ViewAllOffersActivity.this, hashMap));
                                    finish();
                                    //startActivity(NUProfessionalWorkerDashboardActivity.getIntent(ViewAllOffersActivity.this, "VIEWOFFER"));
                                }

//                                getOfferListApi();
                                //Toast.makeText(ViewAllOffersActivity.this, getString(R.string.offer_accept_sccussfully), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ViewAllOffersActivity.this);
                            } else {
                                Toast.makeText(ViewAllOffersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ViewAllOfferResponse> call, Throwable t) {

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dialogSubmitSuccess(String msg) {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_submit_successfully_layout);


        TextView txtMsg = (TextView) dialog.findViewById(R.id.tv_desc);
        TextView btnOK = (TextView) dialog.findViewById(R.id.tv_ok);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageDrawable(getResources().getDrawable(R.drawable.popup_logo));
        txtMsg.setText(msg);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(msg.equalsIgnoreCase(getString(R.string.offer_accept_sccussfully))){
                    if (commingFrom.equalsIgnoreCase("FromNUDP")) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("kFromCat", "Delivery");
                        hashMap.put("kFrom", "Active");

                        startActivity(HomeMainActivity.getIntent(ViewAllOffersActivity.this, hashMap));
                        finish();
                        //startActivity(NUDeliveryPersonDashboardActivity.getIntent(ViewAllOffersActivity.this, "VIEWOFFER"));
                    } else if (commingFrom.equalsIgnoreCase("FromNUPW")) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("kFromCat", "Professional");
                        hashMap.put("kFrom", "Active");

                        startActivity(HomeMainActivity.getIntent(ViewAllOffersActivity.this, hashMap));
                        finish();
                        //startActivity(NUProfessionalWorkerDashboardActivity.getIntent(ViewAllOffersActivity.this, "VIEWOFFER"));
                    }

                    finish();
                    dialog.dismiss();
                }else{
                    if(viewOfferList!=null && !viewOfferList.isEmpty()){
                        dialog.dismiss();
                    }else{
                        finish();
                        dialog.dismiss();
                    }
                }

            }
        });

        dialog.show();

    }


    private void showDialogA(String id, String type) {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);
        registerNow.setText(R.string.ok);
        notNow.setText(R.string.cancel);

        if (type.equalsIgnoreCase("Accept"))
            textView.setText(R.string.are_you_sure_you_want_accept_this_offer);
        else
            textView.setText(R.string.are_you_sure_you_want_reject_offer);

        registerNow.setVisibility(View.VISIBLE);
        notNow.setVisibility(View.VISIBLE);
        btn_ok.setVisibility(View.GONE);


        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("Accept")) {
                    callAcceptOfferApi(id);
                } else {
                    callRejectOfferApi(id);
                }
                dialog.dismiss();
            }
        });
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void callRejectOfferApi(String offerId) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("offerId", offerId);
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<ViewAllOfferResponse> beanCall = apiInterface.rejectOffer(token, body);
                beanCall.enqueue(new Callback<ViewAllOfferResponse>() {
                    @Override
                    public void onResponse(Call<ViewAllOfferResponse> call, Response<ViewAllOfferResponse> response) {
                        MyDialog.getInstance(ViewAllOffersActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ViewAllOfferResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                //finish();

                                getOfferListApi();
                                //Toast.makeText(ViewAllOffersActivity.this, R.string.offer_reject_successfully, Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ViewAllOffersActivity.this);
                            } else {
                                Toast.makeText(ViewAllOffersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ViewAllOfferResponse> call, Throwable t) {

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(viewOfferList == null || viewOfferList.size()==0){
            setResult(RESULT_OK);
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pushNotifyReceiver != null) {
            unregisterReceiver(pushNotifyReceiver);
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
