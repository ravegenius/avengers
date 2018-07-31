package com.jason.avengers.other.activities.drawer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jason.avengers.R;

/**
 * Created by jason on 2018/6/26.
 */

public class DrawerFragment extends LazyLoadFragment {

    private int mPosition;
    private RecyclerView mRecyclerView;

    @Override
    protected int getResourcesLayout() {
        return R.layout.layout_item_drawer_fragment;
    }

    @Override
    protected void init(View view) {
        if (null != getArguments()) {
            this.mPosition = getArguments().getInt("position", -1);
        }
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new DrawerFragmentAdapter(mPosition));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    ((DrawerActivity) getActivity()).setScrollLayoutDraggable(true);
                    System.out.println("do something for Top");
                } else {
                    ((DrawerActivity) getActivity()).setScrollLayoutDraggable(false);
                    System.out.println("do something!!!!");
                }
            }
        });
    }

    @Override
    protected void destroy() {
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
