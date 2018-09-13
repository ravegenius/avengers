package com.jason.avengers.common.sns;

import android.os.Bundle;

import com.jason.avengers.common.sns.callback.SNSCallback;

/**
 * Created by jason on 2018/9/4.
 */

public abstract class Sns {

    protected Sns() {
        throw new RuntimeException("Can`t init this class Sns");
    }

    protected abstract void doAuth(Sns.SNSAuthCallback authCallback);

    protected abstract void doShare(Type type, Bundle args, SNSShareCallback shareCallback);

    public class Constants {
        public static final String WX_APP_ID = "";
        public static final String WX_AUTH_SCOPE = "snsapi_userinfo";
        public static final String WX_AUTH_STATE = "carjob_wx_login";
        public static final String WX_MINI_APP_UN = "gh_377d641c310d";
        public static final int WX_TIMELINE_VERSION = 0x21020001;

        public static final String WB_APP_KEY = "";
        public static final String WB_REDIRECT_URL = "";
        public static final String WB_SCOPE = "";
    }

    public enum Type {
        WX,
        WX_ZONE,
        QQ,
        QQ_ZONE,
        WB,
        MORE,
    }

    public enum ParamsType {
        NONE,
        TXT,
        WEB,
        IMG,
        VID,
        MINIAPP,
    }

    public class Params {
        public static final String PARAMS_TYPE = "params_type";
        public static final String PARAMS_TITLE = "params_title";
        public static final String PARAMS_DESC = "params_desc";
        public static final String PARAMS_THUMB = "params_thumb";
        public static final String PARAMS_WEB_URL = "params_web_url";
        public static final String PARAMS_IMG_URL = "params_img_url";
        public static final String PARAMS_VID_URL = "params_vid_url";
        public static final String PARAMS_MINIAPP_URL = "params_miniapp_url";
        public static final String PARAMS_MINIAPP_PATH = "params_miniapp_path";
    }

    public interface SNSAuthCallback extends SNSCallback {
    }

    public interface SNSShareCallback extends SNSCallback {
    }
}
