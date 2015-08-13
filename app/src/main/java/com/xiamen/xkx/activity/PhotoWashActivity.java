package com.xiamen.xkx.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.xiamen.xkx.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class PhotoWashActivity extends AppCompatActivity implements OnClickListener {
    private String fileName = "";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photowash);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.imgbtn_back).setOnClickListener(this);
        findViewById(R.id.imgbtn_home).setOnClickListener(this);
        findViewById(R.id.tv_gallery).setOnClickListener(this);
        findViewById(R.id.tv_take_photo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.imgbtn_home:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_gallery:
                intent.setClass(this, PhotoUploadActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_take_photo:
                //intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                ////这个参数是确定要选择的内容为图片
                //intent.setType("image/*");
                ////这个参数 不太懂，唯一知道的是：设置了参数，就会调用裁剪，如果不设置，就会跳过裁剪的过程。
                //intent.putExtra("crop", "true");
                ////这个是裁剪时候的 裁剪框的 X 方向的比例。
                //intent.putExtra("aspectX", 1);
                ////同上Y 方向的比例. (注意： aspectX, aspectY ，两个值都需要为 整数，如果有一个为浮点数，就会导致比例失效。)
                //intent.putExtra("aspectY", 1);
                ////返回数据的时候的 X 像素大小。
                //intent.putExtra("outputX", 80);
                ////返回的时候 Y 的像素大小。
                //intent.putExtra("outputY", 80);
                ////是否要返回值。 一般都要。
                //intent.putExtra("return-data", true);
                //startActivityForResult(intent, 0);


                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = imageUri;
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile", "SD card is not avaiable/writeable right now.");
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            // Toast.makeText(this, name, Toast.LENGTH_LONG).show();
            // Bundle bundle = data.getExtras();
            ContentResolver cr = getContentResolver();
            Bitmap bitmap = null;
            try {
                bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            // 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            // ???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
            File file = new File("/sdcard/myImage/");
            if (!file.exists()) {
                file.mkdir();// 创建文件夹
            }
            // fileName = "/sdcard/myImage/" + name;
            File imagefile = new File(file, name);
            if (!imagefile.exists()) {
                try {
                    imagefile.createNewFile();
                    b = new FileOutputStream(imagefile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                    b.flush();
                    b.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent();
            intent.setClass(this, PhotoTakeActivity.class);
            intent.putExtra("Uri", selectedImage);
            intent.putExtra("fileName", selectedImage.getPath());
            startActivity(intent);
            // ((ImageView)
            // findViewById(R.id.imageView1)).setImageBitmap(bitmap);//
            // 将图片显示在ImageView里
        }
    }
}
