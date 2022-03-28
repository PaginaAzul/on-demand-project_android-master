package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.fragment.ActiveProvideServiceProffFrag;
import com.pagin.azul.fragment.NewProvideServiceProffFrag;
import com.pagin.azul.fragment.PastProvideServiceProffFrag;
import com.pagin.azul.fragment.PendingProvideServiceProffFrag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProvideServiceProffOfferActivity extends AppCompatActivity {
    private NewProvideServiceProffFrag newFrag;
    private PendingProvideServiceProffFrag pendingFrag;
    private ActiveProvideServiceProffFrag activeFrag;
    private PastProvideServiceProffFrag pastFrag;


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

    public static Intent getIintent(Context context) {
        Intent intent = new Intent(context, ProvideServiceProffOfferActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_service_proff_offer);
        ButterKnife.bind(this);

        newFrag = new NewProvideServiceProffFrag();
        pendingFrag = new PendingProvideServiceProffFrag();
        activeFrag = new ActiveProvideServiceProffFrag();
        pastFrag = new PastProvideServiceProffFrag();


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

    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }


    @OnClick({R.id.tv_new, R.id.tv_pending, R.id.tv_active, R.id.tv_past, R.id.iv_back})
    void onClick(View view) {

        switch (view.getId()) {
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

            case R.id.iv_back:
                onBackPressed();
                break;

            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, newFrag).commit();
                tvNew.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPandding.setTextColor(getResources().getColor(R.color.blacklight));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                break;

        }
    }

}
