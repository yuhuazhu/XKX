package com.xiamen.xkx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_shake;                  //摇一摇图标
    private ImageButton imgBtn_select_scenic;    //选择景点按钮
    private ImageButton imgBtn_massage;          //全身按摩按钮
    private ImageButton imgBtn_service;          //服务按钮
//    private boolean isService = false;
    private boolean isService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //初始化控件
    public void initView() {
        iv_shake = (ImageView) findViewById(R.id.img_shake);
        imgBtn_select_scenic = (ImageButton) findViewById(R.id.imgBtn_select_scenic);
        imgBtn_massage = (ImageButton) findViewById(R.id.imgBtn_massage);
        imgBtn_service = (ImageButton) findViewById(R.id.imgBtn_service);
    }

    public void buttonOut() {
        TranslateAnimation translate_right = new TranslateAnimation(1, 1, 1, 1);
        imgBtn_select_scenic.setAnimation(translate_right);
        imgBtn_massage.setAnimation(translate_right);
    }

    public void buttonIn() {
        TranslateAnimation translate_left = new TranslateAnimation(1, 1, 1, 1);
        imgBtn_select_scenic.setAnimation(translate_left);
        imgBtn_massage.setAnimation(translate_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_service:
                if (isService) {
//                    imgBtn_service.setBackground();
                    buttonOut();
                } else {
//                    imgBtn_service.setBackground();
                    buttonIn();
                }
                isService = !isService;
        }
    }
}
