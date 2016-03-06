package com.example.abhi.clean_india;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class newimage extends AppCompatActivity {

    private WebView imageView,imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newimage);
        imageView=(WebView)findViewById(R.id.header_webview);
        imageView1=(WebView)findViewById(R.id.header_webview2);

        imageView.setWebViewClient(new MyBrowser());
        imageView.getSettings().setLoadsImagesAutomatically(true);
        imageView.getSettings().setJavaScriptEnabled(true);
        imageView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        imageView.loadUrl("http://code.yakshaq.in/android/" + timeline.c_id + ".jpg");

        imageView1.setWebViewClient(new MyBrowser());
        imageView1.getSettings().setLoadsImagesAutomatically(true);
        imageView1.getSettings().setJavaScriptEnabled(true);
        imageView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        imageView1.loadUrl("http://code.yakshaq.in/android/"+timeline.t_is+".jpg");

    }

    private class MyBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
