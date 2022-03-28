package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

import org.json.JSONObject;

import java.util.ArrayList;

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
import static com.pagin.azul.Constant.Constants.kOrderId;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class OfferScreenActivity extends AppCompatActivity {
    private ArrayList<NormalUserPendingOrderInner> viewOfferList;
    @BindView(R.id.btn_cancel)
    Button btncancel;
    @BindView(R.id.btn_try_again)
    Button btnTryAgain;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.tv_waiting_txt)
    TextView tv_waiting_txt;
    @BindView(R.id.iv_loading)
    ImageView iv_loading;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    private String[] reason = {"Select Reason", "Not suitable for me", "Other reason of cancellation"};


    public static Intent getIntent(Context context, String type) {
        Intent intent = new Intent(context, OfferScreenActivity.class);
        intent.putExtra("Ktype", type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_screen);
        ButterKnife.bind(this);


        uiSetup();


       useHandlerforApi();

    }



    private void useHandlerforApi() {


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                getOfferListApi();

            }
        }, 40000);



    }

    @OnClick({R.id.btn_try_again, R.id.btn_cancel, R.id.btn_back})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                showCancelDialog();
                break;

            case R.id.btn_try_again:
                uiSetup();
                getOfferListApi();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }


    private void uiSetup() {
        iv_loading.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.loader));
        tv_waiting_txt.setVisibility(View.VISIBLE);
        tv_msg.setText(getResources().getString(R.string.order_offer));
        btnTryAgain.setVisibility(View.GONE);

    }


    private void offernotacceptShowUI() {
        iv_loading.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.close_o));
        tv_waiting_txt.setVisibility(View.GONE);
        tv_msg.setText(getResources().getString(R.string.no_delivery_captain));
        btnTryAgain.setVisibility(View.VISIBLE);


    }


    private void showCancelDialog() {
        Dialog dialog = new Dialog(OfferScreenActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_order_layout, null, false);
        dialog.setContentView(view);

        TextView tv_ok = view.findViewById(R.id.tv_ok);
        TextView cancel_txt = view.findViewById(R.id.cancel_txt);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        EditText edt_reason = view.findViewById(R.id.edt_reason);
        Spinner spinner_cancel = view.findViewById(R.id.spinner_cancel);
        RelativeLayout rl_cancel = view.findViewById(R.id.rl_cancel);


        ///////////////////
        Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } else {
                    cancel_txt.setText(reason[position]);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reason);
        spinner_cancel.setAdapter(adapter);
        spinner_cancel.setOnItemSelectedListener(onItemSelectedListener);


        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_cancel.performClick();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void showAcceptOrderDialog() {
        Dialog dialog = new Dialog(OfferScreenActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_offer_accept_layout, null, false);
        dialog.setContentView(view);

        Button btn_accept = view.findViewById(R.id.btn_accept);
        Button btn_reject = view.findViewById(R.id.btn_reject);
        TextView tv_profile_name = view.findViewById(R.id.tv_profile_name);
        TextView pickuptodrop = view.findViewById(R.id.pickuptodrop);
        TextView workertopickup = view.findViewById(R.id.workertopickup);
        TextView dilevery_tym = view.findViewById(R.id.dilevery_tym);
        TextView msg = view.findViewById(R.id.msg);
        ImageView user_pic = view.findViewById(R.id.user_pic);


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    private void getOfferListApi() {
        try {
            MyDialog.getInstance(OfferScreenActivity.this).showDialog(OfferScreenActivity.this);
            String token = SharedPreferenceWriter.getInstance(OfferScreenActivity.this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("lat", SharedPreferenceWriter.getInstance(this).getString(kLat));
                jsonObject.put("long", SharedPreferenceWriter.getInstance(this).getString(kLong));
                jsonObject.put("orderId", SharedPreferenceWriter.getInstance(this).getString(kOrderId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getOfferList(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(OfferScreenActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    viewOfferList = response.body().getDataList();

                                    showAcceptOrderDialog();

                                } else {
                                    offernotacceptShowUI();
                                }

                                Toast.makeText(OfferScreenActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OfferScreenActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NormalUserPendingOrderResponse> call, Throwable t) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    //...delay hit Api..Using Handler........//
    private void onUseHandler(){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //add your code here
                    }
                }, 1000);

            }
        });

    }
}

