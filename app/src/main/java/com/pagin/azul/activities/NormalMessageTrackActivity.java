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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
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
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.bean.ChatResponse;
import com.pagin.azul.bean.MessageChat;
import com.pagin.azul.bean.MessageTrackInner;
import com.pagin.azul.bean.MessageTrackResponse;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.RequestOrderResponse;
import com.pagin.azul.bean.UserTypeResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.match.socketFileUploader.FileUploadManager;
import com.pagin.azul.match.socketFileUploader.UploadFileMoreDataReqListener;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.pagin.azul.utils.LocaleHelper;
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

public class NormalMessageTrackActivity extends AppCompatActivity {
    public static final int IMAGE_FILE = 5;
    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private final String SEND_MEDIA = "MEDIA";
    @BindView(R.id.tvBusyerName)
    TextView tvBusyerName;
    @BindView(R.id.tvTrack)
    TextView tvTrack;
    @BindView(R.id.tvCall)
    TextView tvCall;
    @BindView(R.id.tvDeliveryCost)
    TextView tvDeliveryCost;
    @BindView(R.id.ivBuyerProfile)
    ImageView ivBuyerProfile;
    @BindView(R.id.tv_rat_num)
    TextView tv_rat_num;
    @BindView(R.id.orderId)
    TextView orderId;
    @BindView(R.id.iv_backchat)
    ImageView iv_backchat;
    @BindView(R.id.title)
    TextView title;
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
    @BindView(R.id.webUrl)
    WebView webUrl;
    @BindView(R.id.btn_send)
    ImageView btn_send;
    @BindView(R.id.btn_attachment)
    ImageView btn_attachment;
    @BindView(R.id.rv_Chat_list)
    RecyclerView rv_Chat_list;
    boolean isFirstTime;
    @BindView(R.id.edt_chat_box)
    EditText edt_typetxt;
    private NormalUserPendingOrderInner getDataInner;
    private String[] reason = {"Select Reason", "No longer need it", "Captain will be late", "Other"};
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    private HashMap<String, String> media_path = new HashMap<>();
    private String msgType = "";
    private Socket socket;
    private Socket mSocket;
    private ArrayList<MessageChat> MessageList;
    private MessageAdapter messageAdapter;
    private MessageTrackInner msgTrackData;
    private String mobile = "9825256869";


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String offerType = intent.getStringExtra("offerAvailable");
            if (offerType != null) {
                if (offerType.equalsIgnoreCase("orderCancelFromDelivery")) {

                    bottomSheetWithdrawConformation("orderCancelFromDelivery");

                } else if (intent.getStringExtra("offerAvailable").equalsIgnoreCase("orderCancelFromProfessional")) {

                    bottomSheetWithdrawConformation("orderCancelFromProfessional");

                } else {
                    normalActiveOrderOrderApi();
                    callChatHistoryApi();
                }
            } else {
                normalActiveOrderOrderApi();
                callChatHistoryApi();
            }
        }
    };


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
    // TODO: 25/1/18 messagechecking
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //MessageList.clear();
                    JSONObject data = (JSONObject) args[0];
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
                            MessageList.add(new MessageChat(senderId, receiverId, msg, "Text", "", createdAt, profilePic, ""));
                            /*rv_Chat_list.setLayoutManager(new LinearLayoutManager(NormalMessageTrackActivity.this));
                            messageAdapter = new MessageAdapter(MessageList, NormalMessageTrackActivity.this);
                            rv_Chat_list.setAdapter(messageAdapter);*/
                            messageAdapter.updateMessage(MessageList);
                            rv_Chat_list.setVisibility(View.VISIBLE);
                            //no_data.setVisibility(View.GONE);
                        } else if (msgType.equalsIgnoreCase("Media")) {
                            profilePic = data.getString("profilePic");
                            media = data.getString("media");
                            MessageList.add(new MessageChat(senderId, receiverId, "", "Media", media, createdAt, profilePic, ""));
                            /*rv_Chat_list.setLayoutManager(new LinearLayoutManager(NormalMessageTrackActivity.this));
                            messageAdapter = new MessageAdapter(MessageList, NormalMessageTrackActivity.this);
                            rv_Chat_list.setAdapter(messageAdapter);*/
                            messageAdapter.updateMessage(MessageList);
                            rv_Chat_list.setVisibility(View.VISIBLE);
                            // no_data.setVisibility(View.GONE);
                        } else if (msgType.equalsIgnoreCase("Location")) {
                            url = data.getString("url");
                            //profilePic = data.getString("profilePic");
                            media = data.getString("message");
                            String url1 = data.getString("url");
                            MessageList.add(new MessageChat(senderId, receiverId, media, "Location", "", createdAt, "", url));
                            /*rv_Chat_list.setLayoutManager(new LinearLayoutManager(NormalMessageTrackActivity.this));
                            messageAdapter = new MessageAdapter(MessageList, NormalMessageTrackActivity.this);
                            rv_Chat_list.setAdapter(messageAdapter);*/
                            messageAdapter.updateMessage(MessageList);
                            rv_Chat_list.setVisibility(View.VISIBLE);

                        }


                        //messageAdapter.notifyDataSetChanged();
                        scrollToBottom();
                        MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            });
        }
    };
    private Emitter.Listener onConnect = args -> runOnUiThread(() -> {
        //DialogFactory.showToast(getApplicationContext(), getString(R.string.connected));
        // Toast.makeText(NUMessageDeliveryPersonActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    });
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //DialogFactory.showToast(getApplicationContext(), getString(R.string.disconnected));
                    //Toast.makeText(NormalMessageTrackActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
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

                    // Toast.makeText(NormalMessageTrackActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();

                    //Toast.makeText(NormalMessageTrackActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();

                }
            });
        }
    };

    public static Intent getIntent(Context context, NormalUserPendingOrderInner getDataInner) {
        Intent intent = new Intent(context, NormalMessageTrackActivity.class);
        intent.putExtra("kData", (Serializable) getDataInner);
        return intent;
    }


    //.....chat work........////..................///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setLanguage();
        setContentView(R.layout.activity_normal_message_track);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            getDataInner = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
        }

        tvBusyerName.setText(getDataInner.getOfferAcceptedOfName());
        orderId.setText(getString(R.string.order_id)+" "+getDataInner.getOrderNumber());

        /*if (!getDataInner.getOfferAcceptedOfProfilePic().equalsIgnoreCase("")) {
            Picasso.with(this).load(getDataInner.getOfferAcceptedOfProfilePic()).into(ivBuyerProfile);
        }*/

        String currency = getDataInner.getCurrency()!=null?getDataInner.getCurrency():"";
        tvDeliveryCost.setText("Delivery Cost " + getDataInner.getDeliveryOffer() + " "+currency);
