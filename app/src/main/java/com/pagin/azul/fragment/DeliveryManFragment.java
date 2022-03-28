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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.bean.DeliveryPersonResponse;
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

public class DeliveryManFragment extends Fragment {
    private String[] vehicle = {"Vehicle Type", "FourWheeler", "TwoWheeler"};
    @BindView(R.id.tv_vehicle_type)
    TextView vehicletype;
    @BindView(R.id.spinner_vehicle_type)
    Spinner spinnervehicle;

    @BindView(R.id.iv_id1)
    ImageView iv_id1;
    @BindView(R.id.iv_id2)
    ImageView iv_id2;
    @BindView(R.id.iv_prf)
    ImageView iv_prf;
    @BindView(R.id.iv_verify)
    ImageView iv_verify;
    @BindView(R.id.rl_id1)
    RelativeLayout rl_id1;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.tv_about_txt)
    EditText tv_about_txt;
    @BindView(R.id.edt_vehicle_no)
    EditText edt_vehicle_no;
    @BindView(R.id.iv_vehicle_license)
    ImageView iv_vehicle_license;
    @BindView(R.id.edt_insurance_no)
    EditText edt_insurance_no;
    @BindView(R.id.edt_bank_ac)
    EditText edt_bank_ac;
    @BindView(R.id.edt_emergecy_phn)
    EditText edt_emergecy_phn;
    @BindView(R.id.tv_imagaclick)
    TextView tv_imagaclick;

    @BindView(R.id.iv_insurance)
    ImageView iv_insurance;
    @BindView(R.id.rl_insurance)
    RelativeLayout rl_insurance;

    @BindView(R.id.rl_vehicle_license)
    RelativeLayout rl_vehicle_license;
    @BindView(R.id.rl_id2)
    RelativeLayout rl_id2;

    private File imagePath1 = null;
    private File imagePath2 = null;
    private File imagePath3 = null;
    private File imagePath4 = null;
    private File imagePath5 = null;
    private int START_VERIFICATION = 1001;
    private File fileFlyer;


    ///////////////////
    Spinner.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            } else {
                vehicletype.setText(vehicle[position]);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_man, container, false);
        ButterKnife.bind(this, view);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vehicle);
        spinnervehicle.setAdapter(adapter);
        spinnervehicle.setOnItemSelectedListener(onItemSelectedListener);

        getDeliveryDetailsApi();
        hideKeyboard(getActivity());

        return view;
    }


    @OnClick({R.id.ll_id1, R.id.ll_id2, R.id.tv_imagaclick, R.id.ll_upload_insurance, R.id.ll_vehicle_license,
            R.id.rl_vehicle_type, R.id.tv_vehicle_type, R.id.rl_update})
    void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_vehicle_type:
                spinnervehicle.performClick();
                break;
            case R.id.ll_id1:
                selectImage(getActivity(), 111);
                break;
            case R.id.ll_id2:
                selectImage(getActivity(), 222);
                break;
            case R.id.tv_imagaclick:
                selectImage(getActivity(), 333);
                break;
            case R.id.ll_vehicle_license:
                selectImage(getActivity(), 444);
                break;
            case R.id.ll_upload_insurance:
                selectImage(getActivity(), 555);
                break;
            case R.id.rl_update:
                if (!validation()) {
                    return;
                }
                callDeliveryPersonService();
                break;


        }
    }


    private boolean validation() {
        if (tv_about_txt.getText().toString().equalsIgnoreCase("") || tv_about_txt.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please write something about", Toast.LENGTH_SHORT).show();
            return false;
//        } else if (edt_vehicle_no.getText().toString().isEmpty()) {
//            Toast.makeText(getContext(), "Please provide vehicle number", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (edt_insurance_no.getText().toString().isEmpty()) {
//            Toast.makeText(getContext(), "Please provide insurance number", Toast.LENGTH_SHORT).show();
//            return false;
        } else if (edt_emergecy_phn.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Please provide emergency number", Toast.LENGTH_SHORT).show();
            return false;
//        } else if (edt_bank_ac.getText().toString().isEmpty()) {
//            Toast.makeText(getContext(), "Please provide account number", Toast.LENGTH_SHORT).show();
//            return false;
        }
        return true;
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
                    rl_id1.setVisibility(View.VISIBLE);
                    imagePath1 = fileFlyer;
                    iv_id1.setImageURI(Uri.fromFile(fileFlyer));


                }
                break;
            case 222:
                if (fileFlyer.exists() && fileFlyer != null) {
                    rl_id2.setVisibility(View.VISIBLE);
                    imagePath2 = fileFlyer;
                    iv_id2.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 333:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath3 = fileFlyer;
                    iv_verify.setVisibility(View.VISIBLE);
                    iv_prf.setImageURI(Uri.fromFile(fileFlyer));
                }
                break;
            case 444:
                if (fileFlyer.exists() && fileFlyer != null) {
                    imagePath4 = fileFlyer;
                    rl_vehicle_license.setVisibility(View.VISIBLE);
                    iv_vehicle_license.setImageURI(Uri.fromFile(fileFlyer));
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

    private void getDeliveryDetailsApi() {
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

                Call<DeliveryPersonResponse> beanCall = apiInterface.getDeliveryDetails(body);

                beanCall.enqueue(new Callback<DeliveryPersonResponse>() {
                    @Override
                    public void onResponse(Call<DeliveryPersonResponse> call, Response<DeliveryPersonResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            DeliveryPersonResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {


                                tv_about_txt.setText(response.body().getDeliveryPersonInner().getDeliveryPAboutUs());
                                tv_id.setText(response.body().getDeliveryPersonInner().getDeliveryPersonUniqueId());
                                vehicletype.setText(response.body().getDeliveryPersonInner().getVehicleType());
                                edt_vehicle_no.setText(response.body().getDeliveryPersonInner().getVehicleNumber());
                                edt_insurance_no.setText(response.body().getDeliveryPersonInner().getInsuranceNumber());
                                edt_bank_ac.setText(response.body().getDeliveryPersonInner().getDeliveryPBankAC());
                                edt_emergecy_phn.setText(response.body().getDeliveryPersonInner().getDeliveryPEmergencyContact());


                                rl_id1.setVisibility(View.VISIBLE);
                                rl_id2.setVisibility(View.VISIBLE);
                                rl_vehicle_license.setVisibility(View.VISIBLE);
                                rl_insurance.setVisibility(View.VISIBLE);


                                if (response.body().getDeliveryPersonInner().getDeliveryPId2() != null && !response.body().getDeliveryPersonInner().getDeliveryPId2().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getDeliveryPersonInner().getDeliveryPId2()).into(iv_id2);
                                }

                                if (response.body().getDeliveryPersonInner().getVehicleLicense() != null && !response.body().getDeliveryPersonInner().getVehicleLicense().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getDeliveryPersonInner().getVehicleLicense()).into(iv_vehicle_license);
                                }

                                if (response.body().getDeliveryPersonInner().getDeliverPId1() != null && !response.body().getDeliveryPersonInner().getDeliverPId1().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getDeliveryPersonInner().getDeliverPId1()).into(iv_id1);
                                }
                                if (response.body().getDeliveryPersonInner().getUploadedInsurance() != null && !response.body().getDeliveryPersonInner().getUploadedInsurance().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getDeliveryPersonInner().getUploadedInsurance()).into(iv_insurance);
                                }
                                if (response.body().getDeliveryPersonInner().getDeliveryPProfilePic() != null && !response.body().getDeliveryPersonInner().getDeliveryPProfilePic().equalsIgnoreCase("")) {
                                    Picasso.with(getActivity()).load(response.body().getDeliveryPersonInner().getDeliveryPProfilePic()).into(iv_prf);
                                }


                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<DeliveryPersonResponse> call, Throwable t) {

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
        RequestBody aboutUs = RequestBody.create(MediaType.parse("text/plain"), tv_about_txt.getText().toString());
        RequestBody vehicleNumber = RequestBody.create(MediaType.parse("text/plain"), edt_vehicle_no.getText().toString());
        RequestBody vehicleType = RequestBody.create(MediaType.parse("text/plain"), vehicletype.getText().toString());
        RequestBody isuranceNumber = RequestBody.create(MediaType.parse("text/plain"), edt_insurance_no.getText().toString());
        RequestBody bankAc = RequestBody.create(MediaType.parse("text/plain"), edt_bank_ac.getText().toString());
        RequestBody emergencyContact = RequestBody.create(MediaType.parse("text/plain"), edt_emergecy_phn.getText().toString());
        RequestBody userType = RequestBody.create(MediaType.parse("text/plain"), "DeliveryPersion");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));

        fields.put("aboutUs", aboutUs);
        fields.put("vehicleNumber", vehicleNumber);
        fields.put("vehicleType", vehicleType);
        fields.put("insuranceNumber", isuranceNumber);
        fields.put("bankAC", bankAc);
        fields.put("emergencyContact", emergencyContact);
        fields.put("userType", userType);
        fields.put("userId", userId);
        fields.put("langCode", RequestBody.create(MediaType.parse("text/plain"), SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE)));


        return fields;
    }


    private void callDeliveryPersonService() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);
            Call<ProfessionalWorkerResponse> call;
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

                if (imagePath3 != null) {
                    profile_body = RequestBody.create(MediaType.parse("image/*"), imagePath3);
                    prfpic = MultipartBody.Part.createFormData("profilePic", imagePath3.getName(), profile_body);

                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.updateDeliveryPerson(setUpMapData(), Id1, Id2, vehicleLicense, insurance, prfpic);
                } else {
                    ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                    call = apiInterface.updateDeliveryPersonwithoutPic(setUpMapData(), Id1, Id2, vehicleLicense, insurance);
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


}
