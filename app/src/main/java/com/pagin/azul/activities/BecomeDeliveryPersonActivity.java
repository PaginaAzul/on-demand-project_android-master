package com.pagin.azul.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.ProfessionalWorkerResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.TakeImage;

import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class BecomeDeliveryPersonActivity extends AppCompatActivity {
    private int START_VERIFICATION = 1001;
    private File fileFlyer;
    private File imagePath1 = null;
    private File imagePath2 = null;
    private File imagePath3 = null;
    private File imagePath4 = null;
    private File imagePath5 = null;

    private String[] vehicle = {"Vehicle Type", "FourWheeler", "TwoWheeler"};

    @BindView(R.id.ll_id1)
    LinearLayout ll_id1;
    @BindView(R.id.ll_id2)
    LinearLayout ll_id2;
    @BindView(R.id.rl_iv_prf)
    RelativeLayout rl_iv_prf;
    @BindView(R.id.ll_v_license)
    LinearLayout ll_v_license;
    @BindView(R.id.ll_insurance)
    LinearLayout ll_insurance;

    @BindView(R.id.iv_insurance)
    ImageView iv_insurance;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.iv_License)
    ImageView iv_License;
    @BindView(R.id.iv_prf)
    ImageView iv_prf;
    @BindView(R.id.iv_id2_image)
    ImageView iv_id2_image;
    @BindView(R.id.iv_id1_image)
    ImageView iv_id1_image;
    @BindView(R.id.iv_varify)
    ImageView iv_varify;

    @BindView(R.id.rl_id_1)
    RelativeLayout rl_id_1;
    @BindView(R.id.rl_id_2)
    RelativeLayout rl_id_2;
    @BindView(R.id.rl_license)
    RelativeLayout rl_license;
    @BindView(R.id.rl_insurance)
    RelativeLayout rl_insurance;

    @BindView(R.id.about_txt)
    EditText about_txt;
    @BindView(R.id.vehicle_no)
    EditText vehicle_no;
    @BindView(R.id.bank_ac)
    EditText bank_ac;
    @BindView(R.id.emergency_contct)
    EditText emergency_contct;
    @BindView(R.id.insurance_no)
    EditText insurance_no;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.spinner_vehicle_type)
    Spinner spinnervehicle;
    @BindView(R.id.tv_vehicle_type)
    TextView tv_vehicle;
    @BindView(R.id.take_image)
    TextView take_image;


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, BecomeDeliveryPersonActivity.class);
        return intent;
    }


    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                tv_vehicle.setText(vehicle[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_delivery_person);
        ButterKnife.bind(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicle);
        spinnervehicle.setAdapter(adapter);
        spinnervehicle.setOnItemSelectedListener(onItemSelectedListener);


    }


    @OnClick({R.id.ll_id1, R.id.ll_id2, R.id.take_image, R.id.ll_v_license, R.id.ll_insurance,
            R.id.btn_submit, R.id.rl_vehicle_type, R.id.btn_back})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_id1:
                selectImage(BecomeDeliveryPersonActivity.this, 111);
                break;
            case R.id.ll_id2:
                selectImage(BecomeDeliveryPersonActivity.this, 222);
                break;
            case R.id.take_image:
                selectImage(BecomeDeliveryPersonActivity.this, 333);
                break;
            case R.id.ll_v_license:
                selectImage(BecomeDeliveryPersonActivity.this, 444);
                break;
            case R.id.ll_insurance:
                selectImage(BecomeDeliveryPersonActivity.this, 555);
                break;
            case R.id.btn_submit:
                if (!Validation()) {
                    return;
                }
                callDeliveryPersonService();
                break;
            case R.id.rl_vehicle_type:
                spinnervehicle.performClick();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;


        }
    }


    public void selectImage(Activity activity, final int code) {
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

            Intent intent = new Intent(BecomeDeliveryPersonActivity.this, TakeImage.class);
            intent.putExtra("from", "camera");
            startActivityForResult(intent, code);
            dialog.dismiss();
        });
        txt_choosefromlibrary.setOnClickListener(v -> {

            Intent intent = new Intent(BecomeDeliveryPersonActivity.this, TakeImage.class);
            intent.putExtra("from", "gallery");
            startActivityForResult(intent, code);
            dialog.dismiss();
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
        if (requestCode == START_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (resultCode == RESULT_OK) {
            if (data.getStringExtra("filePath") != null) {
                fileFlyer = new File(data.getStringExtra("filePath"));
                setImageToList(requestCode, fileFlyer);


            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setImageToList(int code, File file) {
        switch (code) {
            case 111:
                if (fileFlyer.exists() && fileFlyer != null) {
                    rl_id_1.setVisibility(View.VISIBLE);
                    imagePath1 = fileFlyer;
                    iv_id1_image.setImageURI(Uri.fromFile(fileFlyer));


                }
                break;
            case 222:
                if (fileFlyer.exists() && fileFlyer != null) {
                    rl_id_2.setVisibility(View.VISIBLE);
                    imagePath2 = fileFlyer;
                    iv_id2_image.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 333:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath3 = fileFlyer;
                    iv_varify.setVisibility(View.VISIBLE);
                    iv_prf.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 444:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath4 = fileFlyer;
                    rl_license.setVisibility(View.VISIBLE);
                    iv_License.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 555:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath5 = fileFlyer;
                    rl_insurance.setVisibility(View.VISIBLE);
                    iv_insurance.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;

            default:
                break;
        }
    }


    private Map<String, RequestBody> setUpMapData() throws JSONException {

        Map<String, RequestBody> fields = new HashMap<>();

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(kToken));
        RequestBody aboutUs = RequestBody.create(MediaType.parse("text/plain"), about_txt.getText().toString());
        RequestBody vehicleNumber = RequestBody.create(MediaType.parse("text/plain"), vehicle_no.getText().toString());
        RequestBody vehicleType = RequestBody.create(MediaType.parse("text/plain"), tv_vehicle.getText().toString());
        RequestBody isuranceNumber = RequestBody.create(MediaType.parse("text/plain"), insurance_no.getText().toString());
        RequestBody bankAc = RequestBody.create(MediaType.parse("text/plain"), bank_ac.getText().toString());
        RequestBody emergencyContact = RequestBody.create(MediaType.parse("text/plain"), emergency_contct.getText().toString());
        RequestBody userType = RequestBody.create(MediaType.parse("text/plain"), "DeliveryPersion");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(kUserId));

        fields.put("aboutUs", aboutUs);
        fields.put("vehicleNumber", vehicleNumber);
        fields.put("vehicleType", vehicleType);
        fields.put("insuranceNumber", isuranceNumber);
        fields.put("bankAC", bankAc);
        fields.put("emergencyContact", emergencyContact);
        fields.put("userType", userType);
        fields.put("userId", userId);
        fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE)));

        return fields;
    }


    private void callDeliveryPersonService() {
        try {
            MyDialog.getInstance(this).showDialog(BecomeDeliveryPersonActivity.this);
            String token = SharedPreferenceWriter.getInstance(BecomeDeliveryPersonActivity.this).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody Id1_body;
                RequestBody Id2_body;
                RequestBody vehicleLicense_body;
                RequestBody insurance_body;
                RequestBody profile_body;
                MultipartBody.Part Id1, Id2, insurance, vehicleLicense, prfpic;

                if (imagePath1 != null) {

                    Id1_body = RequestBody.create(MediaType.parse("image/*"), imagePath1);
                    Id1 = MultipartBody.Part.createFormData("id1", imagePath1.getName(), Id1_body);
                } else {
                    Id1 = MultipartBody.Part.createFormData("id1", "");
                }
                if (imagePath2 != null) {
                    Id2_body = RequestBody.create(MediaType.parse("image/*"), imagePath2);
                    Id2 = MultipartBody.Part.createFormData("id2", imagePath2.getName(), Id2_body);
                } else {
                    Id2 = MultipartBody.Part.createFormData("id2", "");
                }
                if (imagePath3 != null) {
                    profile_body = RequestBody.create(MediaType.parse("image/*"), imagePath3);
                    prfpic = MultipartBody.Part.createFormData("profilePic", imagePath3.getName(), profile_body);
                } else {
                    prfpic = MultipartBody.Part.createFormData("profilePic", "");
                }
                if (imagePath4 != null) {
                    vehicleLicense_body = RequestBody.create(MediaType.parse("image/*"), imagePath4);
                    vehicleLicense = MultipartBody.Part.createFormData("vehicleLicense", imagePath4.getName(), vehicleLicense_body);
                } else {
                    vehicleLicense = MultipartBody.Part.createFormData("vehicleLicense", "");
                }
                if (imagePath5 != null) {
                    insurance_body = RequestBody.create(MediaType.parse("image/*"), imagePath5);
                    insurance = MultipartBody.Part.createFormData("insurance", imagePath5.getName(), insurance_body);
                } else {
                    insurance = MultipartBody.Part.createFormData("insurance", "");
                }

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                Call<ProfessionalWorkerResponse> call = apiInterface.deliveryPerson(setUpMapData(), Id1, Id2, vehicleLicense, insurance, prfpic);
                call.enqueue(new retrofit2.Callback<ProfessionalWorkerResponse>() {
                    @Override
                    public void onResponse(Call<ProfessionalWorkerResponse> call, Response<ProfessionalWorkerResponse> response) {
                        MyDialog.getInstance(BecomeDeliveryPersonActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            ProfessionalWorkerResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {

                                finish();

                                Toast.makeText(BecomeDeliveryPersonActivity.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(BecomeDeliveryPersonActivity.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(BecomeDeliveryPersonActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfessionalWorkerResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyDialog.getInstance(BecomeDeliveryPersonActivity.this).hideDialog();
                        String s = "";
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private boolean Validation() {
        if (imagePath1 == null) {
            Toast.makeText(this, "Id1 is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (imagePath2 == null) {
            Toast.makeText(this, "Id2 is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (imagePath3 == null) {
            Toast.makeText(this, "Please select profile pic", Toast.LENGTH_SHORT).show();
            return false;

        } else if (tv_vehicle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please select vehicle type", Toast.LENGTH_SHORT).show();
            return false;

       } else if (emergency_contct.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter emergency contact", Toast.LENGTH_SHORT).show();
            return false;

        } else if (about_txt.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please write something about profile", Toast.LENGTH_SHORT).show();
            return false;

//        } else if (imagePath4 == null) {
//
//            Toast.makeText(this, "vehicleLicense image is required", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (imagePath5 == null) {
//            Toast.makeText(this, "insurance image is required", Toast.LENGTH_SHORT).show();
//            return false;
//
//        }
        }

        return true;

    }

}
