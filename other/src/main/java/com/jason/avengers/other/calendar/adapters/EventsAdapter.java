package com.jason.avengers.other.calendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.beans.Event;
import com.jason.avengers.other.calendar.holders.EventHolder;

import java.util.List;

/**
 * @author Jason
 */
public class EventsAdapter extends RecyclerView.Adapter<EventHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<Event> mData;

    public EventsAdapter(LayoutInflater layoutInflater) {
        this.mLayoutInflater = layoutInflater;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(mLayoutInflater.inflate(R.layout.other_layout_item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        holder.bindView(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void notifyData(List<Event> events) {
        this.mData = events;
        notifyDataSetChanged();
    }
}