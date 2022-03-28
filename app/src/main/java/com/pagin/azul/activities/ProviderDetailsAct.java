package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.adapter.PreviousWorkAdapter;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.utils.CommonUtility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pagin.azul.Constant.Constants.kCategoryName;

public class ProviderDetailsAct extends AppCompatActivity implements CommonListener {

    @BindView(R.id.tvTitleP)
    TextView tvTitleP;

    @BindView(R.id.tvDistance)
    TextView tvDistance;

    @BindView(R.id.tvProviderName)
    TextView tvProviderName;

    @BindView(R.id.tvAllRatings)
    TextView tvAllRatings;

    @BindView(R.id.tv_rat_num)
    TextView tv_rat_num;

    @BindView(R.id.tv_dchargs)
    TextView tv_dchargs;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.tv_dTime)
    TextView tv_dTime;

    @BindView(R.id.tvCategory)
    TextView tvCategory;

    @BindView(R.id.tvModeOfTransport)
    TextView tvModeOfTransport;

    @BindView(R.id.ivProviderPic)
    ImageView ivProviderPic;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvPreviousWork)
    RecyclerView rvPreviousWork;

    private NormalUserPendingOrderInner providerDetails;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);
        ButterKnife.bind(this);
        gpsTracker = new GPSTracker(this,this);
        setToolbar();
        getIntentData();
    }

    @OnClick({R.id.ivBack,R.id.tvDistance})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvDistance:
                startActivity(ActiveTrackingActivity.getIntent(this, providerDetails, "FromNUActive"));
                break;
        }
    }

    private void getIntentData() {
        if(getIntent().getExtras()!=null){
            providerDetails = (NormalUserPendingOrderInner) getIntent().getSerializableExtra(Constants.kData);
            if(providerDetails!=null){
                Glide.with(this)
                        .load(providerDetails.getOfferAcceptedByProfilePic())
                        .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                                .error(R.drawable.profile_default))
                        .into(ivProviderPic);
                tvDistance.setText(providerDetails.getCurrentToPicupLocation()+" Km");
                /*try{
                    double time = distance(gpsTracker.getLatitude(),gpsTracker.getLongitude(),
                            providerDetails.getLocationProvider().getCoordinates().get(1),providerDetails.getLocationProvider().getCoordinates().get(0));
                    DecimalFormat df = new DecimalFormat("#.##");
                    time = Double.parseDouble(df.format(time));
                    tvDistance.setText(time+" Km");
                    providerDetails.setPickupToDropLocation(String.valueOf(time));
                }catch (Exception e){
                    e.printStackTrace();
                }*/
                tvProviderName.setText(providerDetails.getOfferMakeByName());
                tvAllRatings.setText(providerDetails.getTotalRating() + " "+getString(R.string.rating));
                tv_rat_num.setText(providerDetails.getAvgRating());
                String currency = providerDetails.getCurrency()!=null?providerDetails.getCurrency():"";
                tv_dchargs.setText(providerDetails.getMinimumOffer() + " "+currency+" "+getString(R.string.only));
                tv_msg.setText(providerDetails.getMessage());
                tv_dTime.setText(providerDetails.getApprxTime());
                tvCategory.setText(SharedPreferenceWriter.getInstance(this).getString(kCategoryName));
                //tvModeOfTransport.setText(providerDetails.getTransportMode());
                String transportMode = providerDetails.getTransportMode();
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                    if(transportMode.equalsIgnoreCase("Carro próprio"))
                        tvModeOfTransport.setText("Own Vehicle");
                    else if(transportMode.equalsIgnoreCase("Carro da empresa"))
                        tvModeOfTransport.setText("Company Vehicle");
                    else if(transportMode.equalsIgnoreCase("Sem transporte"))
                        tvModeOfTransport.setText("No Vehicle");
                    else
                        tvModeOfTransport.setText(transportMode);
                }else{
                    if(transportMode.equalsIgnoreCase("Own Vehicle"))
                        tvModeOfTransport.setText("Carro próprio");
                    else if(transportMode.equalsIgnoreCase("Company Vehicle"))
                        tvModeOfTransport.setText("Carro da empresa");
                    else if(transportMode.equalsIgnoreCase("No Vehicle"))
                        tvModeOfTransport.setText("Sem transporte");
                    else
                        tvModeOfTransport.setText(transportMode);
                    //holder.msg.setText(context.getString(R.string.require)+" "+activeList.get(position).getSeletTime());
                }
                ArrayList<String> workImages = providerDetails.getWorkImage();
                if(workImages!=null){
                    // set up recycler view data for previous work
                    rvPreviousWork.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                    rvPreviousWork.setAdapter(new PreviousWorkAdapter(this,workImages,this));
                }
            }
        }
    }

    public void setToolbar(){
        setSupportActionBar(toolbar);
        tvTitleP.setText(R.string.provider_details);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onImageClick(String workImage) {
        showFullImage(workImage);
    }

    private void showFullImage(String img) {
        final Dialog dialog1 = new Dialog(this, R.style.ThemeDialogCustom);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.image_show_layout);

        ImageView yes_btn = (ImageView) dialog1.findViewById(R.id.img_main_iv);
        ImageView no_btn = (ImageView) dialog1.findViewById(R.id.img_close_iv);


//        aQuery.id(yes_btn).image(img, false, false);

        /*Glide.with(this)
                .load(img)
                .apply(new RequestOptions().placeholder(R.drawable.default_p))
                .into(yes_btn);*/

        Glide.with(this)
                .load(img)
                .apply(RequestOptions.placeholderOf(R.drawable.spinner)
                        .error(R.drawable.default_image))
                .into(yes_btn);

        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.semitransparent)));

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();

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
