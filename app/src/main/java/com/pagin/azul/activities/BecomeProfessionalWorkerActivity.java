package com.pagin.azul.activities;

import android.app.Activity;
import android.app.Dialog;
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

public class BecomeProfessionalWorkerActivity extends AppCompatActivity {
    private File imagePath1 = null;
    private File imagePath2 = null;
    private File imagePath3 = null;
    private File imagePath4 = null;
    private File imagePath5 = null;

    private int START_VERIFICATION = 1001;
    private File fileFlyer;
    private String[] industry = {"Industry", "Chemical", "Automobile", "IT"};
    private String[] section = {"Section", "Dummy Section1", "Dummy Section2", "Dummy Section3"};
    private String[] professionalList = {"Professional", "Chemical Professional", "Automobile Professional", "IT Professional"};
    private String[] OfficialGovID = {"Official Goverment ID", "Passport", "Driving License", "Adhar Card"};

    @BindView(R.id.tv_GovermentID)
    TextView tv_GovermentID;

    @BindView(R.id.tv_industry)
    TextView tv_industry;
    @BindView(R.id.tv_section)
    TextView tv_section;
    @BindView(R.id.tv_professional)
    TextView tv_professional;
    @BindView(R.id.spinnerindustry)
    Spinner spinnerindustry;
    @BindView(R.id.spinnersection)
    Spinner spinnersection;
    @BindView(R.id.spinnerprofessional)
    Spinner spinnerprofessional;
    @BindView(R.id.spinnerGovermentID)
    Spinner spinnerGovermentID;

