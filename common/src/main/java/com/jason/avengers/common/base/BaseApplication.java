package com.jason.avengers.common.base;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * Created by jason on 2018/6/22.
 */

public abstract class BaseApplication extends Application {

    private static BaseApplication mInstance;

    public static BaseApplication getInstance() {
        return mInstance;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initAfterCreate(this);
    }

    protected abstract void initAfterCreate(Application application);
}