//        if (!getDataInner.getOfferAcceptedOfProfilePic().equalsIgnoreCase("")) {
//            Picasso.with(this).load(getDataInner.getOfferAcceptedOfProfilePic()).into(ivBuyerProfile);
//        }
        Glide.with(this)
                .load(getDataInner.getOfferAcceptedOfProfilePic())
                .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                        .error(R.drawable.profile_default))
                .into(ivBuyerProfile);


        starttopickup.setText(getDataInner.getCurrentToPicupLocation() + "km");
        tv_rat_num.setText(getDataInner.getAvgRating());

//        workertopickup.setText(getDataInner.getdr() + "km");


        if (getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
            tvDeliveryCost.setText(getString(R.string.service_cost)+" "+getDataInner.getDeliveryOffer() + " "+currency);
            //tv_pickupsatatus.setText("Drop Off");
            tv_pickupsatatus.setText(R.string.arrived);
            //tv_deliveredstatus.setText(R.string.work_in_progress);
            tv_deliveredstatus.setText(R.string.prof_workr);
            pickuptodrop.setText("0.0 KM");

        } else {
            tvDeliveryCost.setText("Delivery Cost " + getDataInner.getDeliveryOffer() + " "+currency);
            pickuptodrop.setText(getDataInner.getPickupToDropLocation() + "km");
        }

        normalActiveOrderOrderApi();

        Uri uri = Uri.parse("https://res.cloudinary.com/boss8055/image/upload/v1558617086/ikrm9elx95eayq2zjflv.jpg");
        callChatHistoryApi();
        connectSockettest();
        setUpRecyclerView();

        generateImageFromPdf(uri);

    }

    @OnClick({R.id.tvTrack, R.id.tvCall, R.id.btn_send, R.id.btn_attachment, R.id.iv_backchat, R.id.tvInvoice})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvTrack:
                startActivity(ActiveTrackingActivity.getIntent(this, getDataInner, "FromNUActive", msgTrackData));
                break;

            case R.id.tvCall:
                mobile = getDataInner.getOfferAcceptedOfCountryCode() + getDataInner.getOfferAcceptedOfMobileNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mobile));
                startActivity(intent);
                break;

            case R.id.btn_attachment:
                bottomSheetOpen();
                break;

            case R.id.btn_send:

                if (!edt_typetxt.getText().toString().trim().isEmpty()) {
                    sendMessage(getDataInner.getOfferAcceptedOfId());

                } else {
                    Toast.makeText(this, R.string.please_type_something, Toast.LENGTH_SHORT).show();
                }

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

            case R.id.tvInvoice:
                if (tvInvoice.getText().toString().equalsIgnoreCase(getString(R.string.review_invoice_simple))) {
                    webUrl.getSettings().setJavaScriptEnabled(true);
                    webUrl.loadUrl("http://docs.google.com/gview?embedded=true&url=" + msgTrackData.getInvoicePdf());
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("Tap Successful"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void normalActiveOrderOrderApi() {
        try {
            MyDialog.getInstance(this).showDialog(NormalMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("orderId", getDataInner.get_id());
            jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            Call<MessageTrackResponse> call = apiInterface.normalActiveOrder(body);
            call.enqueue(new Callback<MessageTrackResponse>() {
                @Override
                public void onResponse(Call<MessageTrackResponse> call, Response<MessageTrackResponse> response) {
                    MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        MessageTrackResponse signUpWithMobileResp = response.body();
                        if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {

                            msgTrackData = response.body().getMsgTrackData();


                            if (getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker")) {
                                if (msgTrackData.getStatus().equalsIgnoreCase("Request")) {
                                    bottomSheetWithdrawConformation("orderCancelFromProfessional");
                                }
                                if (msgTrackData.getGoStatus().equalsIgnoreCase("false")) {
                                    tvInvoice.setVisibility(View.GONE);
                                    tvCreateInvoice.setVisibility(View.GONE);
                                } else if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                                    tvTrack.setVisibility(View.VISIBLE);
                                    if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")) {
                                        tvInvoice.setVisibility(View.VISIBLE);
                                        tvworkdone.setVisibility(View.GONE);
                                        tvCreateInvoice.setVisibility(View.GONE);
                                        tvArrived.setVisibility(View.GONE);
                                        if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                            tvArrived.setVisibility(View.VISIBLE);
                                        }
                                    } else if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true")) {
                                        tvworkdone.setVisibility(View.GONE);
                                        tvCreateInvoice.setVisibility(View.VISIBLE);
                                        tvArrived.setVisibility(View.VISIBLE);
                                        tvInvoice.setVisibility(View.VISIBLE);
                                        tvInvoice.setText(R.string.review_invoice);
                                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));

                                        if (msgTrackData.getArrivedStatus().equalsIgnoreCase("false")) {
                                            tvInvoice.setText(getResources().getString(R.string.review_invoice));
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvworkdone.setVisibility(View.GONE);
                                            tvArrived.setVisibility(View.GONE);
                                            tvCreateInvoice.setVisibility(View.VISIBLE);
                                        } else if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                            tvInvoice.setText(getResources().getString(R.string.review_invoice));
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvworkdone.setVisibility(View.GONE);
                                            tvArrived.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.VISIBLE);


                                        }
                                    }
                                    if (msgTrackData.getWorkDoneStatus().equalsIgnoreCase("false")) {
                                        tvworkdone.setVisibility(View.GONE);
                                    } else {
                                        tvworkdone.setVisibility(View.VISIBLE);
                                        showDialogDone("Professional");
                                    }
                                }
                            } else {
                                if (msgTrackData.getStatus().equalsIgnoreCase("Request")) {
                                    bottomSheetWithdrawConformation("orderCancelFromDelivery");
                                }
                                if (msgTrackData.getGoStatus().equalsIgnoreCase("false")) {
                                    tvInvoice.setVisibility(View.GONE);
                                    tvCreateInvoice.setVisibility(View.GONE);
                                } else if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                                    tvTrack.setVisibility(View.VISIBLE);
                                    if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")) {
                                        tvInvoice.setVisibility(View.VISIBLE);
                                        tvworkdone.setVisibility(View.GONE);
                                        tvCreateInvoice.setVisibility(View.GONE);
                                        tvArrived.setVisibility(View.GONE);

                                        if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                            tvInvoice.setText(getResources().getString(R.string.review_invoice));
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvworkdone.setVisibility(View.GONE);
                                            //tvArrived.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setText(getString(R.string.arrived));
                                            //tvArrived.setText(getString(R.string.invoice_created));

                                            if (msgTrackData.getWorkDoneStatus().equalsIgnoreCase("false")) {
                                                tvworkdone.setVisibility(View.GONE);
                                            } else {
                                                tvworkdone.setVisibility(View.VISIBLE);
                                                showDialogDone("Delivery");
                                            }
                                        }

                                    } else if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true")) {
                                        tvworkdone.setVisibility(View.GONE);
                                        tvCreateInvoice.setVisibility(View.VISIBLE);
                                        tvArrived.setVisibility(View.VISIBLE);
                                        tvArrived.setText(getString(R.string.invoice_created));
                                        tvInvoice.setVisibility(View.VISIBLE);
                                        tvInvoice.setText(R.string.review_invoice);
                                        tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));

                                        if (msgTrackData.getArrivedStatus().equalsIgnoreCase("false")) {
                                            tvInvoice.setText(getResources().getString(R.string.review_invoice));
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvworkdone.setVisibility(View.GONE);
                                            tvArrived.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.GONE);
                                            tvArrived.setText(getString(R.string.invoice_created));
                                        } else if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                            tvInvoice.setText(getResources().getString(R.string.review_invoice));
                                            tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));
                                            tvworkdone.setVisibility(View.GONE);
                                            tvArrived.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setVisibility(View.VISIBLE);
                                            tvCreateInvoice.setText(getString(R.string.arrived));
                                            tvArrived.setText(getString(R.string.invoice_created));

                                            if (msgTrackData.getWorkDoneStatus().equalsIgnoreCase("false")) {
                                                tvworkdone.setVisibility(View.GONE);
                                            } else {
                                                tvworkdone.setVisibility(View.VISIBLE);
                                                showDialogDone("Delivery");
                                            }
                                        }
                                    }
                                }
                            }

                           /* if (msgTrackData.getGoStatus().equalsIgnoreCase("true")) {
                                if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("false")) {

                                    tvInvoice.setVisibility(View.VISIBLE);
                                    tvworkdone.setVisibility(View.GONE);
                                    tvCreateInvoice.setVisibility(View.GONE);
                                    tvArrived.setVisibility(View.GONE);

                                } else if (msgTrackData.getInvoiceStatus().equalsIgnoreCase("true")) {

                                    tvworkdone.setVisibility(View.GONE);
                                    tvCreateInvoice.setVisibility(View.GONE);
                                    tvArrived.setVisibility(View.VISIBLE);
                                    tvInvoice.setText(R.string.review_invoice);
                                    tvInvoice.setTextColor(getResources().getColor(R.color.purpalMedium));

                                    if (msgTrackData.getArrivedStatus().equalsIgnoreCase("true")) {
                                        tvInvoice.setText(getResources().getString(R.string.edit_invoice));
                                        tvworkdone.setVisibility(View.VISIBLE);
                                        tvArrived.setVisibility(View.GONE);
                                        tvCreateInvoice.setVisibility(View.GONE);

                                    }

                                }
                            }*/

                        } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(NormalMessageTrackActivity.this);

                        } else {
                            Toast.makeText(NormalMessageTrackActivity.this, "" + signUpWithMobileResp.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(NormalMessageTrackActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<MessageTrackResponse> call, Throwable t) {
                    MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDialogDone(String type) {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate);

        Button btnRequire = (Button) dialog.findViewById(R.id.btn_ok);


        btnRequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (type.equalsIgnoreCase("Professional")) {

                    startActivity(RatingAndRiviewActivity.getIntent(NormalMessageTrackActivity.this, getDataInner, "PastNUP"));
                    finish();

                } else {

                    startActivity(RatingAndRiviewActivity.getIntent(NormalMessageTrackActivity.this, getDataInner, "PastNUD"));
                    finish();

                }

//                if(type.equalsIgnoreCase("Professional")){
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("kFromCat", "Professional");
//                    hashMap.put("kFrom", "Past");
//
//                    startActivity(HomeMainActivity.getIntent(NormalMessageTrackActivity.this, hashMap));
//
//                }else {
//
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("kFromCat", "Delivery");
//                    hashMap.put("kFrom", "Past");
//
//                    startActivity(HomeMainActivity.getIntent(NormalMessageTrackActivity.this, hashMap));
//
//
//                }
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void changeDeliveryCaptainApi() {
        try {
            MyDialog.getInstance(this).showDialog(NormalMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            jsonObject.put("orderId", getDataInner.get_id());
            jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());
            Call<MessageTrackResponse> call = apiInterface.changeDeliveryCaptain(token, body);
            call.enqueue(new Callback<MessageTrackResponse>() {
                @Override
                public void onResponse(Call<MessageTrackResponse> call, Response<MessageTrackResponse> response) {
                    MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                    if (response.isSuccessful()) {
                        MessageTrackResponse signUpWithMobileResp = response.body();
                        if (signUpWithMobileResp.getStatus().equalsIgnoreCase("SUCCESS")) {

                            Toast.makeText(NormalMessageTrackActivity.this, "" + signUpWithMobileResp.getResponse_message(), Toast.LENGTH_SHORT).show();

                        } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(NormalMessageTrackActivity.this);
                        } else {
                            Toast.makeText(NormalMessageTrackActivity.this, "" + signUpWithMobileResp.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(NormalMessageTrackActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<MessageTrackResponse> call, Throwable t) {
                    MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                    t.printStackTrace();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bottomSheetOpen() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.bottom_normal_track_layout, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView tv_admin = sheetView.findViewById(R.id.tv_admin);
        TextView tv_share_img = sheetView.findViewById(R.id.tv_share_img);
        TextView tv_share_loc = sheetView.findViewById(R.id.tv_share_loc);
        TextView tv_del_cap = sheetView.findViewById(R.id.tv_del_cap);
        if (getDataInner.getServiceType().equalsIgnoreCase("ProfessionalWorker"))
            tv_del_cap.setText(R.string.change_service_provider);
        else
            tv_del_cap.setText("Change Delivery Captain");

        TextView tv_cancel_Order = sheetView.findViewById(R.id.tv_cancel_Order);
        TextView tv_cancel = sheetView.findViewById(R.id.tv_cancel);

        // initView();
        tv_del_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetWithdraw();
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
        //Share Live Location////////
        tv_share_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(ShareLocationActivity.getIntent(getApplicationContext()), 100);

                bottomSheetDialog.dismiss();
            }
        });

        tv_cancel_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog();
                bottomSheetDialog.dismiss();
            }
        });


        tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(ContactAdmin.getIntent(NormalMessageTrackActivity.this));
                startActivity(ReportOrderActivity.getIntent(NormalMessageTrackActivity.this, "CustomerServiceAdmin"));
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

    private void bottomSheetWithdraw() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.dialog_change_captain_layout, null);
        bottomSheetDialog.setContentView(sheetView);


        TextView tv_cancel = sheetView.findViewById(R.id.btn_cancel);
        TextView btn_ok = sheetView.findViewById(R.id.btn_ok);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDeliveryCaptainApi();
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

    private void bottomSheetWithdrawConformation(String type) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = this.getLayoutInflater().inflate(R.layout.dialog_change_captain_layout, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.setCancelable(false);


        TextView tvMsgBottom = sheetView.findViewById(R.id.tvMsgBottom);
        if (type.equalsIgnoreCase("orderCancelFromDelivery"))
            tvMsgBottom.setText("Your delivery captain ask for change captain?");
        else
            tvMsgBottom.setText(R.string.your_professional_captain_ask_for_change);

        TextView tv_cancel = sheetView.findViewById(R.id.btn_cancel);
        TextView btn_ok = sheetView.findViewById(R.id.btn_ok);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptWithdrawOrderRequest(type);
                bottomSheetDialog.dismiss();
            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineWithdrawOrderRequest();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    //api acceptWithdrawOrderRequest
    private void acceptWithdrawOrderRequest(String type) {
        try {
            MyDialog.getInstance(NormalMessageTrackActivity.this).showDialog(NormalMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("orderId", getDataInner.get_id());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RequestOrderResponse> beanCall = apiInterface.acceptWithdrawOrderRequest(token, body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                startActivity(MapProfessinalWorkerActivity.getIntent(NormalMessageTrackActivity.this, type, getDataInner.get_id()));

                            } else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(NormalMessageTrackActivity.this);
                            } else {
                                Toast.makeText(NormalMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    //api declineWithdrawOrderRequest
    private void declineWithdrawOrderRequest() {
        try {
            MyDialog.getInstance(NormalMessageTrackActivity.this).showDialog(NormalMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("orderId", getDataInner.get_id());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RequestOrderResponse> beanCall = apiInterface.declineWithdrawOrderRequest(token, body);

                beanCall.enqueue(new Callback<RequestOrderResponse>() {
                    @Override
                    public void onResponse(Call<RequestOrderResponse> call, Response<RequestOrderResponse> response) {
                        MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            RequestOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                //finish();

                                Toast.makeText(NormalMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(NormalMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    //send message
    private void sendMessage(String receiverId) {

        try {
            JSONObject obj = new JSONObject();

            obj.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            obj.put("receiverId", receiverId);
            obj.put("roomId", getDataInner.getRoomId());
            obj.put("message", edt_typetxt.getText().toString().trim());
            obj.put("messageType", "Text");
            obj.put("profilePic", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kProfile));
            mSocket.emit("message", obj);

            edt_typetxt.setText("");
            msgType = "";
            msgType = "Text";


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //send message
    private void sendLocation(String receiverId, String lat, String lng) {

        try {
            JSONObject obj = new JSONObject();

            obj.put("senderId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
            obj.put("receiverId", receiverId);
            obj.put("roomId", getDataInner.getRoomId());
            obj.put("message", "https://www.google.com/maps?q=" + lat + "," + lng + "&z=17&hl=en");
            obj.put("messageType", "Location");
            obj.put("profilePic", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kProfile));
            mSocket.emit("message", obj);

            edt_typetxt.setText("");
            msgType = "";
            msgType = "Location";


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void connectSockettest() {
        MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
        try {
            //mSocket = IO.socket("http://3.128.74.178:3000");
            mSocket = IO.socket("http://3.129.47.202:3000");
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

    private void scrollToBottom() {
        rv_Chat_list.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void setUpRecyclerView() {
        rv_Chat_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(MessageList, NormalMessageTrackActivity.this);
        rv_Chat_list.setAdapter(messageAdapter);
    }


    //////IMage Send//////////////

    private void callChatHistoryApi() {
        try {
            MyDialog.getInstance(NormalMessageTrackActivity.this).showDialog(NormalMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kUserId));
                jsonObject.put("roomId", getDataInner.getRoomId());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ChatResponse> beanCall = apiInterface.getChatHistory(token, body);

                beanCall.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ChatResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    MessageList = response.body().getDataList();
                                    messageAdapter.updateMessage(MessageList);
                                    /*rv_Chat_list.setLayoutManager(new LinearLayoutManager(NormalMessageTrackActivity.this));
                                    messageAdapter = new MessageAdapter(MessageList, NormalMessageTrackActivity.this);
                                    rv_Chat_list.setAdapter(messageAdapter);*/
                                    //messageAdapter.notifyDataSetChanged();

                                    //no_data.setVisibility(View.GONE);
                                    rv_Chat_list.setVisibility(View.VISIBLE);

                                    new Handler().post(new Runnable() {
                                        @Override
                                        public void run() {
//                                    recyler_view_comments.smoothScrollToPosition(main_data.size()-1);
                                            rv_Chat_list.scrollToPosition(MessageList.size() - 1);
                                            rv_Chat_list.setVisibility(View.VISIBLE);
                                            // no_data.setVisibility(View.GONE);

                                        }
                                    });


                                } else {
                                    //no_data.setVisibility(View.VISIBLE);
                                    // rv_Chat_list.setVisibility(View.GONE);
                                }
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(NormalMessageTrackActivity.this);
                            } else {
                                Toast.makeText(NormalMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    public void sendAndGetBinaryData(String path, int type) {
        String uni_code = String.valueOf(System.currentTimeMillis());

        /*if (type == IMAGE_FILE) {
            MessageList.add(new MessageChat(SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kUserId), "", "", imagePath, "", "Just now", "User", ""));
        }
        messageAdapter.notifyDataSetChanged();*/
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

                Intent intent = new Intent(NormalMessageTrackActivity.this, TakeImage.class);
                intent.putExtra("from", "camera");
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                dialog.dismiss();
            }
        });
        txt_choosefromlibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NormalMessageTrackActivity.this, TakeImage.class);
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

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {

                String address1 = data.getStringExtra("ADDRESS");
                String lat1 = data.getStringExtra("LAT");
                String lat2 = data.getStringExtra("LONG");

                sendLocation(getDataInner.getOfferAcceptedOfId(), lat1, lat2);


            }
        } else if (requestCode == START_VERIFICATION) {
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
//        else {
//         String   address1 = data.getStringExtra("ADDRESS");
//            String     lat1 = data.getStringExtra("LAT");
//            String  lat2 = data.getStringExtra("LONG");
//        }
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
    }

    private void showCancelDialog() {
        Dialog dialog = new Dialog(NormalMessageTrackActivity.this);
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
                    //cancel_txt.setText(reason[position]);
                    cancel_txt.setText(parent.getItemAtPosition(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.cancel_reasons));
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
                    Toast.makeText(NormalMessageTrackActivity.this, "Please select the reason", Toast.LENGTH_SHORT).show();
                } else {
                    callOrderCancelApi(cancel_txt.getText().toString(), edt_reason.getText().toString());
                    dialog.dismiss();

                }
                //dialog.dismiss();
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

    private void callOrderCancelApi(String cancelTxt, String reason) {
        try {
            MyDialog.getInstance(this).showDialog(NormalMessageTrackActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("orderId", getDataInner.get_id());
                jsonObject.put("orderCanelReason", reason);
                jsonObject.put("orderCancelMessage", cancelTxt);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<UserTypeResponse> beanCall = apiInterface.orderCancel(token, body);

                beanCall.enqueue(new Callback<UserTypeResponse>() {
                    @Override
                    public void onResponse(Call<UserTypeResponse> call, Response<UserTypeResponse> response) {
                        MyDialog.getInstance(NormalMessageTrackActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            UserTypeResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                String checktype = response.body().getUserTypeResponse().getServiceType();

                                showCancelOderSuccessfully();

                                //Toast.makeText(CancellationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(NormalMessageTrackActivity.this);
                            } else {
                                Toast.makeText(NormalMessageTrackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showCancelOderSuccessfully() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);
        registerNow.setText("Ok");
        notNow.setText("Cancel");

        notNow.setVisibility(View.GONE);
        registerNow.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);

        textView.setText("Order cancelled successfully");


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private class FileUploadTask extends AsyncTask<String, Integer, String> {
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
        private String file_path = "";
        private String room_id = "";
        private String receiver_id = "";
        private String attachment_type = SEND_MEDIA;
        private String uni_code = "";
        private UploadFileMoreDataReqListener callback;
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
        private FileUploadManager mFileUploadManager;
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
                        res.put("sender_id", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kUserId));
                        res.put("receiver_id", receiver_id);
                        res.put("message", getString(R.string.read_attachement));
                        res.put("messageType", "Media");
                        res.put("attachment_type", attachment_type);
                        res.put("profilePic", SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kProfile));
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

        public FileUploadTask(String file_path, String uni_code) {
            this.file_path = file_path;
            room_id = getDataInner.getRoomId();
            receiver_id = SharedPreferenceWriter.getInstance(NormalMessageTrackActivity.this).getString(kUserId);
            this.uni_code = uni_code;
            Log.e("path uni_code", "path uni_code-- " + file_path);

            attachment_type = SEND_MEDIA;

        }

        @Override
        protected void onPreExecute() {
            MyDialog.getInstance(NormalMessageTrackActivity.this).showDialog(NormalMessageTrackActivity.this);
            Log.e("mFileUploadManager", "in it mFileUploadManager for-- " + file_path);
            mFileUploadManager = new FileUploadManager();
        }

        @Override
        protected String doInBackground(String... params) {
            boolean isSuccess = mSocket.connected();
            if (isSuccess) {
                mFileUploadManager.prepare(file_path, NormalMessageTrackActivity.this);

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
                media_path.remove(uni_code);
                mFileUploadManager.close();
                mSocket.off("uploadFileMoreDataReq", uploadFileMoreDataReq);
                mSocket.off("uploadFileCompleteRes", onCompletedddd);
                uploadFileOnServer(media_path);
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

        private void setUploadFileMoreDataReqListener(final UploadFileMoreDataReqListener callbackk) {
            callback = callbackk;
            mSocket.on("uploadFileMoreDataReq", uploadFileMoreDataReq);
        }

        private void setUploadFileCompleteListener() {
            mSocket.on("uploadFileCompleteRes", onCompletedddd);
        }
    }

    private void setLanguage() {
        // check language from data base
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                LocaleHelper.setLocale(this,"en");
            }else {
                LocaleHelper.setLocale(this, "pt");
            }
        }else {
            LocaleHelper.setLocale(this,"pt");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");

            //SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kEnglish);
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
