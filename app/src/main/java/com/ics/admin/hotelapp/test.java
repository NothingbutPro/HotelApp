package com.ics.admin.hotelapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test extends AppCompatActivity {
    static final boolean $assertionsDisabled = (!test.class.desiredAssertionStatus());
    private static final int FCR = 1;
    private static final String TAG = test.class.getSimpleName();
    private Boolean exit = Boolean.valueOf(false);
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    WebView webpage;
    private ProgressDialog progress_initial;
    private AlertDialog Network_dialog;
    private ProgressBar progressBar;


    class MyWebViewClient extends WebChromeClient {
        MyWebViewClient() {
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public  void createWebPagePrint(WebView webView) {
		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;*/
            PrintManager printManager = (PrintManager) getSystemService(test.this.PRINT_SERVICE);
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
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (test.this.mUMA != null) {
                test.this.mUMA.onReceiveValue(null);
            }
            test.this.mUMA = filePathCallback;
            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            if (takePictureIntent.resolveActivity(test.this.getPackageManager()) != null) {
                File file = null;
                try {
                    file = test.this.createImageFile();
                    takePictureIntent.putExtra("PhotoPath", test.this.mCM);
                } catch (IOException ex) {
                    Log.e(test.TAG, "Image file creation failed", ex);
                }
                if (file != null) {
                    test.this.mCM = "file:" + file.getAbsolutePath();
                    takePictureIntent.putExtra("output", Uri.fromFile(file));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent("android.intent.action.GET_CONTENT");
            contentSelectionIntent.addCategory("android.intent.category.OPENABLE");
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray = takePictureIntent != null ? new Intent[]{takePictureIntent} : new Intent[0];
            Intent chooserIntent = new Intent("android.intent.action.CHOOSER");
            chooserIntent.putExtra("android.intent.extra.INTENT", contentSelectionIntent);
            chooserIntent.putExtra("android.intent.extra.TITLE", "Image Chooser");
            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", intentArray);
            test.this.startActivityForResult(chooserIntent, 1);
            return true;
        }
    }

    public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(test.this.getApplicationContext(), "Failed loading app!", Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            if (resultCode == -1 && requestCode == 1) {
                if (this.mUMA != null) {
                    if (intent != null) {
                        if (intent.getDataString() != null) {
                            results = new Uri[]{Uri.parse(intent.getDataString())};
                        }
                    } else if (this.mCM != null) {
                        results = new Uri[]{Uri.parse(this.mCM)};
                    }
                } else {
                    return;
                }
            }
            this.mUMA.onReceiveValue(results);
            this.mUMA = null;
        } else if (requestCode == 1 && this.mUM != null) {
            Uri result = (intent == null || resultCode != -1) ? null : intent.getData();
            this.mUM.onReceiveValue(result);
            this.mUM = null;
        }
    }

    public void onBackPressed() {
        if (this.exit.booleanValue()) {
            finish();
            return;
        }
        Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_LONG).show();
        this.exit = Boolean.valueOf(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                test.this.exit = Boolean.valueOf(false);
            }
        }, 2000);

    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        getSupportActionBar().hide();
        if (VERSION.SDK_INT >= 23 && !(ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0)) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
        }
        this.webpage = (WebView) findViewById(R.id.webpage);
        if ($assertionsDisabled || this.webpage != null) {
            WebSettings webSettings = this.webpage.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);
            if (VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(0);
                this.webpage.setLayerType(2, null);
            } else if (VERSION.SDK_INT >= 19) {
                this.webpage.setLayerType(2, null);
            } else if (VERSION.SDK_INT < 19) {
                this.webpage.setLayerType(1, null);
            }
            this.webpage.setWebViewClient(new Callback());
            this.webpage.loadUrl("http://www.rooms.buzybeds.com/");

            webpage.setWebChromeClient(new MyWebViewClient());

            return;
        }

        progress_initial = new ProgressDialog(this);
        getSupportActionBar().hide();
        int vc = Color.parseColor("#feca01");
        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(test.this);
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
//        //
//        webpage.loadUrl("http://www.rooms.buzybeds.com/");

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(100);
        progressBar.setProgress(1);
        //swapnil sir code

        //
//        if(webpage.getSettings().getAllowFileAccess())
//        {
//            Toast.makeText(this, "its true", Toast.LENGTH_SHORT).show();
//        }
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
                Toast.makeText(test.this, ""+url, Toast.LENGTH_SHORT).show();
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

    private File createImageFile() throws IOException {
        return File.createTempFile("img_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_", ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }

    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (event.getAction() == 0) {
            switch (keyCode) {
                case 4:
                    if (this.webpage.canGoBack()) {
                        this.webpage.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //==========================
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}