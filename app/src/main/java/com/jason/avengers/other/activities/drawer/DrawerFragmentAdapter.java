package com.jason.avengers.other.activities.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseAdapter;
import com.jason.avengers.base.BaseItemViewHolder;

/**
 * Created by jason on 2018/6/26.
 */

public class DrawerFragmentAdapter extends BaseAdapter {

    private int position;

    public DrawerFragmentAdapter(int position) {
        this.position = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseItemViewHolder(parent, R.layout.layout_item_drawer_fragment_item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.textview)).setText(this.position + "_" + position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
