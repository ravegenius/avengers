package com.jason.avengers.other.calendar.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.CalendarCommon;
import com.jason.avengers.other.calendar.beans.Event;
import com.jason.core.utils.DensityUtils;

/**
 * @author Jason
 */
public class EventHolder extends RecyclerView.ViewHolder {

    public Event event;
    public final TextView eventTimeView;
    public final TextView eventLevelView;
    public final TextView eventMoneyView;
    public final TextView eventOwnerView;

    public EventHolder(View itemView) {
        super(itemView);
        eventTimeView = itemView.findViewById(R.id.event_time);
        eventLevelView = itemView.findViewById(R.id.event_level);
        eventMoneyView = itemView.findViewById(R.id.event_money);
        eventOwnerView = itemView.findViewById(R.id.event_owner);
    }

    public void bindView(Event event) {
        this.event = event;

        if (event == null) {
            return;
        }
        if (event.isGroup()) {
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(30)));
            itemView.setBackgroundResource(R.color.event_group_color);
        } else {
            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(40)));
            itemView.setBackgroundResource(event.isDone() ? R.color.event_done_color : R.color.event_color);
        }

        eventTimeView.setText(event.getEventTime());

        if (event.isGroup()) {
            eventLevelView.setVisibility(View.GONE);
        } else {
            eventLevelView.setVisibility(View.VISIBLE);
            eventLevelView.setText(event.getLevel());
        }

        eventMoneyView.setText(CalendarCommon.MONEY_DF.format(event.getMoney()));

        if (event.getOwner() == null) {
            eventOwnerView.setVisibility(View.GONE);
        } else {
            eventOwnerView.setVisibility(View.VISIBLE);
            eventOwnerView.setText(event.getOwner().getName());
        }
    }
}