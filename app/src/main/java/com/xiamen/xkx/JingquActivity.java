package com.xiamen.xkx;

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
        list.add(new ItemData("鼓浪屿风琴博物馆", R.mipmap.img_gulangyu));
        list.add(new ItemData("厦门海底世界", R.mipmap.img_haidi_shijie));
        list.add(new ItemData("日光岩", R.mipmap.img_riguangyan));
        list.add(new ItemData("菽庄花园", R.mipmap.img_shu_zhuang_huayuan));
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
                //TODO start another activity
            } else if (view.getId() == R.id.iv_pos || view.getId() == R.id.tv_pos) {
                //TODO start another activity
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

        private ImageView iv;
        private TextView title;


        public MyHolder(View itemView) {
            super(itemView);
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final TextView tvJingdian = (TextView) holder.itemView.findViewById(R.id.tv_jingdian);
            final ImageView ivJingdian = (ImageView) holder.itemView.findViewById(R.id.iv_jingdian);
            tvJingdian.setText(list.get(position).title);
            ivJingdian.setImageResource(list.get(position).imgID);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO start another activity
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
