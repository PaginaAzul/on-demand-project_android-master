package com.pagin.azul.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

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

public class CancellationActivity extends AppCompatActivity {
    @BindView(R.id.spinner_cancel)
    Spinner spinner_cancel;

    @BindView(R.id.cancel_txt)
    TextView cancel_txt;
    @BindView(R.id.edt_reason)
    EditText edt_reason;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.rl_cancel)
    RelativeLayout rl_cancel;
    private String cmFrom = "";
    private NormalUserPendingOrderInner getDataInner;
    private String[] reason = {"Select Reason", "Not suitable for me", "Other reason of cancellation"};


    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                //cancel_txt.setText(reason[position]);
                cancel_txt.setText(parent.getItemAtPosition(position).toString());
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    public static Intent getIntent(Context context, NormalUserPendingOrderInner getDataInner) {
        Intent intent = new Intent(context, CancellationActivity.class);
        intent.putExtra("kData", (Serializable) getDataInner);
        return intent;
    }

    public static Intent getIntent(Context context, NormalUserPendingOrderInner getDataInner, String cmFrom) {
        Intent intent = new Intent(context, CancellationActivity.class);
        intent.putExtra("kData", (Serializable) getDataInner);
        intent.putExtra("kFrom", (String) cmFrom);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            getDataInner = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            cmFrom = (String) getIntent().getStringExtra("kFrom");

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.reasons));
        spinner_cancel.setAdapter(adapter);
        spinner_cancel.setOnItemSelectedListener(onItemSelectedListener);


    }

    @OnClick({R.id.iv_back, R.id.btn_submit, R.id.rl_cancel})
    void onCliclk(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (cancel_txt.getText().toString().equalsIgnoreCase(getString(R.string.select_reason))) {
                    Toast.makeText(this, R.string.please_select_reasons, Toast.LENGTH_SHORT).show();

                } else {
                    callOrderCancelApi();
                }
                break;
            case R.id.rl_cancel:
                spinner_cancel.performClick();
                break;
        }
    }


    private void callOrderCancelApi() {
        try {
            MyDialog.getInstance(this).showDialog(CancellationActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", getDataInner.get_id());
                jsonObject.put("orderCanelReason", cancel_txt.getText().toString());
                jsonObject.put("orderCancelMessage", edt_reason.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.orderCancel(token, body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(CancellationActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (cmFrom != null) {
                                    if (cmFrom.equalsIgnoreCase("FromNUDP")) {

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("kFromCat", "Delivery");
                                        hashMap.put("kFrom", "Pending");
                                        startActivity(HomeMainActivity.getIntent(CancellationActivity.this, hashMap));
                                        finish();

                                    } else {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("kFromCat", "Professional");
                                        hashMap.put("kFrom", "Pending");
                                        startActivity(HomeMainActivity.getIntent(CancellationActivity.this, hashMap));
                                        finish();

                                    }
                                } else {
                                    String checktype = response.body().getUserTypeResponse().getServiceType();
                                    if (checktype.equalsIgnoreCase("DeliveryPersion")) {
                                        startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(CancellationActivity.this, "Cancellation"));
                                    } else if (checktype.equalsIgnoreCase("ProfessinalWorker")) {
                                        startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(CancellationActivity.this, "Cancellation"));
                                    }
                                }
                                finish();

                                //Toast.makeText(CancellationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(CancellationActivity.this);
                            } else {
                                Toast.makeText(CancellationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserTypeResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
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
