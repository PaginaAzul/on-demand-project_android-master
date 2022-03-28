package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.CreateInvoiceResponse;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.ProfessionalWorkerResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.TakeImage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class CreateInvoiceActivity extends AppCompatActivity {
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    @BindView(R.id.invoice_img)
    ImageView invoice_img;
    @BindView(R.id.good_cost)
    TextView good_cost;
    @BindView(R.id.delivery_charge)
    TextView delivery_charge;
    @BindView(R.id.vat_charges)
    TextView vat_charges;
    @BindView(R.id.total_amt)
    TextView total_amt;
    @BindView(R.id.edt_cost)
    EditText edt_cost;
    @BindView(R.id.bt_send)
    Button bt_send;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    private String invoiceImgurl="";
    private NormalUserPendingOrderInner getDataList;
    private String commingFrom;
    private String orderId = "";

    public static Intent getIntent(Context context, NormalUserPendingOrderInner pendingOrderInner, String commingFrom) {
        Intent intent = new Intent(context, CreateInvoiceActivity.class);
        intent.putExtra("kData", (Serializable) pendingOrderInner);
        intent.putExtra("kCome", (Serializable) commingFrom);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        ButterKnife.bind(this);
        //tv_text.setPaintFlags(tv_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (getIntent() != null) {
            getDataList = (NormalUserPendingOrderInner) getIntent().getSerializableExtra("kData");
            commingFrom = (String) getIntent().getStringExtra("kCome");
        }

        if (commingFrom.equalsIgnoreCase("ActiveDP")) {
            orderId = getDataList.get_id();
            callInvoiceDetailsApi(orderId);
        }else if(commingFrom.equalsIgnoreCase("ActivePW")){
            orderId = getDataList.get_id();
            callInvoiceDetailsApi(orderId);
        }

        edt_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edt_cost.getText().toString().equalsIgnoreCase("")) {
                    good_cost.setText(edt_cost.getText().toString());
                    String cost = edt_cost.getText().toString();

                    try{
                        //double deliveryVat = Integer.parseInt(cost) + 0 + 0;
                        double totalAmount = Double.parseDouble(cost) + Double.parseDouble(delivery_charge.getText().toString().split("\\s")[0]) + Double.parseDouble(vat_charges.getText().toString().split("\\s")[0]);
                        total_amt.setText(String.valueOf(totalAmount));
                    }catch (Exception e){
                        e.printStackTrace();

                    }


                }
            }
        });

    }


    @OnClick({R.id.invoice_img, R.id.bt_send, R.id.iv_close})
    public void onClick(View view) {
        switch ((view.getId())) {
            case R.id.invoice_img:
                selectImage();
                break;
            case R.id.bt_send:
                if (commingFrom.equalsIgnoreCase("ActiveDP")) {
                    orderId = getDataList.get_id();
                    if(!edt_cost.getText().toString().isEmpty()) {
                        if (imagePath != null) {
                            createInvoiceByDPApi();
                        } else {
                            if (invoiceImgurl != null && !invoiceImgurl.equalsIgnoreCase("")) {
                                createInvoiceByDPApi();
                            }else {
                                Toast.makeText(this, "Please select the invoice image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Please enter good cost", Toast.LENGTH_SHORT).show();
                    }
                }else if(commingFrom.equalsIgnoreCase("ActivePW")){
                    orderId = getDataList.get_id();
                    if(!edt_cost.getText().toString().isEmpty()) {
                    if (imagePath != null) {
                        createInvoiceByDPApi();
                    }else {
                        if (invoiceImgurl != null && !invoiceImgurl.equalsIgnoreCase("")) {
                            createInvoiceByDPApi();
                        }else {
                            Toast.makeText(this, "Please select the invoice image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                        Toast.makeText(this, "Please enter good cost", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.iv_close:
                finish();
                break;
        }
    }


    private Map<String, RequestBody> setUpMapData() throws JSONException {

        Map<String, RequestBody> fields = new HashMap<>();

        RequestBody goodsPrice = RequestBody.create(MediaType.parse("text/plain"), good_cost.getText().toString());
        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"), edt_cost.getText().toString());
        RequestBody orderID = RequestBody.create(MediaType.parse("text/plain"), orderId);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(kUserId));

        fields.put("userId", userId);
        fields.put("orderId", orderID);
        fields.put("goodsPrice", goodsPrice);
        fields.put("amount", amount);
        fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE)));

        return fields;
    }

    //call Api
    private void createInvoiceByDPApi() {
        try {
            MyDialog.getInstance(this).showDialog(CreateInvoiceActivity.this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            Call<ProfessionalWorkerResponse> call;
            if (!token.isEmpty()) {
                RequestBody profile_body;
                MultipartBody.Part profilePart;
                if (imagePath != null) {
                    File file = new File(imagePath);
                    profile_body = RequestBody.create(MediaType.parse("image/*"), file);
                    profilePart = MultipartBody.Part.createFormData("invoiceImage", file.getName(), profile_body);
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.createInvoiceByDeliveryPerson(setUpMapData(), profilePart);
                } else {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.createInvoiceByDeliveryPerson(setUpMapData());
                }
                call.enqueue(new retrofit2.Callback<ProfessionalWorkerResponse>() {
                    @Override
                    public void onResponse(Call<ProfessionalWorkerResponse> call, Response<ProfessionalWorkerResponse> response) {
                        MyDialog.getInstance(CreateInvoiceActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ProfessionalWorkerResponse editProfileResponce = response.body();
                            if (editProfileResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                finish();

                                Toast.makeText(CreateInvoiceActivity.this, "" + editProfileResponce.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateInvoiceActivity.this, "" + editProfileResponce.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(CreateInvoiceActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfessionalWorkerResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyDialog.getInstance(CreateInvoiceActivity.this).hideDialog();
                        String s = "";
                    }

                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callInvoiceDetailsApi(String orderID) {
        try {
            MyDialog.getInstance(CreateInvoiceActivity.this).showDialog(CreateInvoiceActivity.this);
            String token = SharedPreferenceWriter.getInstance(CreateInvoiceActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderId", orderID);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<CreateInvoiceResponse> beanCall = apiInterface.getInvoiceDetails(token,body);
                beanCall.enqueue(new Callback<CreateInvoiceResponse>() {
                    @Override
                    public void onResponse(Call<CreateInvoiceResponse> call, Response<CreateInvoiceResponse> response) {
                        MyDialog.getInstance(CreateInvoiceActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            CreateInvoiceResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if(signUpResponce.getInvoiceInner().getInvoiceStatus().equalsIgnoreCase("false")){
                                    String deliveryOffer = getDataList.getDeliveryOffer();
                                    String tax = getDataList.getTax();
                                    delivery_charge.setText(deliveryOffer+" Euro + ");
                                    vat_charges.setText(tax+" Euro");
                                    try{
                                        String goodCost = good_cost.getText().toString().split("\\s")[0];
                                        double totalAmount = Double.parseDouble(goodCost) + Double.parseDouble(deliveryOffer) + Double.parseDouble(tax);
                                        total_amt.setText(String.valueOf(totalAmount));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    edt_cost.setText(response.body().getInvoiceInner().getInvoiceSubtoatl());
                                    good_cost.setText(response.body().getInvoiceInner().getInvoiceSubtoatl());
                                    delivery_charge.setText(response.body().getInvoiceInner().getDeliveryOffer()+" Euro + ");
                                    vat_charges.setText(response.body().getInvoiceInner().getTax());
                                    //total_amt.setText(response.body().getInvoiceInner().getTotal());

                                    try {
                                        String goodCost = good_cost.getText().toString().split("\\s")[0];
                                        double totalAmount = Double.parseDouble(goodCost) + Double.parseDouble(response.body().getInvoiceInner().getDeliveryOffer()) + Double.parseDouble(response.body().getInvoiceInner().getTax());
                                        total_amt.setText(String.valueOf(totalAmount));
                                    }catch (Exception e){

                                    }

                                    if (response.body().getInvoiceInner().getInvoiceImage() != null && !response.body().getInvoiceInner().getInvoiceImage().equalsIgnoreCase("")) {
                                        Picasso.with(CreateInvoiceActivity.this).load(response.body().getInvoiceInner().getInvoiceImage()).into(invoice_img);
                                        invoiceImgurl =response.body().getInvoiceInner().getInvoiceImage();
                                    }
                                }


                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateInvoiceActivity.this, response.body().getResponse_message(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<CreateInvoiceResponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
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
        txt_takephoto.setOnClickListener(v -> {

            Intent intent = new Intent(CreateInvoiceActivity.this, TakeImage.class);
            intent.putExtra("from", "camera");
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
            dialog.dismiss();
        });
        txt_choosefromlibrary.setOnClickListener(v -> {

            Intent intent = new Intent(CreateInvoiceActivity.this, TakeImage.class);
            intent.putExtra("from", "gallery");
            startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (resultCode == RESULT_OK) {
            if (data.getStringExtra("filePath") != null) {
                imagePath = data.getStringExtra("filePath");
                fileFlyer = new File(data.getStringExtra("filePath"));

                if (fileFlyer.exists() && fileFlyer != null) {
                    invoice_img.setImageURI(Uri.fromFile(fileFlyer));
                }
            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        } else if (requestCode == RESULT_OK)
            super.onActivityResult(requestCode, resultCode, data);
    }

}
