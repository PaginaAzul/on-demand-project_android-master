package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pagin.azul.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayYourCredit extends AppCompatActivity {
    @BindView(R.id.btn_cancel)
    Button btnSave;

    @BindView(R.id.ivBackAddMoney)
    ImageView ivBackAddMoney;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, PayYourCredit.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_your_credit);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_cancel,R.id.ivBackAddMoney})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                startActivity(WalletManagerCardPay.getIntent(this));
                break;
            case R.id.ivBackAddMoney:
                startActivity(new Intent(this,AddMoney.class));
                break;
        }
    }
}
