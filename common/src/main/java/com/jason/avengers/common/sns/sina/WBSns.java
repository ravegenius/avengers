package com.jason.avengers.common.sns.sina;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.jason.avengers.common.base.BaseApplication;
import com.jason.avengers.common.sns.Sns;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.lang.ref.WeakReference;

/**
 * Created by jason on 2018/9/4.
 */

public class WBSns extends Sns {

    private static WBSns sInstance;
    private SsoHandler mSsoHandler;
    private WbShareHandler mShareHandler;
    private WeakReference<Activity> mActivity;

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
        if (authCallback == null) {
            return;
        }

        if (mAuthCallbackRef != null) {
            mAuthCallbackRef.clear();
        }
        mAuthCallbackRef = new WeakReference<>(authCallback);
        Intent intent = new Intent(BaseApplication.getInstance(), WBEntryActivity.class);
        intent.putExtra(WBEntryActivity.WB_ENTRY_TYPE, WBEntryActivity.WB_ENTRY_AUTH);
        BaseApplication.getInstance().startActivity(intent);
    }

    public void authorize(Activity activity) {
        mSsoHandler = new SsoHandler(activity);
        mSsoHandler.authorize(mSelfWbAuthListener);
    }

    public void authorizeCallBack(Activity activity, int requestCode, int resultCode, Intent data) {
        mActivity = new WeakReference<>(activity);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void refreshToken() {
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(BaseApplication.getInstance());
        if (token.isSessionValid() && !TextUtils.isEmpty(token.getRefreshToken())) {
            AccessTokenKeeper.refreshToken(Constants.WB_APP_KEY, BaseApplication.getInstance(), new RequestListener() {
                @Override
                public void onComplete(String response) {
                }

                @Override
                public void onWeiboException(WeiboException e) {
                }
            });
        }
    }

    public void clearToken() {
        AccessTokenKeeper.clear(BaseApplication.getInstance());
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
        Intent intent = new Intent(BaseApplication.getInstance(), WBEntryActivity.class);
        intent.putExtra(WBEntryActivity.WB_ENTRY_TYPE, WBEntryActivity.WB_ENTRY_SHARE);
        intent.putExtras(args);
        BaseApplication.getInstance().startActivity(intent);
    }

    public void shareMessage(Activity activity, Bundle args) {
        if (activity == null || args == null) {
            return;
        }

        int paramsType = args.getInt(Params.PARAMS_TYPE, ParamsType.NONE.ordinal());
        if (paramsType == ParamsType.NONE.ordinal()) {
            return;
        }

        WeiboMultiMessage weiboMessage = null;
        if (paramsType == ParamsType.TXT.ordinal()) {
            weiboMessage = buildTxtMessage(args);
        } else if (paramsType == ParamsType.WEB.ordinal()) {
            weiboMessage = buildWebMessage(args);
        } else if (paramsType == ParamsType.IMG.ordinal()) {
            weiboMessage = buildImgMessage(args);
        } else if (paramsType == ParamsType.VID.ordinal()) {
            weiboMessage = buildVidMessage(args);
        }

        mShareHandler = new WbShareHandler(activity);
        mShareHandler.registerApp();
        mShareHandler.setProgressColor(0xff33b5e5);
        mShareHandler.shareMessage(weiboMessage, false);
    }

    private WeiboMultiMessage buildTxtMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String webUrl = args.getString(Params.PARAMS_WEB_URL);

        TextObject textObject = new TextObject();
        textObject.identify = Utility.generateGUID();
        textObject.title = title;
        textObject.description = desc;
        if (!TextUtils.isEmpty(thumbPath)) {
            Bitmap thumbBitmap = null;
            textObject.setThumbImage(thumbBitmap);
        }
        textObject.actionUrl = webUrl;
        textObject.text = desc;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = textObject;
        return weiboMessage;
    }

    private WeiboMultiMessage buildWebMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String webUrl = args.getString(Params.PARAMS_WEB_URL);

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = desc;
        Bitmap thumbbitmap = null;
        // 设置 Bitmap 类型的图片到视频对象里
        // 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        if (!TextUtils.isEmpty(thumbPath)) {
            mediaObject.setThumbImage(thumbbitmap);
        }
        mediaObject.actionUrl = webUrl;
        mediaObject.defaultText = desc;

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = mediaObject;
        return weiboMessage;
    }

    private WeiboMultiMessage buildImgMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String webUrl = args.getString(Params.PARAMS_WEB_URL);
        String imgPath = args.getString(Params.PARAMS_IMG_PATH);

        ImageObject imageObject = new ImageObject();
        imageObject.identify = Utility.generateGUID();
        imageObject.title = title;
        imageObject.description = desc;
        if (!TextUtils.isEmpty(thumbPath)) {
            Bitmap thumbBitmap = null;
            imageObject.setThumbImage(thumbBitmap);
        }
        imageObject.actionUrl = webUrl;
        if (!TextUtils.isEmpty(imgPath)) {
            Bitmap imgBitmap = null;
            imageObject.setImageObject(imgBitmap);
        }

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.imageObject = imageObject;
        return weiboMessage;
    }

    private WeiboMultiMessage buildVidMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String webUrl = args.getString(Params.PARAMS_WEB_URL);
        String vidUrl = args.getString(Params.PARAMS_VID_URL);

        VideoSourceObject videoObject = new VideoSourceObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = title;
        videoObject.description = desc;
        if (!TextUtils.isEmpty(thumbPath)) {
            Bitmap thumbBitmap = null;
            videoObject.setThumbImage(thumbBitmap);
        }
        videoObject.actionUrl = webUrl;
        videoObject.coverPath = Uri.parse(thumbPath);
        videoObject.videoPath = Uri.parse(vidUrl);

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.videoSourceObject = videoObject;
        return weiboMessage;
    }

    public void doResultIntent(Activity activity, Intent intent) {
        mActivity = new WeakReference<>(activity);
        if (mShareHandler != null) {
            mShareHandler.doResultIntent(intent, mSelfWbShareCallback);
        }
    }

    private void finishActivity() {
        if (mActivity == null || mActivity.get() == null) {
            return;
        }
        mActivity.get().finish();
    }

    private WbAuthListener mSelfWbAuthListener = new WbAuthListener() {
        @Override
        public void onSuccess(Oauth2AccessToken token) {
            if (token.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(BaseApplication.getInstance(), token);
            }
            finishActivity();

            if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
                return;
            }
            mAuthCallbackRef.get().onSNSResultSuccess();
        }

        @Override
        public void cancel() {
            finishActivity();

            if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
                return;
            }
            mAuthCallbackRef.get().onSNSResultCancel();
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            finishActivity();

            if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
                return;
            }
            mAuthCallbackRef.get().onSNSResultFailure();
        }
    };

    private WbShareCallback mSelfWbShareCallback = new WbShareCallback() {
        @Override
        public void onWbShareSuccess() {
            finishActivity();

            if (mShareCallbackRef == null || mShareCallbackRef.get() == null) {
                return;
            }
            mShareCallbackRef.get().onSNSResultSuccess();
        }

        @Override
        public void onWbShareCancel() {
            finishActivity();

            if (mShareCallbackRef == null || mShareCallbackRef.get() == null) {
                return;
            }
            mShareCallbackRef.get().onSNSResultCancel();
        }

        @Override
        public void onWbShareFail() {
            finishActivity();

            if (mShareCallbackRef == null || mShareCallbackRef.get() == null) {
                return;
            }
            mShareCallbackRef.get().onSNSResultFailure();
        }
    };
}
