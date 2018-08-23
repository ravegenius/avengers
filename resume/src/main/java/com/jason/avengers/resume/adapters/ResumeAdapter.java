package com.jason.avengers.resume.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jason.avengers.common.base.BaseAdapter;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.resume.R;
import com.jason.avengers.resume.beans.EducationBean;
import com.jason.avengers.resume.beans.ResumeBean;
import com.jason.avengers.user.beans.UserBean;

import java.util.List;

/**
 * Created by jason on 2018/3/21.
 */

public class ResumeAdapter extends BaseAdapter<BaseItemViewHolder, BaseItemBean> {

    BaseItemViewHolder.Action mBaseItemViewAction;

    public ResumeAdapter setBaseItemViewAction(BaseItemViewHolder.Action action) {
        mBaseItemViewAction = action;
        return this;
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case UserBean.ItemViewType:
                return UserBean.onCreateViewHolder(parent);
//                        .addAction(R.id.user_info_phone, mBaseItemViewAction)
//                        .addAction(R.id.user_info_email, mBaseItemViewAction);
            case ResumeBean.ItemViewType:
                return ResumeBean.onCreateViewHolder(parent)
                        .addAction(R.id.resume_info_layout, mBaseItemViewAction);
            case EducationBean.ItemViewType:
                return EducationBean.onCreateViewHolder(parent);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
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

    @Override
    public long generateHeaderId(int position) {
        return getItemViewType(position);
    }

    @Override
    public BaseItemViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new BaseItemViewHolder(parent, R.layout.resume_layout_item_view_header_info);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case UserBean.ItemViewType:
                UserBean.onBindHeaderViewHolder((BaseItemViewHolder) holder);
                break;
            case ResumeBean.ItemViewType:
                ResumeBean.onBindHeaderViewHolder((BaseItemViewHolder) holder);
                break;
            case EducationBean.ItemViewType:
                EducationBean.onBindHeaderViewHolder((BaseItemViewHolder) holder);
                break;
            default:
                super.onBindHeaderViewHolder(holder, position);
                break;
        }
    }
}
