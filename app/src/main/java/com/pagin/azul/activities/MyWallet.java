package com.pagin.azul.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.fragment.WalletDeliveryWorkerFrag;
import com.pagin.azul.fragment.WalletNormalUserFrag;
import com.pagin.azul.fragment.WalletProWorkerFrag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWallet extends AppCompatActivity {
    private WalletNormalUserFrag normalUserFrag;
    private WalletDeliveryWorkerFrag deliveryWorkerFrag;
    private WalletProWorkerFrag proWorkerFrag;

    @BindView(R.id.wallet_normalUser)
    TextView walletNormalUser;
    @BindView(R.id.wallet_delivery_worker)
    TextView walletDeliveryUser;
    @BindView(R.id.wallet_pro_worker)
    TextView walletProWorker;
    @BindView(R.id.iv_back)
    ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);

        normalUserFrag = new WalletNormalUserFrag();
        deliveryWorkerFrag = new WalletDeliveryWorkerFrag();
        proWorkerFrag = new WalletProWorkerFrag();


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            removeAllFragments(getSupportFragmentManager());
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, normalUserFrag).commit();
        }

    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @OnClick({R.id.wallet_normalUser, R.id.wallet_delivery_worker, R.id.wallet_pro_worker, R.id.iv_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_normalUser:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, normalUserFrag).commit();
                walletNormalUser.setTextColor(getResources().getColor(R.color.white));
                walletDeliveryUser.setTextColor(getResources().getColor(R.color.black));
                walletProWorker.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.wallet_delivery_worker:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, deliveryWorkerFrag).commit();
                walletDeliveryUser.setTextColor(getResources().getColor(R.color.white));
                walletNormalUser.setTextColor(getResources().getColor(R.color.black));
                walletProWorker.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.wallet_pro_worker:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, proWorkerFrag).commit();
                walletProWorker.setTextColor(getResources().getColor(R.color.white));
                walletNormalUser.setTextColor(getResources().getColor(R.color.black));
                walletDeliveryUser.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, normalUserFrag).commit();
                break;
        }
    }

}
