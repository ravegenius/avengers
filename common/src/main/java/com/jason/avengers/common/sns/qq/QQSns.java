package com.jason.avengers.common.sns.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jason.avengers.common.base.BaseApplication;
import com.jason.avengers.common.sns.Sns;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by jason on 2018/9/4.
 */

public class QQSns extends Sns {

    private static QQSns sInstance;
    private WeakReference<Tencent> mTencent;

    private QQSns() {
        Tencent tencent = Tencent.createInstance(Constants.QQ_APP_ID, BaseApplication.getInstance());
        mTencent = new WeakReference<>(tencent);
    }

    public static synchronized QQSns getInstance() {
        if (null == sInstance) {
            synchronized (QQSns.class) {
                if (null == sInstance) {
                    sInstance = new QQSns();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void doAuth(Sns.SNSAuthCallback authCallback) {
        if (authCallback == null) {
            return;
        }

        if (mAuthCallbackRef != null) {
            mAuthCallbackRef.clear();
        }
        mAuthCallbackRef = new WeakReference<>(authCallback);
        Intent intent = new Intent(BaseApplication.getInstance(), QQEntryActivity.class);
        intent.putExtra(QQEntryActivity.QQ_ENTRY_TYPE, QQEntryActivity.QQ_ENTRY_AUTH);
        BaseApplication.getInstance().startActivity(intent);
    }

    public void login(Activity activity) {
        if (mTencent == null || mTencent.get() == null) {
            return;
        }
        mTencent.get().login(activity, "all", mIUiListener);
    }

    @Override
    protected void doShare(Type type, Bundle args, SNSShareCallback shareCallback) {
        if (type == null || args == null || shareCallback == null) {
            return;
        }
        int paramsType = args.getInt(Params.PARAMS_TYPE, ParamsType.NONE.ordinal());
        if (paramsType == ParamsType.NONE.ordinal()) {
            return;
        }

        if (mShareCallbackRef != null) {
            mShareCallbackRef.clear();
        }
        mShareCallbackRef = new WeakReference<>(shareCallback);
        Intent intent = new Intent(BaseApplication.getInstance(), QQEntryActivity.class);
        intent.putExtra(QQEntryActivity.QQ_ENTRY_TYPE, QQEntryActivity.QQ_ENTRY_SHARE);
        intent.putExtras(args);
        BaseApplication.getInstance().startActivity(intent);
    }

    public void share(Activity activity, Bundle args) {
        if (mTencent == null || mTencent.get() == null) {
            return;
        }
        mTencent.get().shareToQQ(activity, args, new IUiListener() {
            @Override
            public void onComplete(Object object) {
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private IUiListener mIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object object) {
            initOpenidAndToken((JSONObject) object);

            if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
                return;
            }
            mAuthCallbackRef.get().onSNSResultSuccess();
        }

        @Override
        public void onError(UiError uiError) {
            if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
                return;
            }
            mAuthCallbackRef.get().onSNSResultFailure();
        }

        @Override
        public void onCancel() {
            if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
                return;
            }
            mAuthCallbackRef.get().onSNSResultCancel();
        }
    };

    private void initOpenidAndToken(JSONObject jsonObject) {
        if (mTencent == null || mTencent.get() == null || jsonObject == null) {
            return;
        }

        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                mTencent.get().setAccessToken(token, expires);
                mTencent.get().setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }
}
