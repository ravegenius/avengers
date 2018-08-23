package com.jason.avengers.other.beans;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.other.R;

import static com.jason.avengers.common.configs.ItemViewType.OTHER;

/**
 * Created by jason on 2018/3/30.
 */

public class OtherBean extends BaseItemBean {

    public static final int ItemViewType = OTHER;

    private Type type;
    private Action action;
    private String title;

    public OtherBean(Type type, String title, @NonNull Action action) {
        this.type = type;
        this.action = action;
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public int getItemViewType() {
        return ItemViewType;
    }

    @Override
    public void onBindViewHolder(BaseItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((TextView) holder.getView(R.id.other_info_title)).setText(getTitle());
        holder.setViewAction(R.id.other_info_layout, this);
    }

    public static BaseItemViewHolder onCreateViewHolder(ViewGroup parent) {
        return new BaseItemViewHolder(parent, R.layout.other_layout_item_view_other_info);
    }

    public enum Type {
        Lable
    }

    public interface Action {

        void doAction();
    }
}
