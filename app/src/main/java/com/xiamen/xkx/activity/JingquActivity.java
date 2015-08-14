package com.xiamen.xkx.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiamen.xkx.R;

import java.util.ArrayList;


public class JingquActivity extends AppCompatActivity {

    boolean isPopup = false;
    private View popupView;
    private ArrayList<ItemData> list = new ArrayList<ItemData>();
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jingqu);

        initData();
        initView();

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        //设置布局样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        //设置适配器
        RecyclerView.Adapter adapter = new MyAdapter();
        rv.setAdapter(adapter);
    }

    private void initView() {
        popupView = findViewById(R.id.rl_popup);
        final ImageButton btnBack = (ImageButton) findViewById(R.id.imgbtn_back);
        final ImageButton btnMore = (ImageButton) findViewById(R.id.imgbtn_more);
        final ImageView ivJieshao = (ImageView) findViewById(R.id.iv_jieshao);
        final ImageView ivPos = (ImageView) findViewById(R.id.iv_pos);
        final TextView tvJieshao = (TextView) findViewById(R.id.tv_jieshao);
        final TextView tvPos = (TextView) findViewById(R.id.tv_pos);

        ButtonOnClickListener listener = new ButtonOnClickListener();
        popupView.setOnClickListener(listener);
        btnBack.setOnClickListener(listener);
        btnMore.setOnClickListener(listener);
        ivJieshao.setOnClickListener(listener);
        ivPos.setOnClickListener(listener);
        tvJieshao.setOnClickListener(listener);
        tvPos.setOnClickListener(listener);
    }

    private void initData() {
        Intent it = getIntent();
        int which = it.getIntExtra("which", 0);
        if (which == 2) {
            list.add(new ItemData("旅游集散服务中心", R.mipmap.img_jisanfuwu));
            list.add(new ItemData("沙雕公园", R.mipmap.img_shadiao));
            list.add(new ItemData("梦幻海岸主题乐园", R.mipmap.img_menghuanleyuan));
        } else {
            list.add(new ItemData("鼓浪屿风琴博物馆", R.mipmap.img_gulangyu));
            list.add(new ItemData("郑成功纪念馆", R.mipmap.img_zheng_cheng_gong_ji_nian_guan));
            list.add(new ItemData("厦门海底世界", R.mipmap.img_haidi_shijie));
            list.add(new ItemData("日光岩", R.mipmap.img_riguangyan));
            list.add(new ItemData("菽庄花园", R.mipmap.img_shu_zhuang_huayuan));
            list.add(new ItemData("海天堂构", R.mipmap.img_hai_tian_tang_gou));
        }
    }

    class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.imgbtn_more) {
                isPopup = !isPopup;
                if (isPopup) {
                    popupView.setVisibility(View.VISIBLE);
                } else {
                    popupView.setVisibility(View.INVISIBLE);
                }
            } else if (view.getId() == R.id.imgbtn_back) {
                finish();
            } else if (view.getId() == R.id.iv_jieshao || view.getId() == R.id.tv_jieshao) {
                Intent intent = new Intent(JingquActivity.this, IntroductionActivity.class);
                intent.putExtra("景点", "鼓浪屿景区");
                startActivity(intent);
            } else if (view.getId() == R.id.iv_pos || view.getId() == R.id.tv_pos) {
                Intent intent = new Intent(JingquActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        }
    }

    class ItemData {
        String title;
        int imgID;

        public ItemData(String title, int imgID) {
            this.title = title;
            this.imgID = imgID;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tvTitle;
        private TextView tvJiesao;
        private TextView tvDown;


        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_jingdian);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_jingdian);
            tvJiesao = (TextView) itemView.findViewById(R.id.tv_jieshao);
            tvDown = (TextView) itemView.findViewById(R.id.tv_ziyuanbao);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        LayoutInflater inflater = null;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (inflater == null) inflater = LayoutInflater.from(JingquActivity.this);
            return new MyHolder(inflater.inflate(R.layout.item_jingdian_list, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final TextView tvJingdian = (TextView) holder.itemView.findViewById(R.id.tv_jingdian);
            final ImageView ivJingdian = (ImageView) holder.itemView.findViewById(R.id.iv_jingdian);
            tvJingdian.setText(list.get(position).title);
            ivJingdian.setImageResource(list.get(position).imgID);
            final MyHolder mh = (MyHolder) holder;
            // 干掉资源包
            if (position >= 1) {
                mh.tvDown.setTextColor(Color.GRAY);
                mh.tvJiesao.setTextColor(Color.GRAY);
                // TODO: 灰色图标
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_download_black);
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mh.tvDown.setCompoundDrawables(drawable, null, null, null);
            } else {
                mh.tvTitle.setTextColor(Color.BLACK);
                mh.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (position == 1) {//open zhengcheng gong app
                            String pkgName = "com.mgd.zcg3d";
                            String launcherActivity = "com.unity3d.player.UnityPlayerNativeActivity";
                            ComponentName component = new ComponentName(pkgName, launcherActivity);
                            intent = new Intent();
                            intent.setComponent(component);
                        } else {
                            intent = new Intent(JingquActivity.this, MapActivity.class);
                        }
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(JingquActivity.this, "请安装郑成功app", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mh.tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JingquActivity.this, MapActivity.class);
                        startActivity(intent);
                    }
                });
                final String str = mh.tvTitle.getText().toString();
                mh.tvJiesao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(JingquActivity.this, IntroductionActivity.class);
                        intent.putExtra("景点", str);
                        startActivity(intent);
                    }
                });
                mh.tvDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });
            }


        }


        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
