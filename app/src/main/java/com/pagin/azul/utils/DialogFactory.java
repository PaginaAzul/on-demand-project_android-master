//package com.mobu.jokar.utils;
//
///**
// * Created by promatics on 1/15/2018.
// */
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//import co.butterflies.R;
//import co.butterflies.app.widget.sweetAlert.SweetAlertDialog;
//
//public class DialogFactory
//{
//    private static OnDialogButtonListener onDialogButtonListener;
//    public static void showLog(String title, String message) {
//        Log.e(title,message);
//    }
//
//    public static void showToast(Context context, String message) {
//        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
//    }
//
//    public static void createSimpleErrorDialog(Context context, String message) {
//        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                .setTitleText(context.getString(R.string.sorry))
//                .setContentText(message)
//                .show();
//    }
//
//
//    public static void createSimpleErrorDialogPerformEvent(Context context, String message, String buttonText, final int position) {
//        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                .setTitleText(context.getString(R.string.sorry))
//                .setContentText(message)
//                .setConfirmText(buttonText)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog)
//                    {
//                        sDialog.dismiss();
//                        if(onDialogButtonListener!=null)
//                        {
//                            onDialogButtonListener.onYesClickListener(position);
//                        }
//                    }
//                })
//                .show();
//    }
//
//
//    public static void createQuestionDialog(Context context, String title, String message, final int position, String yes, String no)
//    {
//        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText(title)
//                .setContentText(message)
//                .setConfirmText(yes)
//                .setCancelText(no)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog)
//                    {
//                        sDialog.dismiss();
//                        if(onDialogButtonListener!=null)
//                        {
//                            onDialogButtonListener.onYesClickListener(position);
//                        }
//                    }
//                })
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.dismiss();
//                        if(onDialogButtonListener!=null)
//                        {
//                            onDialogButtonListener.onNoClickListener(position);
//                        }
//                    }
//                })
//                .show();
//    }
//
//    public static void createSuccessDialogPerformEvent(Context context, String message, String buttonText, final int position) {
//        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                .setTitleText(context.getString(R.string.thanks))
//                .setContentText(message)
//                .setConfirmText(buttonText)
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog)
//                    {
//                        sDialog.dismiss();
//                        if(onDialogButtonListener!=null)
//                        {
//                            onDialogButtonListener.onYesClickListener(position);
//                        }
//                    }
//                })
//                .show();
//    }
//
//
//    public static void createSimpleSuccessDialog(Context context, String message) {
//        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                .setTitleText(context.getString(R.string.thanks))
//                .setContentText(message)
//                .show();
//    }
//
//    public static AlertDialog createPrograssDialog(Context context)
//    {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.customDialog);
//        dialogBuilder.setCancelable(false);
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
//        dialogBuilder.setView(dialogView);
//        AlertDialog alertDialog = dialogBuilder.create();
//        Window window = alertDialog.getWindow();
//        alertDialog.setCancelable(false);
//        alertDialog.setCanceledOnTouchOutside(false);
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//        return alertDialog;
//    }
//
//    /*..............todo permissions for camera and read and write external storage.............*/
//    public static boolean permissionCheck(Activity activity) {
//        if (Build.VERSION.SDK_INT >= 23)
//        {
//            int hasReadPermission = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//            int hasWritePermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int hasCameraPermissions = activity.checkSelfPermission(Manifest.permission.CAMERA);
//
//            ArrayList<String> permissionList = new ArrayList<String>();
//
//            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            }
//            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//            if (hasCameraPermissions != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.CAMERA);
//            }
//            if (!permissionList.isEmpty()) {
//                activity.requestPermissions(permissionList.toArray(new String[permissionList.size()]), 2);
//            }else {
//                return true;
//            }
//        }
//        else if (Build.VERSION.SDK_INT<23){
//            return true;
//        }
//        return false;
//    }
//
//    public interface OnDialogButtonListener
//    {
//        public void onNoClickListener(int position);
//        public void onYesClickListener(int position);
//    }
//    public static void setOnOnDialogButtonClickListener(OnDialogButtonListener onDialogButtonListenerr)
//    {
//        onDialogButtonListener=onDialogButtonListenerr;
//    }
//}
