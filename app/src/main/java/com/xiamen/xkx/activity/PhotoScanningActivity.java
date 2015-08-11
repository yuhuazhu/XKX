package com.xiamen.xkx.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xiamen.xkx.R;
import com.xiamen.xkx.utils.ImageUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PhotoScanningActivity extends AppCompatActivity implements OnClickListener {
    private String url;
    private ImageView img;
    private ImageButton btnback;

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 移除ActionBar，在setContent之前调用下面这句，保证没有ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photoscan);
        initData();
        initUI();
    }

    public void home(View v) {
        Intent intent = null;

        intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private void initData() {

        url = getIntent().getStringExtra("QRCODE");
    }

    private void initUI() {
        // 得到可用的图片
        img = (ImageView) this.findViewById(R.id.img);
        findViewById(R.id.imgbtn_back).setOnClickListener(this);
        URL l;
        try {
            l = new URL(url);
            // 显示
            com.xiamen.xkx.utils.ImageUtils.onLoadImage(l, new ImageUtils.OnLoadImageListener() {

                @Override
                public void OnLoadImage(Bitmap bitmap, String bitmapPath) {
                    if (bitmap != null) {
                        img.setImageBitmap(bitmap);
                    }

                }
            });
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // new Thread(new Runnable(){
        //
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // URL imageUrl = url;
        // try {
        // HttpURLConnection conn = (HttpURLConnection)
        // imageUrl.openConnection();
        // InputStream inputStream = conn.getInputStream();
        // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        // Message msg = new Message();
        // msg.obj = bitmap;
        // handler.sendMessage(msg);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // }
        //
        // }).start();

        // img = new ImageView(this);
        // LinearLayout l = new LinearLayout(this);
        // LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
        // LayoutParams.FILL_PARENT);
        // l.addView(img);
        // addContentView(l, params);
        // new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        //
        // //图片资源
        // // 25. String url =
        // "http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
        // // 26. //得到可用的图片
        // // 27. Bitmap bitmap = getHttpBitmap(url);
        // // 28. imageView = (ImageView)this.findViewById(R.id.imageViewId);
        // // 29. //显示
        // // 30. imageView.setImageBitmap(bitmap);
        //
        // // TODO Auto-generated method stub
        // Drawable drawable = LoadImageFromWebOperations(url);
        // img.setImageDrawable(drawable);
        // }
        // }).start();

    }

    private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            System.out.println("Exc=" + e);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            default:
                break;
        }
    }

}
