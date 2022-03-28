package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.pagin.azul.R;
import com.pagin.azul.bean.OrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.ActiveDeliveryWorkerOrderDashboardFrag;
import com.pagin.azul.fragment.NewOderDashboardDeliverWorkerFrag;
import com.pagin.azul.fragment.PastDeliveryWorkerOrderDashboardFrag;
import com.pagin.azul.fragment.PendingDeliveryWorkerOrderDashboardFrag;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.NetworkChangeReceiver;

import org.json.JSONObject;

import java.io.Serializable;

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

public class DeliveryWorkerOrderDashboardActivity extends AppCompatActivity {
    private NewOderDashboardDeliverWorkerFrag newFrag;
    private PendingDeliveryWorkerOrderDashboardFrag pendingFrag;
    private ActiveDeliveryWorkerOrderDashboardFrag activeFrag;
    private PastDeliveryWorkerOrderDashboardFrag pastFrag;
    private GPSTracker gpsTracker;

    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_pending)
    TextView tvPandding;
    @BindView(R.id.tv_past)
    TextView tvPast;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_noti)
    ImageView iv_noti;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_backDelivery)
    ImageView btnBack;

    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.container)
    FrameLayout container;
    private boolean isFirstTime = false;
    private NetworkChangeReceiver mNetworkReceiver;
    private String commingfrom = "";

    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, DeliveryWorkerOrderDashboardActivity.class);
        intent.putExtra("kFrom", (Serializable) type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_worker_order_dashboard);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            commingfrom = (String) getIntent().getStringExtra("kFrom");
        }

        btnBack.setVisibility(View.VISIBLE);

        iv_back.setVisibility(View.GONE);
        iv_noti.setVisibility(View.GONE);
        tvTitle.setText("My Order's Dashboard");
        tvTitle.setVisibility(View.VISIBLE);

        gpsTracker = new GPSTracker(this, this);
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));


        newFrag = new NewOderDashboardDeliverWorkerFrag();
        pendingFrag = new PendingDeliveryWorkerOrderDashboardFrag();
        activeFrag = new ActiveDeliveryWorkerOrderDashboardFrag();
        pastFrag = new PastDeliveryWorkerOrderDashboardFrag();

        mNetworkReceiver = new NetworkChangeReceiver();
        mNetworkReceiver.registerCallback(new NetworkChangeReceiver.InternetResponse() {
            @Override
            public void onInternetCheck(boolean isConnect) {

                if (isConnect)
                    disableofflineAnimation();
                else
                    runofflineAnimation();

            }
        });

        if (commingfrom.equalsIgnoreCase("Cancellation")) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, pendingFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.white));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));

        }else if(commingfrom.equalsIgnoreCase("DeliveryOffer")) {

            tvNew.setEnabled(false);
            tvPast.setEnabled(false);
            tvPandding.setEnabled(false);
            getSupportFragmentManager().beginTransaction().add(R.id.container, activeFrag).commit();

            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.white));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));
        }else if(commingfrom.equalsIgnoreCase("true")){
            tvNew.setEnabled(false);
            tvPast.setEnabled(false);
            tvPandding.setEnabled(false);

            getSupportFragmentManager().beginTransaction().add(R.id.container, activeFrag).commit();

            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.white));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));
        }else if(commingfrom.equalsIgnoreCase(RatingAndRiviewActivity.class.getSimpleName())){
            tvNew.setEnabled(true);
            tvPast.setEnabled(true);
            tvPandding.setEnabled(true);
            tvActive.setEnabled(false);

            getSupportFragmentManager().beginTransaction().replace(R.id.container, new PastDeliveryWorkerOrderDashboardFrag()).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvActive.setEnabled(false);
            tvPast.setEnabled(true);
            tvPandding.setEnabled(false);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment != null) {
                removeAllFragments(getSupportFragmentManager());
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.container, newFrag).commit();
                prepareData();
            }
        }

        callcheckCurrentOrderApi();

    }

    private void prepareData() {
        tvNew.setTextColor(getResources().getColor(R.color.white));
        tvActive.setTextColor(getResources().getColor(R.color.blacklight));
        tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
        tvPast.setTextColor(getResources().getColor(R.color.blacklight));
    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkBroadCast();
    }

    @OnClick({R.id.tv_new, R.id.tv_pending, R.id.tv_active, R.id.tv_past, R.id.iv_backDelivery})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, newFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_pending:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new PendingDeliveryWorkerOrderDashboardFrag()).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_active:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ActiveDeliveryWorkerOrderDashboardFrag()).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.white));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_past:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new PastDeliveryWorkerOrderDashboardFrag()).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.iv_backDelivery:
                onBackPressed();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new PendingDeliveryWorkerOrderDashboardFrag()).commit();
                break;
        }
    }

    /////for ShowSelectedPastTab..///
    public void selectTab() {
        tvNew.setTextColor(getResources().getColor(R.color.blacklight));
        tvActive.setTextColor(getResources().getColor(R.color.blacklight));
        tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
        tvPast.setTextColor(getResources().getColor(R.color.white));
    }

    //////run offline animatin based on network connctivity/////////////
    public void runofflineAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        lottieAnimationView.setAnimation("no_internet_connection.json");
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
    }

    //////disable offline animatin based on network connctivity/////////////
    public void disableofflineAnimation() {
        if (lottieAnimationView != null) {
            lottieAnimationView.pauseAnimation();
        }
        container.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.GONE);
        reloadFragment();

    }

    private void reloadFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            removeAllFragments(getSupportFragmentManager());
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, newFrag).commit();
        }
        //prepareData();
    }

    private void registerNetworkBroadCast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }


    private void callcheckCurrentOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<OrderResponse> beanCall = apiInterface.checkCurrentOrder(token, body);

                beanCall.enqueue(new Callback<OrderResponse>() {
                    @Override
                    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                        MyDialog.getInstance(DeliveryWorkerOrderDashboardActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            OrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                               String deliveryActiveOrder = response.body().getData().getDeliveryActiveOrder();
                                //professionalActiveOrder = response.body().getData().getProfessionalActiveOrder();

                                // Toast.makeText(HomeMapActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(DeliveryWorkerOrderDashboardActivity.this);
                            } else {
                                //Toast.makeText(HomeMapActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        startActivity(HomeMainActivity.getIntent(DeliveryWorkerOrderDashboardActivity.this, ""));
        finish();
    }
}
