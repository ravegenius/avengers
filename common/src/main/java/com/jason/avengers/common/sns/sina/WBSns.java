package com.jason.avengers.common.sns.sina;

import android.os.Bundle;

import com.jason.avengers.common.base.BaseApplication;
import com.jason.avengers.common.sns.Sns;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * Created by jason on 2018/9/4.
 */

public class WBSns extends Sns {

    private static WBSns sInstance;
    private SsoHandler mSsoHandler;

    private WBSns() {
        AuthInfo authInfo = new AuthInfo(BaseApplication.getInstance(), Constants.WB_APP_KEY, Constants.WB_REDIRECT_URL, Constants.WB_SCOPE);
        WbSdk.install(BaseApplication.getInstance(), authInfo);
    }

    public static synchronized WBSns getInstance() {
        if (null == sInstance) {
            synchronized (WBSns.class) {
                if (null == sInstance) {
                    sInstance = new WBSns();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void doAuth(Sns.SNSAuthCallback authCallback) {
    }

    @Override
    protected void doShare(Type type, Bundle args, SNSShareCallback shareCallback) {
    }

    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
        }

        @Override
        public void cancel() {
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
        }
    }
}
