package com.ics.admin.hotelapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WebView webpage;
    private ProgressBar progressBar;
    public ProgressDialog progress_initial;
    AlertDialog Network_dialog;
    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE=1;
    private Uri imageUri;

    WebView webView;


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

        webpage = (WebView) findViewById(R.id.webpage);
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.getSettings().setSupportZoom(true);
        webpage.getSettings().setBuiltInZoomControls(true);
        //ALLOW
        webpage.getSettings().setPluginState(WebSettings.PluginState.OFF);
        webpage.getSettings().setLoadWithOverviewMode(true);
        webpage.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webpage.getSettings().setUseWideViewPort(true);
        webpage.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        webpage.getSettings().setAllowFileAccess(true);
        webpage.getSettings().setAllowFileAccess(true);
        webpage.getSettings().setAllowContentAccess(true);
        webpage.getSettings().supportZoom();
        //
        webpage.loadUrl("http://www.rooms.buzybeds.com/");

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(100);
        progressBar.setProgress(1);
        //swapnil sir code

        //
        if(webpage.getSettings().getAllowFileAccess())
        {
            Toast.makeText(this, "its true", Toast.LENGTH_SHORT).show();
        }
        webpage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            webpage.getSettings().setAllowContentAccess(true);

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

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
               // Toast.makeText(MainActivity.this, "Console"+consoleMessage.message(), Toast.LENGTH_SHORT).show();
                return super.onConsoleMessage(consoleMessage);
            }

//            @Override
//            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
//                Toast.makeText(MainActivity.this, "url is"+url, Toast.LENGTH_SHORT).show();
//
//                super.onReceivedTouchIconUrl(view, url, precomposed);
//            }
        });



        webpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            //    Toast.makeText(v.getContext(), "webpage hit resul"+webpage.getHitTestResult().getExtra(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this, "webpage hit resul"+webpage.getHitTestResult().getExtra(), Toast.LENGTH_SHORT).show();
            }
        });
        webpage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, ""+url, Toast.LENGTH_SHORT).show();
                Log.e("the usrl" , ""+url);

             //   showAttachmentDialog();


            }

            public boolean onTouch(View v, MotionEvent event) {
                WebView.HitTestResult hr = ((WebView)v).getHitTestResult();

              //  Log.i(TAG, “getExtra = “+ hr.getExtra() + “\t\t Type=” + hr.getType());
                return false;
            }
//            @Override
//            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
//               // Toast.makeText(MainActivity.this, "Request recieved"+request, Toast.LENGTH_SHORT).show();
//                super.onReceivedClientCertRequest(view, request);
//            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @SuppressLint("JavascriptInterface")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //    Toast.makeText(MainActivity.this, "Click has been performed", Toast.LENGTH_SHORT).show();
                    }
                });
                view.addJavascriptInterface(new Object()
                {
                    public void performClick()
                    {
                     //   Toast.makeText(MainActivity.this, "Click has been performed", Toast.LENGTH_SHORT).show();
                        // Deal with a click on the OK button
                    }
                }, "ok");
             //   createWebPagePrint(view);
                progressBar.setVisibility(View.GONE);
                progress_initial.dismiss();
            }

        });

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  void createWebPagePrint(WebView webView) {
		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;*/
        PrintManager printManager = (PrintManager) getSystemService(MainActivity.this.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A5);
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());

        if(printJob.isCompleted()){
            Toast.makeText(getApplicationContext(),"print_complete", Toast.LENGTH_LONG).show();
        }
        else if(printJob.isFailed()){
            Toast.makeText(getApplicationContext(),"print_failed", Toast.LENGTH_LONG).show();
        }
        // Save the job object for later status checking
    }

    //----------------------------
    private void showAttachmentDialog() {
//        this.mUploadMessage = uploadMsg;

        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "TestApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        this.imageUri = Uri.fromFile(file); // save to the private variable

        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");

        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] { captureIntent });

        this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == this.mUploadMessage) {
                return;
            }

            Uri result;
            if (resultCode != RESULT_OK) {
                result = null;
            } else {
                result = intent == null ? this.imageUri : intent.getData(); // retrieve from the private variable if the intent is null
            }

            this.mUploadMessage.onReceiveValue(result);
            this.mUploadMessage = null;
        }
    }

    //==========================
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
