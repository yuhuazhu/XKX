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
import android.view.Window;

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
