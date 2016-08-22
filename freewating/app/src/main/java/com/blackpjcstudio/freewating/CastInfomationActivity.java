package com.blackpjcstudio.freewating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CastInfomationActivity extends AppCompatActivity {

    private LinearLayout linearCast;
    private String castLink = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_infomation);
        getSupportActionBar().setTitle(getIntent().getStringExtra("Title"));
        init();
    }

    private void init() {
        linearCast = (LinearLayout) findViewById(R.id.linearCast);
        castLink = getIntent().getStringExtra("Link");
        if(castLink != null) {
            new InquiryCast().start();
        }
    }

    private class InquiryCast extends Thread {

        private ProgressDialog mProgressDialog;

        @Override
        public void run() {
            try {
                showProgress();
                Document document = Jsoup.connect("http://baykoreans.net" + castLink).get();
                Elements elementsRed = document.select("A[class=button red xLarge]");
                for(int i=0;i<elementsRed.size();i++) {
                    Element element = elementsRed.get(i);
                    String link = element.attr("href");
                    layoutAttachButton(link, link);
                }
                exitProgress();
            } catch (Exception e) {
                e.printStackTrace();
                exitProgress();
            }
        }

        private void layoutAttachButton(final String name, final String link) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = new Button(CastInfomationActivity.this);
                    button.setText(link);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button thisButton = (Button) v;
                            Intent intent = new Intent(CastInfomationActivity.this, WebViewActivity.class);
                            intent.putExtra("Link", thisButton.getText());
                            startActivity(intent);
                        }
                    });
                    linearCast.addView(button);
                }
            });
        }

        private void showProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog = ProgressDialog.show(CastInfomationActivity.this, "", "잠시만 기다려주세요.", true);
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
