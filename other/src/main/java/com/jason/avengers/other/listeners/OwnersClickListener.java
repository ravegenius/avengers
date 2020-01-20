package com.jason.avengers.other.listeners;

import android.view.View;

import com.jason.avengers.other.holders.OwnersHolder;

/**
 * @author Jason
 */
public interface OwnersClickListener {

    void onColorClickListener(OwnersHolder holder, View view);

    void onDetailClickListener(OwnersHolder holder, View view);

    void onSaveClickListener(OwnersHolder holder, View view);

    void onDeleteClickListener(OwnersHolder holder, View view);
}