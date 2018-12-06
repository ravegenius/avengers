package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.jason.avengers.common.base.BaseAdapter;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.common.widgets.AdParallelImageView;
import com.jason.avengers.common.widgets.FoldTextView;
import com.jason.avengers.other.R;

/**
 * Created by jason on 2018/5/10.
 */

@Route(path = RouterPath.OTHER_PARALLELAD)
public class ParallelAdActivity extends BaseNoMVPActivity {

    private BaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_parallel_ad);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseAdapter() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case 100:
                        return new BaseItemViewHolder(parent, R.layout.other_layout_item_paralle_ad);
                    default:
                        return new BaseItemViewHolder(parent, R.layout.other_layout_item_paralle_ad_other);
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
                            ((FoldTextView) holder.getView(R.id.title)).setText(position + ">>>>>>>>>>>标题标题");
                        } else {
                            ((FoldTextView) holder.getView(R.id.title)).setText(position + ">>>>>>>>>>>标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题标题");
                        }

                        if (position % 3 == 0) {
                            ((TextView) holder.getView(R.id.content)).setText(position + ">>>>>>>>>>>内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
                        } else {
                            ((TextView) holder.getView(R.id.content)).setText(position + ">>>>>>>>>>>内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容" +
                                    "内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容");
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
        };
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                AdParallelImageView.compute(recyclerView, R.id.adParallelImageView);
            }
        });
    }
}
