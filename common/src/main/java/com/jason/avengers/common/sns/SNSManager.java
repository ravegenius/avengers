package com.jason.avengers.common.sns;

import android.os.Bundle;

import com.jason.avengers.common.sns.more.MoreSns;
import com.jason.avengers.common.sns.qq.QQSns;
import com.jason.avengers.common.sns.sina.WBSns;
import com.jason.avengers.common.sns.wechat.WXSns;

/**
 * Created by jason on 2018/9/4.
 */

public enum SNSManager {

    INSTANCE;

    public void doAuth(Sns.Type type, Sns.SNSAuthCallback authCallback) {
        Sns sns = null;
        switch (type) {
            case WX:
                sns = WXSns.getInstance();
                break;
            case QQ:
                sns = QQSns.getInstance();
                break;
            case WB:
                sns = WBSns.getInstance();
                break;
        }

        if (sns != null) {
            sns.doAuth(authCallback);
        }
    }

    public void doShare(Sns.Type type, Bundle args, Sns.SNSShareCallback shareCallback) {
        Sns sns = null;
        switch (type) {
            case WX:
            case WX_ZONE:
                sns = WXSns.getInstance();
                break;
            case QQ:
            case QQ_ZONE:
                sns = QQSns.getInstance();
                break;
            case WB:
                sns = WBSns.getInstance();
                break;
            case MORE:
                sns = MoreSns.getInstance();
                break;
        }

        if (sns != null) {
            sns.doShare(type, args, shareCallback);
        }
    }
}
