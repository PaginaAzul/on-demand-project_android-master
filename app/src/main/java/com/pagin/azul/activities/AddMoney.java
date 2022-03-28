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

public class AddMoney extends AppCompatActivity {

    @BindView(R.id.ibvBackAddMoney)
    ImageView ibvBackAddMoney;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AddMoney.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_pay_credit, R.id.bank_transfer,R.id.ibvBackAddMoney})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pay_credit:
                startActivity(PayYourCredit.getIntent(this));
                break;
            case R.id.bank_transfer:
                startActivity(BankToBankTransfer.getIntent(this));
                break;
            case R.id.ibvBackAddMoney:
                startActivity(new Intent(this,MyWallet.class));
                break;
        }

    }
}
