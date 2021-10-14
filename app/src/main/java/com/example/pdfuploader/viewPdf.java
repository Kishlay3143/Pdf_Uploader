package com.example.pdfuploader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLEncoder;

public class viewPdf extends AppCompatActivity {

    WebView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        getSupportActionBar().hide();  // Hide Action Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   // Hide Status Bar

        pdfView = findViewById(R.id.pdfView);
        pdfView.getSettings().setJavaScriptEnabled(true); // Yo will get all the pages of the pdf using this line of code.



        //Zoom in & out functionality
        WebSettings mWebSettings = pdfView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);

        String fileName = getIntent().getStringExtra("fileName");
        String fileUrl = getIntent().getStringExtra("fileUrl");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(fileName);
        pd.setMessage("Loading.....!!!");

        pdfView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });
        String url ="";
        try {
            url= URLEncoder.encode(fileUrl,"UTF-8");
        } catch (Exception exception) {
        }

        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
//        setContentView(pdfView);

    }
}