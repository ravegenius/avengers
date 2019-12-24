package com.jason.avengers.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jason.avengers.common.annotations.TrackMethod;

/**
 * Created by jason on 2018/3/15.
 */

public abstract class BaseActivity<P extends BasePresenter, V extends BaseView> extends AppCompatActivity {

    private P mPresenter;
    private V mAttachView;

    protected abstract P initPresenter();

    protected abstract V initAttachView();

    protected void attachPresenter() {
        mPresenter = initPresenter();
        mAttachView = initAttachView();
        if (mPresenter != null && mAttachView != null) {
            mPresenter.attach(mAttachView);
        }
    }

    protected void detachPresenter() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
    }

    protected P getPresenter() {
        return mPresenter;
    }

    protected V getAttachView() {
        return mAttachView;
    }

    @TrackMethod
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter();
    }

    @TrackMethod
    @Override
    protected void onDestroy() {
        detachPresenter();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
