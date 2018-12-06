package com.jason.avengers.common.sns.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jason.avengers.common.R;
import com.jason.avengers.common.base.BaseApplication;
import com.jason.avengers.common.sns.Sns;
import com.jason.core.utils.GifUtils;
import com.jason.core.utils.StringUtils;
import com.jason.core.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXEmojiObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

/**
 * Created by jason on 2018/9/4.
 */

public class WXSns extends Sns {

    private static final String TAG = "WXSns";
    private static WXSns sInstance;
    private WeakReference<IWXAPI> mWxApi;

    private WXSns() {
        initWxApi();
    }

    private void initWxApi() {
        IWXAPI api = WXAPIFactory.createWXAPI(BaseApplication.getInstance(), Constants.WX_APP_ID, false);
        api.registerApp(Constants.WX_APP_ID);
        mWxApi = new WeakReference<>(api);
    }

    public static synchronized WXSns getInstance() {
        if (null == sInstance) {
            synchronized (WXSns.class) {
                if (null == sInstance) {
                    sInstance = new WXSns();
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
        SendAuth.Req req = new SendAuth.Req();
        req.scope = Constants.WX_AUTH_SCOPE;
        req.state = Constants.WX_AUTH_STATE;
        if (mWxApi == null || mWxApi.get() == null) {
            initWxApi();
        }
        mWxApi.get().sendReq(req);
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
        WXMediaMessage message = null;
        if (paramsType == ParamsType.TXT.ordinal()) {
            message = buildTxtMessage(args);
        } else if (paramsType == ParamsType.WEB.ordinal()) {
            message = buildWebMessage(args);
        } else if (paramsType == ParamsType.IMG.ordinal()) {
            message = buildImgMessage(args);
        } else if (paramsType == ParamsType.VID.ordinal()) {
            message = buildVidMessage(args);
        } else if (paramsType == ParamsType.MINIAPP.ordinal()) {
            message = buildMiniappMessage(args);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        req.scene = Type.WX.equals(type) ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        if (mWxApi == null || mWxApi.get() == null) {
            initWxApi();
        }
        mWxApi.get().sendReq(req);
    }

    private WXMediaMessage buildTxtMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = new WXTextObject(desc);
        message.title = title;
        message.description = desc;
        if (StringUtils.isEmpty(thumbPath)) {
            // TODO 使用默认缩略图
        } else {
            // TODO 根据thumb路径生成缩略图
        }
        return message;
    }

    private WXMediaMessage buildWebMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String webUrl = args.getString(Params.PARAMS_WEB_URL);

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = new WXWebpageObject(webUrl);
        message.title = title;
        message.description = desc;
        if (StringUtils.isEmpty(thumbPath)) {
            // TODO 使用默认缩略图
        } else {
            // TODO 根据thumb路径生成缩略图
        }
        return message;
    }

    private WXMediaMessage buildImgMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String imgPath = args.getString(Params.PARAMS_IMG_PATH);

        WXMediaMessage message = new WXMediaMessage();
        if (GifUtils.isGif(imgPath)) {
            message.mediaObject = new WXEmojiObject(imgPath);
        } else {
            WXImageObject wxImageObject = new WXImageObject();
            wxImageObject.imagePath = imgPath;
            message.mediaObject = wxImageObject;
        }
        message.title = title;
        message.description = desc;
        if (StringUtils.isEmpty(thumbPath)) {
            // TODO 使用默认缩略图
        } else {
            // TODO 根据thumb路径生成缩略图
        }
        return message;
    }

    private WXMediaMessage buildVidMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String vidUrl = args.getString(Params.PARAMS_VID_URL);

        WXMediaMessage message = new WXMediaMessage();
        WXVideoObject wxVideoObject = new WXVideoObject();
        wxVideoObject.videoUrl = vidUrl;
        message.mediaObject = wxVideoObject;
        message.title = title;
        message.description = desc;
        if (StringUtils.isEmpty(thumbPath)) {
            // TODO 使用默认缩略图
        } else {
            // TODO 根据thumb路径生成缩略图
        }
        return message;
    }

    private WXMediaMessage buildMiniappMessage(Bundle args) {
        String title = args.getString(Params.PARAMS_TITLE);
        String desc = args.getString(Params.PARAMS_DESC);
        String thumbPath = args.getString(Params.PARAMS_THUMB_PATH);
        String mintappUrl = args.getString(Params.PARAMS_MINIAPP_URL);
        String mintappPath = args.getString(Params.PARAMS_MINIAPP_PATH);

        WXMediaMessage message = new WXMediaMessage();
        WXMiniProgramObject wxMiniProgramObject = new WXMiniProgramObject();
        wxMiniProgramObject.webpageUrl = mintappUrl;
        wxMiniProgramObject.path = mintappPath;
        wxMiniProgramObject.userName = Constants.WX_MINI_APP_UN;
        wxMiniProgramObject.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        message.mediaObject = wxMiniProgramObject;
        message.title = title;
        message.description = desc;
        if (StringUtils.isEmpty(thumbPath)) {
            // TODO 使用默认缩略图
        } else {
            // TODO 根据thumb路径生成缩略图
        }
        return message;
    }

