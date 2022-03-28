package com.pagin.azul.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;



public class NetworkChangeReceiver extends BroadcastReceiver {

    private  InternetResponse listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)) {
               listener.onInternetCheck(true);
                Log.e("check-------", "Connected");
            } else {
                listener.onInternetCheck(false);
                Log.e("check-------", "Conectivity Failure !!! ");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    ///////////callback to MainActivity for handle event//////
    public interface InternetResponse {
        void onInternetCheck(boolean isConnect);
    }

    public void registerCallback(InternetResponse listener){
        this.listener=listener;
    }

}