package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.RateDeliveryManFrag;
import com.pagin.azul.fragment.RateNormalUserFrag;
import com.pagin.azul.fragment.RateProfessionalFrag;
import com.pagin.azul.utils.CommonUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyRate extends AppCompatActivity {
    private RateNormalUserFrag rateNormalUserFrag;
    private RateProfessionalFrag professionalFrag;
    private RateDeliveryManFrag deliveryManFrag;

    @BindView(R.id.tv_normal_user)
    TextView normalUser;

    @BindView(R.id.tv_professional)
    TextView tvProfessional;

    @BindView(R.id.tv_delivery_man)
    TextView tvDeliveryman;

    @BindView(R.id.ivBackRate)
    ImageView ivBackRate;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MyRate.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rate);
        ButterKnife.bind(this);


        rateNormalUserFrag = new RateNormalUserFrag();
        professionalFrag = new RateProfessionalFrag();
        deliveryManFrag = new RateDeliveryManFrag();


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            removeAllFragments(getSupportFragmentManager());
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, rateNormalUserFrag).commit();
        }

    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @OnClick({R.id.tv_normal_user, R.id.tv_professional, R.id.tv_delivery_man, R.id.ivBackRate})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_normal_user:

                getSupportFragmentManager().beginTransaction().replace(R.id.container, rateNormalUserFrag).commit();
                normalUser.setTextColor(getResources().getColor(R.color.white));
                tvProfessional.setTextColor(getResources().getColor(R.color.black));
                tvDeliveryman.setTextColor(getResources().getColor(R.color.black));

                break;
            case R.id.tv_professional:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, professionalFrag).commit();
                normalUser.setTextColor(getResources().getColor(R.color.black));
                tvProfessional.setTextColor(getResources().getColor(R.color.white));
                tvDeliveryman.setTextColor(getResources().getColor(R.color.black));

                break;
            case R.id.tv_delivery_man:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, deliveryManFrag).commit();
                normalUser.setTextColor(getResources().getColor(R.color.black));
                tvProfessional.setTextColor(getResources().getColor(R.color.black));
                tvDeliveryman.setTextColor(getResources().getColor(R.color.white));
                break;

            case R.id.ivBackRate:
                onBackPressed();
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, rateNormalUserFrag).commit();
                break;
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
