package com.jason.avengers.common.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.itemTouchHelper.ItemTouchHelperViewHolder;

/**
 * Created by jason on 2018/4/2.
 */

public class BaseItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    private SparseArray<View> mViewArray = new SparseArray<View>();
    private SparseArray<Action> mViewActions = new SparseArray<>();

    public BaseItemViewHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public BaseItemViewHolder addAction(@IdRes int viewId, Action action) {
        mViewActions.put(viewId, action);
        return this;
    }

    public View getView(@IdRes int viewId) {
        View view = mViewArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViewArray.append(viewId, view);
        }
        return view;
    }

    public Context getContext() {
        if (itemView != null) {
            return itemView.getContext();
        }
        return null;
    }

    public void setViewAction(@IdRes final int viewId, @NonNull final BaseItemBean baseItemBean) {
        View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAction(viewId, baseItemBean);
            }
        });
    }

    public void doAction(@IdRes final int viewId, @NonNull final BaseItemBean baseItemBean) {
        Action action = mViewActions.get(viewId);
        if (action != null)
            action.doAction(getView(viewId), baseItemBean);
    }

    @Override
    public void onItemSelected() {
    }

    @Override
    public void onItemClear() {
    }

    public interface Action {
        void doAction(View view, BaseItemBean baseItemBean);
    }
}
