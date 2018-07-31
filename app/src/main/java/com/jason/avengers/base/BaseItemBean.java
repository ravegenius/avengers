package com.jason.avengers.base;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

/**
 * Created by jason on 2018/3/21.
 */

public class BaseItemBean extends BaseBean {

    public void onBindViewHolder(BaseItemViewHolder holder, int position) {
    }

    public int getItemViewType() {
        return UltimateViewAdapter.VIEW_TYPES.NORMAL;
    }
}
