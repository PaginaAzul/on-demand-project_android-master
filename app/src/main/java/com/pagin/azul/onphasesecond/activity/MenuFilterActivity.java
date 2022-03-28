package com.pagin.azul.onphasesecond.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.onphasesecond.bottomsheet.RestroInfoBottomSheet;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pagin.azul.Constant.Constants.kLat;
import static com.pagin.azul.Constant.Constants.kLong;
import static com.pagin.azul.Constant.Constants.kToken;
import static com.pagin.azul.Constant.Constants.kUserId;

public class MenuFilterActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tasteCB)
    CheckBox tasteCB;

    @BindView(R.id.TypeCB)
    CheckBox TypeCB;

    @BindView(R.id.EatTypeCB)
    CheckBox EatTypeCB;

    @BindView(R.id.sweetRB)
    RadioButton sweetRB;

    @BindView(R.id.saltyRB)
    RadioButton saltyRB;

    @BindView(R.id.mixedRB)
    RadioButton mixedRB;

    @BindView(R.id.vegRB)
    RadioButton vegRB;

    @BindView(R.id.noVegRB)
    RadioButton noVegRB;

    @BindView(R.id.LunchRB)
    RadioButton LunchRB;

    @BindView(R.id.breakFastRB)
    RadioButton breakFastRB;

    @BindView(R.id.dinnerRB)
    RadioButton dinnerRB;

    private String resAndStoreId;
    private String cuisine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_filter);
        ButterKnife.bind(this);
        CommonUtilities.setToolbar(this,mainToolbar,tvTitle,getString(R.string.filter));
        getIntentData();
    }

    @OnClick({R.id.applyFilterBtn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.applyFilterBtn:
                serviceFilterApi();
                break;
        }
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            resAndStoreId = getIntent().getStringExtra(ParamEnum.ID.theValue());
            cuisine = getIntent().getStringExtra(ParamEnum.CUISINE.theValue());
            tasteCB.setChecked(getIntent().getBooleanExtra("taste",false));
            TypeCB.setChecked(getIntent().getBooleanExtra("type",false));
            EatTypeCB.setChecked(getIntent().getBooleanExtra("eatType",false));
            sweetRB.setChecked(getIntent().getBooleanExtra("sweet",false));
            saltyRB.setChecked(getIntent().getBooleanExtra("salty",false));
            mixedRB.setChecked(getIntent().getBooleanExtra("mixed",false));
            vegRB.setChecked(getIntent().getBooleanExtra("veg",false));
            noVegRB.setChecked(getIntent().getBooleanExtra("nonVeg",false));
            LunchRB.setChecked(getIntent().getBooleanExtra("lunch",false));
            breakFastRB.setChecked(getIntent().getBooleanExtra("breakfast",false));
            dinnerRB.setChecked(getIntent().getBooleanExtra("dinner",false));
        }
    }

    private void serviceFilterApi(){
        try {
            MyDialog.getInstance(this).showDialog(this);
            String token = SharedPreferenceWriter.getInstance(this).getString(kToken);
            //if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                if (!token.isEmpty())
                    jsonObject.put("userId", SharedPreferenceWriter.getInstance(this).getString(kUserId));
                jsonObject.put("latitude", SharedPreferenceWriter.getInstance(this).getString(kLat));
                jsonObject.put("longitude", SharedPreferenceWriter.getInstance(this).getString(kLong));
                jsonObject.put("langCode", SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE));
                jsonObject.put("type", "Food");
                jsonObject.put("sweet", sweetRB.isChecked());
                jsonObject.put("salty", saltyRB.isChecked());
                jsonObject.put("mixed", mixedRB.isChecked());
                jsonObject.put("veg", vegRB.isChecked());
                jsonObject.put("nonVeg", noVegRB.isChecked());
                jsonObject.put("lunch", LunchRB.isChecked());
                jsonObject.put("breakfast", breakFastRB.isChecked());
                jsonObject.put("dinner", dinnerRB.isChecked());
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("cuisine", cuisine);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.fiterApi(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        MyDialog.getInstance(MenuFilterActivity.this).hideDialog();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                Intent intent = new Intent();
                                intent.putParcelableArrayListExtra(ParamEnum.DATA_LIST.theValue(),model.getData());
                                intent.putExtra("sweet",sweetRB.isChecked());
                                intent.putExtra("salty",saltyRB.isChecked());
                                intent.putExtra("mixed",mixedRB.isChecked());
                                intent.putExtra("veg",vegRB.isChecked());
                                intent.putExtra("nonVeg",noVegRB.isChecked());
                                intent.putExtra("lunch",LunchRB.isChecked());
                                intent.putExtra("breakfast",breakFastRB.isChecked());
                                intent.putExtra("dinner",dinnerRB.isChecked());
                                intent.putExtra("taste",tasteCB.isChecked());
                                intent.putExtra("type",TypeCB.isChecked());
                                intent.putExtra("eatType",EatTypeCB.isChecked());
                                setResult(Activity.RESULT_OK,intent);
                                finish();

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(MenuFilterActivity.this);
                            } else {
                                Toast.makeText(MenuFilterActivity.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FavoriteModel> call, Throwable t) {
                    }
                });
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}