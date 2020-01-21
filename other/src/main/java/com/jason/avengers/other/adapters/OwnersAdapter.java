package com.jason.avengers.other.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jason.avengers.other.beans.OwnerBean;
import com.jason.avengers.other.holders.OwnerHolder;
import com.jason.avengers.other.listeners.OwnerClickListener;

import java.util.List;

/**
 * @author Jason
 */
public class OwnersAdapter extends RecyclerView.Adapter<OwnerHolder> {

    private final LayoutInflater mLayoutInflater;
    private final OwnerClickListener mOwnerClickListener;
    private List<OwnerBean> mData;

    public OwnersAdapter(LayoutInflater layoutInflater, OwnerClickListener mListener) {
        this.mLayoutInflater = layoutInflater;
        this.mOwnerClickListener = mListener;
    }

    @Override
    public OwnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OwnerHolder(mLayoutInflater, parent, mOwnerClickListener);
    }

    @Override
    public void onBindViewHolder(OwnerHolder holder, int position) {
        OwnerBean ownerBean = mData.get(position);
        holder.bindView(ownerBean);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void notifyData(List<OwnerBean> entities) {
        this.mData = entities;
        notifyDataSetChanged();
    }

    public void notifyInserted(int position, OwnerBean ownerBean) {
        this.mData.add(position, ownerBean);
        notifyItemInserted(position);
    }

    public void notifyRemove(int position) {
        this.mData.remove(position);
        notifyItemRemoved(position);
    }
}