package com.blackpjcstudio.freewating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportMultipleWindows(true);
        mLink = getIntent().getStringExtra("Link");

        new InquiryCast().start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    private class InquiryCast extends Thread {

        private ProgressDialog mProgressDialog;

        @Override
        public void run() {
            try {
                showProgress();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://dramainfinity.com/dmotion/?xink=k5MrhX4wCqnLn8ebNA0")
                        .build();
                Response response = client.newCall(request).execute();
                Log.d("body", response.body().string());

                Document document = Jsoup.parse(response.body().string());
                Elements elements = document.select("IFRAME");
                Log.d("element", elements.text());
                for(int i=0;i<elements.size();i++) {
                    Element element = elements.get(i);
                    Log.d("src", element.attr("src"));
                    if(element.attr("src").contains("http://www.dailymotion.com")) {
                        openWeb(element.attr("src"));
                        break;
                    }
                }

                exitProgress();
            } catch (Exception e) {
                e.printStackTrace();
                exitProgress();
            }
        }

        private void openWeb(final String link) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(link);
                }
            });
        }

        private void showProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog = ProgressDialog.show(WebViewActivity.this, "", "잠시만 기다려주세요.", true);
                }
            });

        }

        private void exitProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
        }
    }
}
