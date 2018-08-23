package com.jason.avengers.common.widgets.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.ServiceWorkerClient;
import android.webkit.ServiceWorkerController;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jason.avengers.common.base.BaseApplication;

/**
 * Created by jason on 2018/6/29.
 */

public class WebViewHelper {

    private static boolean isInitServiceWorker = false;

    @TargetApi(Build.VERSION_CODES.N)
    public static synchronized void initServiceWorker() throws Exception {
        if (isInitServiceWorker)
            return;
        isInitServiceWorker = true;
        ServiceWorkerController swController = ServiceWorkerController.getInstance();
        swController.getServiceWorkerWebSettings().setAllowContentAccess(false);
        swController.getServiceWorkerWebSettings().setAllowFileAccess(false);
        swController.getServiceWorkerWebSettings().setBlockNetworkLoads(false);
        swController.getServiceWorkerWebSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        swController.setServiceWorkerClient(new ServiceWorkerClient() {
            private static final String TAG = "ServiceWorkerClient";

            @Override
            public WebResourceResponse shouldInterceptRequest(WebResourceRequest request) {
                // Capture request here and generate response or allow pass-through
                // by returning null.
                Log.d(TAG, "shouldInterceptRequest" + ">>" + request.getUrl());
                return null;
            }
        });
    }

    public static void initWebSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 设置字默认大小
        webSettings.setDefaultFontSize(40);
        //支持缩放
        webSettings.setSupportZoom(false);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);

        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        final String cachePath = BaseApplication.getInstance().getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(cachePath);
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);

        webSettings.setDatabaseEnabled(true);
        final String dbPath = BaseApplication.getInstance().getDir("db", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dbPath);

        webSettings.setRenderPriority(WebSettings.RenderPriority.LOW);
    }
}
