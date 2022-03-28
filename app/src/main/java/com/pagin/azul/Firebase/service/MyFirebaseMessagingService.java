package com.pagin.azul.Firebase.service;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pagin.azul.Firebase.app.Config;
import com.pagin.azul.Firebase.utils.NotificationUtils;
import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Siddiqui on 18/7/17.
 * Copyright to Mobulous Technology Pvt. Ltd.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "PUSH";
    private NormalUserPendingOrderInner getDataInner;
    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken= s;
        Log.v(TAG, "sendRegistrationToServer: " + refreshedToken);
        SharedPreferenceWriter.getInstance(MyFirebaseMessagingService.this).writeStringValue(SharedPreferenceKey.DEVICE_TOKEN,refreshedToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null)
            return;

        try {

            handleMyDataMessage(remoteMessage);

            //new BackgroundWork().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMyDataMessage(RemoteMessage remoteMessage) throws Exception {

        // Log.d("daas", remoteMessage + "");

        Map<String, String> data = remoteMessage.getData();
        JSONObject jsonObject = new JSONObject(data);

        String title = jsonObject.getString("title");
        String body = jsonObject.getString("message");

        String type = jsonObject.getString("type");
        notiType(type);

//        if (type.equalsIgnoreCase("workDoneByPW")) {
//
//            String orderId = jsonObject.getString("orderId");
//            String delyUserId = jsonObject.getString("deliveryUserId");
//
//            //startActivity(DialogActivity.getIntent(getApplicationContext(), type, orderId, delyUserId));
//
//            Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
//            intent.putExtra("kType", type);
//            intent.putExtra("kOrderId", orderId);
//            intent.putExtra("KDeliveryId", delyUserId);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplicationContext().startActivity(intent);
//
//        } else if (type.equalsIgnoreCase("workDoneByDP")) {
//            String orderId = jsonObject.getString("orderId");
//            String delyUserId = jsonObject.getString("deliveryUserId");
//
//            Intent intent = new Intent(getApplicationContext(), DialogActivity.class);
//            intent.putExtra("kType", type);
//            intent.putExtra("kOrderId", orderId);
//            intent.putExtra("KDeliveryId", delyUserId);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getApplicationContext().startActivity(intent);
//
//        }


        if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
           /* pushNotification.putExtra(kBody, body);
            pushNotification.putExtra(kTitle, "");*/

            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            showNotificationMessage(getApplicationContext(), title, body, "", new Intent());
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();


        } else {
            if (!title.equalsIgnoreCase("New Message Received")) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                showNotificationMessage(getApplicationContext(), title, body, "", new Intent());
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }
//                Intent resultIntent = HomeMapActivity.getIntent(getApplicationContext(),"");
//                showNotificationMessage(getApplicationContext(), title, body, NotificationUtils.generateTimeStamp(), resultIntent);
        }
            /*else if (body.equals(kCancelBookingFoodie)) {
                Intent resultIntent = MyReservationScreen.getIntent(getApplicationContext());
                showNotificationMessage(getApplicationContext(), title, body, Utils.generateTimeStamp(), resultIntent);
            } else if (body.equals(kCancelBookingHost)) {
                Intent resultIntent = MyHistoryActivity.getIntent(getApplicationContext());
                showNotificationMessage(getApplicationContext(), title, body, Utils.generateTimeStamp(), resultIntent);
            } else if (body.equals(kCompletebooking)) {
                Intent resultIntent = MyHistoryActivity.getIntent(getApplicationContext());
                showNotificationMessage(getApplicationContext(), title, body, Utils.generateTimeStamp(), resultIntent);
            }else  if(body.equals(kPaymentRecieve)){
                Intent resultIntent = HomeHostActivity.getIntent(getApplicationContext());
                showNotificationMessage(getApplicationContext(), title, body, Utils.generateTimeStamp(), resultIntent);
            }else if(body.equals("by_admin")){
                Intent resultIntent= HostNotificationActivity.getIntent(getApplicationContext());
                showNotificationMessage(getApplicationContext(), title, body, Utils.generateTimeStamp(), resultIntent);

            }

            else {
                Intent resultIntent = MyHistoryActivity.getIntent(getApplicationContext());
                showNotificationMessage(getApplicationContext(), title, body, Utils.generateTimeStamp(), resultIntent);
            }
*/
    }

    private void notiType(String type) {

        if (type.equalsIgnoreCase("offerAvailableTest")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAvailable", "offerAvailable");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("workDoneByDP")) {
            Bundle bundle = new Bundle();
            bundle.putString("workDoneByDP", "workDoneByDP");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("workDoneByPW")) {
            Bundle bundle = new Bundle();
            bundle.putString("workDoneByPW", "workDoneByPW");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("offerAvailableProfessional")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAvailable", "offerAvailableProfessional");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("invoiceAvailable")) {
            Bundle bundle = new Bundle();
            bundle.putString("invoiceAvailable", "invoiceAvailable");
            Intent intent = new Intent("invoiceAvailable");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("goStatus")) {
            Bundle bundle = new Bundle();
            bundle.putString("goStatus", "goStatus");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("arrivedStatus")) {
            Bundle bundle = new Bundle();
            bundle.putString("arrivedStatus", "arrivedStatus");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("requestApproved")) {
            Bundle bundle = new Bundle();
            bundle.putString("requestApproved", "requestApproved");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(MyFirebaseMessagingService.this).sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("offerAcceptOfDelivery")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "offerAcceptOfDelivery");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("offerAcceptOfProfessional")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "offerAcceptOfProfessional");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("orderAvailable")) {

            Bundle bundle = new Bundle();
            bundle.putString("orderAvailable", "orderAvailable");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderAvailableForDelivery")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "orderAvailableForDelivery");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderAvailableForProfessional")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "orderAvailableForProfessional");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderCancelByUserDeliveryOrder")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "orderCancelByUserDeliveryOrder");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderCancelByUserProfessionalOrder")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "orderCancelByUserProfessionalOrder");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderUnavailableForProfessional")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "orderUnavailableForProfessional");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderUnavailable")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "orderUnavailable");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);


        } else if (type.equalsIgnoreCase("offerAvailable")) {

            Bundle bundle = new Bundle();
            bundle.putString("offerAvailable", "offerAvailable");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("offerDelete")) {

            Toast.makeText(this, "offerDelete", Toast.LENGTH_SHORT).show();

        } else if (type.equalsIgnoreCase("orderCancel")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAccept", "orderCancel");
            Intent intent = new Intent("offerAccept");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("offerReject")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "offerReject");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);


        } else if (type.equalsIgnoreCase("professionalAction")) {
            Bundle bundle = new Bundle();
            bundle.putString("professionalAction", "professionalAction");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("deliveryAction")) {
            Bundle bundle = new Bundle();
            bundle.putString("professionalAction", "deliveryAction");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("orderCancelFromProfessional")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAvailable", "orderCancelFromProfessional");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("acceptWithdrawRequestDeliveryPersion")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAccept", "acceptWithdrawRequestDeliveryPersion");
            Intent intent = new Intent("offerAccept");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        } else if (type.equalsIgnoreCase("acceptWithdrawRequestProfessionalWorker")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAccept", "acceptWithdrawRequestProfessionalWorker");
            Intent intent = new Intent("offerAccept");
            intent.putExtras(bundle);
            sendBroadcast(intent);

        } else if (type.equalsIgnoreCase("orderCancelFromDelivery")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAvailable", "orderCancelFromDelivery");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);


        } else if (type.equalsIgnoreCase("offerRejectProfessional")) {
            Bundle bundle = new Bundle();
            bundle.putString("offerAcceptOfDelivery", "offerRejectProfessional");
            Intent intent = new Intent("Tap Successful");
            intent.putExtras(bundle);
            sendBroadcast(intent);
        }
    }

    private Integer convertIntoInteger(String value) {
        Integer intvalue = 0;
        try {
            intvalue = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intvalue;
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            //If the app is in background, firebase itself handles the notification
            // showNotificationMessageWithBigImage(getApplicationContext(), "", message, "", new Intent(getApplicationContext(), SuccessStory.class), "");
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    private void showDialogDone(String nType, String orderId, String delyUserId) {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnRequire = (Button) dialog.findViewById(R.id.btn_ok);
        getDataInner = new NormalUserPendingOrderInner();


        getDataInner.setOfferAcceptedOfId(delyUserId);
        getDataInner.setOfferId(orderId);

        btnRequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (nType.equalsIgnoreCase("workDoneByPW")) {
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put("kFromCat", "Professional");
//                    hashMap.put("kFrom", "Past");

                    //startActivity(RatingAndRiviewActivity.getIntent(getApplicationContext(), getDataInner,"PastNUP"));

                    //startActivity(HomeMainActivity.getIntent(NormalMessageTrackActivity.this, hashMap));

                } else {

                    // startActivity(RatingAndRiviewActivity.getIntent(getApplicationContext(), getDataInner,"PastNUD"));
                    // finish();

                    //startActivity(HomeMainActivity.getIntent(NormalMessageTrackActivity.this, hashMap));


                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

//    //AsynTask......
//    public class BackgroundWork extends AsyncTask<JSONObject, String, String> {
//
//        @Override
//        protected String doInBackground(JSONObject... objects) {
//
//            String type = null;
//            try {
//                type = handleMyDataMessage(objects[0]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return type;
//        }
//
//        @Override
//        protected void onPostExecute(String type) {
//            super.onPostExecute(type);
//            Log.e("type", type);
//
//            if (type.equalsIgnoreCase("workDoneByPW")) {
//
//                // showDialogDone("","","");
//
//                //MyApplication.getInstance().myDialog();
//            }
//        }
//    }
}
