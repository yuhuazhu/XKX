package com.xiamen.xkx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiamen.xkx.R;

/**
 * Created by Administrator on 2015/8/17.
 * //设备租赁界面
 */
public class LeaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease);
    }

    public void startIntorduction(View v) {
        Intent intent = new Intent(LeaseActivity.this, DeviceIntroductionActivity.class);
        startActivity(intent);
    }

    public void startHire(View v) {
        Intent intent = new Intent(LeaseActivity.this, DeviceHireActivity.class);
        startActivity(intent);
    }

    public void startOrder(View v) {
        Intent intent = new Intent(LeaseActivity.this, OrderTrackingActivity.class);
        startActivity(intent);
    }
}
