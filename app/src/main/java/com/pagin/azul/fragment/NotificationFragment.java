package com.pagin.azul.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.adapter.NotificationAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.bean.NormalUserPendingOrderResponse;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class NotificationFragment extends Fragment {
    private NotificationAdapter notiAdapter;
    private ArrayList<NormalUserPendingOrderInner> notiList;


    @BindView(R.id.no_data)
    TextView no_data;

    @BindView(R.id.rv_noti)
    RecyclerView rvNoti;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, NotificationFragment.class);
        return intent;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notification, container, false);

        ButterKnife.bind(this,view);
        //tvTitle.setText("Notification");

        notiList = new ArrayList<>();

//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });

        callNotificationListApi();


        return view;
    }


    private void callNotificationListApi() {
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

                Call<NormalUserPendingOrderResponse> beanCall = apiInterface.notificationList(token, body);

                beanCall.enqueue(new Callback<NormalUserPendingOrderResponse>() {
                    @Override
                    public void onResponse(Call<NormalUserPendingOrderResponse> call, Response<NormalUserPendingOrderResponse> response) {
                        MyDialog.getInstance(getActivity()).hideDialog();
                        if (response.isSuccessful()) {
                            NormalUserPendingOrderResponse signUpResponce = response.body();
                            if (signUpResponce.getStatus().equalsIgnoreCase("SUCCESS")) {
                                if (response.body().getPendingOrderInner().getOrderdocData() != null && response.body().getPendingOrderInner().getOrderdocData().size() > 0) {
                                    notiList = response.body().getPendingOrderInner().getOrderdocData();
                                    rvNoti.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    notiAdapter = new NotificationAdapter(getActivity(), notiList);
                                    rvNoti.setAdapter(notiAdapter);
                                    no_data.setVisibility(View.GONE);
                                    rvNoti.setVisibility(View.VISIBLE);
                                } else {
                                    no_data.setVisibility(View.VISIBLE);
                                    rvNoti.setVisibility(View.GONE);
                                }

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else if (response.body().getStatus().equalsIgnoreCase("FAILURE") && response.body().getMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
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
}
