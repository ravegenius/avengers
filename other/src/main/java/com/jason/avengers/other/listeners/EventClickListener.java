package com.jason.avengers.other.listeners;

import android.view.View;

import com.jason.avengers.other.holders.EventHolder;

/**
 * @author Jason
 */
public interface EventClickListener {

    void onEventClickListener(EventHolder holder, View view);

    void onOwnerClickListener(EventHolder holder, View view);
}