    protected void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        if (mWxApi == null || mWxApi.get() == null) {
            initWxApi();
        }
        mWxApi.get().handleIntent(intent, handler);
    }

    protected void onReq(BaseReq req) {
        if (req == null) {
            return;
        }

        switch (req.getType()) {
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                printShowMsg((SendMessageToWX.Req) req);
                break;
            default:
                break;
        }
    }

    private void printShowMsg(SendMessageToWX.Req req) {
        WXMediaMessage wxMsg = req.message;

        StringBuffer msg = new StringBuffer();
        msg.append("title: ");
        msg.append(wxMsg.title);
        msg.append("\n");
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");

        if (wxMsg.mediaObject instanceof WXTextObject) {
            WXTextObject obj = (WXTextObject) wxMsg.mediaObject;
            msg.append("[WXTextObject]obj.text: ");
            msg.append(obj.text);
        } else if (wxMsg.mediaObject instanceof WXWebpageObject) {
            WXWebpageObject obj = (WXWebpageObject) wxMsg.mediaObject;
            msg.append("[WXWebpageObject]obj.webpageUrl: ");
            msg.append(obj.webpageUrl);
        } else if (wxMsg.mediaObject instanceof WXEmojiObject) {
            WXEmojiObject obj = (WXEmojiObject) wxMsg.mediaObject;
            msg.append("[WXEmojiObject]obj.emojiPath: ");
            msg.append(obj.emojiPath);
        } else if (wxMsg.mediaObject instanceof WXImageObject) {
            WXImageObject obj = (WXImageObject) wxMsg.mediaObject;
            msg.append("[WXImageObject]obj.imagePath: ");
            msg.append(obj.imagePath);
        } else if (wxMsg.mediaObject instanceof WXVideoObject) {
            WXVideoObject obj = (WXVideoObject) wxMsg.mediaObject;
            msg.append("[WXVideoObject]obj.videoUrl: ");
            msg.append(obj.videoUrl);
        } else if (wxMsg.mediaObject instanceof WXMiniProgramObject) {
            WXMiniProgramObject obj = (WXMiniProgramObject) wxMsg.mediaObject;
            msg.append("[WXMiniProgramObject]obj.webpageUrl: ");
            msg.append(obj.webpageUrl);
            msg.append("\n");
            msg.append("obj.userName: ");
            msg.append(obj.userName);
            msg.append("\n");
            msg.append("obj.path: ");
            msg.append(obj.path);
        }

        Log.d(TAG, msg.toString());
    }

    protected void onResp(BaseResp resp) {
        if (resp == null) {
            return;
        }

        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_SENDAUTH:
                onAuthResp(resp);
                break;
            default:
                onShareResp(resp);
                break;
        }
    }

    private void onAuthResp(BaseResp resp) {
        if (mAuthCallbackRef == null || mAuthCallbackRef.get() == null) {
            return;
        }

        int result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.sns_wx_errcode_success;
                mAuthCallbackRef.get().onSNSResultSuccess();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.sns_wx_errcode_cancel;
                mAuthCallbackRef.get().onSNSResultCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.sns_wx_errcode_deny;
                mAuthCallbackRef.get().onSNSResultFailure();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.sns_wx_errcode_unsupported;
                mAuthCallbackRef.get().onSNSResultFailure();
                break;
            default:
                result = R.string.sns_wx_errcode_unknown;
                mAuthCallbackRef.get().onSNSResultFailure();
                break;
        }
        ToastUtils.showShortToast(result);
    }

    private void onShareResp(BaseResp resp) {
        if (mShareCallbackRef == null || mShareCallbackRef.get() == null) {
            return;
        }

        int result;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.sns_wx_errcode_success;
                mShareCallbackRef.get().onSNSResultSuccess();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.sns_wx_errcode_cancel;
                mShareCallbackRef.get().onSNSResultCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.sns_wx_errcode_deny;
                mShareCallbackRef.get().onSNSResultFailure();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.sns_wx_errcode_unsupported;
                mShareCallbackRef.get().onSNSResultFailure();
                break;
            default:
                result = R.string.sns_wx_errcode_unknown;
                mShareCallbackRef.get().onSNSResultFailure();
                break;
        }
        ToastUtils.showShortToast(result);
    }
}
