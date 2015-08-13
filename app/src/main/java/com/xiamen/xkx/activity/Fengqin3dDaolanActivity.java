package com.xiamen.xkx.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.xiamen.xkx.R;

public class Fengqin3dDaolanActivity extends AppCompatActivity {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hulishan);

        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("3D导览");
        wv = (WebView) findViewById(R.id.webView);

        wv.loadUrl("http://www.zxplanet.com/zxdata/organ/index.html");
        //enable interact with the page
        wv.getSettings().setJavaScriptEnabled(true);
        // enable media in the page
        wv.setWebChromeClient(new WebChromeClient());
        //enable zoomable
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        findViewById(R.id.imgbtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv.removeAllViews();
        wv.destroy();
    }
}
