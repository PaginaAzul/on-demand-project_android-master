package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.fragment.FragmentActive;
import com.pagin.azul.fragment.FragmentNew;
import com.pagin.azul.fragment.FragmentPast;
import com.pagin.azul.fragment.FragmentPending;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeOfferDetailsActivity extends AppCompatActivity {
    private FragmentNew fragmentNew;
    private FragmentPending fragmentPending;
    private FragmentActive fragmentActive;
    private FragmentPast fragmentPast;

    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_pending)
    TextView tvPandding;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_noti)
    ImageView ivNoti;

    @BindView(R.id.iv_backDelivery)
    ImageView btnBack;

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeOfferDetailsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        ButterKnife.bind(this);
        tvNew.setTextColor(getResources().getColor(R.color.white));
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setText("My Order's Dashboard");
        ivNoti.setVisibility(View.GONE);
        // ivBack.setImageDrawable(getResources().getDrawable(R.drawable.back));
        ivBack.setVisibility(View.GONE);

        //ivBack.setPadding(15, 0, 0, 0);

        fragmentNew = new FragmentNew();
        fragmentPending = new FragmentPending();
        fragmentActive = new FragmentActive();
        fragmentPast = new FragmentPast();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) {
            removeAllFragments(getSupportFragmentManager());
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentNew).commit();
            tvNew.setTextColor(getResources().getColor(R.color.white));
            tvActive.setTextColor(getResources().getColor(R.color.blacklight));
            tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
            tvPost.setTextColor(getResources().getColor(R.color.blacklight));
        }

    }

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }


    @OnClick({R.id.tv_new, R.id.tv_pending, R.id.tv_active, R.id.tv_post, R.id.tv_filter, R.id.iv_backDelivery})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_new:

                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentNew).commit();
                tvNew.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPost.setTextColor(getResources().getColor(R.color.blacklight));

                break;
            case R.id.tv_pending:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentPending).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.white));
                tvPost.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_active:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentActive).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.white));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPost.setTextColor(getResources().getColor(R.color.blacklight));
                break;
            case R.id.tv_post:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentPast).commit();
                tvNew.setTextColor(getResources().getColor(R.color.blacklight));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPost.setTextColor(getResources().getColor(R.color.white));
                break;

            case R.id.tv_filter:
                openBottomSheetBanner();
                break;
            case R.id.iv_backDelivery:
                onBackPressed();
                break;


            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentNew).commit();
                break;

        }
    }


    private void openBottomSheetBanner() {
        View view = getLayoutInflater().inflate(R.layout.filter_buttonsheet_layout, null);
        final Dialog mBottomSheetDialog = new Dialog(this, R.style.ThemeDialogCustom);
        mBottomSheetDialog.getWindow().setDimAmount(0.85f);

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        mBottomSheetDialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

    }


    private void showDialogOffer() {

        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_buttonsheet_layout);

      /*  dialog.findViewById(R.id.btn_prfsnal_offer_near_you).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeOfferDetailsActivity.getIntent(HomeMapActivity.this));
                dialog.dismiss();
            }
        });*/
//        no_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog1.dismiss();
//            }
//        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
