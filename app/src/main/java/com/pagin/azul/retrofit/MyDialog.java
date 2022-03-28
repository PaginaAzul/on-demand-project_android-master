package com.pagin.azul.retrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.pagin.azul.R;

/**
 * Created by mobua06 on 16/6/17.
 */

public class MyDialog {

    private static Context context;
    private ProgressDialog progressDialog;
    static MyDialog myDialog;

    /* public MyDialog(Context context)
     {//R.style.AppCompatAlertDialogStyle
         progressDialog = new ProgressDialog(context, R.style.Theme_AppCompat_Dialog_Alert);
         progressDialog.setMessage("Loading...");
         progressDialog.setCanceledOnTouchOutside(false);
     }*/
    private MyDialog() {

    }

    public synchronized static MyDialog getInstance(Context context) {
        if (myDialog == null) {
            try {
                myDialog = new MyDialog();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return myDialog;
    }


    public void showDialog() {
        try {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void showDialog(Activity activity) {
        try {
            if(progressDialog==null)
                progressDialog=new ProgressDialog(activity);

            if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.setMessage(activity.getString(R.string.loading));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void hideDialog() {
        try {
            if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
