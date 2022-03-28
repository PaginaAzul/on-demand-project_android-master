package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kProfile;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class MyOrderDashboardActivity extends AppCompatActivity {
    private GPSTracker gpsTracker;
    private String checktype;

    private String checkNormalUser, checkDeliveryPerson, checkProfessionalWorker;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tvNormalUser)
    TextView tvNormalUser;

    @BindView(R.id.tvDeliveryWorker)
    TextView tvDeliveryWorker;

    @BindView(R.id.tvProfessioalWoker)
    TextView tvProfessioalWoker;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MyOrderDashboardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_dashboard);
        ButterKnife.bind(this);


        gpsTracker = new GPSTracker(this, this);
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLat, String.valueOf(gpsTracker.getLatitude()));
        SharedPreferenceWriter.getInstance(this).writeStringValue(kLong, String.valueOf(gpsTracker.getLongitude()));







        callCheckUserTypeApi();
    }

    @OnClick({R.id.tvNormalUser, R.id.tvDeliveryWorker, R.id.tvProfessioalWoker, R.id.iv_back})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvNormalUser:
                if(checkNormalUser != null && !checkNormalUser.equalsIgnoreCase("")){
                if (checkNormalUser.equalsIgnoreCase("true")) {
                    startActivity(HomeMapActivity.getIntent(this, "my_order_deshbord"));
                    //   startActivity(new Intent(HomeMapActivity.class,"my_order_deshbord"));
                } else {
                    showDialog();
                }

                }else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvDeliveryWorker:
                if(checkDeliveryPerson != null && !checkDeliveryPerson.equalsIgnoreCase("")){
                if (checkDeliveryPerson.equalsIgnoreCase("true")) {
                    startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(this,"Dashboard"));
                } else {
                    showDialog();
                }
                }else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvProfessioalWoker:
                if (checkProfessionalWorker.equalsIgnoreCase("true")) {
                    startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(this,"Dashboard"));
                } else {
                    showDialog();
                }
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }

    }


    private void callCheckUserTypeApi() {
        try {
            MyDialog.getInstance(this).showDialog(MyOrderDashboardActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.checkUserType(token,body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(MyOrderDashboardActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                //checktype = response.body().getTypeData();

                                checkNormalUser = response.body().getSignupWithNormalPerson();
                                checkDeliveryPerson = response.body().getAdminVerifyDeliveryPerson();
                                checkProfessionalWorker = response.body().getAdminVerifyProfessionalWorker();

                                SharedPreferenceWriter.getInstance(MyOrderDashboardActivity.this).writeStringValue(kProfile,response.body().getProfilePic());


                                //Toast.makeText(MyOrderDashboardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyOrderDashboardActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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


    private void showDialog() {
        Dialog dialog = new Dialog(MyOrderDashboardActivity.this);
        View view = getLayoutInflater().inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);


        if (checkDeliveryPerson.equalsIgnoreCase("false")) {
            textView.setText("Your delivery person request is under processing. So you can not access Delivery Person dashboard");

        } else if (checkProfessionalWorker.equalsIgnoreCase("false")) {
            textView.setText("Your proffessional worker request is under processing. So you can not access Proffesional Worker dashboard");
        }

        registerNow.setVisibility(View.GONE);
        notNow.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);


        btn_ok.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
