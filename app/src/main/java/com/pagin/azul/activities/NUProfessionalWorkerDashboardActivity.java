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
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.NUActiveProfessionalWorkerFrag;
import com.pagin.azul.fragment.NUNewProfessionalWorkerFrag;
import com.pagin.azul.fragment.NUPastProfessionalWorkerfrag;
import com.pagin.azul.fragment.NUPendingProfessionalWorkerFrag;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.utils.NetworkChangeReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;

public class NUProfessionalWorkerDashboardActivity extends AppCompatActivity {
    private GPSTracker gpsTracker;
    private NUNewProfessionalWorkerFrag newFrag;
    private NUPendingProfessionalWorkerFrag pendingFrag;
    private NUActiveProfessionalWorkerFrag activeFrag;
    private NUPastProfessionalWorkerfrag pastFrag;

    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.container)
    FrameLayout container;
    private boolean isFirstTime = false;
    private NetworkChangeReceiver mNetworkReceiver;

    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_pending)
    TextView tvPandding;
    @BindView(R.id.tv_past)
    TextView tvPast;
    @BindView(R.id.iv_back)
    ImageView btnBack;
    private String commingFrom = "";

    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, NUProfessionalWorkerDashboardActivity.class);
        intent.putExtra("ktype", type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuprofessional_worker);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            commingFrom = (String) getIntent().getStringExtra("ktype");
        }

        newFrag = new NUNewProfessionalWorkerFrag();
        pendingFrag = new NUPendingProfessionalWorkerFrag();
        activeFrag = new NUActiveProfessionalWorkerFrag();
        pastFrag = new NUPastProfessionalWorkerfrag();

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

        if (commingFrom.equalsIgnoreCase("VIEWOFFER")) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, activeFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.white));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment != null) {
                removeAllFragments(getSupportFragmentManager());
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.container, pendingFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
            }
        }

        gpsTracker = new GPSTracker(this, this);
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));

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
            getSupportFragmentManager().beginTransaction().add(R.id.container, pendingFrag).commit();
            tvNew.setTextColor(getResources().getColor(R.color.blacklight));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.white));
            tvPast.setTextColor(getResources().getColor(R.color.blacklight));
        }
    }

    @OnClick({R.id.tv_new, R.id.tv_pending, R.id.tv_active, R.id.tv_past, R.id.iv_back})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_new:

                startActivity(HomeMapActivity.getIntent(this, "deliverydashboard"));
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, newFrag).commit();
//                tvNew.setTextColor(getResources().getColor(R.color.white));
//                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
//                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
//                tvPast.setTextColor(getResources().getColor(R.color.blacklight));

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

            case R.id.iv_back:
                onBackPressed();
                break;


            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, pendingFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;

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
}
