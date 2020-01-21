package com.jason.avengers.other.listeners;

import android.view.View;

import com.jason.avengers.other.holders.OwnerHolder;

/**
 * @author Jason
 */
public interface OwnerClickListener {

    void onColorClickListener(OwnerHolder holder, View view);

    void onDetailClickListener(OwnerHolder holder, View view);

    void onSaveClickListener(OwnerHolder holder, View view);

    void onDeleteClickListener(OwnerHolder holder, View view);
}