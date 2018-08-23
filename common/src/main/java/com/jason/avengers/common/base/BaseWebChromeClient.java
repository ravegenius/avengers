package com.jason.avengers.common.base;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * Created by jason on 2018/6/21.
 */

public class BaseWebChromeClient extends WebChromeClient {

    private static final String TAG = "BaseWebChromeClient";

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Log.d(TAG, "onProgressChanged" + ">>" + newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        Log.d(TAG, "onReceivedTitle");
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        Log.d(TAG, "onReceivedIcon");
    }

    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        Log.d(TAG, "onReceivedTouchIconUrl");
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        Log.d(TAG, "onShowCustomView");
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        Log.d(TAG, "onShowCustomView");
    }

    @Override
    public void onHideCustomView() {
        Log.d(TAG, "onHideCustomView");
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        Log.d(TAG, "onCreateWindow");
        return true;
    }

    @Override
    public void onRequestFocus(WebView view) {
        Log.d(TAG, "onRequestFocus");
    }

    @Override
    public void onCloseWindow(WebView window) {
        Log.d(TAG, "onCloseWindow");
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "onJsAlert" + ">>" + url + ">>" + message);
        result.cancel();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "onJsConfirm" + ">>" + url + ">>" + message);
        result.cancel();
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Log.d(TAG, "onJsPrompt" + ">>" + url + ">>" + message + ">>" + defaultValue);
        result.cancel();
        return true;
    }

    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "onJsBeforeUnload");
        result.cancel();
        return true;
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        Log.d(TAG, "onExceededDatabaseQuota");
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        Log.d(TAG, "onReachedMaxAppCacheSize");
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        Log.d(TAG, "onGeolocationPermissionsShowPrompt");
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        Log.d(TAG, "onGeolocationPermissionsHidePrompt");
    }

    @Override
    public void onPermissionRequest(PermissionRequest request) {
        Log.d(TAG, "onPermissionRequest");
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest request) {
        Log.d(TAG, "onPermissionRequestCanceled");
    }

    @Override
    public boolean onJsTimeout() {
        Log.d(TAG, "onJsTimeout");
        return true;
    }

    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        Log.d(TAG, "onConsoleMessage" + ">>" + message + ">>" + lineNumber + ">>" + sourceID);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d(TAG, "onConsoleMessage" + ">>" + consoleMessage.message() + ">>" + consoleMessage.lineNumber() + ">>" + consoleMessage.sourceId());
        return true;
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        Log.d(TAG, "getDefaultVideoPoster");
        return super.getDefaultVideoPoster();
    }

    @Override
    public View getVideoLoadingProgressView() {
        Log.d(TAG, "getVideoLoadingProgressView");
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        Log.d(TAG, "getVisitedHistory");
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        Log.d(TAG, "onShowFileChooser");
        return true;
    }
}
