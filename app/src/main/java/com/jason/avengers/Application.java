package com.jason.avengers;

import com.jason.avengers.common.base.BaseApplication;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.core.stetho.StethoUtils;
import com.jason.core.utils.SharedPrefUtils;
import com.jason.core.utils.Utils;

/**
 * Created by jason on 2018/8/20.
 */

public class Application extends BaseApplication {

    /**
     * 配置基础代码属性 App壳工程就是定义及配置参数使用
     *
     * @param application
     */
    @Override
    protected void initAfterCreate(android.app.Application application) {
        // First must init Utils context
        Utils.init(application);

        SharedPrefUtils.init(application);
        StethoUtils.init(application);
        ObjectBoxBuilder.INSTANCE.build(application, BuildConfig.DEBUG);
        RouterBuilder.INSTANCE.init(application, BuildConfig.DEBUG);
    }
}
