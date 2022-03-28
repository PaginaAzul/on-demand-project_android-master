package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.utils.CommonUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactAdmin extends AppCompatActivity {

    @BindView(R.id.customerServiceAdminBtn)
    TextView customerServiceAdminBtn;

    @BindView(R.id.captainAssistantAdminBtn)
    TextView captainAssistantAdminBtn;
    @BindView(R.id.BankAccountAdminBtn)
    TextView BankAccountAdminBtn;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ContactAdmin.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_admin);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.customerServiceAdminBtn, R.id.iv_back, R.id.captainAssistantAdminBtn,R.id.BankAccountAdminBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customerServiceAdminBtn:
                startActivity(ReportOrderActivity.getIntent(this, "CustomerServiceAdmin"));
                break;
            case R.id.captainAssistantAdminBtn:
                startActivity(ReportOrderActivity.getIntent(this, "CaptainAssistantAdmin"));
                finish();
                break;
            case R.id.BankAccountAdminBtn:
                startActivity(ReportOrderActivity.getIntent(this, "BankAccountAdmin"));
                finish();
                break;

            case R.id.iv_back:
                //startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(ContactAdmin.this, "ContactAdmin"));
                finish();
                break;
        }
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
