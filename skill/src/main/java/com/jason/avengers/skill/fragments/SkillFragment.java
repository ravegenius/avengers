package com.jason.avengers.skill.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jason.avengers.common.base.BaseFragment;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BaseItemViewHolder;
import com.jason.avengers.skill.R;
import com.jason.avengers.skill.adapters.SkillAdapter;
import com.jason.avengers.skill.presenters.SkillPresenter;
import com.jason.avengers.skill.views.SkillView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.List;

/**
 * Created by jason on 2018/3/15.
 */

public class SkillFragment extends BaseFragment implements SkillView, BaseItemViewHolder.Action {

    private UltimateRecyclerView mUltimateRecyclerView;
    private SkillAdapter mUltimateViewAdapter;
    private SkillPresenter mPresenter;

    @Override
    protected int getResourcesLayout() {
        return R.layout.skill_fragment_skill;
    }

    @Override
    protected void init(View view) {
        initView(view);
        initData();
    }

    private void initView(View view) {
        mUltimateRecyclerView = view.findViewById(R.id.skill_recycler_view);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUltimateRecyclerView.setAdapter(mUltimateViewAdapter = new SkillAdapter().setBaseItemViewAction(this));

//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mUltimateViewAdapter) {
//
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                //这个方法还有别的方法可以重载  可以控制如滑动删除等功能
//                //控制拖动的方向   这里设置了智能上下拖动交换位置
//                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                //控制滑动删除的方向  这里设置了只能左滑删除
//                final int swipeFlags = ItemTouchHelper.LEFT;
//                return makeMovementFlags(dragFlags, swipeFlags);
//            }
//
//            @Override
//            public boolean isItemViewSwipeEnabled() {
//                //这里控制开启或关闭item是否可以滑动删除的功能
//                return super.isItemViewSwipeEnabled();
//            }
//
//            @Override
//            public boolean isLongPressDragEnabled() {
//                //控制长按拖动功能
//                return super.isLongPressDragEnabled();
//            }
//        };
//        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(mUltimateRecyclerView.mRecyclerView);

        //设置头部一定要在setAdapter后面，因为这个操作会调用adapter的方法来显示头部，如果adapter为null，则出错
        // mUltimateRecyclerView.setParallaxHeader(headerView);
        mUltimateRecyclerView.setRefreshing(true);
        mUltimateRecyclerView.enableDefaultSwipeRefresh(true);//开启下拉刷新
        mUltimateRecyclerView.reenableLoadmore();//开启上拉加载更多
        mUltimateRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSkillData();
            }
        });
        mUltimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
            }
        });
    }

    private void initData() {
        mPresenter = new SkillPresenter();
        mPresenter.attach(this);
        getSkillData();
    }

    private void getSkillData() {
        if (mPresenter != null) {
            mPresenter.getSkillData();
        }
    }

    @Override
    protected void destroy() {
        destroyData();
    }

    private void destroyData() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    @Override
    public void getSkillDataSuccess(List<BaseItemBean> beans) {
        if (mUltimateRecyclerView != null) {
            mUltimateRecyclerView.setRefreshing(false);
        }
        if (mUltimateViewAdapter != null) {
            mUltimateViewAdapter.notifyData(beans);
        }
    }

    @Override
    public void getSkillDataFailure(String errorMsg) {
        if (mUltimateRecyclerView != null) {
            mUltimateRecyclerView.setRefreshing(false);
        }
        // Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();

        if (mUltimateViewAdapter != null) {
//            List<BaseItemBean> datas = TestUtils.initSkillTestData();
//            mUltimateViewAdapter.notifyData(datas);
        }
    }

    @Override
    public void doAction(View view, BaseItemBean baseItemBean) {
//        switch (view.getId()) {
//            case R.id.skill_info_points:
//                SkillInfoBean skillInfoBean = (SkillInfoBean) baseItemBean;
//                GlobalRouter.startSkillTab(getActivity(), skillInfoBean);
//                Toast.makeText(getContext(), skillInfoBean.getCurrentClickedPoint(), Toast.LENGTH_SHORT).show();
//                break;
//        }
    }
}
