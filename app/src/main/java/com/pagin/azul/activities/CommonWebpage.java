package com.pagin.azul.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.pagin.azul.Constant.Constants;
import com.pagin.azul.R;
import com.pagin.azul.database.SharedPreferenceKey;
import com.pagin.azul.database.SharedPreferenceWriter;
import com.pagin.azul.retrofit.MyDialog;
import com.pagin.azul.utils.CommonUtility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonWebpage extends AppCompatActivity {

   @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.webviewBlog)
    WebView webView;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();

            if (bundle.getString("Page").equalsIgnoreCase("tv_aboutUs"))
            {
                //setLanguageUrl("http://3.128.74.178/about.html","http://3.128.74.178/aboutPt.html");
                setLanguageUrl("http://3.129.47.202/about.html","http://3.129.47.202/aboutPt.html");
                //url="http://3.128.74.178/about.html";
                tv_title.setText(R.string.about_us);
            }
            else if (bundle.getString("Page").equalsIgnoreCase("tv_privacyPolicy"))
            {
                //setLanguageUrl("http://3.128.74.178/privacy.html","http://3.128.74.178/privacyPt.html");
                setLanguageUrl("http://3.129.47.202/privacy.html","http://3.129.47.202/privacyPt.html");
                //url="http://3.128.74.178/privacy.html";
                tv_title.setText(R.string.pricy_policy);
            }
            else if (bundle.getString("Page").equalsIgnoreCase("tv_terms_condition"))
            {
                //setLanguageUrl("http://3.128.74.178/terms.html","http://3.128.74.178/termsPt.html");
                setLanguageUrl("http://3.129.47.202/terms.html","http://3.129.47.202/termsPt.html");
                //url ="http://3.128.74.178/terms.html";
                tv_title.setText(R.string.term_and_condition);
            }
            else if (bundle.getString("Page").equalsIgnoreCase("tv_contactUs"))
            {
                //setLanguageUrl("http://3.128.74.178/contact.html","http://3.128.74.178/contact.html");
                setLanguageUrl("http://3.129.47.202/contact.html","http://3.129.47.202/contact.html");
                //url ="http://3.128.74.178/contact.html";
                tv_title.setText(R.string.contact_us);
            }
        }
        init();
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void init() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);

        MyDialog.getInstance(this).showDialog(CommonWebpage.this);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                MyDialog.getInstance(CommonWebpage.this).hideDialog();
                view.loadUrl(url);
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                MyDialog.getInstance(CommonWebpage.this).hideDialog();

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                MyDialog.getInstance(CommonWebpage.this).hideDialog();
            }
        });
        // webView.loadUrl("http://mobuloustech.com/camel_api/blog");
        webView.loadUrl(url);
    }

    @OnClick(R.id.iv_back)
    void onclick(View view)
    {
     switch (view.getId())
     {
         case R.id.iv_back:
             onBackPressed();
             break;
     }
    }

    private void setLanguageUrl(String urlEng, String urlPort) {
        // check language from data base
        if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                url = urlEng;
            }else {
                url = urlPort;
            }
        }else {
            url = urlPort;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
                if(SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                    super.attachBaseContext(CommonUtility.wrap(newBase, "en"));
                }else {
                    super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
                }
            }else {
                super.attachBaseContext(CommonUtility.wrap(newBase, "pt"));
            }
        }
        else {
            super.attachBaseContext(newBase);
        }
    }
}
