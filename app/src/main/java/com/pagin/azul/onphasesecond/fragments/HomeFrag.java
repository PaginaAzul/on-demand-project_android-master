package com.pagin.azul.onphasesecond.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.pagin.azul.R;
import com.pagin.azul.bean.OrderResponse;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.activity.GroceryDetails;
import com.pagin.azul.onphasesecond.activity.MyFavoritesActivity;
import com.pagin.azul.onphasesecond.activity.RestaurantDetails;
import com.pagin.azul.onphasesecond.activity.ViewAllListing;
import com.pagin.azul.onphasesecond.adapters.HomeAdapter;
import com.pagin.azul.onphasesecond.adapters.RestroListingAdapter;
import com.pagin.azul.onphasesecond.model.ExclusiveOfferModel;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
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

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class HomeFrag extends Fragment implements CommonListener, SwipeRefreshLayout.OnRefreshListener {

    public HomeFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.recyclerHome)
    RecyclerView recyclerHome;

    @BindView(R.id.recyclerFilterHome)
    RecyclerView recyclerFilterHome;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.swipeRegresh)
    SwipeRefreshLayout swipeRegresh;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    private HomeAdapter homeAdapter;
    //private RestroListingAdapter adapter;
    private Context mContext;
    private ArrayList<ExclusiveOfferModel.Datum> offerList;
    private ArrayList<RestaurantResponse> nearByRestroList;
    private ArrayList<RestaurantResponse> nearByStoreList;
    private String lat;
    private String lon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        mContext = getContext();
        /*GPSTracker gpsTracker = new GPSTracker(mContext, getActivity());
        lat = String.valueOf(gpsTracker.getLatitude());
        lon = String.valueOf(gpsTracker.getLongitude());*/
        lat = SharedPreferenceWriter.getInstance(mContext).getString(kLat);
        lon = SharedPreferenceWriter.getInstance(mContext).getString(kLong);
        //mShimmerViewContainer.startShimmerAnimation();
        swipeRegresh.setOnRefreshListener(this);
        setUpRecyclerView();
        serviceGetExclusiveOfferList(lat,lon,true);
        return view;
    }

    private void setUpRecyclerView() {
        offerList = new ArrayList<>();
        nearByRestroList = new ArrayList<>();
        nearByStoreList = new ArrayList<>();

        recyclerHome.setLayoutManager(new LinearLayoutManager(mContext));
        homeAdapter=new HomeAdapter(mContext,this,offerList,nearByRestroList,nearByStoreList);
        recyclerHome.setAdapter(homeAdapter);

        /*recyclerFilterHome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestroListingAdapter(getContext());
        recyclerFilterHome.setAdapter(adapter);*/
    }

    @Override
    public void onViewMoreClick(int position) {
        if(position==1){
            Intent intent = new Intent(mContext, ViewAllListing.class);
            intent.putExtra(ParamEnum.TYPE.theValue(),ParamEnum.RESTAURANT.theValue());
            intent.putExtra(ParamEnum.DATA_LIST.theValue(),nearByRestroList);
            startActivity(intent);
        }else if(position==2){
            Intent intent = new Intent(mContext, ViewAllListing.class);
            intent.putExtra(ParamEnum.TYPE.theValue(),ParamEnum.GROCERY.theValue());
            intent.putExtra(ParamEnum.DATA_LIST.theValue(),nearByStoreList);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(int parentPosition, int childPosition) {
        if (parentPosition == 1) {
            Intent intent = new Intent(mContext, RestaurantDetails.class);
            intent.putExtra(ParamEnum.DATA.theValue(),nearByRestroList.get(childPosition));
            startActivity(intent);
        }else if (parentPosition == 2) {
            Intent intent = new Intent(mContext, GroceryDetails.class);
            intent.putExtra(ParamEnum.DATA.theValue(),nearByStoreList.get(childPosition));
            startActivity(intent);
        } else {
            if(offerList!=null && !offerList.isEmpty()){
                if(offerList.get(childPosition)!=null){
                    if(offerList.get(childPosition).getSellerData().getStoreType().equalsIgnoreCase("Restaurant")){
                        Intent intent = new Intent(mContext, RestaurantDetails.class);
                        //intent.putExtra(ParamEnum.DATA.theValue(),offerList.get(childPosition));
                        intent.putExtra("fromExclusiveOffer",true);
                        intent.putExtra("id",offerList.get(childPosition).getSellerData().getId());
                        startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent(mContext, GroceryDetails.class);
                        //intent.putExtra(ParamEnum.DATA.theValue(),offerList.get(childPosition).getResAndStoreId());
                        intent.putExtra("fromExclusiveOffer",true);
                        intent.putExtra("id",offerList.get(childPosition).getSellerData().getId());
                        startActivity(intent);
                    }
                }

            }
        }
    }

    public void serviceGetExclusiveOfferList(String lat, String lon, boolean isLoader) {
        try {
            this.lat = lat;
            this.lon = lon;
            if(isLoader){
                tvNoData.setVisibility(View.GONE);
                recyclerHome.setVisibility(View.GONE);
                /*mShimmerViewContainer.setVisibility(View.VISIBLE);
                mShimmerViewContainer.startShimmerAnimation();*/
                MyDialog.getInstance(mContext).showDialog(getActivity());
            }
            //MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();

                JSONObject jsonObject = new JSONObject();
                /*jsonObject.put("latitude", "30.688740750946053");
                jsonObject.put("longitude", "76.76381973706815");*/
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lon);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE));


                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ExclusiveOfferModel> beanCall = apiInterface.getExclusiveOfferList(token,body);
                beanCall.enqueue(new Callback<ExclusiveOfferModel>() {
                    @Override
                    public void onResponse(Call<ExclusiveOfferModel> call, Response<ExclusiveOfferModel> response) {
                        //MyDialog.getInstance(mContext).hideDialog();
                        if (response.isSuccessful()) {
                            ExclusiveOfferModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                offerList.clear();
                                offerList.addAll(model.getData());
                                serviceGetRestaurantAndStoreData(lat,lon);

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponseMessage().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                Toast.makeText(mContext, model.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ExclusiveOfferModel> call, Throwable t) {
                        Log.d(HomeFrag.class.getSimpleName(),"onFailure");
                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serviceGetRestaurantAndStoreData(String latitude, String longitude) {
        try {
            lat = latitude;
            lon = longitude;
            //recyclerHome.setVisibility(View.GONE);
            //mShimmerViewContainer.startShimmerAnimation();
            //MyDialog.getInstance(getActivity()).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);

                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", (SharedPreferenceWriter.getInstance(mContext).getString(kUserId).trim().isEmpty()?
                        null:SharedPreferenceWriter.getInstance(mContext).getString(kUserId)));
                /*jsonObject.put("latitude", "28.5355");
                jsonObject.put("longitude", "77.3910");*/
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.getRestaurantAndStoreData(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(mContext).hideDialog();
                        recyclerHome.setVisibility(View.VISIBLE);
                        /*mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);*/
                        swipeRegresh.setRefreshing(false);

                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                setData(restaurantModel.getData());

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                Toast.makeText(mContext, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void setData(RestaurantResponse data) {
        nearByRestroList.clear();
        nearByStoreList.clear();
        nearByRestroList.addAll(data.getRestaurantList());
        nearByStoreList.addAll(data.getStoreList());
        homeAdapter.updateList(offerList,nearByRestroList,nearByStoreList);
        if(offerList.isEmpty() && nearByRestroList.isEmpty() && nearByStoreList.isEmpty())
            tvNoData.setVisibility(View.VISIBLE);
        else
            tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        serviceGetExclusiveOfferList(lat,lon,false);
        //serviceGetRestaurantAndStoreData(lat,lon);
    }

    @Override
    public void onFavClick(int position, int homePosition, RestaurantResponse restaurantResponse) {
        try {
            //MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(mContext).getString(kToken);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(mContext).getString(kUserId));
                jsonObject.put("resAndStoreId", restaurantResponse.get_id());
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(mContext).getString(SharedPreferenceKey.LANGUAGE));

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.addToFavourite(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        //MyDialog.getInstance(mContext).hideDialog();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if(restaurantResponse.isFav()){
                                    if(homePosition==2)
                                        nearByStoreList.get(position).setFav(false);
                                    else
                                        nearByRestroList.get(position).setFav(false);
                                }else {
                                    if(homePosition==2)
                                        nearByStoreList.get(position).setFav(true);
                                    else
                                        nearByRestroList.get(position).setFav(true);
                                }
                                homeAdapter.updateList(offerList,nearByRestroList,nearByStoreList);

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(getActivity());
                            } else {
                                Toast.makeText(mContext, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FavoriteModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}