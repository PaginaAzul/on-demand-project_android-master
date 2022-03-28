package com.pagin.azul.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.pagin.azul.R;
import com.pagin.azul.bean.NormalUserPendingOrderInner;

public class DialogActivity extends AppCompatActivity {
    NormalUserPendingOrderInner getDataInner;

    public static Intent getIntent(Context context, String type, String orderId, String deliveryId) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("kType", type);
        intent.putExtra("kOrderId", orderId);
        intent.putExtra("KDeliveryId", deliveryId);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        if (getIntent() != null) {
            showOrderReasonDialog(getIntent().getStringExtra("kType"), getIntent().getStringExtra("kOrderId")
                    , getIntent().getStringExtra("KDeliveryId"));
        }

    }


    private void showOrderReasonDialog(String nType, String orderId, String delyUserId) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rate);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Button btnRequire = (Button) dialog.findViewById(R.id.btn_ok);
        getDataInner = new NormalUserPendingOrderInner();

        getDataInner.setOfferAcceptedOfId(delyUserId);
        getDataInner.setOfferId(orderId);

        btnRequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nType.equalsIgnoreCase("workDoneByPW")) {

                    startActivity(RatingAndRiviewActivity.getIntent(DialogActivity.this, getDataInner, "PastNUP"));
                    finish();

                } else {

                    startActivity(RatingAndRiviewActivity.getIntent(DialogActivity.this, getDataInner, "PastNUD"));
                    finish();

                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
