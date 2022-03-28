package com.pagin.azul.onphasesecond.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareReviwsActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_reviws);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.share_reviews));

    }
}