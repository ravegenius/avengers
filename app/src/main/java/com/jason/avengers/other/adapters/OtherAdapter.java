package com.jason.avengers.other.adapters;

import android.view.ViewGroup;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseAdapter;
import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseItemViewHolder;
import com.jason.avengers.other.beans.OtherBean;

import java.util.List;

/**
 * Created by jason on 2018/3/21.
 */

public class OtherAdapter extends BaseAdapter<BaseItemViewHolder, BaseItemBean> {

    BaseItemViewHolder.Action mBaseItemViewAction;

    public OtherAdapter setBaseItemViewAction(BaseItemViewHolder.Action action) {
        mBaseItemViewAction = action;
        return this;
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == OtherBean.ItemViewType)
            return OtherBean.onCreateViewHolder(parent)
                    .addAction(R.id.other_info_layout, mBaseItemViewAction);
        else
            return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        List<BaseItemBean> datas = getData();
        if (datas == null || datas.size() == 0) {
            return super.getItemViewType(position);
        } else {
            return datas.get(position).getItemViewType();
        }
    }
}
