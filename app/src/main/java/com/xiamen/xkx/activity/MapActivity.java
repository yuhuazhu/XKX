package com.xiamen.xkx.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class MapActivity extends Activity implements View.OnClickListener {

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
                            //TODO
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
    private boolean isYinpinPlaying = false;

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
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                int bound = 10;
                int id = 0;
                if (isInRegion(x, y, 100, 100, bound)) {
                    id = 1;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 2;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 3;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 4;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 5;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 6;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 7;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 8;
                } else if (isInRegion(x, y, 200, 200, bound)) {
                    id = 9;
                }
                showInfoDialog(id);
                return false;
            }
        });

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
        int[] imageids = {R.mipmap.ic_back, R.
                mipmap.ic_back, R.mipmap.ic_back, R.mipmap.ic_back, R.mipmap.ic_back};
        // 声音id
        int[] audioids = {R.raw.zi_xing_che_zu_lin, R.raw.zi_xing_che_zu_lin, R.raw.zi_xing_che_zu_lin, R.raw.zi_xing_che_zu_lin};
        int[] yinpinids = {R.raw.zi_xing_che_zu_lin, R.raw.zi_xing_che_zu_lin, R.raw.zi_xing_che_zu_lin, R.raw.zi_xing_che_zu_lin};
        String[] titles = {"abc", "abc", "abc", "abc", "abc", "abc", "abc"};
        String[] str = {"abc", "abc", "abc", "abc", "abc", "abc", "abc"};
        for (int i = 0; i < imageids.length; i++) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + audioids[i]);
            Uri yinpin = Uri.parse("android.resource://" + getPackageName() + "/" + yinpinids[i]);
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
        ImageView ivClose = (ImageView) view.findViewById(R.id.imageView2);
        ImageView ivYinpin = (ImageView) view.findViewById(R.id.imageView4);
        final ImageView ivJiangjie = (ImageView) view.findViewById(R.id.imageView5);
        if (id >= 3) {
            ivYinpin.setClickable(false);
        }
        ivYinpin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isYinpinPlaying = !isYinpinPlaying;
                isJiangjiePlaying = false;
                //播放图标
                //ivJiangjie.setImageResource(R.mipmap.);
                if (isYinpinPlaying) {
                    //TODO CHANGE ICON
                    //ivYinpin.setImageResource(R.mipmap.);
                    audioBinder.audioPlay(data.leftUri);
                } else {
                    //ivYinpin.setImageResource(R.mipmap.);
                    audioBinder.audioPause();
                }
            }
        });

        ivJiangjie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isJiangjiePlaying = !isJiangjiePlaying;
                isYinpinPlaying = false;
                //播放图标
                //ivYinpin.setImageResource(R.mipmap.);
                if (isJiangjiePlaying) {
                    //TODO CHANGE ICON
                    //ivJiangjie.setImageResource(R.mipmap.);
                    audioBinder.audioPlay(data.uri);
                } else {
                    //ivJiangjie.setImageResource(R.mipmap.);
                    audioBinder.audioPause();
                }
            }
        });

        tvTitle.setText(data.title);
        tvInfo.setText(data.text);
        iv.setImageResource(data.imageID);
        final AlertDialog dialog = builder.create();
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
        builder.setView(view);
    }

    public void initView() {
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
        }

    }

    private Uri getAudioUri(int id) {
        String parent = "android.resource://" + getPackageName() + "/";
        if (id == 1) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 2) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 3) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 4) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 5) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 6) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 7) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 8) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else if (id == 9) {
            parent += R.raw.zi_xing_che_zu_lin;
        } else {
            throw new IllegalArgumentException("audio id not exist");
        }
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
