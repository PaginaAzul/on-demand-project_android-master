package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.NavDrawerActivity;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.adapters.RestroListingAdapter;
import com.pagin.azul.onphasesecond.adapters.SearchRestAndGroceryAdapter;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
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

public class ViewAllListing extends AppCompatActivity implements CommonListener {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvViewAll)
    RecyclerView rvViewAll;

    private String type="";
    private SearchRestAndGroceryAdapter searchRestAndGroceryAdapter;
    private ArrayList<RestaurantResponse> resAndStoreListing;
    private String langCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_listing);
        ButterKnife.bind(this);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);
        getIntentData();
        setToolbar();
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            type = getIntent().getStringExtra(ParamEnum.TYPE.theValue());
            resAndStoreListing = getIntent().getParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue());
            setUpRecyclerView(resAndStoreListing);
        }
    }

    private void setToolbar() {
        if(type.equals(ParamEnum.GROCERY.theValue()))
            CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.shops));
        else
            CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.restaurants));
    }

    private void setUpRecyclerView(ArrayList<RestaurantResponse> resAndStoreListing) {
        rvViewAll.setLayoutManager(new LinearLayoutManager(this));
        searchRestAndGroceryAdapter = new SearchRestAndGroceryAdapter(this,this,resAndStoreListing);
        rvViewAll.setAdapter(searchRestAndGroceryAdapter);
    }

    @Override
    public void onResAndStoreClick(int position, ArrayList<RestaurantResponse> restAndStoreList) {
        if(type.equals(ParamEnum.GROCERY.theValue())){
            Intent intent = new Intent(this, GroceryDetails.class);
            intent.putExtra(ParamEnum.DATA.theValue(),restAndStoreList.get(position));
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, RestaurantDetails.class);
            intent.putExtra(ParamEnum.DATA.theValue(),restAndStoreList.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onFavClick(int position, ArrayList<RestaurantResponse> list) {
        try {
            //MyDialog.getInstance(mContext).showDialog(getActivity());
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("resAndStoreId", list.get(position).get_id());
                jsonObject.put("langCode", langCode);

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
                                RestaurantResponse restaurantResponse = list.get(position);
                                if(restaurantResponse.isFav()){
                                    restaurantResponse.setFav(false);
                                }else{
                                    restaurantResponse.setFav(true);
                                }
                                setUpRecyclerView(list);

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ViewAllListing.this);
                            } else {
                                Toast.makeText(ViewAllListing.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
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