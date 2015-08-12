package com.xiamen.xkx.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiamen.xkx.R;
import com.xiamen.xkx.custom.TipView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_shakeshake;                //摇一摇文字
    private ImageView iv_shake;                     //摇一摇图标
    private ImageButton imgBtn_scenic;              //选择景点按钮
    private ImageButton imgBtn_photo;               //全身按摩按钮
    private ImageButton imgBtn_massage;             //全身按摩按钮
    private ImageButton imgBtn_service;             //服务按钮
    private boolean isShowService;                  //是否显示服务内容
    private int animationCount = 5;
    private int width;                              // 屏幕宽度（像素）
    private int height;                             // 屏幕高度（像素）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
        initView();
    }

    //初始化控件
    public void initView() {
        iv_shakeshake = (ImageView) findViewById(R.id.img_shakeshake);
        iv_shake = (ImageView) findViewById(R.id.img_shake);
        imgBtn_scenic = (ImageButton) findViewById(R.id.imgBtn_select_scenic);
        imgBtn_photo = (ImageButton) findViewById(R.id.imgBtn_photo);
        imgBtn_massage = (ImageButton) findViewById(R.id.imgBtn_massage);
        imgBtn_service = (ImageButton) findViewById(R.id.imgBtn_service);
        iv_shake.setOnClickListener(this);
        imgBtn_scenic.setOnClickListener(this);
        imgBtn_service.setOnClickListener(this);
        imgBtn_photo.setOnClickListener(this);
        imgBtn_massage.setOnClickListener(this);
        iv_shakeshake.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finish();
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                return false;
            }
        });
//        RelativeLayout layout = new RelativeLayout(this);
//        TipView tipView = new TipView(this,"获取位置信息，申请打开蓝牙","确定","取消");
//        tipView.setBackgroundResource(R.mipmap.img_tip_bg);
//        int widths = tipView.getWidth();
//        int heights = tipView.getHeight();
//        TextView tv = new TextView(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(100, 500, 200, 700);
//        tipView.setLayoutParams(params);
//        tv.setText("213123123");
//        tv.setLayoutParams(params);
//        layout.addView(tipView);
//        addContentView(layout, params);
    }


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

    //隐藏服务内容动画
    public void buttonOut() {
        TranslateAnimation translate_right = new TranslateAnimation(0, width / 5 * 3, 0, 0);
        // 设置动画持续时间
        translate_right.setDuration(600);
        translate_right.setFillAfter(true);
        imgBtn_photo.setAnimation(translate_right);
        imgBtn_massage.setAnimation(translate_right);
    }

    //显示服务内容动画
    public void buttonIn() {
        TranslateAnimation translate_left = new TranslateAnimation(width / 5 * 3, 0, 0, 0);
        translate_left.setDuration(600);
        translate_left.setFillAfter(true);
        imgBtn_photo.setAnimation(translate_left);
        imgBtn_massage.setAnimation(translate_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_shake:
                shakeShake();
                break;
            case R.id.imgBtn_select_scenic:
                //跳转到景区列表界面
                Intent intent = new Intent(MainActivity.this, JingquListActivity.class);
                intent.putExtra("name", "鼓浪屿风琴博物馆");
                startActivity(intent);
                break;
            case R.id.imgBtn_service:
                if (isShowService) {
                    imgBtn_service.setBackgroundResource(R.mipmap.img_service);
                    imgBtn_photo.setVisibility(View.INVISIBLE);
                    imgBtn_massage.setVisibility(View.INVISIBLE);
                    buttonOut();
                } else {
                    imgBtn_service.setBackgroundResource(R.mipmap.img_service_back);
                    imgBtn_photo.setVisibility(View.VISIBLE);
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
//				Toast.makeText(getApplicationContext(), "哟，赶紧下载安装这个APP吧",
//						Toast.LENGTH_LONG).show();
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://www.ebwing.com/download/appindex.do#");
                    intent.setData(content_url);
                    startActivity(intent);

                }
                // intent.setAction("")
                // intent.setClass(getApplication(), MassageActivity.class);
                // startActivity(intent);
        }
    }
}
