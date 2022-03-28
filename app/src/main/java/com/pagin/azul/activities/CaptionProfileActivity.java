package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.MyProfileInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.fragment.DeliveryManFragment;
import com.pagin.azul.fragment.ProfessionalFragment;
import com.pagin.azul.utils.CommonUtility;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptionProfileActivity extends AppCompatActivity {
    private DeliveryManFragment deliveryManFrag;
    private ProfessionalFragment professionalFrag;

    @BindView(R.id.tv_deliverman)
    TextView tvDeliveryman;
    @BindView(R.id.tv_professional)
    TextView tvProfessional;
    @BindView(R.id.iv_back)
    ImageView btnBack;

    private MyProfileInner profileInner;

    public static Intent getIntent(Context context, MyProfileInner myProfileInner) {
        Intent intent = new Intent(context, CaptionProfileActivity.class);
        intent.putExtra("kData", (Serializable) myProfileInner);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption_profile);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            profileInner = (MyProfileInner) getIntent().getSerializableExtra("kData");
        }

        deliveryManFrag = new DeliveryManFragment();
        professionalFrag = new ProfessionalFragment();

        tvDeliveryman.setTextColor(getResources().getColor(R.color.white));
        tvProfessional.setTextColor(getResources().getColor(R.color.black));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            removeAllFragments(getSupportFragmentManager());
        } else {
            if (profileInner.getSignupWithProfessionalWorker().equalsIgnoreCase("true") && profileInner.getSignupWithDeliveryPerson().equalsIgnoreCase("true")) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, deliveryManFrag).commit();
                tvDeliveryman.setTextColor(getResources().getColor(R.color.white));
                tvProfessional.setTextColor(getResources().getColor(R.color.black));
            } else if (profileInner.getSignupWithProfessionalWorker().equalsIgnoreCase("true")) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, professionalFrag).commit();
                tvDeliveryman.setTextColor(getResources().getColor(R.color.black));
                tvProfessional.setTextColor(getResources().getColor(R.color.white));
            } else if (profileInner.getSignupWithDeliveryPerson().equalsIgnoreCase("true")) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, deliveryManFrag).commit();
                tvDeliveryman.setTextColor(getResources().getColor(R.color.white));
                tvProfessional.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @OnClick({R.id.tv_deliverman, R.id.tv_professional, R.id.iv_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_deliverman:
                if (profileInner.getSignupWithDeliveryPerson().equalsIgnoreCase("true")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, deliveryManFrag).commit();
                    tvDeliveryman.setTextColor(getResources().getColor(R.color.white));
                    tvProfessional.setTextColor(getResources().getColor(R.color.black));
                } else {
                    showDialog();
                }

                break;
            case R.id.tv_professional:
                if (profileInner.getSignupWithProfessionalWorker().equalsIgnoreCase("true")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, professionalFrag).commit();
                    tvDeliveryman.setTextColor(getResources().getColor(R.color.black));
                    tvProfessional.setTextColor(getResources().getColor(R.color.white));
                } else {
                    showDialogprf();
                }

                break;
            case R.id.iv_back:
                onBackPressed();
                break;

        }

    }

    private void showDialog() {
        Dialog dialog = new Dialog(CaptionProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText("You are not registered as delivery worker. Do you want to register as delivery worker?");
        registerNow.setText("OK");
        notNow.setText("Cancel");


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CaptionProfileActivity.this, BecomeDeliveryPersonActivity.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void showDialogprf() {
        Dialog dialog = new Dialog(CaptionProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText("You are not registered as professional worker. Do you want to register as professional worker?");
        registerNow.setText("OK");
        notNow.setText("Cancel");


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CaptionProfileActivity.this, BecomeProfessionalWorkerActivity.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
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
