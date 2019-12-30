package com.jason.avengers.common.database;

import android.app.Application;
import android.util.Log;

import com.jason.avengers.common.database.entity.MyObjectBox;
import com.jason.core.utils.Utils;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by jason on 2018/7/24.
 */

public enum ObjectBoxBuilder {

    INSTANCE;

    private static final String TAG = "ObjectBoxBuilder";

    private BoxStore mBoxStore;

    /**
     * 初始化ObjectBox
     * @param application
     * @param isDebug
     */
    public void build(Application application, boolean isDebug) {
        if (!Utils.App.isMainProcess()) {
            return;
        }
        Log.wtf(TAG, "ObjectBox build");
        mBoxStore = MyObjectBox.builder().androidContext(application).build();
        if (isDebug && mBoxStore != null) {
            boolean started = new AndroidObjectBrowser(mBoxStore).start(application);
            Log.wtf(TAG, "ObjectBoxBrowser Started: " + started);
        }
        Log.wtf(TAG, "ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }
}
