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
import com.pagin.azul.bean.ReasonInner;
import com.pagin.azul.bean.ReasonResponse;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

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

public class ReportOrderActivity extends AppCompatActivity {

    @BindView(R.id.spinner_report)
    Spinner spinner_report;

    @BindView(R.id.cancel_txt)
    TextView cancel_txt;
    @BindView(R.id.edt_reason)
    EditText edt_reason;

    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.rl_cancel)
    RelativeLayout rl_cancel;
    ArrayList<ReasonInner> reasonList;
    private NormalUserPendingOrderInner profileInner;
    private String commingFrom;


    private String[] reason = {"Select Reason", "Inappropriate User", "Seems like a fake user", "Other"};


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


    public static Intent getIntent(Context context, NormalUserPendingOrderInner pendingOrderInner, String commingFrom) {
        Intent intent = new Intent(context, ReportOrderActivity.class);
        intent.putExtra("kData", (Serializable) pendingOrderInner);
        intent.putExtra("kCome", (Serializable) commingFrom);
        return intent;
    }

    public static Intent getIntent(Context context, String commingFrom) {
        Intent intent = new Intent(context, ReportOrderActivity.class);
        intent.putExtra("kCome", (Serializable) commingFrom);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_order);
        ButterKnife.bind(this);
        reasonList = new ArrayList<>();

        if (getIntent() != null) {
            profileInner = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            commingFrom = (String) getIntent().getStringExtra("kCome");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.contact_admin));
        spinner_report.setAdapter(adapter);
        spinner_report.setOnItemSelectedListener(onItemSelectedListener);

        getReasonTypeApi();

    }


    @OnClick({R.id.iv_back, R.id.btn_submit, R.id.rl_cancel})
    void onCliclk(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (cancel_txt.getText().toString().equalsIgnoreCase("Select Reason")) {
                    Toast.makeText(this, "Please Select Reason", Toast.LENGTH_SHORT).show();

                } else {
                    if (commingFrom.equalsIgnoreCase("Pending")) {
                        callReportOrderApi();
                    } else if (commingFrom.equalsIgnoreCase("ActiveDelivery")) {
                        callReportOrderApi();
                    } else if (commingFrom.equalsIgnoreCase("NUActive")) {

                        callNUReportOrderApi();
                    } else if (commingFrom.equalsIgnoreCase("PendingPrf")) {

                        callReportOrderApi();
                    } else if (commingFrom.equalsIgnoreCase("CustomerServiceAdmin")) {

                        callReportcontactUsApi();
                    } else if (commingFrom.equalsIgnoreCase("CaptainAssistantAdmin")) {

                        callReportcontactUsApi();
                    } else if (commingFrom.equalsIgnoreCase("BankAccountAdmin")) {

                        callReportcontactUsApi();
                    }
                }
                break;
            case R.id.rl_cancel:
                spinner_report.performClick();
                break;
        }
    }

    //...Coming From ContactAdminScreen then HIT this Api...///
    private void callReportcontactUsApi() {
        try {
            MyDialog.getInstance(this).showDialog(ReportOrderActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("reason", cancel_txt.getText().toString());
                jsonObject.put("description", edt_reason.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<UserTypeResponse> beanCall = apiInterface.contactUs(token, body);
                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(ReportOrderActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                finish();

                                Toast.makeText(ReportOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReportOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void callReportOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(ReportOrderActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", profileInner.get_id());
                jsonObject.put("orderIssueReason", cancel_txt.getText().toString());
                jsonObject.put("orderIssueMessage", edt_reason.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.orderReport(token, body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(ReportOrderActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                finish();

                                Toast.makeText(ReportOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReportOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void callNUReportOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(ReportOrderActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", profileInner.get_id());
                jsonObject.put("orderIssueReason", cancel_txt.getText().toString());
                jsonObject.put("orderIssueMessage", edt_reason.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.orderReportByNormalUser(token, body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(ReportOrderActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                finish();

                                Toast.makeText(ReportOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReportOrderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getReasonTypeApi() {
        try {
            MyDialog.getInstance(this).showDialog(ReportOrderActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<ReasonResponse> beanCall = apiInterface.reasonType();
                beanCall.enqueue(new Callback<ReasonResponse>() {
                    @Override
                    public void onResponse(Call<ReasonResponse> call, Response<ReasonResponse> response) {
                        MyDialog.getInstance(ReportOrderActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ReasonResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                reasonList = response.body().getDataList();
                                for (int i = 0; i < reasonList.size(); i++) {
                                    String reason = response.body().getDataList().get(i).getReportReason();
                                }


                                //Toast.makeText(ReportOrderActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReportOrderActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ReasonResponse> call, Throwable t) {

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
