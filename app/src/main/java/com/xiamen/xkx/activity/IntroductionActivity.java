package com.xiamen.xkx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiamen.xkx.R;

//介绍界面
public class IntroductionActivity extends Activity {

    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        init();
    }

    public void init() {
        View bar = findViewById(R.id.bar);
        ImageButton imaBtn_left = (ImageButton) bar.findViewById(R.id.bar_left);
        ImageButton imaBtn_right = (ImageButton) bar.findViewById(R.id.bar_right);
        TextView tv_title = (TextView) bar.findViewById(R.id.bar_title);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.grayLayout);

        Intent intent = getIntent();
        str = intent.getStringExtra("景点");
        if (str.equals("鼓浪屿景区")) {
            layout.setVisibility(View.INVISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            tv_title.setText(str);
            TextView vt_in = (TextView) findViewById(R.id.tv_in);
            TextView tv_res = (TextView) findViewById(R.id.tv_res);
            TextView tv_explain = (TextView) findViewById(R.id.tv_explain);
            vt_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(IntroductionActivity.this, MapActivity.class);
                    startActivity(intent);
                }
            });
            tv_res.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            tv_explain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        imaBtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imaBtn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回到首页
                Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
