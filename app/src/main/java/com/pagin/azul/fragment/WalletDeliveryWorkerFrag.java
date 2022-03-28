package com.pagin.azul.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.activities.ContactAdmin;
import com.pagin.azul.adapter.WalletTranstionHistoryAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletDeliveryWorkerFrag extends Fragment {

    //    private ProWorkerDeliveryAdapter historyAdapter;
    private WalletTranstionHistoryAdapter walletAdapter;
    private ArrayList<NormalUserPendingOrderInner> walletList;

    @BindView(R.id.rv_delivery_history)
    RecyclerView rv_delivery_history;

    @BindView(R.id.rl_add_money)
    RelativeLayout rlAddMoney;

    @BindView(R.id.rl_manage_mycard)
    RelativeLayout rlManageMyCard;

    @BindView(R.id.rl_our_bank_account)
    RelativeLayout rlOurBankAc;

    @BindView(R.id.rl_contact_admin)
    RelativeLayout rlContactAdmin;

    @BindView(R.id.tv_order_count)
    TextView tv_order_count;

    private String yearSelected;
    private String monthSelected;
    private int monthPos = 0;
    private int yearPos;

    private Spinner year, month;
    private int sYear, sMonth;
    private boolean isMntSelected = false;
    private boolean isYrsSelected = false;

    public WalletDeliveryWorkerFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet_delivery_worker, container, false);
        ButterKnife.bind(this, view);

        walletList = new ArrayList<>();
        isMntSelected = false;
        isYrsSelected = false;

        callInvoicDetailsApi();
        return view;

    }

    @OnClick({R.id.rl_add_money, R.id.rl_manage_mycard, R.id.rl_our_bank_account, R.id.rl_contact_admin,
            R.id.rl_our_bank, R.id.rl_myBank, R.id.iv_filter})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_money:
                showDialogCommingSoon();
                //startActivity(AddMoney.getIntent(getActivity()));
                break;
            case R.id.rl_manage_mycard:
                showDialogCommingSoon();
                //startActivity(WalletManageCard.getIintent(getActivity()));
                break;
            case R.id.rl_our_bank_account:
                showDialogCommingSoon();
                //startActivity(PayYourCredit.getIntent(getActivity()));
                break;
            case R.id.rl_myBank:
                showDialogCommingSoon();
                break;
            case R.id.rl_our_bank:
                showDialogCommingSoon();
                break;
            case R.id.rl_contact_admin:
                startActivity(ContactAdmin.getIntent(getActivity()));
                break;
            case R.id.iv_filter:
                bottomSheetOpen();
                break;

        }
    }

    private void callInvoicDetailsApi() {
        try {
            MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(getActivity()).getString(kToken);

            if (!token.isEmpty()) {
                RequestBody profile_body;
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(getActivity()).getString(kUserId));
                jsonObject.put("userType", "DeliveryPerson");
                if (isMntSelected) {
                    jsonObject.put("invoiceMonth", sMonth);
                }
                if (isYrsSelected) {
                    jsonObject.put("invoiceYear", Integer.parseInt(year.getSelectedItem().toString()));
                }
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));
                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());
                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.getInvoicDetails(token, body);
                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                walletList.clear();
                                tv_order_count.setText(response.body().getOrder());
                                if (response.body().getDataList() != null && response.body().getDataList().size() > 0) {
                                    walletList = response.body().getDataList();
                                    rv_delivery_history.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    walletAdapter = new WalletTranstionHistoryAdapter(getActivity(), walletList);
                                    rv_delivery_history.setAdapter(walletAdapter);
                                }else {
                                    //walletAdapter.notifyDataSetChanged();
                                }
                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void showDialogCommingSoon() {
        Dialog dialog = new Dialog(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.comming_soon, null, false);
        dialog.setContentView(view);

        Button btn_ok = view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void bottomSheetOpen() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = this.getLayoutInflater().inflate(R.layout.wallet_filtter_option_show_layout, null);
        bottomSheetDialog.setContentView(sheetView);

        isMntSelected = false;
        isYrsSelected = false;

        year = sheetView.findViewById(R.id.tv_year);
        month = sheetView.findViewById(R.id.tv_month);
        TextView tvApply = sheetView.findViewById(R.id.tv_apply);
        TextView tvClose = sheetView.findViewById(R.id.tv_close);

        initView();


        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callInvoicDetailsApi();
                bottomSheetDialog.dismiss();
            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }


    public void initView() {

        //YEARS

        setUpYearSpinner();
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected = parent.getItemAtPosition(position).toString();
                yearPos = position;
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //MONTHS
        // month_edt = (Spinner) findViewById(R.id.month_edt);
        setUpMonthSpinner();
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthSelected = parent.getItemAtPosition(position).toString();
                monthPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void setUpYearSpinner() {
        String[] spinnerItems = new String[]{
                "Select Year",
                "2019",
                "2020",
                "2021",
                "2022",
                "2023",
                "2024",
                "2025",
                "2026",
                "2028",
                "2029",
                "2030",
                "2031",
                "2032",
                "2033",
                "2034",
                "2035",
                "2036",
                "2037",
                "2038",
                "2039",
                "2040",
                "2041",
                "2042",
                "2043",
                "2044",
                "2045",
                "2046",
                "2047",
                "2048",
                "2049",
                "2050"};


        List<String> spinnerList = new ArrayList<>(Arrays.asList(spinnerItems));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.spinner_drop_singlerow, spinnerList) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            //Disable the first item of spinner.
                            Log.i(TAG, "Position[0]: Spinner first item is disabled");
                            return false;
                        } else {
                            String itemSelected = "Month" + year.getItemAtPosition(position).toString();
                            isYrsSelected = true;
                            Log.i(TAG, "Selected Item[" + position + "]: " + itemSelected);
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                        View spinnerView = super.getDropDownView(position, convertView, parent);

                        TextView spinnerTextV = (TextView) spinnerView;
                        if (position == 0) {
                            //Set the disable spinner item color fade .
                            spinnerTextV.setTextColor(Color.parseColor("#a7a7a6"));
                        } else {
                            //#404040
                            spinnerTextV.setTextColor(Color.parseColor("#b2000000"));
                        }
                        return spinnerView;
                    }
                };

        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_singlerow);

        year.setAdapter(arrayAdapter);

    }

    private void setUpMonthSpinner() {
        String[] spinnerItems = new String[]{
                "Select Month",
                "JANUARY",
                "FEBRUARY",
                "MARCH",
                "APRIL",
                "MAY",
                "JUNE",
                "JULY",
                "AUGUST",
                "SEPTEMBER",
                "OCTOBER",
                "NOVEMBER",
                "DECEMBER"};


        List<String> spinnerList = new ArrayList<>(Arrays.asList(spinnerItems));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.spinner_drop_singlerow, spinnerList) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            //Disable the first item of spinner.
                            Log.i(TAG, "Position[0]: Spinner first item is disabled");
                            return false;
                        } else {
                            sMonth = position;
                            String itemSelected = month.getItemAtPosition(position).toString();
                            isMntSelected = true;
                            Log.i(TAG, "Selected Item[" + position + "]: " + itemSelected);
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                        View spinnerView = super.getDropDownView(position, convertView, parent);

                        TextView spinnerTextV = (TextView) spinnerView;
                        if (position == 0) {
                            //Set the disable spinner item color fade .
                            spinnerTextV.setTextColor(Color.parseColor("#a7a7a6"));
                        } else {
                            //#404040
                            spinnerTextV.setTextColor(Color.parseColor("#b2000000"));
                        }
                        return spinnerView;
                    }
                };

        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_singlerow);

        month.setAdapter(arrayAdapter);


    }

}
