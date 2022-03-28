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
import com.pagin.azul.fragment.ActiveProfessionalWorkerDashboardFrag;
import com.pagin.azul.fragment.NewProfessionalWorkerDashboardFrag;
import com.pagin.azul.fragment.PastProfessionalWorkerDashboardFrag;
import com.pagin.azul.fragment.PendingProfessionalWorkerDashboardFrag;
import com.pagin.azul.utils.NetworkChangeReceiver;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfessionalWorkerOrderDashboardActivity extends AppCompatActivity {
    private NewProfessionalWorkerDashboardFrag newFrag;
    private PendingProfessionalWorkerDashboardFrag pendingFrag;
    private ActiveProfessionalWorkerDashboardFrag activeFrag;
    private PastProfessionalWorkerDashboardFrag pastFrag;

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
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_noti)
    ImageView ivNotification;
    @BindView(R.id.iv_backDelivery)
    ImageView btnBack;

    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.container)
    FrameLayout container;
    private boolean isFirstTime = false;
    private NetworkChangeReceiver mNetworkReceiver;
    private String fromComming = "";

    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, ProfessionalWorkerOrderDashboardActivity.class);
        intent.putExtra("kFrom", (Serializable) type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_worker_order_dashboard);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            fromComming = (String) getIntent().getStringExtra("kFrom");
        }

        btnBack.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.GONE);
        ivNotification.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.my_order_dashboard));

        newFrag = new NewProfessionalWorkerDashboardFrag();
        pendingFrag = new PendingProfessionalWorkerDashboardFrag();
        activeFrag = new ActiveProfessionalWorkerDashboardFrag();
        pastFrag = new PastProfessionalWorkerDashboardFrag();

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
        if(fromComming.equalsIgnoreCase("Cancellation")){
            getSupportFragmentManager().beginTransaction().add(R.id.container, pendingFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.white));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));
        }else if(fromComming.equalsIgnoreCase("offerAcceptOfProfessional")){

            tvNew.setEnabled(false);
            tvPast.setEnabled(false);
            tvPandding.setEnabled(false);
            getSupportFragmentManager().beginTransaction().add(R.id.container, activeFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.white));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));

        }else if(fromComming.equalsIgnoreCase(RatingAndRiviewActivity.class.getSimpleName())) {
            tvNew.setEnabled(false);
            tvPast.setEnabled(true);
            tvPandding.setEnabled(false);
            tvActive.setEnabled(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, pastFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.white));

        }else if(fromComming.equalsIgnoreCase("true")){
            tvNew.setEnabled(false);
            tvPast.setEnabled(false);
            tvPandding.setEnabled(false);
            getSupportFragmentManager().beginTransaction().add(R.id.container, activeFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.white));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));

        }else {
            tvActive.setEnabled(false);
            tvPast.setEnabled(true);
            tvPandding.setEnabled(false);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment != null) {
                removeAllFragments(getSupportFragmentManager());
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.container, newFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
            }
        }
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
                getSupportFragmentManager().beginTransaction().replace(R.id.container, pendingFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_active:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, activeFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.white));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_past:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, pastFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.iv_backDelivery:
                onBackPressed();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, pendingFrag).commit();
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
            getSupportFragmentManager().beginTransaction().add(R.id.container, newFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.white));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));
        }
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(HomeMainActivity.getIntent(ProfessionalWorkerOrderDashboardActivity.this, ""));
        finish();
    }
}
