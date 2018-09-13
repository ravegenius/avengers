package com.jason.avengers.common.base;

/**
 * Created by jason on 2018/9/4.
 */

public class BaseNoMVPActivity extends BaseActivity {

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected BaseView initAttachView() {
        return null;
    }
}
