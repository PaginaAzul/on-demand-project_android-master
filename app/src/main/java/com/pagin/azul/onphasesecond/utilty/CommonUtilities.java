package com.pagin.azul.onphasesecond.utilty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pagin.azul.R;
import com.pagin.azul.activities.HomeMainActivity;
import com.pagin.azul.activities.SignUpOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonUtilities {

    public static void setToolbar(final AppCompatActivity activity, androidx.appcompat.widget.Toolbar toolbar, TextView title, String  TitleContent){
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);

        title.setText(TitleContent);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());

    }

    public static void showDialog(Context context) {
        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);

        textView.setText(R.string.you_are_not_logged_in_please_login_sign_up_further_proced);
        registerNow.setText(R.string.ok_upper_case);
        notNow.setText(R.string.cancel);

        notNow.setOnClickListener(v -> dialog.dismiss());
        registerNow.setOnClickListener(v -> {
            context.startActivity(new Intent(context, SignUpOptions.class));
            dialog.dismiss();
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public static void showLoginDialog(Context context) {
        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog.setContentView(view);
        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        TextView textView = view.findViewById(R.id.tv_dialog);


        textView.setText(R.string.you_are_not_logged_in_please_login_sign_up_further_proced);
        registerNow.setText(R.string.ok_upper_case);
        notNow.setText(R.string.cancel);


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SignUpOptions.class));

                dialog.dismiss();
            }
        });
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    public static void dispatchGoogleMap(Context context){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", 28.7041 + "," + 77.1025);
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void dispatchGoogleMap(Context context,String latitude,String longitude){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", latitude + "," + longitude);
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static String getOutputFormat(String responseDate){
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
//            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
//            LocalDate date = LocalDate.parse(responseDate, inputFormatter);
//            String formattedDate = outputFormatter.format(date);
//            return formattedDate;
//        }else {
        SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        SimpleDateFormat outputFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm",Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormate.parse(responseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate =  outputFormate.format(date);
        return formattedDate;
        //}
    }

    public static String getOutputFormats(String responseDate){
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
//            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
//            LocalDate date = LocalDate.parse(responseDate, inputFormatter);
//            String formattedDate = outputFormatter.format(date);
//            return formattedDate;
//        }else {
        SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        SimpleDateFormat outputFormate = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormate.parse(responseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate =  outputFormate.format(date);
        return formattedDate;
        //}
    }


    public static String getMyOrdersOutputFormat(String responseDate){
        SimpleDateFormat inputFormate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        SimpleDateFormat outputFormate = new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.ENGLISH);
        Date date = null;
        try {
            date = inputFormate.parse(responseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 5);
        calendar.add(Calendar.MINUTE, 30);
        calendar.add(Calendar.SECOND, 0);
        String formattedDate = outputFormate.format(calendar.getTime());
        return formattedDate;
    }

    public static String getPriceFormat(String price){
        try{
            /*DecimalFormat formatter = new DecimalFormat("#,###,###");
            return formatter.format(Integer.parseInt(price));*/
            // Update 12/02/2019
            //return String.format("%,d", Integer.parseInt(price));
            return NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(price));
        }catch (Exception e){
            return "";
        }
    }

    public static String getPriceFormat(double price){
        try{
            /*DecimalFormat formatter = new DecimalFormat("#,###,###");
            return formatter.format(Integer.parseInt(price));*/
            // Update 12/02/2019
            //return String.format("%,d", Integer.parseInt(price));
            return NumberFormat.getNumberInstance(Locale.US).format(price);
        }catch (Exception e){
            return "";
        }
    }
}
