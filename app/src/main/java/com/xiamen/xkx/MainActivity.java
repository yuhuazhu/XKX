package com.xiamen.xkx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_shake;                     //摇一摇图标
    private ImageButton imgBtn_scenic;              //选择景点按钮
    private ImageButton imgBtn_photo;               //全身按摩按钮
    private ImageButton imgBtn_massage;             //全身按摩按钮
    private ImageButton imgBtn_service;             //服务按钮
    private boolean isShowService;                  //是否显示服务内容
    private int animationCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //初始化控件
    public void initView() {
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
        TranslateAnimation translate_right = new TranslateAnimation(0, 300, 0, 0);
        // 设置动画持续时间
        translate_right.setDuration(600);
        translate_right.setFillAfter(true);
        imgBtn_photo.setAnimation(translate_right);
        imgBtn_massage.setAnimation(translate_right);
    }

    //显示服务内容动画
    public void buttonIn() {
        TranslateAnimation translate_left = new TranslateAnimation(300, 0, 0, 0);
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
                Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_SHORT).show();
                //跳转到景区列表界面
                shakeShake();
                Intent Intent = new Intent();
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
        }
    }
}
