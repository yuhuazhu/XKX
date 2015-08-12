package com.xiamen.xkx.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xiamen.xkx.R;
import com.xiamen.xkx.adapter.BaseListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhotoUploadActivity extends AppCompatActivity implements OnClickListener {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options; // 配置图片加载及显示选项
    List<Object> imglist = new ArrayList<Object>();
    /**
     * 存储图片地址
     */
    private ArrayList<String> listImgPath;
    private String[] imageUriArray;
    private MyAdapter myAdapter;
    private List<Map<String, Object>> list;
    private String uploadFile = "/sdcard/image.JPG";
    private String actionUrl = "http://www.xmlyt.cn/ajax/Statistics.ashx?sn=addUserPic";
    private String newName = "image.jpg";
    private String QRCODE = "";
    private String position = "";
    private StringBuffer b;

    private Boolean selflag = false;

    private TextView tvUpload;
    private TextView cancel;
    private GridView gridphoto;
    private ImageButton imgbtn_back;
    private RelativeLayout progresslay;
    private ProgressBar progress_horizontal;
    private LocationTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoupload);
        initData();
        initImageLoader(this);
        initUI();
    }

    private void initUI() {
        tvUpload = (TextView) findViewById(R.id.tv_upload);
        cancel = (TextView) findViewById(R.id.tv_cancel);
        progresslay = (RelativeLayout) findViewById(R.id.progresslay);
        progress_horizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
        imgbtn_back = (ImageButton) findViewById(R.id.imgbtn_back);
        gridphoto = (GridView) findViewById(R.id.gridphoto);

        tvUpload.setOnClickListener(this);
        cancel.setOnClickListener(this);
        imgbtn_back.setOnClickListener(this);
        myAdapter = new MyAdapter();
        gridphoto.setAdapter(myAdapter);
        gridphoto.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                position = String.valueOf(arg2);
                myAdapter.setSelectItem(arg2);
                if (selflag) {
                    position = "";
                    selflag = false;
                } else {
                    selflag = true;
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upload:
                if (position.equals("")) {
                    showDialog("请选择一张照片!");
                    return;
                }
                progress_horizontal.setProgress(0);
                progresslay.setVisibility(View.VISIBLE);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("arg2", position);
                task = new LocationTask();
                task.execute(params);
                break;
            case R.id.tv_cancel:
            case R.id.imgbtn_back:
                if (task != null) {
                    task.cancel(true);
                }
                finish();
                break;
        }
    }

    private void initData() {
        // 扫描内存中图片并存入list
        listImgPath = getImgPathList();
        if (listImgPath.size() >= 1) {
            imageUriArray = (String[]) listImgPath.toArray(new String[listImgPath.size()]);
        }
    }

    /**
     * 获取图片地址列表
     *
     * @return list
     */
    private ArrayList<String> getImgPathList() {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, null, null, null);
        while (cursor.moveToNext()) {
            imglist.add(R.mipmap.img_photo_selected);
            list.add(cursor.getString(1));// 将图片路径添加到list中
        }
        cursor.close();
        return list;
    }

    /* 上传文件至Server的方法 */
    private String uploadFile(String str) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false); /* 设置传送的method=POST */
            con.setRequestMethod("POST"); /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); /* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end); /* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(str); /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1; /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) { /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end); /*
                                                                     * close
																	 * streams
																	 */
            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            } /* 将Response显示于Dialog */
            // Toast.makeText(this, "上传成功", 3000).show();

            ds.close();
        } catch (Exception e) {
            // showDialog("上传失败" + e);
        }
        return b.toString();
    }

    /* 显示Dialog的method */
    private void showDialog(String msg) {
        new AlertDialog.Builder(PhotoUploadActivity.this).setTitle("提示").setMessage(msg).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!position.equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(PhotoUploadActivity.this, PhotoScanningActivity.class);
                    intent.putExtra("QRCODE", QRCODE);
                    QRCODE = "";
                    position = "";
                    selflag = false;
                    myAdapter.notifyDataSetChanged();
                    startActivity(intent);
                }
            }
        }).show();
    }


    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        // 配置图片加载及显示选项（还有一些其他的配置，查阅doc文档吧）
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.ic_launcher) // 在ImageView加载过程中显示图片
                .showImageForEmptyUri(R.mipmap.ic_launcher) // image连接地址为空时
                .showImageOnFail(R.mipmap.ic_launcher) // image加载失败
                .cacheInMemory(true) // 加载图片时会在内存中加载缓存
                .cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存
                .build();
    }

    /**
     * 图片加载监听事件 *
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500); // 设置image隐藏动画500ms
                    displayedImages.add(imageUri); // 将图片uri添加到集合中
                }
            }
        }
    }

    /**
     * @Title:
     * @Description:listview适配器
     * @Copyright: Copyright (c) 2014
     * @Company:
     * @author: Administrator
     * @version: 1.0.0.0
     * @Date: 2014-2-14
     */
    class AdapterList extends BaseListAdapter {
        // private ImageLoadingListener animateFirstListener = new
        // AnimateFirstDisplayListener();

        private int selectItem = -1;

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                // 初始化绑定控件
                convertView = getLayoutInflater().inflate(R.layout.activity_griditem, null);
                holder.imgShow = (ImageView) findViewById(R.id.img01);
                // add to convertView
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置img，text具体显示的内容

            // holder.imgShow.setBackground(getResources().getDrawable(R.id.imgbtn_back));
            return convertView;
        }

        /**
         * 适配器
         */
        private class ViewHolder {

            private ImageView imgShow;

        }
    }

    private class LocationTask extends AsyncTask<HashMap<String, String>, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvUpload.setEnabled(false);
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            // TODO Auto-generated method stub
            String str = uploadFile(imageUriArray[Integer.valueOf(params[0].get("arg2"))]);
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i * 10);// 进度条每次更新10%,执行中创建新线程处理onProgressUpdate()
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return str;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            // Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
            progress_horizontal.setProgress(progresses[0]);
            // textView.setText("loading..." + progresses[0] + "%");
        }

        @Override
        protected void onPostExecute(String Signinfo) {
            tvUpload.setEnabled(true);
            try {
                JSONObject jsonObject = new JSONObject(Signinfo);
                if (jsonObject != null) {
                    if (jsonObject.getString("code").equals("0000")) {
                        QRCODE = jsonObject.getJSONObject("result").optString("QRCODE");
                        if (QRCODE.equals("")) {
                            return;
                        } else {
                            showDialog("上传成功");
                            progress_horizontal.setProgress(100);
                            progresslay.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyAdapter extends BaseListAdapter {

        private int selectItem = -1;

        @SuppressWarnings("unused")
        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageUriArray.length;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder;
            if (convertView == null) {

                convertView = getLayoutInflater().inflate(R.layout.activity_griditem, null);

                holder = new ViewHolder();

                holder.img01 = (ImageView) convertView.findViewById(R.id.img01);
                holder.img02 = (ImageView) convertView.findViewById(R.id.img02);

                // 设置交错颜色
                // int[] arrayOfInt = mColors;
                // int colorLength = mColors.length;
                // int selected = arrayOfInt[position % colorLength];
                //
                // convertView.setBackgroundResource(selected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Bitmap bit
            // =BitmapUtils.getLoacalBitmapByAssets(PhotoUploadActivity.this,"file:/"+imageUriArray[position]);
            // holder.img01.setImageBitmap(bit);
            // //holder.img01.setBackgroundDrawable(getResources().getDrawable(R.id.imgbtn_back));

            imageLoader.displayImage("file:/" + imageUriArray[position], holder.img01, options);
            if (position == selectItem) { // 选中状态 高亮
                holder.img02.setBackgroundResource(R.mipmap.img_photo_selected);
                holder.img02.setVisibility(selflag ? View.VISIBLE : View.GONE);// 设置删除按钮是否显示
            } else { // 正常状态
                holder.img02.setVisibility(View.GONE);

            }
            return convertView;
        }

        /**
         * 适配器
         */
        private class ViewHolder {
            private ImageView img01;
            private ImageView img02;
        }
    }
}
