package com.jason.avengers.skill.adapters;

import android.view.ViewGroup;

import com.jason.avengers.skill.R;
import com.jason.avengers.common.base.BaseAdapter;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.skill.beans.SkillInfoBean;

import java.util.List;

/**
 * Created by jason on 2018/3/21.
 */

public class SkillAdapter extends BaseAdapter<BaseItemViewHolder, BaseItemBean> {

    BaseItemViewHolder.Action mBaseItemViewAction;

    public SkillAdapter setBaseItemViewAction(BaseItemViewHolder.Action action) {
        mBaseItemViewAction = action;
        return this;
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SkillInfoBean.ItemViewType)
            return SkillInfoBean.onCreateViewHolder(parent)
                    .addAction(R.id.skill_info_points, mBaseItemViewAction);
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
