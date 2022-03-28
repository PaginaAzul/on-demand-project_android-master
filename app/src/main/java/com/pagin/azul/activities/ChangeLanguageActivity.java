package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.Changelanguage;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MainActivity;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.LocaleHelper;


import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class ChangeLanguageActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView btnBack;

    @BindView(R.id.rl_eng)
    RelativeLayout rlEng;

    @BindView(R.id.radioGroup2)
    RadioGroup radioGroup;

    @BindView(R.id.radio_english)
    RadioButton radio_english;

    @BindView(R.id.radio_portuguese)
    RadioButton radio_portuguese;

    RadioButton radioButton;

    boolean isArabic=false;
    boolean isEng = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        ButterKnife.bind(this);
        getLanguage();
    }

    //All click...
    @OnClick({R.id.iv_back, R.id.btn_updateLang})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.btn_updateLang:
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select language", Toast.LENGTH_SHORT).show();
                } else {
                    // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    radioButton = view.findViewById(selectedId);
                    switch (selectedId) {
                        case R.id.radio_english:
                            updateLanguage("English");
                            break;

                        case R.id.radio_portuguese:
                            updateLanguage("Portuguese");
                            break;
                    }
                }
                break;
        }
    }

    private void getLanguage() {
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish))
                radio_english.setChecked(true);
            else
                radio_portuguese.setChecked(true);
        }else {
            radio_english.setChecked(true);
        }
    }

    private void updateLanguage(String lang) {
        try {
            MyDialog.getInstance(this).showDialog(ChangeLanguageActivity.this);
            String token =  SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("appLanguage",lang);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<Changelanguage> beanCall = apiInterface.changelang(token,body);

                beanCall.enqueue(new Callback<Changelanguage>() {
                    @Override
                    public void onResponse(Call<Changelanguage> call, Response<Changelanguage> response) {
                        MyDialog.getInstance(ChangeLanguageActivity.this).hideDialog();
                        if (response.isSuccessful())
                        {
                            if (response.body().getStatus().equalsIgnoreCase("SUCCESS"))
                            {
                                //onBackPressed();
                                changeLanguage(lang);
                                //Toast.makeText(ChangeLanguageActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ChangeLanguageActivity.this);
                            }
                            else {
                                Toast.makeText(ChangeLanguageActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<Changelanguage> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeLanguage(String languageType) {

        if(languageType.equalsIgnoreCase("English")){
            LocaleHelper.setLocale(this,"en");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE,Constants.kEnglish);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
            //recreate();
        }else {
            LocaleHelper.setLocale(this, "pt");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
            //recreate();
        }

        //startActivity(HomeMainActivity.getIntent(this, ""));
        startActivity(new Intent(this, MainActivity.class));
        finish();

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
