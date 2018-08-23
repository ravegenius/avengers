package com.jason.core.stetho;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by jason on 2018/6/22.
 */

public class StethoUtils {

    public static void init(Context context) {
        Stetho.initializeWithDefaults(context);
    }

    public static OkHttpClient.Builder addStethoNetworkInterceptor(OkHttpClient.Builder builder) {
        builder = builder.addNetworkInterceptor(new StethoInterceptor());
        return builder;
    }
}
