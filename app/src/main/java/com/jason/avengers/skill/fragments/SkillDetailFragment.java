package com.jason.avengers.skill.fragments;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseFragment;
import com.jason.avengers.base.BaseWebChromeClient;
import com.jason.avengers.base.BaseWebViewClient;
import com.jason.avengers.widgets.webview.WebViewHelper;

/**
 * Created by jason on 2018/4/8.
 */

public class SkillDetailFragment extends BaseFragment {

    private static final String TAG = "SkillDetailFragment";
    public static final String PARAM_DATA = "param_data";

    private WebViewClient webViewClient = new BaseWebViewClient();
    private WebChromeClient webChromeClient = new BaseWebChromeClient();
    private WebView mWebView;

    @Override
    protected int getResourcesLayout() {
        return R.layout.layout_skill_detail;
    }

    @SuppressLint("NewApi")
    @Override
    protected void init(View view) {
        WebView.setWebContentsDebuggingEnabled(true);

        String data = getArguments().getString(PARAM_DATA);

        mWebView = view.findViewById(R.id.skill_detail_webview);

        WebViewHelper.initWebSettings(mWebView);

        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(webChromeClient);

        mWebView.loadUrl(data);
    }

    @Override
    protected void destroy() {
    }
}
