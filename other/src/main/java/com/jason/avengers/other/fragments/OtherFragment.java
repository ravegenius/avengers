package com.jason.avengers.other.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jason.avengers.common.base.BaseFragment;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.adapters.OtherAdapter;
import com.jason.avengers.other.beans.OtherBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/3/15.
 */

public class OtherFragment extends BaseFragment implements BaseItemViewHolder.Action {

    private UltimateRecyclerView mUltimateRecyclerView;
    private OtherAdapter mOtherAdapter;

    @Override
    protected int getResourcesLayout() {
        return R.layout.other_fragment_other;
    }

    @Override
    protected void init(View view) {
        mUltimateRecyclerView = view.findViewById(R.id.other_recycler_view);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUltimateRecyclerView.setAdapter(mOtherAdapter = new OtherAdapter().setBaseItemViewAction(this));

        List<BaseItemBean> beanList = new ArrayList<>();
        beanList.add(new OtherBean(OtherBean.Type.Lable, "RichEditor", new OtherBean.Action() {
            @Override
            public void doAction() {
                RouterBuilder.INSTANCE.build(RouterPath.OTHER_RICHEDITOR).navigation(getActivity());
            }
        }));
        beanList.add(new OtherBean(OtherBean.Type.Lable, "Drawer", new OtherBean.Action() {
            @Override
            public void doAction() {
                RouterBuilder.INSTANCE.build(RouterPath.OTHER_DRAWER).navigation(getActivity());
            }
        }));
        beanList.add(new OtherBean(OtherBean.Type.Lable, "ParallelAd", new OtherBean.Action() {
            @Override
            public void doAction() {
                RouterBuilder.INSTANCE.build(RouterPath.OTHER_PARALLELAD).navigation(getActivity());
            }
        }));
        beanList.add(new OtherBean(OtherBean.Type.Lable, "ObjectBox", new OtherBean.Action() {
            @Override
            public void doAction() {
                RouterBuilder.INSTANCE.build(RouterPath.OTHER_OBJECTBOX).navigation(getActivity());
            }
        }));
        mOtherAdapter.notifyData(beanList);
    }

    @Override
    protected void destroy() {
    }

    @Override
    public void doAction(View view, BaseItemBean baseItemBean) {
        OtherBean otherBean = (OtherBean) baseItemBean;
        otherBean.getAction().doAction();
    }
}
