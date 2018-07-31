package com.jason.avengers.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jason on 2018/3/15.
 */

public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourcesLayout(), container, false);
        init(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        destroy();
        super.onDestroyView();
    }

    protected abstract int getResourcesLayout();

    protected abstract void init(View view);

    protected abstract void destroy();
}
