package com.ics.admin.hotelapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private WebView webpage;
    private ProgressBar progressBar;
    public ProgressDialog progress_initial;
    AlertDialog Network_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress_initial = new ProgressDialog(this);
        getSupportActionBar().hide();
        int vc = Color.parseColor("#feca01");
        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   finish();
                }
            });
            builder.create();
             Network_dialog = builder.create();
            Network_dialog.setCancelable(false);
            Network_dialog.setTitle("Please connect to internet");
            Network_dialog.show();
        }else{
            progress_initial.show();
        }

    /*    webpage = (WebView) findViewById(R.id.webpage);
        webpage.setWebViewClient(new MyBrowser());
        webpage.getSettings().setLoadsImagesAutomatically(true);
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webpage.loadUrl("http://saibabacollege.com/Hotel/");*/


        webpage = (WebView) findViewById(R.id.webpage);
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.getSettings().setSupportZoom(true);
        webpage.getSettings().setBuiltInZoomControls(true);
        webpage.loadUrl("http://www.rooms.buzybeds.com/");

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(100);
        progressBar.setProgress(1);

        webpage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

        });
        webpage.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);


            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                progress_initial.dismiss();
            }

        });

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onBackPressed() {
        if (webpage.canGoBack()) {
            webpage.goBack();
        } else {
            super.onBackPressed();
        }
    }
  /*  private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }*/
}
