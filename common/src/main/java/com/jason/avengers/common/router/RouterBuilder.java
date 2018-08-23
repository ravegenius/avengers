package com.jason.avengers.common.router;

import android.app.Application;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by jason on 2018/8/20.
 */

public enum RouterBuilder {

    INSTANCE;

    public void init(Application application, Boolean debug) {
        if (debug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
    }

    public Postcard build(String path) {
        return ARouter.getInstance().build(path);
    }
}
