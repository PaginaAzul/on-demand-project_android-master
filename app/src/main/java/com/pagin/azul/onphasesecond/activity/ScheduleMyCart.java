package com.pagin.azul.onphasesecond.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.activities.ManageAddressActivity;
import com.pagin.azul.bean.AddressList;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.helper.GPSTracker;
import com.pagin.azul.onphasesecond.adapters.MenuDetailsAdapter;
import com.pagin.azul.onphasesecond.adapters.ProductAdapter;
import com.pagin.azul.onphasesecond.adapters.TimeSlotAdapter;
import com.pagin.azul.onphasesecond.model.FavoriteModel;
import com.pagin.azul.onphasesecond.model.ProductModel;
import com.pagin.azul.onphasesecond.model.ProductResponse;
import com.pagin.azul.onphasesecond.model.RestaurantModel;
import com.pagin.azul.onphasesecond.model.RestaurantResponse;
import com.pagin.azul.onphasesecond.utilty.CommonUtilities;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.pagin.azul.retrofit.ApiClientConnection;
import com.pagin.azul.retrofit.ApiInterface;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

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
import static com.pagin.azul.onphasesecond.utilty.CommonUtilities.getOutputFormats;

public class ScheduleMyCart extends AppCompatActivity implements CommonListener/*, DatePickerDialog.OnDateSetListener*/ {

    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.recyclerMyCart)
    RecyclerView recyclerMyCart;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.tvRestroAddress)
    TextView tvRestroAddress;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.tvDeliveryCharges)
    TextView tvDeliveryCharges;

    @BindView(R.id.tvTotalAmountPaid)
    TextView tvTotalAmountPaid;

    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tv_slotss)
    TextView tv_slot;



    @BindView(R.id.tv_dat)
    TextView tv_dat;
    @BindView(R.id.tv_days)
    TextView tv_days;
    @BindView(R.id.tv_tslot)
    TextView tv_tslot;
    @BindView(R.id.con_design)
    ConstraintLayout con_design;

    private TextView tvDate;
    private TextView tvMorningTime;
    private TextView tvAfternoonTime;
    private TextView tvTimeSlote;
    private TextView tvMorning;
    private TextView tvMorningTimeSlotNotFound;
    private TextView tvAfternoonTimeSlotNotFound;
    private TextView tvEveningTimeSlotNotFound;
    private Spinner spTimeSlot;

    private String type = "";
    private String token;
    private String userId;
    private String langCode;
    private ArrayList<RestaurantResponse> menuDataList;
    private ProductAdapter menuDetailsAdapter;
    private String latitude;
    private String longitude;
    private String deliveryLat;
    private String deliveryLon;
    private String landmark;
    private String buildingAndApart;
    private String excepetdDeliveryTime;
    private String deli_charge = "0";
    private String service_charge = "0";

    private String currency = "";
    private String address = "";
    private static final int ADDRESS_REQ = 31;
    private String month = "";
    private String years = "";
    private String day = "";
    private String lat="",lon="";
    String check="0";

    private int yyyy;
    private int mm;
    private int dd;
    private String resAndStoreId;
    private String weekDay,datesss;
    private String deliveryDate;
    private String deliverySlot;
    private String deliveryTimeSlot;
    private ArrayList<ProductResponse> daysList;
    private ArrayList<ProductResponse> weekList;
    private ArrayList<ProductResponse> morningList;
    private ArrayList<ProductResponse> afternoonList;
    private ArrayList<ProductResponse> eveningList;
    private TimeSlotAdapter morningAdapter;
    private TimeSlotAdapter afternoonAdapter;
    private TimeSlotAdapter eveningAdapter;
    private String minimumValue = "";
    private double totalAmount;
    private double total;

    AdapterView.OnItemSelectedListener timeSlotSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0)
                return;
            deliverySlot = daysList.get(position).getTimeSlot();
            tvTimeSlote.setText(deliverySlot);
            deliveryTimeSlot = daysList.get(position).getOpenTime() + " - " + daysList.get(position).getCloseTime();
            tvMorningTime.setText(deliveryTimeSlot);


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_my_cart);
        ButterKnife.bind(this);
        tvPay.setVisibility(View.VISIBLE);
        GPSTracker gpsTracker = new GPSTracker(ScheduleMyCart.this,ScheduleMyCart.this);
        lat = String.valueOf(gpsTracker.getLatitude());
        lon = String.valueOf(gpsTracker.getLongitude());
        con_design.setVisibility(View.GONE);
        CommonUtilities.setToolbar(this, mainToolbar, tvTitle, getString(R.string.my_cart));
        token = SharedPreferenceWriter.getInstance(this).getString(kToken);
        userId = SharedPreferenceWriter.getInstance(this).getString(kUserId);
        langCode = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE);


        setDaysList();
        getIntentData();
        setUpRecyclerView();
        serviceGetCartItem();



    }


    void checktime()

    {

        String string3= SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime);
        String string8= SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.ctime);

        if(!(string3.contains("AM")||string3.contains("PM")))
        {
            try {


                Date mToday = new Date();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String curTime = sdf.format(mToday);


                Date time1 = new SimpleDateFormat("HH:mm").parse(string3);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                Date time2 = new SimpleDateFormat("HH:mm").parse(string8);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);

                String someRandomTime = curTime;
                Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();
                if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                    check="1";
                    //checkes whether the current time is between 14:49:00 and 20:11:13.
                    System.out.println(true);
                }
                else
                {
                    check="0";

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else

        {
            try {



                String string1 = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime);

                Date time1 = new SimpleDateFormat("HH:mm aaa").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);
                calendar1.add(Calendar.DATE, 1);


                String string2 = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.ctime);
                Date time2 = new SimpleDateFormat("HH:mm aaa").parse(string2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);
                calendar2.add(Calendar.DATE, 1);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm aaa");
                Calendar calander = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aaa");


                String someRandomTime = simpleDateFormat.format(calander.getTime()).toString();
                Date d = new SimpleDateFormat("HH:mm aaa").parse(someRandomTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(d);
                calendar3.add(Calendar.DATE, 1);

                Date x = calendar3.getTime();


                try {
                    Date mToday = new Date();

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String curTime = sdf.format(mToday);
                    Date start = sdf.parse(string1);
                    Date end = sdf.parse(string2);
                    Date userDate = sdf.parse(curTime);

                    if(end.before(start))
                    {
                        Calendar mCal = Calendar.getInstance();
                        mCal.setTime(end);
                        mCal.add(Calendar.DAY_OF_YEAR, 1);
                        end.setTime(mCal.getTimeInMillis());
                    }

                    Log.d("curTime", userDate.toString());
                    Log.d("start", start.toString());
                    Log.d("end", end.toString());


                    if (userDate.after(start) && userDate.before(end)) {
                        check="1";
                    }
                    else{
                        check="0";
                    }
                } catch (ParseException e) {
                    // Invalid date was entered
                }






//                if(string1.contains("AM")&&string2.contains("AM"))
//                {
//
//                    String[] timeArray = string1.split(":");
//                    String[] timeArray1 = string2.split(":");
//                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
//
//
//                    int HH = Integer.parseInt(timeArray[0]);
//                    int HH1 = Integer.parseInt(timeArray1[0]);
//                    int HH2 = Integer.parseInt(timeArray2[0]);
//                    if(simpleDateFormat.format(calander.getTime()).toString().contains("PM"))
//                    {
//                        check="0";
//                    }
//                    else if(HH<=HH2&&HH1>=HH2)                    {
//                        check="1";
//                    }
//                    else
//                    {
//                        check="0";
//
//                    }
//                }
//                else if(string1.contains("PM")&&string2.contains("PM"))
//                {
//                    String[] timeArray = string1.split(":");
//                    String[] timeArray1 = string2.split(":");
//                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
//
//
//                    int HH = Integer.parseInt(timeArray[0]);
//                    int HH1 = Integer.parseInt(timeArray1[0]);
//                    int HH2 = Integer.parseInt(timeArray2[0]);
//                    Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();
//
//
//                    if(simpleDateFormat.format(calander.getTime()).toString().contains("AM"))
//                    {
//                        check="0";
//                    }
//
//
//                   else  if(HH<=HH2&&HH1>=HH2)                    {
//                        check="1";
//                    }
//                    else
//                    {
//                        check="0";
//
//                    }
//                }
//                else
//                {
//
//                    Toast.makeText(context, "yessss", Toast.LENGTH_SHORT).show();
//
//
//                    try {
//                        Date mToday = new Date();
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
//                        String curTime = sdf.format(mToday);
//                        Date start = sdf.parse(string1);
//                        Date end = sdf.parse(string2);
//                        Date userDate = sdf.parse(curTime);
//
//                        if(end.before(start))
//                        {
//                            Calendar mCal = Calendar.getInstance();
//                            mCal.setTime(end);
//                            mCal.add(Calendar.DAY_OF_YEAR, 1);
//                            end.setTime(mCal.getTimeInMillis());
//                        }
//
//                        Log.d("curTime", userDate.toString());
//                        Log.d("start", start.toString());
//                        Log.d("end", end.toString());
//
//
//                        if (userDate.after(start) && userDate.before(end)) {
//                            Toast.makeText(context, "yesss", Toast.LENGTH_SHORT).show();
//                            Log.d("result", "falls between start and end , go to screen 1 ");
//                        }
//                        else{
//                            Toast.makeText(context, "nooo", Toast.LENGTH_SHORT).show();
//
//                            Log.d("result", "does not fall between start and end , go to screen 2 ");
//                        }
//                    } catch (ParseException e) {
//                        // Invalid date was entered
//                    }
////                    Toast.makeText(context, "third", Toast.LENGTH_SHORT).show();
////
////                    String[] timeArray = string1.split(":");
////                    String[] timeArray1 = string2.split(":");
////                    String[] timeArray2 =simpleDateFormat.format(calander.getTime()).toString().split(":");
////
////
////                    int HH = Integer.parseInt(timeArray[0]);
////                    int HH1 = Integer.parseInt(timeArray1[0]);
////                    int HH2 = Integer.parseInt(timeArray2[0]);
////
////
////                    if(simpleDateFormat.format(calander.getTime()).toString().contains("AM")&&HH<=HH2)
////                    {
////                        check="1";
////                        Toast.makeText(context, "f", Toast.LENGTH_SHORT).show();
////                    }
////                    else if(simpleDateFormat.format(calander.getTime()).toString().contains("PM")&&HH1>=HH2)
////                    {
////                        check="1";
////
////                    }
////
////                    else   if(HH<=HH2&&HH1>=HH2)                    {
////                        check="1";
////                    }
////
////                    else
////                    {
////
////                        check="0";
////
////                    }
//                }
            } catch (ParseException e) {


                e.printStackTrace();
            }
        }

    }


    private boolean validations(){
        if (weekDay == null || weekDay.equals("")) {
            Toast.makeText(this, getString(R.string.please_choose_delivery_day), Toast.LENGTH_SHORT).show();
            return false;
        } else if (deliveryTimeSlot == null || deliveryTimeSlot.equals("")) {
            Toast.makeText(this, getString(R.string.please_select_time_slot), Toast.LENGTH_SHORT).show();
            return false;
        } if(minimumValue==null || minimumValue.isEmpty()){
            //Toast.makeText(this, getString(R.string.minimum_value_paid_value)+minimumValue, Toast.LENGTH_SHORT).show();
            return false;
        }else if (totalAmount < Double.parseDouble(minimumValue)) {
            Toast.makeText(this, getString(R.string.minimum_value_paid_value)+minimumValue, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!check.equals("1"))
        {
            Toast.makeText(this,"time slots are not valid",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getReminingTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }
    private String getReminingTimes() {
        String delegate = "hh:mm";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }
    private String getOpenTime(String time) {
        String delegate = "hh:mm";
        return (String) DateFormat.format(delegate, Long.parseLong(time));
    }
    private String getCloseTime(String tim) {
        String delegate = "hh:mm";
        return (String) DateFormat.format(delegate, Long.parseLong(tim));
    }



    @OnClick({R.id.tvChange, R.id.tvPay, R.id.tvScheduleDelivery, R.id.ivDistance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChange:
                Intent intent = new Intent(this, ManageAddressActivity.class);
                startActivityForResult(intent, ADDRESS_REQ);
                break;
            case R.id.tvPay:

                if(        SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime).isEmpty()|| SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.otime)==null)
                {
                    if(validation())
                        servicePlaceOrder();
                }
                else
                {
                    checktime();

                    if(validations())
                        servicePlaceOrder();
                }

                break;
            case R.id.tvScheduleDelivery:
                showScheduleDialog();
                break;
            case R.id.ivDistance:
                CommonUtilities.dispatchGoogleMap(this, latitude, longitude);
                break;
        }
    }

    private boolean validation() {
        if (weekDay == null || weekDay.equals("")) {
            Toast.makeText(this, getString(R.string.please_choose_delivery_day), Toast.LENGTH_SHORT).show();
            return false;
        } else if (deliveryTimeSlot == null || deliveryTimeSlot.equals("")) {
            Toast.makeText(this, getString(R.string.please_select_time_slot), Toast.LENGTH_SHORT).show();
            return false;
        } if(minimumValue==null || minimumValue.isEmpty()){
            //Toast.makeText(this, getString(R.string.minimum_value_paid_value)+minimumValue, Toast.LENGTH_SHORT).show();
            return false;
        }else if (totalAmount < Double.parseDouble(minimumValue)) {
            Toast.makeText(this, getString(R.string.minimum_value_paid_value)+minimumValue, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void setDaysList() {

        weekList = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 24 * i);
            calendar.add(Calendar.MINUTE, 0);
            calendar.add(Calendar.SECOND, 0);
            String timeSlot = dateFormat.format(calendar.getTime());
            String day = dayFormat.format(calendar.getTime());
            ProductResponse productResponse = new ProductResponse();
            productResponse.setTimeSlot(timeSlot);
            productResponse.setDay(day);
            weekList.add(productResponse);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setDay(getString(R.string.select));
        weekList.add(0, productResponse);


        /*final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = mdformat.format(new Date());

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("EEEE");
        String currentDay = mdformat.format(calendar.getTime());
        days.remove(currentDay);*/
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            type = getIntent().getStringExtra(ParamEnum.TYPE.theValue());
        }
        try {
            deliveryLat = SharedPreferenceWriter.getInstance(this).getString(kLat);
            deliveryLon = SharedPreferenceWriter.getInstance(this).getString(kLong);
            double lat = Double.parseDouble(deliveryLat);
            double lon = Double.parseDouble(deliveryLon);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            tvAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerView() {
        morningList = new ArrayList<>();
        afternoonList = new ArrayList<>();
        eveningList = new ArrayList<>();
        menuDataList = new ArrayList<>();
        recyclerMyCart.setLayoutManager(new LinearLayoutManager(this));
        menuDetailsAdapter = new ProductAdapter(this, this, menuDataList);
        recyclerMyCart.setAdapter(menuDetailsAdapter);
    }

    public void showOrderPlacedDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_order_placed);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView tvBackHome = dialog.findViewById(R.id.tvBackHome);
        TextView tvOkay = dialog.findViewById(R.id.tvOkay);
        if (type.equals(SubOfferActivity.class.getSimpleName())) {
            tvBackHome.setVisibility(View.VISIBLE);
            tvOkay.setText(R.string.shop_more_offer);
        }

        tvOkay.setOnClickListener(v -> finish());

        tvBackHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        dialog.show();
    }

    public void showScheduleDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_schedule_delivery);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //dialog.setCancelable(false);

        TextView tvOkay = dialog.findViewById(R.id.tvOkay);
        tvDate = dialog.findViewById(R.id.tvDate);
        tvDate.setText(weekDay);
        tvMorningTime = dialog.findViewById(R.id.tvMorningTime);
        tvMorningTime.setText(deliveryTimeSlot);
        tvAfternoonTime = dialog.findViewById(R.id.tvAfternoonTime);
        tvTimeSlote = dialog.findViewById(R.id.tvTimeSlote);
        tvTimeSlote.setText(deliverySlot);
        Spinner spDays = dialog.findViewById(R.id.spDays);
        spTimeSlot = dialog.findViewById(R.id.spTimeSlot);
        TextView tvAfterNoon = dialog.findViewById(R.id.tvAfterNoon);
        TextView tvEvening = dialog.findViewById(R.id.tvEvening);
        RecyclerView rvMorning = dialog.findViewById(R.id.rvMorning);
        RecyclerView rvAfternoon = dialog.findViewById(R.id.rvAfternoon);
        RecyclerView rvEvening = dialog.findViewById(R.id.rvEvening);
        ConstraintLayout clMorning = dialog.findViewById(R.id.clMorning);
        ConstraintLayout clAfternoon = dialog.findViewById(R.id.clAfternoon);
        ConstraintLayout clEvening = dialog.findViewById(R.id.clEvening);
        tvMorningTimeSlotNotFound = dialog.findViewById(R.id.tvMorningTimeSlotNotFound);
        tvAfternoonTimeSlotNotFound = dialog.findViewById(R.id.tvAfternoonTimeSlotNotFound);
        tvEveningTimeSlotNotFound = dialog.findViewById(R.id.tvEveningTimeSlotNotFound);

        // set morning adapter
        rvMorning.setLayoutManager(new GridLayoutManager(this, 2));
        morningAdapter = new TimeSlotAdapter(this, morningList, "Morning", this);
        rvMorning.setAdapter(morningAdapter);

        // set afternoon adapter
        rvAfternoon.setLayoutManager(new GridLayoutManager(this, 2));
        afternoonAdapter = new TimeSlotAdapter(this, afternoonList, "Afternoon", this);
        rvAfternoon.setAdapter(afternoonAdapter);

        // set evening adapter
        rvEvening.setLayoutManager(new GridLayoutManager(this, 2));
        eveningAdapter = new TimeSlotAdapter(this, eveningList, "Evening", this);
        rvEvening.setAdapter(eveningAdapter);

        showNoSlotFound();

        WeekSpinnerAdapter adapter = new WeekSpinnerAdapter(ScheduleMyCart.this,
                android.R.layout.simple_spinner_dropdown_item, weekList);
        spDays.setAdapter(adapter);

        //setSpinner(this,spDays,days);
        spDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                //String day = parent.getItemAtPosition(position).toString();
                weekDay = weekList.get(position).getDay();

                deliveryDate = weekList.get(position).getTimeSlot();
                tvDate.setText(weekDay);
                tvMorningTime.setText("");
                tvTimeSlote.setText("");
                deliveryTimeSlot = "";
                serviceGetDeliverySlotList(getEnglishFormatDay(weekDay));
                tvTimeSlote.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_ic, 0);
                /*clMorning.setVisibility(View.VISIBLE);
                tvAfterNoon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_ic, 0);
                clAfternoon.setVisibility(View.VISIBLE);
                tvEvening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_ic, 0);
                clEvening.setVisibility(View.VISIBLE);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvOkay.setOnClickListener(v -> {
            //if (validation())
            con_design.setVisibility(View.VISIBLE);
            tv_slot.setText(deliveryTimeSlot);
            tv_days.setText(weekDay);
            tv_dat.setText(deliveryDate);
            int val=0;

            for (int i = 0; i < morningList.size(); i++) {
                {
                    if(    morningList.get(i).isSelected())
                    {
                        val=1;
                    }
                }
            }


            for (int i = 0; i < afternoonList.size(); i++) {
                if( afternoonList.get(i).isSelected())
                {
                    val=2;
                }
            }

            for (int i = 0; i < eveningList.size(); i++) {
                if( eveningList.get(i).isSelected())
                {
                    val=3;
                }            }

            if(val==1)
            {
                tv_tslot.setText("Morning");

            }
            else if(val==2)
            {
                tv_tslot.setText("Afternoon");

            }
            else if(val==3)
            {
                tv_tslot.setText("Evening");

            }
            else
            {
                tv_tslot.setText("");

            }


            dialog.dismiss();
        });

        tvDate.setOnClickListener(v -> {
            /*if(!day.isEmpty() && !month.isEmpty() && !years.isEmpty())
                showDate(Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(years), R.style.NumberPickerStyle);
            else
                showDatePickerDialog();*/
            spDays.performClick();
        });

        tvTimeSlote.setOnClickListener(view -> {
            //spTimeSlot.performClick()
            if (clMorning.getVisibility() == View.GONE) {
                tvTimeSlote.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_ic, 0);
                clMorning.setVisibility(View.VISIBLE);
            } else {
                tvTimeSlote.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_icon, 0);
                clMorning.setVisibility(View.GONE);
            }

        });

        tvAfterNoon.setOnClickListener(view -> {
            if (clAfternoon.getVisibility() == View.GONE) {
                tvAfterNoon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_ic, 0);
                clAfternoon.setVisibility(View.VISIBLE);
            } else {
                tvAfterNoon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_icon, 0);
                clAfternoon.setVisibility(View.GONE);
            }
        });

        tvEvening.setOnClickListener(view -> {
            if (clEvening.getVisibility() == View.GONE) {
                tvEvening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_ic, 0);
                clEvening.setVisibility(View.VISIBLE);
            } else {
                tvEvening.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down_icon, 0);
                clEvening.setVisibility(View.GONE);
            }
        });

        /*tvMorningTime.setOnClickListener(view -> {
            showTimePicker(tvMorningTime);
        });

        tvAfternoonTime.setOnClickListener(view -> {
            showTimePicker(tvAfternoonTime);
        });*/

        dialog.show();
    }

    private void showNoSlotFound() {
        if (!morningList.isEmpty()) {
            tvMorningTimeSlotNotFound.setVisibility(View.GONE);
        } else {
            tvMorningTimeSlotNotFound.setVisibility(View.VISIBLE);
        }
        if (!afternoonList.isEmpty()) {
            tvAfternoonTimeSlotNotFound.setVisibility(View.GONE);
        } else {
            tvAfternoonTimeSlotNotFound.setVisibility(View.VISIBLE);
        }
        if (!eveningList.isEmpty()) {
            tvEveningTimeSlotNotFound.setVisibility(View.GONE);
        } else {
            tvEveningTimeSlotNotFound.setVisibility(View.VISIBLE);
        }
    }

    private String getEnglishFormatDay(String day) {
        switch (day) {
            case "segunda-feira":
            case "segunda":
            case "segunda feira":
                return "Monday";
            case "terça":
            case "terça-feira":
            case "terça feira":
                return "Tuesday";
            case "quarta":
            case "quarta-feira":
            case "quarta feira":
                return "Wednesday";
            case "quinta":
            case "quinta feira":
            case "quinta-feira":
                return "Thursday";
            case "sexta-feira":
            case "sexta feira":
            case "sexta":
                return "Friday";
            case "sábado":
                return "Saturday";
            case "domingo":
                return "Sunday";
            default:
                return day;
        }
    }

    private void serviceGetCartItem() {

        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<FavoriteModel> beanCall = apiInterface.getCartItem(token, body);

                beanCall.enqueue(new Callback<FavoriteModel>() {
                    @Override
                    public void onResponse(Call<FavoriteModel> call, Response<FavoriteModel> response) {
                        MyDialog.getInstance(ScheduleMyCart.this).hideDialog();
                        if (response.isSuccessful()) {
                            FavoriteModel model = response.body();
                            if (model.getStatus().equalsIgnoreCase("SUCCESS")) {

                                menuDataList.clear();
                                menuDataList.addAll(model.getData());
                                menuDetailsAdapter.notifyDataSetChanged();

                                if (menuDataList != null && !menuDataList.isEmpty()) {
                                    showNoDataText(View.VISIBLE, View.GONE);
                                    RestaurantResponse details = menuDataList.get(0);
                                    RestaurantResponse sellerData = details.getSellerData();
                                    tvRestroAddress.setText(sellerData.getAddress());
                                    latitude = sellerData.getLatitude();
                                    longitude = sellerData.getLongitude();
                                    excepetdDeliveryTime = sellerData.getDeliveryTime();
                                    resAndStoreId = details.getResAndStoreId();

                                    serviceGetDeliveryCharge(resAndStoreId);
                                } else {
                                    showNoDataText(View.GONE, View.VISIBLE);
                                }

                                setBillData();

                            } else if (model.getStatus().equalsIgnoreCase("FAILURE") && model.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ScheduleMyCart.this);
                            } else {
                                Toast.makeText(ScheduleMyCart.this, model.getResponse_message(), Toast.LENGTH_SHORT).show();
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

    private void serviceGetDeliveryCharge(String resAndStoreId) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("resAndStoreId", resAndStoreId);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.getDeliveryCharge(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(ScheduleMyCart.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                deli_charge = restaurantModel.getData().getDeliveryCharge();
                                currency = restaurantModel.getData().getCurrency();
                                minimumValue = restaurantModel.getData().getMinimumValue();
                                setBillData();


                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {

                                CommonUtility.showDialog1(ScheduleMyCart.this);
                            } else {
                                tvPay.setVisibility(View.GONE);

                                Toast.makeText(ScheduleMyCart.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBillData() {
        total = 0;
        if (menuDataList != null && !menuDataList.isEmpty())
            for (RestaurantResponse temp : menuDataList) {
                String offerPrice = temp.getProductData().getOfferPrice();
                if(temp.getSellerData()!=null)
                {
                    if(temp.getSellerData().getOfferStatus().equals("Active")) {
                        if (temp.getProductData().getoStatus().equals("Active")) {
                            if (offerPrice != null) {
                                Date c = Calendar.getInstance().getTime();

                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                try {

                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


                                    String str1 = formattedDate;
                                    Date date1 = formatter.parse(str1);

                                    String str2 = getOutputFormats(temp.getProductData().getEndDate());
                                    Date date2 = formatter.parse(str2);

                                    if (date2.compareTo(date1) >= 0) {

                                        total += temp.getQuantity() * Double.parseDouble(offerPrice);

                                    } else {

                                        total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());


                                    }

                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

                            }


                        } else {
                            total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

                        }
                    }

                    else {
                        total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

                    }
                }
                else {
                    total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());

                }




            }
        /*if (type.equalsIgnoreCase(SubOfferActivity.class.getSimpleName())) { // add offer price if applicable
            if (menuDataList != null && !menuDataList.isEmpty())
                for (RestaurantResponse temp : menuDataList) {
                    total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getOfferPrice());
                }
        } else {
            if (menuDataList != null && !menuDataList.isEmpty())
                for (RestaurantResponse temp : menuDataList) {
                    total += temp.getQuantity() * Double.parseDouble(temp.getProductData().getPrice());
                }
        }*/
        try {
            //double totalPrice = Double.parseDouble(CommonUtilities.getPriceFormat(String.valueOf(total)));
            if (currency != null && !currency.isEmpty()) { // if currency is available then show currency by backend otherwise show static Kz
                tvTotalPrice.setText(CommonUtilities.getPriceFormat(total) + " " + currency);
                String deliveryCharge = CommonUtilities.getPriceFormat(deli_charge);
                tvDeliveryCharges.setText(deliveryCharge + " " + currency);

                deliveryCharge=  deliveryCharge.replace(",","");


                totalAmount = total + Double.parseDouble(deliveryCharge);



                tvTotalAmountPaid.setText(CommonUtilities.getPriceFormat(totalAmount) + " " + currency);
            } else {
                tvTotalPrice.setText(CommonUtilities.getPriceFormat(total) + " Kz");
                String deliveryCharge = CommonUtilities.getPriceFormat(deli_charge);

                tvDeliveryCharges.setText(deliveryCharge + " Kz");
                deliveryCharge=  deliveryCharge.replace(",","");
                totalAmount = total + Double.parseDouble(deliveryCharge);


                tvTotalAmountPaid.setText(CommonUtilities.getPriceFormat(totalAmount) + " Kz");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddItem(int position, ArrayList<RestaurantResponse> menuDataList) {
        int quantity = menuDataList.get(position).getQuantity();
        serviceUpdateCart(menuDataList.get(position).getProductId(), position, quantity + 1);
    }

    @Override
    public void onRemoveItem(int position, ArrayList<RestaurantResponse> menuDataList) {
        int quantity = menuDataList.get(position).getQuantity();
        serviceUpdateCart(menuDataList.get(position).getProductId(), position, quantity - 1);
    }

    private void serviceUpdateCart(String productId, int position, int quantity) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("langCode", langCode);
                jsonObject.put("productId", productId);
                jsonObject.put("quantity", quantity);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.updateCart(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(ScheduleMyCart.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                if (quantity == 0)
                                    menuDataList.remove(menuDataList.get(position));
                                else
                                    menuDataList.get(position).setQuantity(quantity);

                                menuDetailsAdapter.notifyDataSetChanged();

                                if (!menuDataList.isEmpty()) {
                                    showNoDataText(View.VISIBLE, View.GONE);
                                    setBillData();
                                } else {
                                    showNoDataText(View.GONE, View.VISIBLE);
                                }

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ScheduleMyCart.this);
                            } else {
                                Toast.makeText(ScheduleMyCart.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQ && resultCode == Activity.RESULT_OK && data != null) {
            AddressList addressList = (AddressList) data.getSerializableExtra(ParamEnum.DATA.theValue());
            if (addressList != null) {
                address = addressList.getAddress();
                deliveryLat = addressList.getLat();
                deliveryLon = addressList.getLongs();
                landmark = addressList.getLandmark();
                buildingAndApart = addressList.getBuildingAndApart();
                tvAddress.setText(landmark + " " + buildingAndApart + ", " + address);
            }
        }
    }

    private void servicePlaceOrder() {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("deliveryDate", deliveryDate);
                jsonObject.put("deliveryCharge", deli_charge);
                jsonObject.put("totalPrice", totalAmount);
                jsonObject.put("price", total);
                jsonObject.put("orderType", "Product");
                if (type.equalsIgnoreCase(SubOfferActivity.class.getSimpleName())) {
                    jsonObject.put("offerApplicable", true);
                    jsonObject.put("offerAmount", total);
                } else {
                    jsonObject.put("offerApplicable", false);
                    jsonObject.put("offerAmount", "0");
                }
                jsonObject.put("address", address);
                jsonObject.put("latitude", deliveryLat);
                jsonObject.put("longitude", deliveryLon);
                jsonObject.put("landmark", landmark);
                jsonObject.put("buildingAndApart", buildingAndApart);
                jsonObject.put("excepetdDeliveryTime", excepetdDeliveryTime);
                jsonObject.put("deliverySlot", deliverySlot);
                jsonObject.put("deliveryTimeSlot", deliveryTimeSlot);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<RestaurantModel> beanCall = apiInterface.placeOrder(token, body);

                beanCall.enqueue(new Callback<RestaurantModel>() {
                    @Override
                    public void onResponse(Call<RestaurantModel> call, Response<RestaurantModel> response) {
                        MyDialog.getInstance(ScheduleMyCart.this).hideDialog();
                        if (response.isSuccessful()) {
                            RestaurantModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {

                                showOrderPlacedDialog();

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ScheduleMyCart.this);
                            } else {
                                Toast.makeText(ScheduleMyCart.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = mdformat.format(calendar.getTime());

        return currentDate;
    }

    /*public void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = mdformat.format(calendar.getTime());
        String[] currentDateArray = currentDate.split("/");
        yyyy = Integer.parseInt(currentDateArray[0]);
        mm = Integer.parseInt(currentDateArray[1]) - 1;
        dd = Integer.parseInt(currentDateArray[2]);

        showDate(dd, mm, yyyy, R.style.NumberPickerStyle);
    }

    private  void showDate(int dayOfMonth, int monthOfYear, int year, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                //.showDaySpinner(false)
                //.showTitle(false)
                .minDate(yyyy,mm,dd)
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        day = String.valueOf(dayOfMonth);
        month = String.valueOf(monthOfYear);
        years = String.valueOf(year);
        String calenderDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        *//*SimpleDateFormat inputFormate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormate.parse(calenderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormate.format(date);*//*
        tvDate.setText(calenderDate);
    }*/

    private void showNoDataText(int scrollViewVisibility, int visibleNotData) {
        scrollView.setVisibility(scrollViewVisibility);
        tvNoData.setVisibility(visibleNotData);
        tvPay.setVisibility(scrollViewVisibility);
    }

    /*private void showTimePicker(TextView tvTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(this,
                (TimePickerDialog.OnTimeSetListener) (timePicker, hourOfDay, minute) ->
                        tvTime.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute)), hour, mins, true);
        tpd.show();
    }*/

    private void serviceGetDeliverySlotList(String day) {
        try {
            MyDialog.getInstance(this).showDialog(this);
            if (!token.isEmpty()) {
                ApiInterface apiInterface = ApiClientConnection.getInstance().createApiInterface();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("resAndStoreId", resAndStoreId);
                jsonObject.put("day", day);
                jsonObject.put("langCode", langCode);

                MediaType json = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(json, jsonObject.toString());

                Call<ProductModel> beanCall = apiInterface.getDeliverySlotList(token, body);

                beanCall.enqueue(new Callback<ProductModel>() {
                    @Override
                    public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                        MyDialog.getInstance(ScheduleMyCart.this).hideDialog();
                        if (response.isSuccessful()) {
                            ProductModel restaurantModel = response.body();
                            if (restaurantModel.getStatus().equalsIgnoreCase("SUCCESS")) {
                                daysList = restaurantModel.getData();
                                if (daysList != null && !daysList.isEmpty()) {
                                    /*ProductResponse commonResponse = new ProductResponse();
                                    commonResponse.setTimeSlot("Select");
                                    daysList.add(0, commonResponse);
                                    CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(ScheduleMyCart.this,
                                            android.R.layout.simple_spinner_dropdown_item, daysList);
                                    spTimeSlot.setAdapter(adapter);
                                    spTimeSlot.setOnItemSelectedListener(timeSlotSelectedListener);*/

                                    createSlotList(daysList);

                                } else {
                                    morningList.clear();
                                    afternoonList.clear();
                                    eveningList.clear();
                                    morningAdapter.notifyDataSetChanged();
                                    afternoonAdapter.notifyDataSetChanged();
                                    eveningAdapter.notifyDataSetChanged();
                                    //Toast.makeText(ScheduleMyCart.this, getString(R.string.time_slot_not_found), Toast.LENGTH_SHORT).show();
                                }

                                showNoSlotFound();

                            } else if (restaurantModel.getStatus().equalsIgnoreCase("FAILURE") && restaurantModel.getResponse_message().equalsIgnoreCase("Invalid Token")) {
                                CommonUtility.showDialog1(ScheduleMyCart.this);
                            } else {
                                Toast.makeText(ScheduleMyCart.this, restaurantModel.getResponse_message(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductModel> call, Throwable t) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createSlotList(ArrayList<ProductResponse> daysList) {
        morningList.clear();
        afternoonList.clear();
        eveningList.clear();
        for (ProductResponse productResponse : daysList) {
            if (productResponse.getTimeSlot().equalsIgnoreCase("Morning"))
                morningList.add(productResponse);
            else if (productResponse.getTimeSlot().equalsIgnoreCase("Afternoon"))
                afternoonList.add(productResponse);
            else if (productResponse.getTimeSlot().equalsIgnoreCase("Evening"))
                eveningList.add(productResponse);
        }
        morningAdapter.notifyDataSetChanged();
        afternoonAdapter.notifyDataSetChanged();
        eveningAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectMorningSlot(int position) {
        deliveryTimeSlot = morningList.get(position).getOpenTime() + " - " + morningList.get(position).getCloseTime();
        for (int i = 0; i < morningList.size(); i++) {
            morningList.get(i).setSelected(false);
        }
        morningList.get(position).setSelected(true);
        morningAdapter.notifyDataSetChanged();

        for (int i = 0; i < afternoonList.size(); i++) {
            afternoonList.get(i).setSelected(false);
        }
        afternoonAdapter.notifyDataSetChanged();

        for (int i = 0; i < eveningList.size(); i++) {
            eveningList.get(i).setSelected(false);
        }
        eveningAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectAfternoonSlot(int position) {
        deliveryTimeSlot = afternoonList.get(position).getOpenTime() + " - " + afternoonList.get(position).getCloseTime();
        for (int i = 0; i < afternoonList.size(); i++) {
            afternoonList.get(i).setSelected(false);
        }
        afternoonList.get(position).setSelected(true);
        afternoonAdapter.notifyDataSetChanged();

        for (int i = 0; i < morningList.size(); i++) {
            morningList.get(i).setSelected(false);
        }
        morningAdapter.notifyDataSetChanged();

        for (int i = 0; i < eveningList.size(); i++) {
            eveningList.get(i).setSelected(false);
        }
        eveningAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectEveningSlot(int position) {
        deliveryTimeSlot = eveningList.get(position).getOpenTime() + " - " + eveningList.get(position).getCloseTime();
        for (int i = 0; i < eveningList.size(); i++) {
            eveningList.get(i).setSelected(false);
        }
        eveningList.get(position).setSelected(true);
        eveningAdapter.notifyDataSetChanged();

        for (int i = 0; i < morningList.size(); i++) {
            morningList.get(i).setSelected(false);
        }
        morningAdapter.notifyDataSetChanged();

        for (int i = 0; i < afternoonList.size(); i++) {
            afternoonList.get(i).setSelected(false);
        }
        afternoonAdapter.notifyDataSetChanged();
    }

    /*private void setSpinner(final Context context, Spinner spinner, ArrayList<String> array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, array) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.layout_custom_spinner,parent ,false);
                TextView label = convertView.findViewById(R.id.spinner_text);
                label.setText(array.get(position));
                if (position == 0) {
                    label.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                } else {
                    label.setTextColor(context.getResources().getColor(android.R.color.black));
                }
                return convertView;
            }
        };
        spinner.setAdapter(adapter);
    }*/

    static class CustomSpinnerAdapter extends ArrayAdapter<ProductResponse> {
        private Context context;
        private ArrayList<ProductResponse> values;

        public CustomSpinnerAdapter(Context context, int textViewResourceId, ArrayList<ProductResponse> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }

        @Override
        public boolean isEnabled(int position) {
            if (position == 0)
                return false;
            else
                return true;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.layout_custom_spinner, parent, false);
            TextView label = convertView.findViewById(R.id.spinner_text);

            label.setText(values.get(position).getTimeSlot());

            if (position == 0) {
                label.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            } else {
                label.setTextColor(context.getResources().getColor(android.R.color.black));
            }


            return convertView;
        }

    }

    static class WeekSpinnerAdapter extends ArrayAdapter<ProductResponse> {
        private Context context;
        private ArrayList<ProductResponse> values;

        public WeekSpinnerAdapter(Context context, int textViewResourceId, ArrayList<ProductResponse> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }

        @Override
        public boolean isEnabled(int position) {
            if (position == 0)
                return false;
            else
                return true;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.layout_custom_spinner, parent, false);
            TextView label = convertView.findViewById(R.id.spinner_text);

            label.setText(values.get(position).getDay());

            if (position == 0) {
                label.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            } else {
                label.setTextColor(context.getResources().getColor(android.R.color.black));
            }


            return convertView;
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")) {
                if (SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)) {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                } else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            } else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        } else {
            super.attachBaseContext(newBase);
        }
    }

}