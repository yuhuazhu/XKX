package com.xiamen.xkx.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.brtbeacon.sdk.BRTBeacon;
import com.xiamen.xkx.R;
import com.xiamen.xkx.service.AudioService;
import com.xiamen.xkx.service.BleScanService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 */
public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    protected BleScanService.BleBinder bleBinder;
    HashMap<Integer, Data> map = new HashMap<Integer, Data>();
    private boolean isJiangjiePlaying = false;
    private AudioService.AudioBinder audioBinder;
    private ServiceConnection bleConn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bleBinder = (BleScanService.BleBinder) service;

            bleBinder.setRegion(null);// 空代表扫描所有
            bleBinder.setOnBleScanListener(new BleScanService.OnBleScanListener() {

                @Override
                public void onNearBleChanged(BRTBeacon oriBeacon, BRTBeacon desBeacon) {
                    //noop
                }

                @Override
                public void onPeriodScan(List<BRTBeacon> scanResultList) {
                    //noop
                }

                @Override
                public void onNearBeacon(BRTBeacon brtBeacon) {
                    trigger(brtBeacon);
                }
            });
        }
    };
    private boolean isYinpinPlaying = false;
    private ServiceConnection audioConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audioBinder = (AudioService.AudioBinder) service;
            audioBinder.setOnPlayCompleteListener(new AudioService.OnPlayCompleteListener() {

                @Override
                public void onPlayComplete() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            isJiangjiePlaying = false;
                            isYinpinPlaying = false;
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // noop
        }
    };

    private void trigger(BRTBeacon brtBeacon) {
        //TODO 弹框
        //showInfoDialog(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_map, null);
        setContentView(view);
        //view.setOnTouchListener(new View.OnTouchListener() {
        //    @Override
        //    public boolean onTouch(View v, MotionEvent event) {
        //        final float x = event.getX();
        //        final float y = event.getY();
        //        int bound = 10;
        //        int id = 0;
        //        if (isInRegion(x, y, 100, 100, bound)) {
        //            id = 1;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 2;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 3;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 4;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 5;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 6;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 7;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 8;
        //        } else if (isInRegion(x, y, 200, 200, bound)) {
        //            id = 9;
        //        }
        //        showInfoDialog(id);
        //        return false;
        //    }
        //});

        bindBleScanService();
        bindAudioService();

        initData();
        initView();
    }

    private boolean isInRegion(float x, float y, int cx, int cy, int bound) {
        return (x - bound <= cx || x + bound >= cx) && (y - bound <= cy || y + bound >= cy);
    }

    private void bindAudioService() {
        Intent service = new Intent(MapActivity.this, AudioService.class);
        bindService(service, audioConn, BIND_AUTO_CREATE);
    }

    private void bindBleScanService() {
        Intent service = new Intent(MapActivity.this, BleScanService.class);
        bindService(service, bleConn, BIND_AUTO_CREATE);
    }

    //TODO
    private void initData() {
        //图片
        int[] imageids = {R.mipmap.img_jiangjie1, R.mipmap.img_jiangjie2, R.mipmap.img_jiangjie3, R.mipmap.img_jiangjie4, R.mipmap.img_jiangjie5, R.mipmap.img_jiangjie6, R.mipmap.img_jiangjie7, R.mipmap.img_jiangjie8, R.mipmap.img_jiangjie9};
        // 声音id
        //TODO 声音还没放进来
        int[] audioids = {R.raw.qin_huyouyi1, R.raw.qin_zhonglei2, R.raw.qin_bier3, 0, R.raw.qin_jixie5, 0, 0, R.raw.qin_bagualou8, 0};
        int[] yinpinids = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] titles = {"胡友义", "风琴的种类", "诺曼·比尔风琴", "笙", "机械风琴", "亚历山大簧片风琴", "台式风琴", "八卦楼", "风琴原理"};
        String[] str = getResources().getStringArray(R.array.taici);
        for (int i = 0; i < imageids.length; i++) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + audioids[i]);
            Uri yinpin = Uri.parse("android.resource://" + getPackageName() + "/" + yinpinids[i]);
            if (audioids[i] == 0) {
                uri = null;
            }
            if (yinpinids[i] == 0) {
                yinpin = null;
            }
            Data data = new Data(imageids[i], yinpin, uri, titles[i], str[i]);
            map.put(i + 1, data);
        }
    }

    private void showInfoDialog(final int id) {
        final Data data = map.get(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_acativity, null);
        TextView tvJiangjie = (TextView) view.findViewById(R.id.textView5);
        TextView tvInfo = (TextView) view.findViewById(R.id.textView6);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView3);
        TextView tvTitle = (TextView) view.findViewById(R.id.textView3);
        final ImageView ivClose = (ImageView) view.findViewById(R.id.imageView2);
        final ImageView ivYinpin = (ImageView) view.findViewById(R.id.imageView4);
        final ImageView ivJiangjie = (ImageView) view.findViewById(R.id.imageView5);
        if (data.leftUri == null) {
            ivYinpin.setClickable(false);
        }
        if (data.uri == null) {
            ivJiangjie.setClickable(false);
        }
        ivYinpin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isYinpinPlaying = !isYinpinPlaying;
                isJiangjiePlaying = false;
                //TODO 没有播放状态，播放图标
                ivJiangjie.setBackgroundResource(R.mipmap.ic_play_blue);
                if (isYinpinPlaying) {
                    ivYinpin.setBackgroundResource(R.mipmap.ic_pause_blue);
                    audioBinder.audioPlay(data.leftUri);
                } else {
                    ivYinpin.setBackgroundResource(R.mipmap.ic_play_blue);
                    audioBinder.audioPause();
                }
            }
        });

        ivJiangjie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isJiangjiePlaying = !isJiangjiePlaying;
                isYinpinPlaying = false;
                ivYinpin.setBackgroundResource(R.mipmap.ic_play_blue);
                if (isJiangjiePlaying) {
                    ivJiangjie.setBackgroundResource(R.mipmap.ic_pause_blue);
                    audioBinder.audioPlay(data.uri);
                } else {
                    ivJiangjie.setBackgroundResource(R.mipmap.ic_play_blue);
                    audioBinder.audioPause();
                }
            }
        });

        tvTitle.setText(data.title);
        tvInfo.setText(data.text);
        iv.setImageResource(data.imageID);
        final AlertDialog dialog = builder.setView(view).create();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    isJiangjiePlaying = false;
                    audioBinder.audioStop();
                }
            }
        });

        tvJiangjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.show();
    }

    public void initView() {
        findViewById(R.id.imgbtn1).setOnClickListener(this);
        findViewById(R.id.imgbtn2).setOnClickListener(this);
        findViewById(R.id.imgbtn3).setOnClickListener(this);
        findViewById(R.id.imgbtn4).setOnClickListener(this);
        findViewById(R.id.imgbtn5).setOnClickListener(this);
        findViewById(R.id.imgbtn6).setOnClickListener(this);
        findViewById(R.id.imgbtn7).setOnClickListener(this);
        findViewById(R.id.imgbtn8).setOnClickListener(this);
        findViewById(R.id.imgbtn9).setOnClickListener(this);


        findViewById(R.id.imgbtn_3d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MapActivity.this, Fengqin3dDaolanActivity.class);
                startActivity(intent);
            }
        });
        View bar = findViewById(R.id.bar);
        ImageButton imaBtn_right = (ImageButton) bar.findViewById(R.id.bar_right);
        ImageButton imaBtn_left = (ImageButton) bar.findViewById(R.id.bar_left);
        TextView tv_title = (TextView) bar.findViewById(R.id.bar_title);
        tv_title.setText("风琴博物馆");
        imaBtn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imaBtn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回到首页
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn1:
                showInfoDialog(1);
                break;
            case R.id.imgbtn2:
                showInfoDialog(2);
                break;
            case R.id.imgbtn3:
                showInfoDialog(3);
                break;
            case R.id.imgbtn4:
                showInfoDialog(4);
                break;
            case R.id.imgbtn5:
                showInfoDialog(5);
                break;
            case R.id.imgbtn6:
                showInfoDialog(6);
                break;
            case R.id.imgbtn7:
                showInfoDialog(7);
                break;
            case R.id.imgbtn8:
                showInfoDialog(8);
                break;
            case R.id.imgbtn9:
                showInfoDialog(9);
                break;
        }
    }

    private Uri getAudioUri(int id) {
        String parent = "android.resource://" + getPackageName() + "/";
        return Uri.parse(parent);
    }

    //TODO 播放与暂停,界面控制
    private void processPalyAndUI(int id) {
        isJiangjiePlaying = !isJiangjiePlaying;
        if (isJiangjiePlaying) {
            //播放
            audioBinder.audioPlay(getAudioUri(id));
        } else {
            //停止
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 摇一摇震动
        unbindService(bleConn);
        unbindService(audioConn);
        //sensorManager.unregisterListener(sensorEventListener);
    }


    class Data {
        int imageID;
        Uri uri;
        Uri leftUri;
        String text;
        String title;

        public Data(int imageID, Uri yinpin, Uri uri, String title, String text) {
            this.imageID = imageID;
            this.leftUri = yinpin;
            this.uri = uri;
            this.title = title;
            this.text = text;
        }
    }
}
