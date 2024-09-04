package com.cd.statussaver.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityWebviewBinding;
import com.cd.statussaver.util.Admenager;


public class WebviewAcitivity extends AppCompatActivity {
    public static Handler handler;
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = WebviewAcitivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;

    ActivityWebviewBinding mainBinding;
    Admenager admenager;

    private class btnInitHandlerListner extends Handler {
        @Override
        public void handleMessage(@NonNull Message message) {
        }


        private btnInitHandlerListner() {
        }
    }

    private class webChromeClients extends WebChromeClient {
        private webChromeClients() {
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CustomClient", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }

        @Override
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "mainBinding.progressBar");
            super.onPageStarted(webView, str, bitmap);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            Log.e(TAG, "mainBinding.progressBar GONE");
            mainBinding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        super.onCreate(savedInstanceState);
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_whatsweb));
        InitHandler();

        if (Build.VERSION.SDK_INT >= 24) {
            onstart();

            mainBinding.webViewscan.clearFormData();
            mainBinding.webViewscan.getSettings().setSaveFormData(true);
            mainBinding.webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            mainBinding.webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            mainBinding.webViewscan.setWebChromeClient(new webChromeClients());
            mainBinding.webViewscan.setWebViewClient(new MyBrowser());
            mainBinding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            mainBinding.webViewscan.getSettings().setAllowFileAccess(true);
            mainBinding.webViewscan.getSettings().setJavaScriptEnabled(true);
            mainBinding.webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
            mainBinding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mainBinding.webViewscan.getSettings().setDatabaseEnabled(true);
            mainBinding.webViewscan.getSettings().setBuiltInZoomControls(false);
            mainBinding.webViewscan.getSettings().setSupportZoom(false);
            mainBinding.webViewscan.getSettings().setUseWideViewPort(true);
            mainBinding.webViewscan.getSettings().setDomStorageEnabled(true);
            mainBinding.webViewscan.getSettings().setAllowFileAccess(true);
            mainBinding.webViewscan.getSettings().setLoadWithOverviewMode(true);
            mainBinding.webViewscan.getSettings().setLoadsImagesAutomatically(true);
            mainBinding.webViewscan.getSettings().setBlockNetworkImage(false);
            mainBinding.webViewscan.getSettings().setBlockNetworkLoads(false);
            mainBinding.webViewscan.getSettings().setLoadWithOverviewMode(true);
            mainBinding.webViewscan.loadUrl("https://web.whatsapp.com/%F0%9F%8C%90/en");
            return;
        }
        onstart();
        mainBinding.webViewscan.clearFormData();
        mainBinding.webViewscan.getSettings().setSaveFormData(true);
        mainBinding.webViewscan.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
        mainBinding.webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mainBinding.webViewscan.setWebChromeClient(new webChromeClients());
        mainBinding.webViewscan.setWebViewClient(new MyBrowser());
        mainBinding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mainBinding.webViewscan.getSettings().setAllowFileAccess(true);
        mainBinding.webViewscan.getSettings().setJavaScriptEnabled(true);
        mainBinding.webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
        mainBinding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mainBinding.webViewscan.getSettings().setDatabaseEnabled(true);
        mainBinding.webViewscan.getSettings().setBuiltInZoomControls(false);
        mainBinding.webViewscan.getSettings().setSupportZoom(false);
        mainBinding.webViewscan.getSettings().setUseWideViewPort(true);
        mainBinding.webViewscan.getSettings().setDomStorageEnabled(true);
        mainBinding.webViewscan.getSettings().setAllowFileAccess(true);
        mainBinding.webViewscan.getSettings().setLoadWithOverviewMode(true);
        mainBinding.webViewscan.getSettings().setLoadsImagesAutomatically(true);
        mainBinding.webViewscan.getSettings().setBlockNetworkImage(false);
        mainBinding.webViewscan.getSettings().setBlockNetworkLoads(false);
        mainBinding.webViewscan.getSettings().setLoadWithOverviewMode(true);
        mainBinding.webViewscan.loadUrl("https://web.whatsapp.com/%F0%9F%8C%90/en");
    }


    public void onstart() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"}, 123);
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001) {
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }
    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            try {
                if (mainBinding.webViewscan.canGoBack()) {
                    mainBinding.webViewscan.goBack();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public void onBackPressed() {
    admenager.showadmobeInterstitialAd();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainBinding.webViewscan.clearCache(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainBinding.webViewscan.clearCache(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        mainBinding.webViewscan.clearCache(true);
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void InitHandler() {
        handler = new btnInitHandlerListner();
    }




}

