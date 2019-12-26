package com.jason.avengers.common.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/3/21.
 */

public class BaseAdapter<VH extends BaseItemViewHolder, D extends BaseItemBean> extends UltimateViewAdapter<VH> {

    private List<D> data;

    @Override
    public VH newFooterHolder(View view) {
        return null;
    }

    @Override
    public VH newHeaderHolder(View view) {
        return null;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (data == null || data.get(position) == null) {
            return;
        }
        data.get(position).onBindViewHolder(holder, position);
    }

    @Override
    public long generateHeaderId(int position) {
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public void notifyData(List<D> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void notifyAddData(List<D> data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<D> getData() {
        return this.data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }
}
