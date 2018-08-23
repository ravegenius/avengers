package com.jason.avengers.common.base;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jason on 2018/6/21.
 */

public class BaseWebViewClient extends WebViewClient {

    private static final String TAG = "BaseWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG, "shouldOverrideUrlLoading" + ">>" + url);
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.d(TAG, "shouldOverrideUrlLoading" + (request == null ? "" : request.getUrl().toString()));
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d(TAG, "onPageStarted" + ">>" + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d(TAG, "onPageFinished" + ">>" + url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.d(TAG, "onLoadResource" + ">>" + url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        Log.d(TAG, "onPageCommitVisible" + ">>" + url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        Log.d(TAG, "shouldInterceptRequest" + ">>" + url);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.shouldInterceptRequest(view, url);
    }

    @SuppressLint("NewApi")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = (request == null ? "" : request.getUrl().toString());
        Log.d(TAG, "shouldInterceptRequest" + ">>" + url);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        Log.d(TAG, "onTooManyRedirects");
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.d(TAG, "onReceivedError");
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.d(TAG, "onReceivedError");
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        Log.d(TAG, "onReceivedHttpError");
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        Log.d(TAG, "onFormResubmission");
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        Log.d(TAG, "doUpdateVisitedHistory");
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.d(TAG, "onReceivedSslError");
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        Log.d(TAG, "onReceivedClientCertRequest");
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        Log.d(TAG, "onReceivedHttpAuthRequest");
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        Log.d(TAG, "shouldOverrideKeyEvent");
        return false;
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        Log.d(TAG, "onUnhandledKeyEvent");
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        Log.d(TAG, "onScaleChanged");
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        Log.d(TAG, "onReceivedLoginRequest");
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        Log.d(TAG, "onRenderProcessGone");
        return false;
    }
}
