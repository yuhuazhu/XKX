package com.xiamen.xkx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xiamen.xkx.R;

/**
 * Created by Administrator on 2015/8/11.
 * 景区位置
 */
public class LocationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
    }

    public void init() {
        View bar = findViewById(R.id.bar);
        ImageButton imaBtn_left = (ImageButton) bar.findViewById(R.id.bar_left);
        ImageButton imaBtn_right = (ImageButton) bar.findViewById(R.id.bar_right);
        TextView tv_title = (TextView) bar.findViewById(R.id.bar_title);
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
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tv_title.setText("位置");
    }
}
