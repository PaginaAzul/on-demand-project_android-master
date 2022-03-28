package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pagin.azul.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletManageCard extends AppCompatActivity {

    @BindView(R.id.ivBackManageCard)
    ImageView ivBackManageCard;


    public static Intent getIintent(Context context) {
        Intent intent = new Intent(context, WalletManageCard.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager_card);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_add_card,R.id.ivBackManageCard})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_card:
                startActivity(AddCard.getIntent(this));
                break;
            case R.id.ivBackManageCard:
                onBackPressed();
                break;
        }
    }
}
