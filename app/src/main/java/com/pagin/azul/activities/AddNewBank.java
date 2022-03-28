package com.pagin.azul.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pagin.azul.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewBank extends AppCompatActivity {

    @BindView(R.id.saveAddNewBankBtn)
    Button saveAddNewBankBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bank);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.saveAddNewBankBtn})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.saveAddNewBankBtn:
                startActivity(new Intent(this,WalletManageCard.class));
                break;
        }
    }
}
