package com.jason.avengers.other.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.other.holders.OwnersHolder;
import com.jason.avengers.other.listeners.OwnersClickListener;

import java.util.List;

/**
 * @author Jason
 */
public class OwnersAdapter extends RecyclerView.Adapter<OwnersHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<CalendarOwnerDBEntity> mData;
    private OwnersClickListener mListener;

    public OwnersAdapter(LayoutInflater layoutInflater, OwnersClickListener mListener) {
        this.mLayoutInflater = layoutInflater;
        this.mListener = mListener;
    }

    @Override
    public OwnersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OwnersHolder(mLayoutInflater, parent, mListener);
    }

    @Override
    public void onBindViewHolder(OwnersHolder holder, int position) {
        CalendarOwnerDBEntity entity = mData.get(position);
        holder.bindView(entity);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void notifyData(List<CalendarOwnerDBEntity> entities) {
        this.mData = entities;
        notifyDataSetChanged();
    }

    public void notifyInserted(int position, CalendarOwnerDBEntity emptyEntity) {
        this.mData.add(position, emptyEntity);
        notifyItemInserted(position);
    }

    public void notifyRemove(int position) {
        this.mData.remove(position);
        notifyItemRemoved(position);
    }
}