    @BindView(R.id.iv_uploadId2)
    ImageView iv_uploadId2;
    @BindView(R.id.iv_uploadId1)
    ImageView iv_uploadId1;
    @BindView(R.id.iv_id1)
    ImageView iv_id1;
    @BindView(R.id.iv_id2)
    ImageView iv_id2;
    @BindView(R.id.iv_prf)
    ImageView ivPrf;
    @BindView(R.id.iv_varify)
    ImageView ivVarify;
    @BindView(R.id.about_txt)
    EditText about_txt;
    @BindView(R.id.tap_plumber)
    EditText tapPlumber;
    @BindView(R.id.bank_ac)
    EditText bank_ac;
    @BindView(R.id.emergency_contct)
    EditText emergency_contct;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.ll_uploadID2)
    LinearLayout ll_uploadID2;
    @BindView(R.id.ll_uploadid1)
    LinearLayout ll_uploadid1;

    @BindView(R.id.ll_id1)
    LinearLayout ll_id1;
    @BindView(R.id.ll_id2)
    LinearLayout ll_id2;
    @BindView(R.id.rl_id_1)
    RelativeLayout rl_id_1;
    @BindView(R.id.rl_id_2)
    RelativeLayout rl_id_2;
    @BindView(R.id.rl_uploadID2)
    RelativeLayout rl_uploadID2;
    @BindView(R.id.rl_uploadID1)
    RelativeLayout rl_uploadID1;
    @BindView(R.id.take_image)
    TextView take_image;


    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                tv_industry.setText(industry[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                tv_section.setText(section[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                tv_professional.setText(professionalList[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener3 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                tv_GovermentID.setText(OfficialGovID[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_professional_worker);
        ButterKnife.bind(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, industry);
        spinnerindustry.setAdapter(adapter);
        spinnerindustry.setOnItemSelectedListener(onItemSelectedListener);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, section);
        spinnersection.setAdapter(adapter1);
        spinnersection.setOnItemSelectedListener(onItemSelectedListener1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, professionalList);
        spinnerprofessional.setAdapter(adapter2);
        spinnerprofessional.setOnItemSelectedListener(onItemSelectedListener2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, OfficialGovID);
        spinnerGovermentID.setAdapter(adapter3);
        spinnerGovermentID.setOnItemSelectedListener(onItemSelectedListener3);

    }


    @OnClick({R.id.rl_industry, R.id.rl_section, R.id.rl_professional,
            R.id.take_image, R.id.ll_id1, R.id.ll_id2, R.id.ll_uploadid1,
            R.id.ll_uploadID2, R.id.rl_GovermentID, R.id.btn_submit, R.id.btn_back})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_industry:
                spinnerindustry.performClick();
                break;
            case R.id.rl_section:
                spinnersection.performClick();
                break;
            case R.id.rl_professional:
                spinnerprofessional.performClick();
                break;
            case R.id.rl_GovermentID:
                spinnerGovermentID.performClick();
                break;
            case R.id.ll_id1:
                selectImage(BecomeProfessionalWorkerActivity.this, 111);
                break;
            case R.id.ll_id2:
                selectImage(BecomeProfessionalWorkerActivity.this, 222);
                break;
            case R.id.take_image:
                selectImage(BecomeProfessionalWorkerActivity.this, 333);
                break;
            case R.id.ll_uploadid1:
                selectImage(BecomeProfessionalWorkerActivity.this, 444);
                break;
            case R.id.ll_uploadID2:
                selectImage(BecomeProfessionalWorkerActivity.this, 555);
                break;
            case R.id.btn_submit:
                if (!Validation()) {
                    return;
                }
                callProfessionalWorkerService();
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

            Intent intent = new Intent(BecomeProfessionalWorkerActivity.this, TakeImage.class);
            intent.putExtra("from", "camera");
            startActivityForResult(intent, code);
            dialog.dismiss();
        });
        txt_choosefromlibrary.setOnClickListener(v -> {

            Intent intent = new Intent(BecomeProfessionalWorkerActivity.this, TakeImage.class);
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
                    iv_id1.setImageURI(Uri.fromFile(fileFlyer));


                }
                break;
            case 222:
                if (fileFlyer.exists() && fileFlyer != null) {
                    rl_id_2.setVisibility(View.VISIBLE);
                    imagePath2 = fileFlyer;
                    iv_id2.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 333:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath3 = fileFlyer;
                    ivVarify.setVisibility(View.VISIBLE);
                    ivPrf.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 444:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath4 = fileFlyer;
                    rl_uploadID1.setVisibility(View.VISIBLE);
                    iv_uploadId1.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 555:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath5 = fileFlyer;
                    rl_uploadID2.setVisibility(View.VISIBLE);
                    iv_uploadId2.setImageURI(Uri.fromFile(fileFlyer));
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
        RequestBody officialGovntId = RequestBody.create(MediaType.parse("text/plain"), tv_GovermentID.getText().toString());
        RequestBody industry = RequestBody.create(MediaType.parse("text/plain"), tv_industry.getText().toString());
        RequestBody section = RequestBody.create(MediaType.parse("text/plain"), tv_section.getText().toString());
        RequestBody professional = RequestBody.create(MediaType.parse("text/plain"), tv_professional.getText().toString());
        RequestBody bankAc = RequestBody.create(MediaType.parse("text/plain"), bank_ac.getText().toString());
        RequestBody emergencyContact = RequestBody.create(MediaType.parse("text/plain"), emergency_contct.getText().toString());
        RequestBody userType = RequestBody.create(MediaType.parse("text/plain"), "ProfessionalWorker");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(kUserId));
        RequestBody professionalTab = RequestBody.create(MediaType.parse("text/plain"), tapPlumber.getText().toString());

        fields.put("token", token);
        fields.put("aboutUs", aboutUs);
        fields.put("officialGovntId", officialGovntId);
        fields.put("industry", industry);
        fields.put("section", section);
        fields.put("professional", professional);
        fields.put("bankAC", bankAc);
        fields.put("emergencyContact", emergencyContact);
        fields.put("userType", userType);
        fields.put("userId", userId);
        fields.put("professionalTab", professionalTab);
        fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE)));


        return fields;
    }


    private void callProfessionalWorkerService() {
        try {
            MyDialog.getInstance(this).showDialog(BecomeProfessionalWorkerActivity.this);
            String token = SharedPreferenceWriter.getInstance(BecomeProfessionalWorkerActivity.this).getString(kToken);
            Call<ProfessionalWorkerResponse> call;

            if (!token.isEmpty()) {
                RequestBody Id1_body;
                RequestBody Id2_body;
                RequestBody Id3_body;
                RequestBody Id4_body;
                RequestBody profile_body;
                MultipartBody.Part Id1, Id2, Id4, Id3, prfpic;

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
                    Id3_body = RequestBody.create(MediaType.parse("image/*"), imagePath4);
                    Id3 = MultipartBody.Part.createFormData("id3", imagePath4.getName(), Id3_body);
                } else {
                    Id3 = MultipartBody.Part.createFormData("id3", "");
                }
                if (imagePath5 != null) {
                    Id4_body = RequestBody.create(MediaType.parse("image/*"), imagePath5);
                    Id4 = MultipartBody.Part.createFormData("id4", imagePath5.getName(), Id4_body);
                } else {
                    Id4 = MultipartBody.Part.createFormData("id4", "");
                }

                if (imagePath3 != null && imagePath4 != null) {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.professionalPerson(setUpMapData(), Id1, Id2, Id3, Id4, prfpic);
                } else if (imagePath3 == null && imagePath4 != null) {

                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.professionalPerson(setUpMapData(), Id1, Id2, Id4, prfpic);
                } else if (imagePath3 != null && imagePath4 == null) {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.professionalPerson(setUpMapData(), Id1, Id2, Id3, prfpic);
                } else {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.professionalPerson(setUpMapData(), Id1, Id2, prfpic);
                }


                call.enqueue(new retrofit2.Callback<ProfessionalWorkerResponse>() {
                    @Override
                    public void onResponse(Call<ProfessionalWorkerResponse> call, Response<ProfessionalWorkerResponse> response) {
                        MyDialog.getInstance(BecomeProfessionalWorkerActivity.this).hideDialog();

                        if (response.isSuccessful()) {

                            ProfessionalWorkerResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                finish();


                                Toast.makeText(BecomeProfessionalWorkerActivity.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(BecomeProfessionalWorkerActivity.this, "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(BecomeProfessionalWorkerActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfessionalWorkerResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyDialog.getInstance(BecomeProfessionalWorkerActivity.this).hideDialog();
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

        } else if (about_txt.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please write about text", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tv_industry.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(this, "Please enter industry", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tv_section.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(this, "Please enter section", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tv_professional.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(this, "Please enter professional", Toast.LENGTH_SHORT).show();

            return false;

        } else if (emergency_contct.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please enter emergency contact", Toast.LENGTH_SHORT).show();

            return false;

//        } else if (imagePath4 == null) {
//
//            Toast.makeText(this, "Id4 is required", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (imagePath5 == null) {
//            Toast.makeText(this, "Id5 is required", Toast.LENGTH_SHORT).show();
//            return false;

        }

        return true;

    }
}
