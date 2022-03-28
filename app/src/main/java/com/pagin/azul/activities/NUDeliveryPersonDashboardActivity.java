package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.fragment.NUActiveDeliveryPersonFrag;
import com.pagin.azul.fragment.NUNewDeliveryPersonFrag;
import com.pagin.azul.fragment.NUPastDeliveryPersonFrag;
import com.pagin.azul.fragment.NUPendingDeliverPersongFrag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NUDeliveryPersonDashboardActivity extends AppCompatActivity {

    private NUNewDeliveryPersonFrag newFrag;
    private NUPendingDeliverPersongFrag pendingFrag;
    private NUActiveDeliveryPersonFrag activeFrag;
    private NUPastDeliveryPersonFrag pastFrag;


    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_pending)
    TextView tvPandding;
    @BindView(R.id.tv_past)
    TextView tvPast;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private String commingFrom="";

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, NUDeliveryPersonDashboardActivity.class);
        return intent;
    }

    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, NUDeliveryPersonDashboardActivity.class);
        intent.putExtra("Ktype", type);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nudelivery_person_dashboard);
        ButterKnife.bind(this);
        newFrag = new NUNewDeliveryPersonFrag();
        pendingFrag = new NUPendingDeliverPersongFrag();
        activeFrag = new NUActiveDeliveryPersonFrag();
        pastFrag = new NUPastDeliveryPersonFrag();

        if (getIntent() != null) {


            if( (String) getIntent().getStringExtra("Ktype")!=null)
            {
                commingFrom = (String) getIntent().getStringExtra("Ktype");
                if(commingFrom.equalsIgnoreCase("VIEWOFFER")){
                    getSupportFragmentManager().beginTransaction().add(R.id.container, activeFrag).commit();
                    tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                    tvActive.setTextColor(getResources().getColor(R.color.white));
                    tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                    tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                }else {
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

            }

        }





    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
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

}
