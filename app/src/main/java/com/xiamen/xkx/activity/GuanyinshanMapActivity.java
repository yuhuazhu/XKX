package com.xiamen.xkx.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
public class GuanyinshanMapActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_map);

        bindBleScanService();
        bindAudioService();

        initData();
        initView();
    }

    private boolean isInRegion(float x, float y, int cx, int cy, int bound) {
        return (x - bound <= cx || x + bound >= cx) && (y - bound <= cy || y + bound >= cy);
    }

    private void bindAudioService() {
        Intent service = new Intent(GuanyinshanMapActivity.this, AudioService.class);
        bindService(service, audioConn, BIND_AUTO_CREATE);
    }

    private void bindBleScanService() {
        Intent service = new Intent(GuanyinshanMapActivity.this, BleScanService.class);
        bindService(service, bleConn, BIND_AUTO_CREATE);
    }

    //TODO
    private void initData() {
        //图片
        int[] imageids = {R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan, R.mipmap.img_guanyinshan};
        // 声音id
        int[] audioids = {R.raw.g_yin_dao_tai, R.raw.g_tiyan3d, R.raw.g_ying_yong_zhan_shi_qu, R.raw.g_you_ke_jie_dai, R.raw.g_xin_xi_bo_fang_ping, R.raw.g_you_ke_shang_che_qu, R.raw.g_lv_you_zi_zhu_fu_wu_qu, R.raw.g_yi_wu_shi, R.raw.g_ban_shou_li_chao_shi};
        int[] yinpinids = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        String[] titles = getResources().getStringArray(R.array.guanyinshan_title);
        String[] str = getResources().getStringArray(R.array.guanyinshan_text);
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
                ivJiangjie.setBackgroundResource(R.mipmap.ic_play_blue);
                if (isYinpinPlaying) {
                    ivYinpin.setBackgroundResource(R.mipmap.ic_pause_blue);
                    if (data.leftUri != null) {
                        audioBinder.audioPlay(data.leftUri);
                    }
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
                    if (data.uri != null) {
                        audioBinder.audioPlay(data.uri);
                    }
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
        try {
            TextView tvTitle = (TextView) findViewById(R.id.bar_title);
            tvTitle.setText("观音山");
        } catch (Exception e) {

        }

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
                final Intent intent = new Intent(GuanyinshanMapActivity.this, Fengqin3dDaolanActivity.class);
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
                Intent intent = new Intent(GuanyinshanMapActivity.this, MainActivity.class);
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
