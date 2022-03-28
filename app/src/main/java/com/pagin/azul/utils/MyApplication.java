package com.pagin.azul.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;


/**
 * Created by Mahipal Singh  mahisingh1@Outlook.com on 28/11/18.
 */
public class MyApplication extends Application {


    /********* How to make the perfect Singleton?: ***********/
//    Link:  https://medium.com/exploring-code/how-to-make-the-perfect-singleton-de6b951dfdb0

    private static volatile MyApplication myApplication = null;
    //    private final String TAG = MyApplication.class.getSimpleName();
    private static Context context = null;
    //private NetworkConnectionCheck connectionCheck;
    //private AsecDataBase db=null;
    // This flag should be set to true to enable VectorDrawable support for API < 21
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    /*public static boolean networkConnectionCheck()
    {
        if(myApplication.connectionCheck==null)
        {
            myApplication.connectionCheck=new NetworkConnectionCheck(myApplication);
        }
        return myApplication.connectionCheck.isConnect();
    }*/


    public static MyApplication getInstance() {

        if (myApplication == null) {     //Check for the first time

            synchronized (MyApplication.class) {    //Check for the second time.
                if (myApplication == null)  // if there is no instance available... create new one
                    myApplication = new MyApplication();
            }
        }
        return myApplication;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        context = this.getApplicationContext();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLanguage();
    }

    private void setLanguage() {
        // check language from data base
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                LocaleHelper.setLocale(this,"en");
            }else {
                LocaleHelper.setLocale(this, "pt");
            }
        }else {
            LocaleHelper.setLocale(this,"pt");
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(this).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
        }
    }

}
