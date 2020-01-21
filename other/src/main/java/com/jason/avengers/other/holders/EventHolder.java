package com.jason.avengers.other.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.avengers.other.R;
import com.jason.avengers.other.beans.EventBean;
import com.jason.avengers.other.common.CalendarCommon;
import com.jason.avengers.other.listeners.EventClickListener;
import com.jason.core.utils.DensityUtils;

/**
 * @author Jason
 */
public class EventHolder extends RecyclerView.ViewHolder {

    public EventBean eventBean;
    public final TextView eventTimeView;
    public final TextView eventLevelView;
    public final TextView eventMoneyView;
    public final TextView eventOwnerView;

    public EventHolder(LayoutInflater layoutInflater, ViewGroup parent, final EventClickListener listener) {
        super(layoutInflater.inflate(R.layout.other_layout_item_event, parent, false));
        eventTimeView = itemView.findViewById(R.id.event_time);
        eventOwnerView = itemView.findViewById(R.id.event_owner);
        eventLevelView = itemView.findViewById(R.id.event_level);
        eventMoneyView = itemView.findViewById(R.id.event_money);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEventClickListener(EventHolder.this, v);
                }
            }
        });

        eventOwnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOwnerClickListener(EventHolder.this, v);
                }
            }
        });
    }

    public void bindView(EventBean eventBean) {
        this.eventBean = eventBean;

        if (eventBean == null) {
            return;
        }
        if (eventBean.isGroup()) {
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(30)));
            itemView.setBackgroundResource(R.color.event_group_color);
        } else {
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(40)));
            itemView.setBackgroundResource(eventBean.isDone() ? R.color.event_done_color : R.color.event_color);
        }

        eventTimeView.setText(eventBean.getEventTime());

        if (eventBean.isGroup()) {
            eventLevelView.setVisibility(View.GONE);
        } else {
            eventLevelView.setVisibility(View.VISIBLE);
            eventLevelView.setText(eventBean.getLevel());
        }

        eventMoneyView.setText(CalendarCommon.MONEY_DF.format(eventBean.getMoney()));

        if (eventBean.getOwnerBean() == null) {
            eventOwnerView.setVisibility(View.GONE);
        } else {
            eventOwnerView.setVisibility(View.VISIBLE);
            eventOwnerView.setText(eventBean.getOwnerBean().getName());
            eventOwnerView.setBackgroundResource(eventBean.isDone() ? R.color.event_done_color : eventBean.getOwnerBean().getColor());
        }
    }
}