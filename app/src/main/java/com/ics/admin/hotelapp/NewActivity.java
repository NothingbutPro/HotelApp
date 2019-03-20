package com.ics.admin.hotelapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.FileNotFoundException;

public class NewActivity extends AppCompatActivity {
    private WebView webpage;
    String FileString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webpage = (WebView) findViewById(R.id.webpage);
        getSupportActionBar().hide();
        WebSettings webSettings = webpage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webpage.loadUrl("http://www.rooms.buzybeds.com/");
        webpage.setWebViewClient(new WebViewClient());
        fixWebViewJSInterface(webpage, new clsJScriptInterface(NewActivity.this), "app", "_gbjsfix");
    }

    @SuppressLint("JavascriptInterface")
    public void fixWebViewJSInterface(WebView webview, Object jsInterface, String jsInterfaceName, String jsSignature) {
        webview.addJavascriptInterface(jsInterface, jsInterfaceName);

    }

    public class clsJScriptInterface {
        private final Context context;

        public clsJScriptInterface(Context paramContext) {
            this.context = paramContext;
        }

        @JavascriptInterface
        public String Uploadfile() {
            //This method will create in your js in website which will accepts string;
            try {

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            return FileString;

        }


    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            //here you can write code for fatch files form gallary and convert in appropriate format this will be your FileString to send to website.

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
