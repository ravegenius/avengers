package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jason.avengers.R;
import com.jason.avengers.base.BaseActivity;
import com.jason.avengers.base.BaseAdapter;
import com.jason.avengers.base.BaseItemViewHolder;
import com.jason.avengers.widgets.AdParallelImageView;
import com.jason.avengers.widgets.ExpandableTextView;

/**
 * Created by jason on 2018/5/10.
 */

public class ParallelAdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parallel_ad);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BaseAdapter() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case 100:
                        return new BaseItemViewHolder(parent, R.layout.layout_item_paralle_ad);
                    default:
                        return new BaseItemViewHolder(parent, R.layout.layout_item_paralle_ad_other);
                }
            }

            @Override
            public void onBindViewHolder(BaseItemViewHolder holder, int position) {
                switch (getItemViewType(position)) {
                    case 100:
                        Glide.with(ParallelAdActivity.this)
                                .load("https://yt-adp.nosdn.127.net/myzhang/10801500_ahdq_20180509.png")
                                //.load("http://img.zcool.cn/community/013b6b57aa9f5b0000018c1b351495.jpg")
                                .into((ImageView) holder.getView(R.id.adParallelImageView));
                        break;
                    default:
                        if (position % 3 == 0) {
                            ((ExpandableTextView) holder.getView(R.id.title)).setExpandedText(position + ">>>>>>>>>>>标题标题");
                        } else {
                            ((ExpandableTextView) holder.getView(R.id.title)).setExpandedText(position + ">>>>>>>>>>>标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题");
                        }

                        if (position % 3 == 0) {
                            ((ExpandableTextView) holder.getView(R.id.content)).setExpandedText(position + ">>>>>>>>>>>内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
                        } else {
                            ((ExpandableTextView) holder.getView(R.id.content)).setExpandedText(position + ">>>>>>>>>>>内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
                        }
                        break;
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 3 || position == 6) return 100;
                return super.getItemViewType(position);
            }

            @Override
            public int getItemCount() {
                return 30;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                AdParallelImageView.compute(recyclerView);
            }
        });
    }
}
