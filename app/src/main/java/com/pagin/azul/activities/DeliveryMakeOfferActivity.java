package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.RequestOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

public class DeliveryMakeOfferActivity extends AppCompatActivity {
    @BindView(R.id.order_no)
    TextView order_no;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.user_pic)
    ImageView user_pic;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_rat_num)
    TextView tv_rat_num;
    @BindView(R.id.tv_view_comments)
    TextView tv_view_comments;
    @BindView(R.id.starttopickup)
    TextView starttopickup;
    @BindView(R.id.tv_dropOff)
    TextView tv_dropOff;
    @BindView(R.id.tv_pick_loc)
    TextView tv_pick_loc;
    @BindView(R.id.tv_dropoff_loc)
    TextView tv_dropoff_loc;
    @BindView(R.id.tv_order_tym)
    TextView tv_order_tym;
    @BindView(R.id.tv_order_details)
    TextView tv_order_details;
    @BindView(R.id.tv_pickup)
    TextView tv_pickup;
    @BindView(R.id.mylocToDropOff)
    TextView mylocToDropOff;

    @BindView(R.id.edtMinimumOffer)
    EditText edtMinimumOffer;
    @BindView(R.id.edtmsg)
    EditText edtmsg;
    @BindView(R.id.edtapproxtym)
    EditText edtapproxtym;
    @BindView(R.id.clDeliLoc)
    ConstraintLayout clDeliLoc;
    @BindView(R.id.rlDelLoc)
    RelativeLayout rlDelLoc;
    @BindView(R.id.rlProfLoc)
    RelativeLayout rlProfLoc;
    @BindView(R.id.rlProfLocTxt)
    RelativeLayout rlProfLocTxt;
    @BindView(R.id.rlDropLocation)
    RelativeLayout rlDropLocation;
    private Dialog waitingDialog;
    private Dialog rejectDialog;
    private Button btn_cancel;
    private ConstraintLayout constantPopup;
    private String[] reason = {"Select Reason", "Not suitable for me", "Other reason of cancellation"};
    private String commingFrom;
    private NormalUserPendingOrderInner offerList;
    //  BROADCAST RECEIVER CLASS Object : TO tap
    private BroadcastReceiver pushNotifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                Bundle bundle = intent.getExtras();

                String p_id = bundle.getString("offerAcceptOfDelivery");


                if (p_id != null) {
                    if (p_id.equalsIgnoreCase("offerAcceptOfDelivery")) {

                        startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(DeliveryMakeOfferActivity.this, "DeliveryOffer"));

                        finish();

                    } else if (p_id.equalsIgnoreCase("offerAcceptOfProfessional")) {

                        startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(DeliveryMakeOfferActivity.this, "offerAcceptOfProfessional"));

                        finish();

                    } else if (p_id.equalsIgnoreCase("offerReject")) {

                        dialogRejectShow(getString(R.string.no_delivery_captain), waitingDialog);
                        waitingDialog.dismiss();

                    } else if (p_id.equalsIgnoreCase("orderUnavailableForProfessional")) {

                        dialogOrderTakenOtherCaptain(getString(R.string.taken_other_pw),"Professional");
                        waitingDialog.dismiss();

                    } else if (p_id.equalsIgnoreCase("orderUnavailable")) {

                        dialogOrderTakenOtherCaptain(getString(R.string.taken_other_dp),"Delivery");
                        waitingDialog.dismiss();

                    } else if (p_id.equalsIgnoreCase("orderCancelByUserDeliveryOrder")) {
                        waitingDialog.dismiss();
                        startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(DeliveryMakeOfferActivity.this, ""));

                        finish();

                    } else if (p_id.equalsIgnoreCase("orderCancelByUserProfessionalOrder")) {
                        waitingDialog.dismiss();
                        startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(DeliveryMakeOfferActivity.this, ""));

                        finish();

                    } else {

                        dialogRejectShow("Sorry, This order has been rejected by the normal user.", waitingDialog);
                        waitingDialog.dismiss();
                    }
                }
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("kFromCat", "DeliveryOffer");
//                hashMap.put("kFrom", "ActiveOffer");
//
//                startActivity(HomeMainActivity.getIntent(DeliveryMakeOfferActivity.this, hashMap));
            }
            Log.w(DeliveryMakeOfferActivity.class.getSimpleName(), "offerAcceptOfDelivery");
        }
    };

    public static Intent getIntent(Context context, NormalUserPendingOrderInner offerList, String from) {
        Intent intent = new Intent(context, DeliveryMakeOfferActivity.class);
        intent.putExtra("kData", (Serializable) offerList);
        intent.putExtra("kFrom", (Serializable) from);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_make_offer);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            commingFrom = (String) getIntent().getStringExtra("kFrom");
            offerList = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
        }

        if (offerList != null) {
            setupData();
        }

        if (commingFrom.equalsIgnoreCase("Professional")) {
            clDeliLoc.setVisibility(View.GONE);
            rlDelLoc.setVisibility(View.GONE);
            rlProfLoc.setVisibility(View.VISIBLE);
            rlProfLocTxt.setVisibility(View.VISIBLE);
            rlDropLocation.setVisibility(View.GONE);
            tv_pickup.setText("Service Location : ");

        } else {
            clDeliLoc.setVisibility(View.VISIBLE);
            rlDelLoc.setVisibility(View.VISIBLE);
            rlProfLoc.setVisibility(View.GONE);
            rlProfLocTxt.setVisibility(View.GONE);

        }

    }

    @OnClick({R.id.rl_submitOffer, R.id.btn_back, R.id.tv_view_comments})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_submitOffer:
                if (!edtMinimumOffer.getText().toString().equalsIgnoreCase("")) {
                    int minioffer = Integer.parseInt(edtMinimumOffer.getText().toString());
                    if (minioffer > 15) {
                        if (offerList.getServiceType().equalsIgnoreCase("ProfessionalWorker"))
                            callMakeOfferApiProf();
                        else
                            callMakeOfferApiDelivery();
                        dialogWaitingShow("first");
                        useHandlerforTime();

                    } else {
                        Toast.makeText(this, "Your minimum offer value is 15", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Enter minimum offer", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btn_back:
                finish();
                break;

            case R.id.tv_view_comments:
                startActivity(UserDetailsActivity.getIntent(this, offerList, DeliveryMakeOfferActivity.class.getSimpleName()));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //      REGISTER LocalBroadcastManager TO HANDLE PUSH
        registerReceiver(pushNotifyReceiver,
                new IntentFilter("Tap Successful"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("Check", "onDestroy");
        if (pushNotifyReceiver != null) {
            //      UNREGISTER LocalBroadcastManager
            unregisterReceiver(pushNotifyReceiver);
        }


    }

    private void dialogWaitingShow(String from) {

        waitingDialog = new Dialog(DeliveryMakeOfferActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        waitingDialog.setContentView(R.layout.dialog_waiting_for_buyer_layout);
        Window window = waitingDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        waitingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        waitingDialog.show();


        ImageView userPic = waitingDialog.findViewById(R.id.circleImageView2);
        ImageView iv_loading = waitingDialog.findViewById(R.id.iv_loading);
        ImageView btn_back = waitingDialog.findViewById(R.id.btn_back);
        TextView userName = waitingDialog.findViewById(R.id.user_name);
        TextView tvratNum = waitingDialog.findViewById(R.id.tvratNum);
        TextView tv_msg = waitingDialog.findViewById(R.id.tv_msg);
        btn_cancel = waitingDialog.findViewById(R.id.btn_cancel);
        constantPopup = waitingDialog.findViewById(R.id.constant_popup);

        RoteteLoader(iv_loading);

        userName.setText(offerList.getName());
        tvratNum.setText(offerList.getAvgRating());

        // Glide.with(this).load(offerList.getProfilePic()).into(userPic);
        Glide.with(this)
                .load(offerList.getProfilePic())
                .apply(RequestOptions.placeholderOf(R.drawable.loader)
                        .error(R.drawable.profile_default))
                .into(userPic);

        tv_msg.setText("Your offer of " + edtMinimumOffer.getText().toString() + " SAR is submitted to the buyer. Please wait until he accepts it.");

        if (from.equalsIgnoreCase("first")) {
            constantPopup.setVisibility(View.VISIBLE);
            btn_cancel.setEnabled(false);

        } else {
            constantPopup.setVisibility(View.GONE);
            btn_cancel.setEnabled(true);
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
                //waitingDialog.dismiss();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingDialog.dismiss();
            }
        });


//        waitingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                finish();
//            }
//        });


    }

    private void dialogOrderTakenOtherCaptain(String msg,String from) {

       Dialog takenOtherCaptainDialog = new Dialog(DeliveryMakeOfferActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        takenOtherCaptainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        takenOtherCaptainDialog.setContentView(R.layout.dialog_order_taken_other_captain);
        Window window = takenOtherCaptainDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        takenOtherCaptainDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        takenOtherCaptainDialog.show();

        Button btn_view_new_orders = takenOtherCaptainDialog.findViewById(R.id.btn_view_new_orders);

        TextView user_name = takenOtherCaptainDialog.findViewById(R.id.user_name);
        ImageView btn_back = takenOtherCaptainDialog.findViewById(R.id.btn_back);
        TextView tvratNum = takenOtherCaptainDialog.findViewById(R.id.tvratNum);
        user_name.setText(offerList.getName());
        TextView tv_msg = takenOtherCaptainDialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        tvratNum.setText(offerList.getAvgRating());
        ConstraintLayout constantPopup = takenOtherCaptainDialog.findViewById(R.id.constant_popup);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_view_new_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from.equalsIgnoreCase("Professional")){
                    startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(DeliveryMakeOfferActivity.this, ""));
                    finish();
                }else {
                    startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(DeliveryMakeOfferActivity.this, ""));
                    finish();

                }

                takenOtherCaptainDialog.dismiss();
            }
        });



    }




    private void dialogRejectShow(String msg, Dialog waitingDialog) {

        rejectDialog = new Dialog(DeliveryMakeOfferActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        rejectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rejectDialog.setContentView(R.layout.dialog_order_cancelled_delivery);
        Window window = rejectDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        rejectDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rejectDialog.show();

        Button btn_cancel = rejectDialog.findViewById(R.id.btn_cancel);
        Button changeOffer = rejectDialog.findViewById(R.id.btn_try_again);
        TextView user_name = rejectDialog.findViewById(R.id.user_name);
        TextView tvratNum = rejectDialog.findViewById(R.id.tvratNum);
        user_name.setText(offerList.getName());
        TextView tv_msg = rejectDialog.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        tvratNum.setText(offerList.getAvgRating());
        ConstraintLayout constantPopup = rejectDialog.findViewById(R.id.constant_popup);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
                rejectDialog.dismiss();
            }
        });

        changeOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (offerList != null) {
                    setupData();
                }
                waitingDialog.dismiss();
                rejectDialog.dismiss();
            }
        });


    }

    private void setupData() {
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        } else if (rejectDialog != null) {
            rejectDialog.dismiss();
        }
        order_no.setText(offerList.getOrderNumber());
        tv_profile_name.setText(offerList.getName());
        starttopickup.setText(offerList.getCurrentToPicupLocation() + "km");
        tv_dropOff.setText(offerList.getPickupToDropLocation() + "km");
        mylocToDropOff.setText(offerList.getCurrentToDrLocation() + "km");
        tv_order_details.setText("- " + offerList.getOrderDetails());
        tv_pick_loc.setText(offerList.getPickupLocation());
        tv_dropoff_loc.setText(offerList.getDropOffLocation());
        tv_rat_num.setText(offerList.getAvgRating());
        tv_order_tym.setText(" Require " + offerList.getSeletTime());
        tv_order_tym.setText(" Require " + offerList.getSeletTime());
        tv_view_comments.setText("(" + offerList.getTotalRating() + " Ratings View all)");

        if (!offerList.getProfilePic().equalsIgnoreCase("")) {

            Glide.with(this)
                    .load(offerList.getProfilePic())
                    .apply(new RequestOptions().placeholder(R.drawable.default_p).override(200, 200))
                    .into(user_pic);
        }

        String getDate = offerList.getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = sdf.parse(server_format);
            System.out.println(date);
            String your_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");
            System.out.println(splitted[1]);    //The second part of the splitted string, i.e time
            // Now you can set the TextView here
            tv_date.setText(String.valueOf(splitted[0]));
            tv_time.setText(String.valueOf(splitted[1] + " " + splitted[2]));

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }

    }

    private void callMakeOfferApiDelivery() {
        try {
            MyDialog.getInstance(DeliveryMakeOfferActivity.this).showDialog(DeliveryMakeOfferActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMakeOfferActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMakeOfferActivity.this).getString(kUserId));
                jsonObject.put("orderId", offerList.get_id());
                jsonObject.put("minimumOffer", edtMinimumOffer.getText().toString());
                jsonObject.put("message", edtmsg.getText().toString());
                jsonObject.put("apprxTime", edtapproxtym.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RequestOrderResponse> beanCall = apiInterface.makeOffer(token, body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMakeOfferActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                useHandlerforTime();

                                Toast.makeText(DeliveryMakeOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                if (waitingDialog != null) {
                                    waitingDialog.dismiss();
                                }
                                Toast.makeText(DeliveryMakeOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOrderResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void orderCancelFromDeliveryApi() {
        try {
            MyDialog.getInstance(DeliveryMakeOfferActivity.this).showDialog(DeliveryMakeOfferActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMakeOfferActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMakeOfferActivity.this).getString(kUserId));
                jsonObject.put("orderId", offerList.get_id());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RequestOrderResponse> beanCall = apiInterface.orderCancelFromDelivery(token, body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMakeOfferActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                finish();

                                Toast.makeText(DeliveryMakeOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(DeliveryMakeOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOrderResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void showCancelDialog() {
        Dialog dialog = new Dialog(DeliveryMakeOfferActivity.this);
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


                if (cancel_txt.getText().toString().equalsIgnoreCase("Select Reason") || cancel_txt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(DeliveryMakeOfferActivity.this, "Please select the reason", Toast.LENGTH_SHORT).show();
                } else {
                    orderCancelFromDeliveryApi();
                    dialog.dismiss();

                }


                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    @Override
    public void onBackPressed() {

        if (commingFrom.equalsIgnoreCase("Professional")) {
            startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(this, ""));
            finish();
        } else {
            startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(this, ""));
            finish();
        }

    }

    private void useHandlerforTime() {


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                //dialogWaitingShow("Close");
                constantPopup.setVisibility(View.GONE);
                btn_cancel.setEnabled(true);


            }
        }, 5000);


    }

    //MakeOfferApiHIt....
    private void callMakeOfferApiProf() {
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", offerList.get_id());
                jsonObject.put("minimumOffer", edtMinimumOffer.getText().toString());
                jsonObject.put("message", edtmsg.getText().toString());
                jsonObject.put("apprxTime", edtapproxtym.getText().toString());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<RequestOrderResponse> beanCall = apiInterface.makeAOfferByProfessionalWorker(token, body);
                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMakeOfferActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                //NewOrderForProfessionalWorkerApi();

                                Toast.makeText(DeliveryMakeOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                waitingDialog.dismiss();
                                Toast.makeText(DeliveryMakeOfferActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOrderResponse> call, Throwable t) {
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //for Rotate waiting Loader

    private void RoteteLoader(ImageView iv_loading) {

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        RotateAnimation animRotate = new RotateAnimation(0.0f, 360.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(1500);
        animRotate.setFillAfter(true);
        animRotate.setRepeatCount(Animation.INFINITE);
        animSet.addAnimation(animRotate);

        iv_loading.startAnimation(animSet);

    }


}
