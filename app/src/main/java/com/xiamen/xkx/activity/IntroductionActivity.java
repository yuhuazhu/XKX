package com.xiamen.xkx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiamen.xkx.R;

//介绍界面
public class IntroductionActivity extends Activity {

    private ImageButton imaBtn_left;
    private ImageButton imaBtn_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        init();
    }

    public void init() {
        View bar = findViewById(R.id.bar);
        imaBtn_left = (ImageButton) bar.findViewById(R.id.bar_left);
        imaBtn_right = (ImageButton) bar.findViewById(R.id.bar_right);
//        TextView tv_title = (TextView) view.findViewById(R.id.bar_title);
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
                Log.e("123", "345");
                Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
