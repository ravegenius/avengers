package com.jason.avengers.other.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jason.avengers.other.beans.EventBean;
import com.jason.avengers.other.holders.EventHolder;
import com.jason.avengers.other.listeners.EventClickListener;

import java.util.List;

/**
 * @author Jason
 */
public class EventsAdapter extends RecyclerView.Adapter<EventHolder> {

    private final LayoutInflater mLayoutInflater;
    private final EventClickListener mEventClickListener;
    private List<EventBean> mData;

    public EventsAdapter(LayoutInflater layoutInflater, EventClickListener listener) {
        this.mLayoutInflater = layoutInflater;
        this.mEventClickListener = listener;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(mLayoutInflater, parent, mEventClickListener);
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        holder.bindView(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void notifyData(List<EventBean> eventBeans) {
        this.mData = eventBeans;
        notifyDataSetChanged();
    }
}