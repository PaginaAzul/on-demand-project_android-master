package com.pagin.azul.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.ProfessionalWorkerReponse;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessionalFragment extends Fragment {
    @BindView(R.id.iv_id1)
    ImageView iv_id1;
    @BindView(R.id.iv_id2)
    ImageView iv_id2;
    @BindView(R.id.iv_prf)
    ImageView iv_prf;
    @BindView(R.id.iv_varify)
    ImageView iv_varify;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.about_txt)
    TextView about_txt;
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
    @BindView(R.id.ll_id1)
    LinearLayout ll_id1;
    @BindView(R.id.ll_id2)
    LinearLayout ll_id2;
    @BindView(R.id.rl_id_1)
    RelativeLayout rl_id_1;
    @BindView(R.id.rl_id_2)
    RelativeLayout rl_id_2;
    @BindView(R.id.imageclick)
    TextView imageclick;
    @BindView(R.id.rl_vehicle_license)
    RelativeLayout rl_vehicle_license;
    @BindView(R.id.ll_vehicle_license)
    LinearLayout ll_vehicle_license;
    @BindView(R.id.ll_upload_certificate)
    LinearLayout ll_upload_certificate;
    @BindView(R.id.rl_industry)
    RelativeLayout rl_industry;
    @BindView(R.id.rl_upload_certificate)
    RelativeLayout rl_upload_certificate;
    @BindView(R.id.iv_vehicle_license)
    ImageView iv_vehicle_license;
    @BindView(R.id.iv_upload_certificate)
    ImageView iv_upload_certificate;
    @BindView(R.id.edt_bank_ac)
    EditText edt_bank_ac;
    @BindView(R.id.edt_emergancy_contct)
    EditText edt_emergancy_contct;
    @BindView(R.id.tap_plumber)
    EditText tap_plumber;
    private String img1, img2, img3, img4, img5;
    private File imagePath1 = null;
    private File imagePath2 = null;
    private File imagePath3 = null;
    private File imagePath4 = null;
    private File imagePath5 = null;
    private int START_VERIFICATION = 1001;
    private File fileFlyer;
    private String[] industry = {"Industry", "Chemical", "Automobile", "IT"};


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
    private String[] section = {"Section", "Dummy Section1", "Dummy Section2", "Dummy Section3"};
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
    private String[] professionalList = {"Professional", "Chemical Professional", "Automobile Professional", "IT Professional"};
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
    private String[] OfficialGovID = {"Official Goverment ID", "Passport", "Driving License", "Adhar Card"};
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


    public ProfessionalFragment() {
        // Required empty public constructor
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_professional, container, false);
        ButterKnife.bind(this, view);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, industry);
        spinnerindustry.setAdapter(adapter);
        spinnerindustry.setOnItemSelectedListener(onItemSelectedListener);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, section);
        spinnersection.setAdapter(adapter1);
        spinnersection.setOnItemSelectedListener(onItemSelectedListener1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, professionalList);
        spinnerprofessional.setAdapter(adapter2);
        spinnerprofessional.setOnItemSelectedListener(onItemSelectedListener2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, OfficialGovID);
        spinnerGovermentID.setAdapter(adapter3);
        spinnerGovermentID.setOnItemSelectedListener(onItemSelectedListener3);


        callProfessionalDetailsApi();

        hideKeyboard(getActivity());

        return view;

    }

    @OnClick({R.id.rl_industry, R.id.rl_section, R.id.rl_professional,
            R.id.imageclick, R.id.ll_id1, R.id.ll_id2, R.id.ll_upload_certificate, R.id.ll_vehicle_license,
            R.id.rl_GovermentID, R.id.rl_update})
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
                selectImage(getActivity(), 111);
                break;
            case R.id.ll_id2:
                selectImage(getActivity(), 222);
                break;
            case R.id.imageclick:
                selectImage(getActivity(), 333);
                break;
            case R.id.ll_upload_certificate:
                selectImage(getActivity(), 444);
                break;
            case R.id.ll_vehicle_license:
                selectImage(getActivity(), 555);
                break;
            case R.id.rl_update:
                if (!Validation()) {
                    return;
                }
                callProfessionalWorkerService();
                break;


        }
    }

    public void selectImage(Activity activity, final int code) {
        final CharSequence[] items = {
//                getResources().getString(R.string.Take_Photo),
//                getResources().getString(R.string.Choose_from_Library),
                "take photo", "take libarary",
                getResources().getString(R.string.cancel)};


        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_imagecapture);


        TextView txt_takephoto = (TextView) dialog.findViewById(R.id.txt_takephoto);
        TextView txt_choosefromlibrary = (TextView) dialog.findViewById(R.id.txt_choosefromgallery);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_button);
        txt_takephoto.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), TakeImage.class);
            intent.putExtra("from", "camera");
            startActivityForResult(intent, code);
            dialog.dismiss();
        });
        txt_choosefromlibrary.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), TakeImage.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_VERIFICATION) {
            if (resultCode == getActivity().RESULT_OK) {
                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();
            }
        } else if (resultCode == getActivity().RESULT_OK) {
            if (data.getStringExtra("filePath") != null) {
                fileFlyer = new File(data.getStringExtra("filePath"));
                setImageToList(requestCode, fileFlyer);


            }
        } else if (requestCode == 1 && resultCode == getActivity().RESULT_CANCELED) {
            getActivity().finish();
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
                    iv_varify.setVisibility(View.VISIBLE);
                    iv_prf.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 444:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath4 = fileFlyer;
                    rl_upload_certificate.setVisibility(View.VISIBLE);
                    iv_upload_certificate.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 555:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath5 = fileFlyer;
                    rl_vehicle_license.setVisibility(View.VISIBLE);
                    iv_vehicle_license.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;

            default:
                break;
        }
    }

    private void callProfessionalDetailsApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ProfessionalWorkerReponse> beanCall = apiInterface.getProfessionalDetails(token, body);

                beanCall.enqueue(new Callback<ProfessionalWorkerReponse>() {
                    @Override
                    public void onResponse(Call<ProfessionalWorkerReponse> call, Response<ProfessionalWorkerReponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            ProfessionalWorkerReponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                about_txt.setText(response.body().getProfessionalWorkerInner().getProfessionalAboutUs());
                                tv_id.setText(response.body().getProfessionalWorkerInner().getProfessiona1PersonUniqueId());
                                tv_industry.setText(response.body().getProfessionalWorkerInner().getIndustry());
                                tv_section.setText(response.body().getProfessionalWorkerInner().getSection());
                                tv_professional.setText(response.body().getProfessionalWorkerInner().getProfessional());
                                edt_bank_ac.setText(response.body().getProfessionalWorkerInner().getProfessionalBankAc());
                                edt_emergancy_contct.setText(response.body().getProfessionalWorkerInner().getProfessionalEmergencyContact());
                                tap_plumber.setText(response.body().getProfessionalWorkerInner().getProfessionalTab());
                                tv_GovermentID.setText(response.body().getProfessionalWorkerInner().getOfficialGovntId());


                                rl_id_1.setVisibility(View.VISIBLE);
                                rl_id_2.setVisibility(View.VISIBLE);
                                rl_vehicle_license.setVisibility(View.VISIBLE);
                                rl_vehicle_license.setVisibility(View.VISIBLE);

                                if (response.body().getProfessionalWorkerInner().getProfessionalId1() != null && !response.body().getProfessionalWorkerInner().getProfessionalId1().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getProfessionalWorkerInner().getProfessionalId1()).into(iv_id1);
                                }
                                if (response.body().getProfessionalWorkerInner().getProfessionalId2() != null && !response.body().getProfessionalWorkerInner().getProfessionalId2().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getProfessionalWorkerInner().getProfessionalId2()).into(iv_id2);
                                }

                                if (response.body().getProfessionalWorkerInner().getProfession1Id3() != null && !response.body().getProfessionalWorkerInner().getProfession1Id3().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getProfessionalWorkerInner().getProfession1Id3()).into(iv_vehicle_license);
                                }

                                if (response.body().getProfessionalWorkerInner().getProfession1Id4() != null && !response.body().getProfessionalWorkerInner().getProfession1Id4().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getProfessionalWorkerInner().getProfession1Id4()).into(iv_upload_certificate);
                                }
                                if (response.body().getProfessionalWorkerInner().getProfessionalProfie() != null && !response.body().getProfessionalWorkerInner().getProfessionalProfie().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getProfessionalWorkerInner().getProfessionalProfie()).into(iv_prf);
                                }

                                img1 = response.body().getProfessionalWorkerInner().getProfessionalId1();
                                img2 = response.body().getProfessionalWorkerInner().getProfessionalId2();
                                img3 = response.body().getProfessionalWorkerInner().getProfession1Id3();
                                img4 = response.body().getProfessionalWorkerInner().getProfession1Id4();
                                img5 = response.body().getProfessionalWorkerInner().getProfessionalProfie();


                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfessionalWorkerReponse> call, Throwable t) {

                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Map<String, RequestBody> setUpMapData() throws JSONException {

        Map<String, RequestBody> fields = new HashMap<>();

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(getActivity()).getString(kToken));
        RequestBody aboutUs = RequestBody.create(MediaType.parse("text/plain"), about_txt.getText().toString());
        RequestBody officialGovntId = RequestBody.create(MediaType.parse("text/plain"), tv_GovermentID.getText().toString());
        RequestBody industry = RequestBody.create(MediaType.parse("text/plain"), tv_industry.getText().toString());
        RequestBody section = RequestBody.create(MediaType.parse("text/plain"), tv_section.getText().toString());
        RequestBody professional = RequestBody.create(MediaType.parse("text/plain"), tv_professional.getText().toString());
        RequestBody bankAc = RequestBody.create(MediaType.parse("text/plain"), edt_bank_ac.getText().toString());
        RequestBody emergencyContact = RequestBody.create(MediaType.parse("text/plain"), edt_emergancy_contct.getText().toString());
        RequestBody userType = RequestBody.create(MediaType.parse("text/plain"), "ProfessionalWorker");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
        RequestBody professionalTab = RequestBody.create(MediaType.parse("text/plain"), tap_plumber.getText().toString());
//        5c909915d52f1f70c756ca0b

        fields.put("token", token);
        fields.put("aboutUs", aboutUs);
        fields.put("officialGovntId", officialGovntId);
        fields.put("industry", industry);
        fields.put("section", section);
        fields.put("professional", professional);
        fields.put("bankAc", bankAc);
        fields.put("emergencyContact", emergencyContact);
        fields.put("userType", userType);
        fields.put("userId", userId);
        fields.put("professionalTab", professionalTab);
        fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE)));


        return fields;
    }

    private void callProfessionalWorkerService() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);
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

                if (imagePath3 != null) {
                    profile_body = RequestBody.create(MediaType.parse("image/*"), imagePath3);
                    prfpic = MultipartBody.Part.createFormData("profilePic", imagePath3.getName(), profile_body);

                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.updateProfessionalPerson(setUpMapData(), Id1, Id2, Id3, Id4, prfpic);

                } else {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.updateProfessionalPersonwithoutPic(setUpMapData(), Id1, Id2, Id3, Id4);
                    //prfpic = MultipartBody.Part.createFormData("profilePic", "");
                }

                call.enqueue(new retrofit2.Callback<ProfessionalWorkerResponse>() {
                    @Override
                    public void onResponse(Call<ProfessionalWorkerResponse> call, Response<ProfessionalWorkerResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();

                        if (response.isSuccessful()) {

                            ProfessionalWorkerResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                getActivity().finish();

                                Toast.makeText(getActivity(), "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getActivity(), "" + signUpResponce.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfessionalWorkerResponse> call, Throwable t) {
                        t.printStackTrace();
                        MyDialog.getInstance(getActivity()).hideDialog();
                        String s = "";
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean Validation() {
        if (img1 == null) {
            Toast.makeText(getActivity(), "Please upload ID1", Toast.LENGTH_SHORT).show();
            return false;
        } else if (img2 == null) {
            Toast.makeText(getActivity(), "Please upload ID2", Toast.LENGTH_SHORT).show();
            return false;
        } else if (img5 == null) {
            Toast.makeText(getActivity(), "Please upload profile pic", Toast.LENGTH_SHORT).show();
            return false;

        } else if (about_txt.getText().toString().isEmpty() || about_txt.getText().toString().trim().equalsIgnoreCase("")) {

            Toast.makeText(getActivity(), "Please write about something", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tv_industry.getText().toString().isEmpty() || tv_industry.getText().toString().equalsIgnoreCase("")) {

            Toast.makeText(getActivity(), "Please enter industry", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tap_plumber.getText().toString().isEmpty() || tap_plumber.getText().toString().trim().equalsIgnoreCase("")) {

            Toast.makeText(getActivity(), "Please enter professional tab plumber", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tv_section.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Please enter section", Toast.LENGTH_SHORT).show();

            return false;

        } else if (tv_professional.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Please enter professional", Toast.LENGTH_SHORT).show();

            return false;
        } else if (edt_emergancy_contct.getText().toString().trim().isEmpty()) {

            Toast.makeText(getActivity(), "Please provide emergancy contact", Toast.LENGTH_SHORT).show();

            return false;

        } else if (img4 == null) {

            Toast.makeText(getActivity(), "Please upload ID4", Toast.LENGTH_SHORT).show();
            return false;
        } else if (img5 == null) {
            Toast.makeText(getActivity(), "Please upload ID1", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

}
