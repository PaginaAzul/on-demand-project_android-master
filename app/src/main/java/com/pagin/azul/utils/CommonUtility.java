package com.pagin.azul.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pagin.azul.R;
import com.pagin.azul.activities.SignUpOptions;
import com.pagin.azul.activities.SplashActivity;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;

import java.util.Locale;
import java.util.Objects;

import static com.pagin.azul.Constant.Constants.kToken;


/**
 * Created by mobulous06 on 21/9/17.
 */
public class CommonUtility {

    public static void showDialog1(final Activity activity) {

        Dialog dialog1 = new Dialog(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog, null, false);
        dialog1.setContentView(view);


        Button notNow = view.findViewById(R.id.btn_notnow);
        Button registerNow = view.findViewById(R.id.btn_registerNow);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        TextView textView = view.findViewById(R.id.tv_dialog);


        notNow.setVisibility(View.GONE);
        registerNow.setVisibility(View.GONE);
        btn_ok.setVisibility(View.VISIBLE);
        ((TextView) dialog1.findViewById(R.id.tv_dialog)).setText(R.string.user_logged_in_another_device);


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferenceWriter.getInstance(activity).writeStringValue(kToken, "");
                Intent intent = new Intent(activity, SignUpOptions.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
                dialog1.dismiss();
            }
        });

        Window window = dialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog1.show();

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        try{
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting()
                        || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }

    public static ContextWrapper wrap(Context context, String language) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        Locale newLocale = new Locale(language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

        return new ContextWrapper(context);
    }

    //    METHOD: TO GET DEVICE_TOKEN FROM FCM
    /*public static void getDeviceToken(Context context) {

        final String TAG = SplashActivity.class.getSimpleName();
        Runnable runnableObj = new Runnable() {
            @Override
            public void run() {
                SharedPreferenceWriter mPreference = SharedPreferenceWriter.getInstance(context);
                Log.w(TAG, "Previous Device Token : " + mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN));

                try {
                    if (mPreference.getString(SharedPreferenceKey.DEVICE_TOKEN).isEmpty()) {
                        String device_token = FirebaseInstanceId.getInstance().getToken();
                        Log.e(TAG, "Generated Device Token : " + device_token);
                        if (device_token == null) {
                            getDeviceToken(context);
                        } else {
                            mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, device_token);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnableObj);
        thread.start();

    }*/

    public static void getDeviceToken(Context context) {

        final Thread thread = new Thread() {
            @Override
            public void run() {
                Log.e(">>>>>>>>>>>>>>", "thred IS  running");
                try {
                    if (SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.DEVICE_TOKEN).isEmpty()) {

                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                            if(!task.isSuccessful()){
                                return;
                            }
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            Log.e("Generated Device Token", "-->" + token);
                            if (token == null) {
                                getDeviceToken(context);
                            } else {
                                SharedPreferenceWriter.getInstance(context).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN,token);
                                //mPreference.writeStringValue(SharedPreferenceKey.DEVICE_TOKEN, token);
                            }
                        });

                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
    }

}
