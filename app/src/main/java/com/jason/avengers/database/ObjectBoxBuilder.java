package com.jason.avengers.database;

import android.app.Application;
import android.util.Log;

import com.jason.core.utils.SystemUtils;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by jason on 2018/7/24.
 */

public class ObjectBoxBuilder {

    private static final String TAG = "ObjectBoxBuilder";

    private BoxStore mBoxStore;

    private static ObjectBoxBuilder mInstance;

    private ObjectBoxBuilder() {
    }

    public static synchronized ObjectBoxBuilder getInstance() {
        if (mInstance == null) {
            mInstance = new ObjectBoxBuilder();
        }
        return mInstance;
    }

    /**
     * 初始化ObjectBox
     * @param application
     * @param isDebug
     */
    public void build(Application application, boolean isDebug) {
        if (!SystemUtils.App.isMainProcess(application)) {
            return;
        }
        Log.i(TAG, "ObjectBox build");
        mBoxStore = MyObjectBox.builder().androidContext(application).build();
        if (isDebug && mBoxStore != null) {
            boolean started = new AndroidObjectBrowser(mBoxStore).start(application);
            Log.i(TAG, "ObjectBoxBrowser Started: " + started);
        }
        Log.i(TAG, "ObjectBox " + BoxStore.getVersion() + " (" + BoxStore.getVersionNative() + ")");
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }
}
