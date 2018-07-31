package com.jason.avengers.base;

import android.annotation.SuppressLint;
import android.app.Application;

import com.jason.avengers.BuildConfig;
import com.jason.avengers.database.ObjectBoxBuilder;
import com.jason.avengers.stetho.StethoUtils;

/**
 * Created by jason on 2018/6/22.
 */

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    public static BaseApplication getInstance() {
        return mInstance;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        StethoUtils.init(mInstance);
        ObjectBoxBuilder.getInstance().build(mInstance, BuildConfig.DEBUG);
    }
}
