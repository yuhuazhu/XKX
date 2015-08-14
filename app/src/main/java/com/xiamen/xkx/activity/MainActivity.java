package com.xiamen.xkx.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.brtbeacon.sdk.BRTBeacon;
import com.xiamen.xkx.R;
import com.xiamen.xkx.service.BleScanService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_shakeshake;                //摇一摇文字
    private ImageView iv_shake;                     //摇一摇图标
    private ImageButton imgBtn_scenic;              //选择景点按钮
    private ImageButton imgBtn_photo;               //照片冲印按钮
    private ImageButton imgBtn_3d_photo;               //3D拍照按钮
    private ImageButton imgBtn_massage;             //全身按摩按钮
    private ImageButton imgBtn_service;             //服务按钮
    private boolean isShowService;                  //是否显示服务内容
    private int animationCount = 5;
    private int width;                              // 屏幕宽度（像素）
    private int height;                             // 屏幕高度（像素）
    private AlertDialog dialog;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private final int MSG_SENSOR_SHAKE = 10;

    private BleScanService.BleBinder bleBinder;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("scan", "onServiceDisconnected()");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("scan", "onServiceConnected");
            bleBinder = (BleScanService.BleBinder) service;

            bleBinder.setRegion(null);// 空代表扫描所有
            bleBinder.setOnBleScanListener(new BleScanService.OnBleScanListener() {

                @Override
                public void onPeriodScan(List<BRTBeacon> scanResultList) {
                }

                @Override
                public void onNearBleChanged(BRTBeacon oriBeacon,
                                             BRTBeacon desBeacon) {
                    Log.e("scan", "onNearBleChanged,current:"
                            + desBeacon.macAddress);
                }

                @Override
                public void onNearBeacon(BRTBeacon brtBeacon) {
                    if (brtBeacon != null) {
                        showTipDialog("检测到您当前位置：鼓浪屿景区，是否进入", 1);
                    } else {
                        Toast.makeText(MainActivity.this, "这次没有扫描到噢", Toast.LENGTH_SHORT).show();
                    }
                    unbindService(conn);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
        initView();
        try {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                showTipDialog("", 0);
            }
        } catch (Exception e) {
            Toast.makeText(this, "您的手机没有蓝牙", Toast.LENGTH_SHORT).show();
        }


        //模拟器测试，真机删除
//        showTipDialog("", 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            iv_shake.clearAnimation();
            sensorManager.unregisterListener(sensorEventListener);
            unbindService(conn);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
        try {
            unbindService(conn);
        } catch (Exception e) {

        }
    }

    //初始化控件
    public void initView() {
        iv_shakeshake = (ImageView) findViewById(R.id.img_shakeshake);
        iv_shake = (ImageView) findViewById(R.id.img_shake);
        imgBtn_scenic = (ImageButton) findViewById(R.id.imgBtn_select_scenic);
        imgBtn_photo = (ImageButton) findViewById(R.id.imgBtn_photo);
        imgBtn_3d_photo = (ImageButton) findViewById(R.id.imgBtn_3d_photo);
        imgBtn_massage = (ImageButton) findViewById(R.id.imgBtn_massage);
        imgBtn_service = (ImageButton) findViewById(R.id.imgBtn_service);
        iv_shake.setOnClickListener(this);
        imgBtn_scenic.setOnClickListener(this);
        imgBtn_service.setOnClickListener(this);
        imgBtn_photo.setOnClickListener(this);
        imgBtn_3d_photo.setOnClickListener(this);
        imgBtn_massage.setOnClickListener(this);
        iv_shakeshake.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (sensorManager != null) {// 注册监听器
            sensorManager
                    .registerListener(
                            sensorEventListener,
                            sensorManager
                                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            if (Math.abs(x) > 15 || Math.abs(y) > 15 || Math.abs(z) > 15) {
                Log.i("12344567", "x轴" + x + "；y轴" + y + "；z轴" + z);
            }
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
                    || Math.abs(z) > medumValue) {
                vibrator.vibrate(400);
                Message msg = new Message();
                msg.what = MSG_SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SENSOR_SHAKE:
                    Intent service = new Intent(MainActivity.this, BleScanService.class);
                    bindService(service, conn, BIND_AUTO_CREATE);
                    shakeShake();
                    break;
            }
        }
    };

    //摇一摇动画
    public void shakeShake() {
        final AnimationSet animationSet = new AnimationSet(true);
        //左右移动多次的动画
        TranslateAnimation animation1 = new TranslateAnimation(0, 30, 0, 0);
        TranslateAnimation animation2 = new TranslateAnimation(0, -60, 0, 0);
        TranslateAnimation animation3 = new TranslateAnimation(0, 60, 0, 0);
        TranslateAnimation animation4 = new TranslateAnimation(0, -30, 0, 0);
        // 设置动画持续时间
        animation1.setDuration(50);
        animation2.setDuration(100);
        animation3.setDuration(100);
        animation4.setDuration(50);
        // 延迟播放
        animation2.setStartOffset(50);
        animation3.setStartOffset(100);
        animation4.setStartOffset(100);
        //把多次的移动动画加入Set
        animationSet.addAnimation(animation1);
        animationSet.addAnimation(animation2);
        animationSet.addAnimation(animation3);
        animationSet.addAnimation(animation4);
//        animationSet.setInterpolator(new LinearInterpolator());
        iv_shake.startAnimation(animationSet);
        iv_shake.setBackgroundResource(R.mipmap.img_shake_run);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //设置重复播放动画没用，只好监听重播
                if (animationCount-- != 0) {
                    iv_shake.startAnimation(animationSet);
                } else {
                    iv_shake.clearAnimation();
                    iv_shake.setBackgroundResource(R.mipmap.img_shake);
                    animationCount = 5;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showTipDialog(String content, int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_activity_main_enable_ble, null);
        TextView tv_tip = (TextView) v.findViewById(R.id.tv_tip);
        Button img_btn_ok = (Button) v.findViewById(R.id.img_btn_ok);
        Button img_btn_cancer = (Button) v.findViewById(R.id.img_btn_cancer);
        dialog = builder.setView(v).create();
        dialog.show();
        if (!content.equals("")) {
            tv_tip.setText(content);
        }
        switch (mode) {
            case 0:
                img_btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BluetoothAdapter.getDefaultAdapter().enable();
                        dialog.dismiss();
                    }
                });
                break;
            case 1:
                img_btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到地图
                        Intent intent = new Intent(MainActivity.this, MapActivity.class);
//                        intent.putExtra("name", "鼓浪屿风琴博物馆");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        }
        img_btn_cancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //隐藏服务内容动画
    public void buttonOut() {
        TranslateAnimation translate_right = new TranslateAnimation(0, width / 5 * 3, 0, 0);
        // 设置动画持续时间
        translate_right.setDuration(600);
        translate_right.setFillAfter(true);
        imgBtn_photo.setAnimation(translate_right);
        imgBtn_3d_photo.setAnimation(translate_right);
        imgBtn_massage.setAnimation(translate_right);
    }

    //显示服务内容动画
    public void buttonIn() {
        TranslateAnimation translate_left = new TranslateAnimation(width / 5 * 3, 0, 0, 0);
        translate_left.setDuration(600);
        translate_left.setFillAfter(true);
        imgBtn_photo.setAnimation(translate_left);
        imgBtn_3d_photo.setAnimation(translate_left);
        imgBtn_massage.setAnimation(translate_left);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_shake:
                vibrator.vibrate(400);
                Intent service = new Intent(MainActivity.this, BleScanService.class);
                bindService(service, conn, BIND_AUTO_CREATE);
                shakeShake();
                break;
            case R.id.imgBtn_select_scenic:
                //跳转到景区列表界面
                intent = new Intent(MainActivity.this, JingquListActivity.class);
                intent.putExtra("name", "鼓浪屿风琴博物馆");
                startActivity(intent);
                break;
            case R.id.imgBtn_photo:
                intent = new Intent(MainActivity.this, PhotoWashActivity.class);
                startActivity(intent);
                break;
            case R.id.imgBtn_3d_photo:
                try {
                    String pkgName = "com.mgd.zcg3d";
                    String launcherActivity = "com.unity3d.player.UnityPlayerNativeActivity";
                    ComponentName component = new ComponentName(pkgName, launcherActivity);
                    intent = new Intent();
                    intent.setComponent(component);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "请安装郑成功app", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgBtn_service:
                if (isShowService) {
                    imgBtn_service.setBackgroundResource(R.drawable.selector_service);
                    imgBtn_photo.setVisibility(View.INVISIBLE);
                    imgBtn_3d_photo.setVisibility(View.INVISIBLE);
                    imgBtn_massage.setVisibility(View.INVISIBLE);
                    buttonOut();
                } else {
                    imgBtn_service.setBackgroundResource(R.drawable.selector_service_back);
                    imgBtn_photo.setVisibility(View.VISIBLE);
                    imgBtn_3d_photo.setVisibility(View.VISIBLE);
                    imgBtn_massage.setVisibility(View.VISIBLE);
                    buttonIn();
                }
                isShowService = !isShowService;
                break;
            case R.id.imgBtn_massage:
                // 跳转到按摩界面
                // 通过包名获取要跳转的app，创建intent对象
                intent = getPackageManager().getLaunchIntentForPackage(
                        "com.ebwing.mass");
                // 这里如果intent为空，就说名没有安装要跳转的应用嘛
                if (intent != null) {
                    // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
                    intent.putExtra("name", "XiaKeXing");
                    intent.putExtra("app", "123456");
                    startActivity(intent);
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                    //Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧",
                    //Toast.LENGTH_LONG).show();
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://www.ebwing.com/download/appindex.do#");
                    intent.setData(content_url);
                    startActivity(intent);

                }
        }
    }
}
