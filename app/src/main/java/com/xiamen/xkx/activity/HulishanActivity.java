package com.xiamen.xkx.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;

import com.xiamen.xkx.R;

public class HulishanActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hulishan);
        WebView wv = (WebView) findViewById(R.id.webView);

        wv.loadUrl("http://m.lvxbang.com/hdmap/hulishan");
        findViewById(R.id.imgbtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
