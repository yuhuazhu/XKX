package com.xiamen.xkx.custom;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiamen.xkx.R;

//自定义提示窗
public class TipView extends LinearLayout {

    private String tipText;
    private String leftText;
    private String rightText;
    private TextView tv;
    private Button btn_leftText;
    private Button btn_rightText;
    private int width_left;
    private int height_left;
    private int width_right;
    private int height_right;

    public TipView(Context context, String tipText, String leftText, String rightText) {
        super(context);
        this.tipText = tipText;
        this.leftText = leftText;
        this.rightText = rightText;
        tv = new TextView(context);
        btn_leftText = new Button(context);
        btn_rightText = new Button(context);
        tv.setText(tipText);
        btn_leftText.setText(leftText);
        btn_rightText.setText(rightText);
        btn_leftText.setBackgroundResource(R.mipmap.img_tip_ok);
        btn_rightText.setBackgroundResource(R.mipmap.img_tip_cancer);
        width_left = btn_leftText.getWidth();
        height_left = btn_leftText.getHeight();
        width_right = btn_rightText.getWidth();
        height_right = btn_rightText.getHeight();
        addView(tv);
        addView(btn_leftText);
        addView(btn_rightText);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
        tv.layout(r / 2, 0, r, b);
        btn_leftText.layout(l + 50, b - t - height_right - 50, l + 50 + width_left, b - t - height_right - 50 + height_left);
        btn_leftText.layout(l + 100 + width_left, b - t - height_right - 50, l + 100 + width_left + width_right, b - t - height_right - 50 + height_right);
    }
}
