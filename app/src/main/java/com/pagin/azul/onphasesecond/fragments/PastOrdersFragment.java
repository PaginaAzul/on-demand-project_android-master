package com.pagin.azul.onphasesecond.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.activity.MyCartActivity;
import com.pagin.azul.onphasesecond.activity.ScheduleMyCart;
import com.pagin.azul.onphasesecond.adapters.OnGoingOrderAdapter;
import com.pagin.azul.onphasesecond.adapters.PreviousAdapter;
import com.pagin.azul.onphasesecond.model.MyOrderModel;
import com.pagin.azul.onphasesecond.model.MyOrderResponse;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONArray;
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

public class PastOrdersFragment extends Fragment implements CommonListener {

    public PastOrdersFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.recyclerPrevious)
    RecyclerView recyclerPrevious;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private Context mContext;
    private String token;
    private String userId;
    private String langCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_past_orders_fragment, container, false);
        ButterKnife.bind(this,view);
        mContext = getContext();
        token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(mContext).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        serviceGetUserOrder();
    }

    public void serviceGetUserOrder() {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("type", ParamEnum.PAST.theValue());
            jsonObject.put("langCode", langCode);


            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<MyOrderModel> beanCall = apiInterface.getUserOrder(token,body);
            beanCall.enqueue(new Callback<MyOrderModel>() {
                @Override
                public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        MyOrderModel model = response.body();
                        if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                            setUpRecylerView(model.getData());

                        } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(getActivity());
                        } else {
                            Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<MyOrderModel> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpRecylerView(ArrayList<MyOrderResponse> data) {
        if(data!=null && !data.isEmpty()){
            tvNoData.setVisibility(View.GONE);
            recyclerPrevious.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerPrevious.setAdapter(new PreviousAdapter(mContext,data,this));
        }else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onReorderClick(int position, ArrayList<MyOrderResponse> data) {
        try {
            MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

            ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            ArrayList<MyOrderResponse> menuDataList = data.get(position).getOrderData();
            JSONArray jsonArray = new JSONArray();
            for(int i=0;i<menuDataList.size();i++){
                JSONObject object = new JSONObject();
                object.put("productId",menuDataList.get(i).getProductId());
                object.put("quantity",menuDataList.get(i).getQuantity());
                jsonArray.put(object);
            }
            jsonObject.put("productData",jsonArray);
            jsonObject.put("langCode", langCode);


            MediaType json = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(json, jsonObject.toString());

            Call<RestaurantModel> beanCall = apiInterface.reOrder(token,body);
            beanCall.enqueue(new Callback<RestaurantModel>() {
                @Override
                public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                    MyDialog.getInstance(mContext).hideDialog();
                    if (response.isSuccessful()) {
                        RestaurantModel model = response.body();
                        if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                            if(data.get(position).getOrderType().equalsIgnoreCase(ParamEnum.PRODUCT.theValue())){
                                Intent intent = new Intent(mContext, ScheduleMyCart.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(mContext, MyCartActivity.class);
                                intent.putExtra(ParamEnum.ADDRESS.theValue(),data.get(position).getAddress());
                                intent.putExtra(ParamEnum.LAT.theValue(),data.get(position).getLatitude());
                                intent.putExtra(ParamEnum.LONG.theValue(),data.get(position).getLongitude());
                                intent.putExtra(ParamEnum.ID.theValue(),data.get(position).getResAndStoreId());
                                startActivity(intent);
                            }

                        } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                            CommonUtility.showDialog1(getActivity());
                        } else {
                            Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<RestaurantModel> call, Throwable t) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}