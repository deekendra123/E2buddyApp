package com.aptitude.learning.e2buddy.School.SuperAdmin.ActivityClass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.aptitude.learning.e2buddy.R;

public class ViewWebLinkActivity extends AppCompatActivity {
    WebView simpleWebView;
    private String link;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_web_link);
        progressBar = findViewById(R.id.progressBar);

        simpleWebView = (WebView) findViewById(R.id.webView);

        progressBar.setVisibility(View.VISIBLE);

        link = getIntent().getStringExtra("link");
        simpleWebView.setWebViewClient(new MyWebViewClient());

        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(link); // load the url on the web view
        progressBar.setVisibility(View.GONE);
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.simpleWebView.canGoBack()) {
            this.simpleWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}