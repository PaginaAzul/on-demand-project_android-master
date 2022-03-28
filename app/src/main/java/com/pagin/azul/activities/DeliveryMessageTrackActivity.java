package com.pagin.azul.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.R;
import com.pagin.azul.bean.ChatResponse;
import com.pagin.azul.bean.MessageChat;
import com.pagin.azul.bean.MessageTrackInner;
import com.pagin.azul.bean.MessageTrackResponse;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.bean.RequestOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.match.socketFileUploader.FileUploadManager;
import com.pagin.azul.match.socketFileUploader.UploadFileMoreDataReqListener;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.TakeImage;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kProfile;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class DeliveryMessageTrackActivity extends AppCompatActivity {
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    private final String SEND_MEDIA = "MEDIA";
    public static final int IMAGE_FILE = 5;
    private HashMap<String, String> media_path = new HashMap<>();
    private String msgType = "";
    private Socket socket;
    private Socket mSocket;
    private ArrayList<MessageChat> MessageList;


    //    @BindView(R.id.no_data)
//    TextView no_data;
    @BindView(R.id.btn_send)
    ImageView btn_send;
    @BindView(R.id.btn_attachment)
    ImageView btn_attachment;
    @BindView(R.id.rv_Chat_list)
    RecyclerView rv_Chat_list;
    boolean isFirstTime;
    @BindView(R.id.edt_chat_box)
    EditText edt_typetxt;
    //    private NormalUserPendingOrderInner getData;
    private MessageAdapter messageAdapter;
    private ScheduledExecutorService scheduleTaskExecutor;


    @BindView(R.id.iv_backchat)
    ImageView iv_backchat;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.ivBuyerProfile)
    ImageView ivBuyerProfile;

    @BindView(R.id.tvBusyerName)
    TextView tvBusyerName;
    @BindView(R.id.tvDeliveryCost)
    TextView tvDeliveryCost;
    @BindView(R.id.tvCall)
    TextView tvCall;
    @BindView(R.id.tvTrack)
    TextView tvTrack;
    @BindView(R.id.workertopickup)
    TextView workertopickup;
    @BindView(R.id.pickuptodrop)
    TextView pickuptodrop;
    @BindView(R.id.starttopickup)
    TextView starttopickup;
    @BindView(R.id.tvInvoice)
    TextView tvInvoice;
    @BindView(R.id.tvArrived)
    TextView tvArrived;
    @BindView(R.id.tvworkdone)
    TextView tvworkdone;
    @BindView(R.id.tvCreateInvoice)
    TextView tvCreateInvoice;
    @BindView(R.id.tv_pickupsatatus)
    TextView tv_pickupsatatus;
    @BindView(R.id.tv_deliveredstatus)
    TextView tv_deliveredstatus;
    @BindView(R.id.tv_rat_num)
    TextView tv_rat_num;
    @BindView(R.id.tv_dropoffstatus)
    TextView tv_dropoffstatus;
    private String mobile = "9856363698";

    private MessageTrackInner msgTrackData;
    private NormalUserPendingOrderInner getDataInner;
    private  GPSTracker gpsTracker;
    private  double lat;
    private  double lng;



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("offerAccept").equalsIgnoreCase("acceptWithdrawRequestProfessionalWorker")){
                startActivity(ProfessionalWorkerOrderDashboardActivity.getIntent(DeliveryMessageTrackActivity.this, ""));

            }else if(intent.getStringExtra("offerAccept").equalsIgnoreCase("acceptWithdrawRequestDeliveryPersion")){
                startActivity(DeliveryWorkerOrderDashboardActivity.getIntent(DeliveryMessageTrackActivity.this, ""));

             }else if(intent.getStringExtra("offerAccept").equalsIgnoreCase("orderCancel")) {
                startActivity(HomeMainActivity.getIntent(DeliveryMessageTrackActivity.this, ""));
                finish();
            }
        }
    };


    public static Intent getIntent(Context context, NormalUserPendingOrderInner getDataInner) {
        Intent intent = new Intent(context, DeliveryMessageTrackActivity.class);
        intent.putExtra("kData", (Serializable) getDataInner);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_message_track);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            getDataInner = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
        }


        tvBusyerName.setText(getDataInner.getOfferAcceptedByName());
        //Glide.with(this).load(getDataInner.getOfferAcceptedByProfilePic()).into(ivBuyerProfile);
        Glide.with(this)
                .load(getDataInner.getOfferAcceptedByProfilePic())
                .apply(RequestOptions.placeholderOf(R.drawable.loader)
                .error(R.drawable.profile_default))
                .into(ivBuyerProfile);
        
        orderId.setText("Order Id: " + getDataInner.getOrderNumber());
        tv_rat_num.setText(getDataInner.getAvgRating());

        if (getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
            tvDeliveryCost.setText("Service Cost " + getDataInner.getDeliveryOffer() + " SAR");
            tv_pickupsatatus.setText("Drop Off");
            tv_deliveredstatus.setText("Professional\nworking");
            tv_dropoffstatus.setText("Delivered");
            pickuptodrop.setText("0.0 KM");

            starttopickup.setText(getDataInner.getCurrentToDrLocation()+" KM");
            workertopickup.setText("0.0 KM");


        } else {
            tvDeliveryCost.setText("Delivery Cost " + getDataInner.getDeliveryOffer() + " SAR");
            pickuptodrop.setText(getDataInner.getPickupToDropLocation() + "km");
            starttopickup.setText(getDataInner.getCurrentToPicupLocation()+" KM");
            tv_deliveredstatus.setText("DropOff");
            tv_dropoffstatus.setText("Delivered");
        }
        setUpRecyclerView();
        //deliveryActiveOrderApi();

        Uri uri = Uri.parse("https://res.cloudinary.com/boss8055/image/upload/v1558617086/ikrm9elx95eayq2zjflv.jpg");
        callChatHistoryApi();
        connectSockettest();
        trackingConnection();

        generateImageFromPdf(uri);

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        gpsTracker = new GPSTracker(this,this);
        lat = gpsTracker.getLatitude();
        lng = gpsTracker.getLongitude();
    }

    @OnClick({R.id.tvArrived, R.id.tvworkdone, R.id.tvCreateInvoice, R.id.tvInvoice,
            R.id.tvCall, R.id.tvTrack, R.id.iv_backchat, R.id.btn_attachment, R.id.btn_send,})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvArrived:
                if(tvArrived.getText().toString().trim().equalsIgnoreCase("Create\nInvoice")){
                    startActivity(CreateInvoiceActivity.getIntent(this, getDataInner, "ActiveDP"));
                }else {
                    callArrivedApi(getDataInner.get_id());
                }
                break;

            case R.id.btn_attachment:
                if(getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker"))
                    bottomSheetProfOpen();
                else
                    bottomSheetOpen();
                break;
            case R.id.btn_send:
                if (!edt_typetxt.getText().toString().trim().isEmpty()) {
                    sendMessage(getDataInner.getOfferAcceptedById());

                } else {
                    Toast.makeText(this, "Please type something", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvInvoice:

                if (tvInvoice.getText().toString().trim().equalsIgnoreCase("Edit\nInvoice")) {
                    startActivity(CreateInvoiceActivity.getIntent(this, getDataInner, "ActiveDP"));
                } else if (tvInvoice.getText().toString().equalsIgnoreCase("Start")) {
                    callGoStatusApi(getDataInner.get_id());
                }
                break;

            case R.id.tvworkdone:
                showWorkDoneDialog(getDataInner.get_id());
                break;

            case R.id.tvCall:
                mobile = getDataInner.getOfferAcceptedByCountryCode()+getDataInner.getOfferAcceptedByMobileNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
                break;
            case R.id.tvTrack:
               // startActivity(ActiveTrackingActivity.getIntent(this, getDataInner, "ActiveDP",msgTrackData));

                startActivity(OrderDetailsOnMapActivity.getIntent(this, getDataInner, "ActiveDP"));
                break;
            case R.id.iv_backchat:
                try {
                    JSONObject object = new JSONObject();
                    object.put("roomId", getDataInner.getRoomId());
                    object.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                    mSocket.emit("room leave", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
                break;

            case R.id.tvCreateInvoice:
                if(tvCreateInvoice.getText().toString().trim().equalsIgnoreCase("Create Invoice")){
                    startActivity(CreateInvoiceActivity.getIntent(this, getDataInner, "ActiveDP"));
                }else {
                    callArrivedApi(getDataInner.get_id());
                }
                //startActivity(CreateInvoiceActivity.getIntent(this, getDataInner, "ActiveDP"));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        deliveryActiveOrderApi();
        callChatHistoryApi();
        //startScheduleExecutorService();

        registerReceiver(broadcastReceiver, new IntentFilter("offerAccept"));
    }



    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    //Handler for sharelocation on server...
    private void startScheduleExecutorService() {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(6);

        // This schedule a runnable task every 5 second
        scheduleTaskExecutor.scheduleAtFixedRate(() -> runOnUiThread(this::updateLoaction), 0, 6, TimeUnit.SECONDS);
    }

    //Make Connection for tracking
    private void trackingConnection() {

        try {
            JSONObject obj = new JSONObject();

            obj.put("roomId", getDataInner.getOrderOwner()+getDataInner.getRealOrderId());
            mSocket.emit("room join", obj);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Share Updated Location
    private void updateLoaction() {

        try {
            JSONObject obj = new JSONObject();

            obj.put("roomId", getDataInner.getOrderOwner()+getDataInner.getRealOrderId());
            obj.put("lattitude", lat);
            obj.put("longitude", lng);
            mSocket.emit("tracking", obj);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deliveryActiveOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderId", getDataInner.get_id());
            jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            Call<MessageTrackResponse> call = apiInterface.deliveryActiveOrder(body);
            call.enqueue(new Callback<MessageTrackResponse>() {
                @Override
                public void onResponse(Call<MessageTrackResponse> call, Response<MessageTrackResponse> response) {
                    MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        MessageTrackResponse signUpWithMobileResp = response.body();
                        if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {

                            if(response.body().getMsgTrackData().getStatus().equalsIgnoreCase("Cancel")){
                                startActivity(HomeMainActivity.getIntent(DeliveryMessageTrackActivity.this, ""));
                                finish();
                            }

                            msgTrackData = response.body().getMsgTrackData();

                            if(getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker")) {

                                if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                                    startScheduleExecutorService();
                                }

                                if (msgTrackData.getGoStatus().equalsIgnoreCase("false")) {
                                    tvInvoice.setText("Start");
                                    tvInvoice.setTextColor(getResources().getColor(R.color.white));
                                    tvInvoice.setBackground(getResources().getDrawable(R.drawable.bg_blue_btn));
                                    tvCreateInvoice.setVisibility(View.GONE);
                                } else if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                                    tvTrack.setVisibility(View.VISIBLE);


                                    if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                        //tvInvoice.setText(getResources().getString(R.string.edit_invoice));
                                        //tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                        //tvInvoice.setVisibility(View.VISIBLE);
                                        tvworkdone.setVisibility(View.VISIBLE);
                                        tvArrived.setVisibility(View.GONE);
                                        tvCreateInvoice.setVisibility(View.VISIBLE);


                                        if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true")) {
                                            tvInvoice.setText(getResources().getString(R.string.edit_invoice));
                                            tvInvoice.setEnabled(true);
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvInvoice.setVisibility(View.VISIBLE);

                                            tvCreateInvoice.setText(getString(R.string.invoice_created));

                                            tvCreateInvoice.setEnabled(false);
                                            tvCreateInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvCreateInvoice.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.VISIBLE);

                                            tvCreateInvoice.setBackground(getResources().getDrawable(R.drawable.bg_white_invoice));
                                        }
                                    } else if (msgTrackData.getArrivedStatus().equalsIgnoreCase("false")) {
                                        tvInvoice.setVisibility(View.VISIBLE);
                                        tvArrived.setVisibility(View.VISIBLE);
                                        tvCreateInvoice.setVisibility(View.GONE);
                                        tvInvoice.setText("Start");
                                        tvInvoice.setTextColor(getResources().getColor(R.color.green));
                                        tvInvoice.setBackgroundColor(getResources().getColor(R.color.white));
                                        tvInvoice.setEnabled(false);
                                    }


//                                if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")) {
//                                    tvInvoice.setText("Go");
//                                    tvInvoice.setTextColor(getResources().getColor(R.color.green));
//                                    tvInvoice.setBackgroundColor(getResources().getColor(R.color.white));
//                                    tvInvoice.setEnabled(false);
//                                    tvCreateInvoice.setVisibility(View.VISIBLE);
//                                    tvArrived.setVisibility(View.VISIBLE);
//
//
//                                    if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
//                                        //tvInvoice.setText(getResources().getString(R.string.edit_invoice));
//                                        //tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
//                                        tvInvoice.setVisibility(View.GONE);
//                                        tvworkdone.setVisibility(View.VISIBLE);
//                                        tvArrived.setVisibility(View.GONE);
//                                        tvCreateInvoice.setVisibility(View.GONE);
//
//                                    }
//


                                }
                            }else {

                                if (msgTrackData.getGoStatus().equalsIgnoreCase("false")) {
                                    tvInvoice.setText("Start");
                                    tvInvoice.setTextColor(getResources().getColor(R.color.white));
                                    tvInvoice.setBackground(getResources().getDrawable(R.drawable.bg_blue_btn));
                                    tvCreateInvoice.setVisibility(View.GONE);
                                } else if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                                    tvTrack.setVisibility(View.VISIBLE);
                                    if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")) {
                                        tvInvoice.setText("Start");
                                        tvInvoice.setTextColor(getResources().getColor(R.color.green));
                                        tvInvoice.setBackgroundColor(getResources().getColor(R.color.white));
                                        tvInvoice.setEnabled(false);
                                        //tvCreateInvoice.setVisibility(View.VISIBLE);
                                        tvArrived.setVisibility(View.VISIBLE);
                                        tvArrived.setText(getString(R.string.create_invoice));

                                        tvCreateInvoice.setVisibility(View.VISIBLE);
                                        tvCreateInvoice.setText(getString(R.string.arrived));


                                        if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                            //tvInvoice.setText(getResources().getString(R.string.edit_invoice));
                                            //tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            //tvInvoice.setVisibility(View.VISIBLE);
                                            tvworkdone.setVisibility(View.VISIBLE);
                                            //tvArrived.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.GONE);


                                           // tvArrived.setText(getString(R.string.invoice_created));
                                           // tvArrived.setEnabled(false);
                                           // tvArrived.setTextColor(getResources().getColor(R.color.purpalMedium));
                                           // tvArrived.setBackground(getResources().getDrawable(R.drawable.bg_white_invoice));


                                        }


                                    } else if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true")) {
                                        tvInvoice.setText(getResources().getString(R.string.edit_invoice));
                                        tvInvoice.setEnabled(true);
                                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                        tvInvoice.setVisibility(View.VISIBLE);
                                        tvworkdone.setVisibility(View.GONE);
                                        tvArrived.setVisibility(View.VISIBLE);
                                        //tvArrived.setVisibility(View.VISIBLE);
                                        tvCreateInvoice.setVisibility(View.VISIBLE);
                                        tvCreateInvoice.setText(getString(R.string.arrived));
                                        tvArrived.setText(getString(R.string.invoice_created));
                                        tvArrived.setEnabled(false);
                                        tvArrived.setTextColor(getResources().getColor(R.color.purpalMedium));
                                        tvArrived.setBackground(getResources().getDrawable(R.drawable.bg_white_invoice));

                                        if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                            tvInvoice.setText(getResources().getString(R.string.edit_invoice));
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvInvoice.setVisibility(View.VISIBLE);
                                            tvworkdone.setVisibility(View.VISIBLE);
                                            tvArrived.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.GONE);


                                            tvArrived.setText(getString(R.string.invoice_created));
                                            tvArrived.setEnabled(false);
                                            tvArrived.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvArrived.setBackground(getResources().getDrawable(R.drawable.bg_white_invoice));


                                        }

                                    }
                                }
                            }

                         } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(DeliveryMessageTrackActivity.this);

                        }else {
                            Toast.makeText(DeliveryMessageTrackActivity.this, "" + signUpWithMobileResp.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DeliveryMessageTrackActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<MessageTrackResponse> call, Throwable t) {
                    MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callGoStatusApi(String orderID) {
        try {
            MyDialog.getInstance(DeliveryMessageTrackActivity.this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.goStatus(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                deliveryActiveOrderApi();
                                callChatHistoryApi();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(DeliveryMessageTrackActivity.this);
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void callArrivedApi(String orderID) {
        try {
            MyDialog.getInstance(DeliveryMessageTrackActivity.this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.arrivedStatus(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                deliveryActiveOrderApi();
                                callChatHistoryApi();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(DeliveryMessageTrackActivity.this);
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    //Call WorkDone API.. by delivery//
    private void callWorkDoneByDeliveryPersonApi(String orderId) {
        try {
            MyDialog.getInstance(DeliveryMessageTrackActivity.this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("orderId", orderId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.workDoneByDeliveryPerson(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                showGreatDialog();

                                //((DeliveryWorkerOrderDashboardActivity)).selectTab();

                                //getFragmentManager().beginTransaction().replace(R.id.container, new PastDeliveryWorkerOrderDashboardFrag()).commit();

//                                allClick();

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(DeliveryMessageTrackActivity.this);
                            } else {
                                Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    //Call WorkDone API..by professional//
    private void callWorkDoneByProfessionalApi(String orderId) {
        try {
            MyDialog.getInstance(DeliveryMessageTrackActivity.this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);
            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("orderId", orderId);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.workDoneByProfessionalWorker(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                showGreatDialog();
                                //Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(DeliveryMessageTrackActivity.this);
                            } else {
                                Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showGreatDialog() {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_great_layout, null, false);
        dialog.setContentView(view);

        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        tvMsg.setText("You completed the service successfully");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(RatingAndRiviewActivity.getIntent(DeliveryMessageTrackActivity.this, getDataInner,DeliveryMessageTrackActivity.class.getSimpleName()));
                finish();
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void withrawOrderDialog() {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_great_layout, null, false);
        dialog.setContentView(view);

        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView top_msg = view.findViewById(R.id.tom_msg);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        top_msg.setText("Successfull!");
        tvMsg.setText("Your request has been sent to the customer please wait to get reply from customer.");

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    private void showWorkDoneDialog(String orderId) {
        Dialog dialog = new Dialog(DeliveryMessageTrackActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText("Are you sure want to work done status update?");
        registerNow.setText("OK");
        notNow.setText("Cancel");


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker")){
                    callWorkDoneByProfessionalApi(orderId);
                }else {
                    callWorkDoneByDeliveryPersonApi(orderId);
                }
                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void bottomSheetOpen() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_track_screen_layout, null);
        bottomSheetDialog.setContentView(sheetView);


        TextView tv_cancel = sheetView.findViewById(R.id.tv_cancel);
        TextView tv_issue_bill = sheetView.findViewById(R.id.tv_issue_bill);
        TextView tv_share_img = sheetView.findViewById(R.id.tv_share_img);
        TextView tv_share_loc = sheetView.findViewById(R.id.tv_share_loc);
        TextView tv_gd_delivered = sheetView.findViewById(R.id.tv_gd_delivered);
        TextView tv_admin = sheetView.findViewById(R.id.tv_admin);
        TextView tv_Withdraw = sheetView.findViewById(R.id.tv_Withdraw);

        // initView();
        tv_issue_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgTrackData.getGoStatus().equalsIgnoreCase("true"))
                    startActivity(CreateInvoiceActivity.getIntent(getApplicationContext(), getDataInner, "ActiveDP"));
                else
                    Toast.makeText(DeliveryMessageTrackActivity.this, "You can't create Invoice right now.", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });


        tv_share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                bottomSheetDialog.dismiss();
            }
        });

        tv_share_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ShareLocationActivity.getIntent(getApplicationContext()),101);
                bottomSheetDialog.dismiss();
            }
        });

        tv_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetWithdraw();
                bottomSheetDialog.dismiss();
            }
        });

        tv_gd_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkDoneDialog(getDataInner.get_id());
                bottomSheetDialog.dismiss();
            }
        });

        tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ContactAdmin.getIntent(DeliveryMessageTrackActivity.this));
                bottomSheetDialog.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void bottomSheetProfOpen() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_track_screen_prof_layout, null);
        bottomSheetDialog.setContentView(sheetView);


        TextView tv_cancel = sheetView.findViewById(R.id.tv_cancel);
        TextView tv_issue_bill = sheetView.findViewById(R.id.tv_issue_bill);
        TextView tv_share_img = sheetView.findViewById(R.id.tv_share_img);
        TextView tv_share_loc = sheetView.findViewById(R.id.tv_share_loc);
        TextView tvServiceCompleted = sheetView.findViewById(R.id.tvServiceCompleted);
        TextView tvFileComplaint = sheetView.findViewById(R.id.tvFileComplaint);
        TextView tv_Withdraw = sheetView.findViewById(R.id.tv_Withdraw);


        if(msgTrackData.getInvoiceStatus().equalsIgnoreCase("true")){
            tv_issue_bill.setText("Change invoice");
        }

        // initView();
        tv_issue_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgTrackData.getGoStatus().equalsIgnoreCase("true"))
                    startActivity(CreateInvoiceActivity.getIntent(getApplicationContext(), getDataInner, "ActiveDP"));
                else
                    Toast.makeText(DeliveryMessageTrackActivity.this, "You can't create Invoice right now.", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });


        tv_share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                bottomSheetDialog.dismiss();
            }
        });

        tv_share_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ShareLocationActivity.getIntent(getApplicationContext()),101);
                bottomSheetDialog.dismiss();
            }
        });

        tv_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetWithdraw();
                bottomSheetDialog.dismiss();
            }
        });

        tvServiceCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWorkDoneDialog(getDataInner.get_id());
                bottomSheetDialog.dismiss();
                //Toast.makeText(DeliveryMessageTrackActivity.this, "Under development", Toast.LENGTH_SHORT).show();
            }
        });

        tvFileComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ContactAdmin.getIntent(DeliveryMessageTrackActivity.this));
                bottomSheetDialog.dismiss();
                //Toast.makeText(DeliveryMessageTrackActivity.this, "Under development", Toast.LENGTH_SHORT).show();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }


    private void bottomSheetWithdraw() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.dialog_withdraw_offer_layout, null);
        bottomSheetDialog.setContentView(sheetView);


        TextView tv_cancel = sheetView.findViewById(R.id.btn_cancel);
        TextView tv_store_loc = sheetView.findViewById(R.id.tv_store_loc);
        TextView tv_want_del = sheetView.findViewById(R.id.tv_want_del);
        TextView btn_ok = sheetView.findViewById(R.id.btn_ok);

        tv_store_loc.setBackground(getResources().getDrawable(R.drawable.withdraw_purpal_bg));
        tv_want_del.setBackground(getResources().getDrawable(R.drawable.bg_light_grey_white_layout));

        tv_store_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_store_loc.setBackground(getResources().getDrawable(R.drawable.withdraw_purpal_bg));
                tv_want_del.setBackground(getResources().getDrawable(R.drawable.bg_light_grey_white_layout));
            }
        });

        tv_want_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_want_del.setBackground(getResources().getDrawable(R.drawable.withdraw_purpal_bg));
                tv_store_loc.setBackground(getResources().getDrawable(R.drawable.bg_light_grey_white_layout));

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCancelFromDeliveryApi();
                bottomSheetDialog.dismiss();
            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }


    private void orderCancelFromDeliveryApi() {
        try {
            MyDialog.getInstance(DeliveryMessageTrackActivity.this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("orderId", getDataInner.getRealOrderId());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RequestOrderResponse> beanCall = apiInterface.orderWithdrawFromDeliveryAndPro(token, body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                withrawOrderDialog();
                                //finish();
                               // Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(DeliveryMessageTrackActivity.this);
                            } else {
                                Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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






    //////Chat.....work.......///////


    //send message
    private void sendMessage(String receiverId) {

        try {
            JSONObject obj = new JSONObject();

            obj.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            obj.put("receiverId", receiverId);
            obj.put("roomId", getDataInner.getRoomId());
            obj.put("message", edt_typetxt.getText().toString().trim());
            obj.put("messageType", "Text");
            obj.put("profilePic", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kProfile));
            mSocket.emit("message", obj);

            edt_typetxt.setText("");
            msgType = "";
            msgType = "Text";


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //.....share location emitt.....///
    //send message
    private void sendLocation(String receiverId,String lat,String lng) {

        try {
            JSONObject obj = new JSONObject();

            obj.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            obj.put("receiverId", receiverId);
            obj.put("roomId", getDataInner.getRoomId());
            obj.put("message", "https://www.google.com/maps?q="+lat+","+lng+"&z=17&hl=en");
            obj.put("messageType", "Location");
            obj.put("profilePic", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kProfile));
            mSocket.emit("message", obj);

            edt_typetxt.setText("");
            msgType = "";
            msgType = "Location";


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void connectSockettest() {
        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
        try {
            mSocket = IO.socket("http://18.189.223.53:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("room join", onLogin);

        mSocket.on("message", onNewMessage);

        mSocket.connect();

        try {
            JSONObject object = new JSONObject();
            object.put("roomId", getDataInner.getRoomId());
            object.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            mSocket.emit("room join", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // Toast.makeText(NUMessageDeliveryPersonActivity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();

//                    for (int jj = 0; jj < args.length; jj++) {
//                        //DialogFactory.showLog("onLogin", "onLogin-- " + args[jj]);
//
//                    }
                }
            });
        }
    };
    int count=0;

    // TODO: 25/1/18 messagechecking
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count++;
                    Log.e("count",""+count);

                    //MessageList.clear();
                    JSONObject data = (JSONObject) args[0];

                    Log.e("json_data", String.valueOf(data));

                    String rooomId;
                    String msg;
                    String receiverId;
                    String senderId;
                    String msgType;
                    String time;
                    String media;
                    String profilePic;
                    String createdAt;
                    String url;

                    try {
                        rooomId = data.getString("roomId");
                        msg = data.getString("message");
                        receiverId = data.getString("receiverId");
                        senderId = data.getString("senderId");
                        msgType = data.getString("messageType");

                        createdAt = data.getString("createdAt");

                        if (msgType.equalsIgnoreCase("Text")) {
                            profilePic = data.getString("profilePic");
                            MessageList.add(new MessageChat(senderId, receiverId, msg, "Text", "", createdAt, profilePic,""));
                            messageAdapter.updateMessage(MessageList);
                            rv_Chat_list.setVisibility(View.VISIBLE);
                            // no_data.setVisibility(View.GONE);
                        } else if (msgType.equalsIgnoreCase("Media")) {
                            profilePic = data.getString("profilePic");
                            media = data.getString("media");
                            MessageList.add(new MessageChat(senderId, receiverId, "", "Media", media, createdAt, profilePic,""));
                            messageAdapter.updateMessage(MessageList);
                            rv_Chat_list.setVisibility(View.VISIBLE);



                            //no_data.setVisibility(View.GONE);
                        } else if(msgType.equalsIgnoreCase("Location")){
                            url = data.getString("url");
                            //profilePic = data.getString("profilePic");
                            media = data.getString("message");
                            String url1 = data.getString("url");
                            MessageList.add(new MessageChat(senderId, receiverId, media, "Location", "", createdAt, "",url));
                            messageAdapter.updateMessage(MessageList);

                            rv_Chat_list.setVisibility(View.VISIBLE);

                        }

//                        messageAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            });
        }
    };

    private void scrollToBottom() {
        rv_Chat_list.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnect = args -> runOnUiThread(() -> {
        //DialogFactory.showToast(getApplicationContext(), getString(R.string.connected));
         //Toast.makeText(DeliveryMessageTrackActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    });

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //DialogFactory.showToast(getApplicationContext(), getString(R.string.disconnected));
                    //Toast.makeText(DeliveryMessageTrackActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // DialogFactory.showLog("ERROR CONNECT", "ERROR CONNECT");
                    //Toast.makeText(DeliveryMessageTrackActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    private void setUpRecyclerView() {
        MessageList = new ArrayList<>();
        rv_Chat_list.setLayoutManager(new LinearLayoutManager(DeliveryMessageTrackActivity.this));
        messageAdapter = new MessageAdapter(MessageList, DeliveryMessageTrackActivity.this);
        rv_Chat_list.setAdapter(messageAdapter);
    }


    private void callChatHistoryApi() {
        try {
            MyDialog.getInstance(DeliveryMessageTrackActivity.this).showDialog(DeliveryMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("roomId", getDataInner.getRoomId());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ChatResponse> beanCall = apiInterface.getChatHistory(token, body);

                beanCall.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        MyDialog.getInstance(DeliveryMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ChatResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    MessageList = response.body().getDataList();

                                    messageAdapter.updateMessage(MessageList);
                                    //no_data.setVisibility(View.GONE);
                                    rv_Chat_list.setVisibility(View.VISIBLE);

                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
//                                    recyler_view_comments.smoothScrollToPosition(main_data.size()-1);
                                            rv_Chat_list.scrollToPosition(MessageList.size() - 1);
                                            rv_Chat_list.setVisibility(View.VISIBLE);
                                            //no_data.setVisibility(View.GONE);

                                        }
                                    });

                                } else {
                                    //no_data.setVisibility(View.VISIBLE);
//                                    rv_Chat_list.setLayoutManager(new LinearLayoutManager(DeliveryMessageTrackActivity.this));
//                                    messageAdapter = new MessageAdapter(new ArrayList<>(), DeliveryMessageTrackActivity.this);
//                                    rv_Chat_list.setAdapter(messageAdapter);
//                                    messageAdapter.notifyDataSetChanged();
                                    //rv_Chat_list.setVisibility(View.GONE);
                                }
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliveryMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //////IMage Send//////////////

    public void sendAndGetBinaryData(String path, int type) {
        String uni_code = String.valueOf(System.currentTimeMillis());

        if (type == IMAGE_FILE) {
            MessageList.add(new MessageChat(SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId), "", "", imagePath, "", "Just now", "User",""));
        }
        messageAdapter.notifyDataSetChanged();
        //scrollToBottom();
        media_path.put(uni_code, path);
        if (media_path.size() == 1) {
            uploadFileOnServer(media_path);
        }
    }


    private void uploadFileOnServer(HashMap<String, String> map) {
        if (map.size() > 0) {
            for (String entry : map.keySet()) {
                String key = entry;
                String value = map.get(key);
                // new FileUploadTask(value, key).execute();//Value ==== Media Path, key========Unicode
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new FileUploadTask(value, key).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

                break;
            }
        }
    }


    private class FileUploadTask extends AsyncTask<String, Integer, String> {
        private String file_path = "";
        private String room_id = "";
        private String receiver_id = "";
        private String attachment_type = SEND_MEDIA;
        private String uni_code = "";

        private UploadFileMoreDataReqListener callback;
        private FileUploadManager mFileUploadManager;

        public FileUploadTask(String file_path, String uni_code) {
            this.file_path = file_path;
            room_id = getDataInner.getRoomId();
            receiver_id = SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId);
            this.uni_code = uni_code;
            Log.e("path uni_code", "path uni_code-- " + file_path);

            attachment_type = SEND_MEDIA;

        }

        @Override
        protected void onPreExecute() {
            Log.e("mFileUploadManager", "in it mFileUploadManager for-- " + file_path);
            mFileUploadManager = new FileUploadManager();


        }

        @Override
        protected String doInBackground(String... params) {
            boolean isSuccess = mSocket.connected();
            if (isSuccess) {
                mFileUploadManager.prepare(file_path, DeliveryMessageTrackActivity.this);

                // This function gets callback when server requests more data
                setUploadFileMoreDataReqListener(mUploadFileMoreDataReqListener);
                // This function will get a call back when upload completes
                setUploadFileCompleteListener();
                // Tell server we are ready to start uploading ..
                if (mSocket.connected()) {
                    JSONArray jsonArr = new JSONArray();
                    JSONObject res = new JSONObject();
                    try {
                        res.put("Name", mFileUploadManager.getFileName());
                        res.put("Size", mFileUploadManager.getFileSize());
                        res.put("room_id", room_id);
                        res.put("message", room_id);
                        jsonArr.put(res);
                        mSocket.emit("uploadFileStart", jsonArr);
                    } catch (JSONException e) {
                        //TODO: Log errors some where..
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Log.e("onPostExecute result-- ", "onPostExecute result--");
            if (s == null) {
                return;
            }
            if (s.equalsIgnoreCase("OK")) {
                Log.e("uploaded","yes");
                media_path.remove(uni_code);
                mFileUploadManager.close();
                mSocket.off("uploadFileMoreDataReq", uploadFileMoreDataReq);
               mSocket.off("uploadFileCompleteRes", onCompletedddd);
//                uploadFileOnServer(media_path);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // super.onProgressUpdate(values);
            if (values[0] > 107) {
                if (media_path.containsKey(uni_code)) {
                    onPostExecute("OK");
                }
            }
        }


        private UploadFileMoreDataReqListener mUploadFileMoreDataReqListener = new UploadFileMoreDataReqListener() {
            @Override
            public void uploadChunck(int offset, int percent) {
                Log.e("CHAT_ACTIVITY", String.format("Uploading %d completed. offset at: %d", percent, offset));
                // Read the next chunk
                mFileUploadManager.read(offset);
                if (mSocket.connected()) {
                    JSONArray jsonArr = new JSONArray();
                    JSONObject res = new JSONObject();
                    try {
                        res.put("Name", mFileUploadManager.getFileName());
                        res.put("Data", mFileUploadManager.getData());
                        res.put("chunkSize", mFileUploadManager.getBytesRead());
                        res.put("room_id", room_id);
                        res.put("sender_id", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kUserId));
                        res.put("receiver_id", receiver_id);
                        res.put("message", getString(R.string.read_attachement));
                        res.put("messageType", "Media");
                        res.put("attachment_type", attachment_type);
                        res.put("profilePic", SharedPreferenceWriter.getInstance(DeliveryMessageTrackActivity.this).getString(kProfile));
                        jsonArr.put(res);
                        // This will trigger server 'uploadFileChuncks' function
                        mSocket.emit("uploadFileChuncks", jsonArr);

                        msgType = "";
                        msgType = "Media";
                    } catch (JSONException e) {
                        //TODO: Log errors some where..
                    }
                }
            }

            @Override
            public void err(JSONException e) {
                // TODO Auto-generated method stub
            }
        };

        Emitter.Listener uploadFileMoreDataReq = new Emitter.Listener() {
            @SuppressLint("LongLogTag")
            @Override
            public void call(Object... args) {
                for (int jj = 0; jj < args.length; jj++) {
                    Log.e("setUploadFileMoreDataReqListener", "setUploadFileMoreDataReqListener-- " + args[jj]);
                }
                try {
                    JSONObject json_data = (JSONObject) args[0];
                    int place = json_data.getInt("Place");
                    int percent = json_data.getInt("Percent");
                    publishProgress(json_data.getInt("Percent"));
                    callback.uploadChunck(place, percent);

                } catch (JSONException e) {
                    callback.err(e);
                }
            }
        };


        Emitter.Listener onCompletedddd = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json_data = (JSONObject) args[0];
                if (json_data.has("IsSuccess")) {
                    publishProgress(110);
                    return;
                }
            }
        };

        private void setUploadFileMoreDataReqListener(final UploadFileMoreDataReqListener callbackk) {
            callback = callbackk;
            mSocket.on("uploadFileMoreDataReq", uploadFileMoreDataReq);
        }

        private void setUploadFileCompleteListener() {
            mSocket.on("uploadFileCompleteRes", onCompletedddd);
        }
    }

    //PdfiumAndroid (https://github.com/barteksc/PdfiumAndroid)
//https://github.com/barteksc/AndroidPdfViewer/issues/49
    void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            e.printStackTrace();
            //todo with exception
        }
    }


    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";

    private void saveImage(Bitmap bmp) {
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(folder, "PDF.png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            //todo with exception
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }
    }


    public void selectImage() {
        final CharSequence[] items = {
//                getResources().getString(R.string.Take_Photo),
//                getResources().getString(R.string.Choose_from_Library),
                "take photo", "take libarary",
                getResources().getString(R.string.cancel)};

        final Dialog dialog = new Dialog(this, R.style.MyDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_imagecapture);


        TextView txt_takephoto = (TextView) dialog.findViewById(R.id.txt_takephoto);
        TextView txt_choosefromlibrary = (TextView) dialog.findViewById(R.id.txt_choosefromgallery);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_button);
        txt_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DeliveryMessageTrackActivity.this, TakeImage.class);
                intent.putExtra("from", "camera");
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                dialog.dismiss();
            }
        });
        txt_choosefromlibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryMessageTrackActivity.this, TakeImage.class);
                intent.putExtra("from", "gallery");
                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {

                String   address1 = data.getStringExtra("ADDRESS");
                String     lat1 = data.getStringExtra("LAT");
                String  lat2 = data.getStringExtra("LONG");

                sendLocation(getDataInner.getOfferAcceptedById(),lat1,lat2);


            }}else if (requestCode == START_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (resultCode == RESULT_OK) {
            if (data.getStringExtra("filePath") != null) {
                imagePath = data.getStringExtra("filePath");
                sendAndGetBinaryData(imagePath, IMAGE_FILE);
                fileFlyer = new File(data.getStringExtra("filePath"));

                if (fileFlyer.exists() && fileFlyer != null) {
                    // ivMyProfile.setImageURI(Uri.fromFile(fileFlyer));
                }
            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("message", onNewMessage);
        mSocket.off("room join", onLogin);

if(scheduleTaskExecutor != null) {
    scheduleTaskExecutor.shutdownNow();
}
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdownNow();
        }
    }
}
