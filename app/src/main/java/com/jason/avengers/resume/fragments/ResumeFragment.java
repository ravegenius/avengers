package com.jason.avengers.resume.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseFragment;
import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseItemViewHolder;
import com.jason.avengers.common.GlobalRouter;
import com.jason.avengers.resume.adapters.ResumeAdapter;
import com.jason.avengers.resume.presenters.ResumePresenter;
import com.jason.avengers.resume.views.ResumeView;
import com.jason.avengers.test.TestUtils;
import com.jason.avengers.user.beans.UserBean;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

/**
 * Created by jason on 2018/3/15.
 */

public class ResumeFragment extends BaseFragment implements ResumeView, BaseItemViewHolder.Action {

    private UltimateRecyclerView mUltimateRecyclerView;
    private ResumeAdapter mUltimateViewAdapter;
    private ResumePresenter mPresenter;

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_resume;
    }

    @Override
    protected void init(View view) {
        initView(view);
        initData();
    }

    private void initView(View view) {
        mUltimateRecyclerView = view.findViewById(R.id.resume_recycler_view);
        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUltimateRecyclerView.setAdapter(mUltimateViewAdapter = new ResumeAdapter().setBaseItemViewAction(this));
        mUltimateRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(mUltimateViewAdapter));
//
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
                getResumeData();
            }
        });
        mUltimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
            }
        });
    }

    private void initData() {
        mPresenter = new ResumePresenter();
        mPresenter.attach(this);
        getResumeData();
    }

    private void getResumeData() {
        if (mPresenter != null) {
            mPresenter.getResumeData();
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
    public void getResumeDataSuccess(List<BaseItemBean> beans) {
        if (mUltimateRecyclerView != null) {
            mUltimateRecyclerView.setRefreshing(false);
        }
        if (mUltimateViewAdapter != null) {
            mUltimateViewAdapter.notifyData(beans);
        }
    }

    @Override
    public void getResumeDataFailure(String errorMsg) {
        if (mUltimateRecyclerView != null) {
            mUltimateRecyclerView.setRefreshing(false);
        }
        // Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();

        if (mUltimateViewAdapter != null) {
            List<BaseItemBean> datas = TestUtils.initResumeTestData();
            mUltimateViewAdapter.notifyData(datas);
        }
    }

    @Override
    public void doAction(View view, final BaseItemBean baseItemBean) {
        switch (view.getId()) {
            case R.id.user_info_phone:
                GlobalRouter.startCall(ResumeFragment.this, view, (UserBean) baseItemBean);
                break;
            case R.id.user_info_email:
                GlobalRouter.startEmail(this, (UserBean) baseItemBean);
                break;
            case R.id.resume_info_layout:
                Toast.makeText(getContext(), "显示详细工作经历", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
