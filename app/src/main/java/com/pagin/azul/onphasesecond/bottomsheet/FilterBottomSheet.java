package com.pagin.azul.onphasesecond.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pagin.azul.R;
import com.pagin.azul.callback.CommonListener;
import com.pagin.azul.onphasesecond.adapters.BottomMenuItemAdap;
import com.pagin.azul.onphasesecond.utilty.ParamEnum;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterBottomSheet extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.rgPrice)
    RadioGroup rgPrice;

    @BindView(R.id.rbHigh)
    RadioButton rbHigh;

    @BindView(R.id.rbLow)
    RadioButton rbLow;

    @BindView(R.id.tvDate)
    TextView tvDate;

    private Context mContext;
    private CommonListener listener;
    private String month = "";
    private String years = "";
    private String day = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_filter, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        getIntentData();
        return view;

    }

    @OnClick({R.id.btnApply, R.id.tvDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnApply:
                //if(validation()) {
                    listener.onApplyFilter(rgPrice.getCheckedRadioButtonId(),tvDate.getText().toString());
                    dismiss();
                //}
                break;
            case R.id.tvDate:
                if(!day.isEmpty() && !month.isEmpty() && !years.isEmpty())
                    showDate(Integer.parseInt(day),Integer.parseInt(month),Integer.parseInt(years), R.style.NumberPickerStyle);
                else
                    showDatePickerDialog();
                break;
        }
    }

    private void getIntentData() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            switch (bundle.getInt(ParamEnum.ID.theValue())){
                case R.id.rbHigh:
                    rbHigh.setChecked(true);
                    break;
                case R.id.rbLow:
                    rbLow.setChecked(true);
                    break;
            }
            tvDate.setText(bundle.getString(ParamEnum.DATA.theValue()));
        }
    }

    private boolean validation(){
        if(rgPrice.getCheckedRadioButtonId() == -1){
            Toast.makeText(getContext(), getString(R.string.please_select_low_to_hight),Toast.LENGTH_SHORT).show();
            return false;
        }else if(tvDate.getText().toString().isEmpty()){
            Toast.makeText(getContext(), getString(R.string.please_select_validity_date),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setOnCommonListener(CommonListener listener) {
        this.listener = listener;
    }

    public void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = mdformat.format(calendar.getTime());
        String[] currentDateArray = currentDate.split("/");
        int yyyy = Integer.parseInt(currentDateArray[0]);
        int mm = Integer.parseInt(currentDateArray[1]) - 1;
        int dd = Integer.parseInt(currentDateArray[2]);

        showDate(dd, mm, yyyy, R.style.NumberPickerStyle);
    }

    private  void showDate(int dayOfMonth, int monthOfYear, int year, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                //.minDate(yyyy,mm,dd)
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        day = String.valueOf(dayOfMonth);
        month = String.valueOf(monthOfYear);
        years = String.valueOf(year);
        tvDate.setText(String.format(Locale.US, "%02d/%02d/%02d", dayOfMonth, monthOfYear + 1,year));
    }


}
