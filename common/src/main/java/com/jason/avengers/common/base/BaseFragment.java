package com.jason.avengers.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jason.avengers.common.annotations.TrackMethod;

/**
 * Created by jason on 2018/3/15.
 */

public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    @TrackMethod
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TrackMethod
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @TrackMethod
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourcesLayout(), container, false);
        init(view);
        return view;
    }

    @TrackMethod
    @Override
    public void onDestroyView() {
        destroy();
        super.onDestroyView();
    }

    protected abstract int getResourcesLayout();

    protected abstract void init(View view);

    protected abstract void destroy();
}
