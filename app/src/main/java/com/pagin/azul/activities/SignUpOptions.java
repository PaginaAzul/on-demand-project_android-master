package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.utils.CommonUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class SignUpOptions extends AppCompatActivity {
    @BindView(R.id.startSignInBtn)
    Button startSignInBtn;
    @BindView(R.id.startSignUpBtn)
    Button startSignUpBtn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    public static Intent getIntent(Context context){
        return new Intent(context,SignUpOptions.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvTitle.setVisibility(View.GONE);
        iv_back.setVisibility(View.GONE);
    }
    @OnClick({R.id.startSignInBtn,R.id.startSignUpBtn,R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.startSignInBtn:
                dispatchSignInActivity();
                break;
            case R.id.startSignUpBtn:
                dispatchSignUp();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
    private void dispatchSignUp() {
        startActivity(SignUp.getIntent(this));
        //finish();

    }
    private void dispatchSignInActivity() {

        startActivity(SignIn.getIntent(this));
        //finish();

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
