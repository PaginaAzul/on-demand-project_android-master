package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pagin.azul.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletManagerCardPay extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, WalletManagerCardPay.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager_card_pay);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.rl_add_card, R.id.btn_payNow})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_add_card:
                startActivity(AddCard.getIntent(this));
                break;

            case R.id.btn_payNow:
                showDialog();
                break;
        }
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wallet_money_added_success);

        Button btnRequire = (Button) dialog.findViewById(R.id.btn_ok);


        btnRequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }
}
