package com.pagin.azul.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.pagin.azul.R;

import butterknife.ButterKnife;

public class ManageAddresses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_addresses);
        ButterKnife.bind(this);
    }
}
