package com.jason.avengers.common.sns.qq;

import android.os.Bundle;

import com.jason.avengers.common.sns.Sns;

/**
 * Created by jason on 2018/9/4.
 */

public class QQSns extends Sns {

    private static QQSns sInstance;

    private QQSns() {
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
    }

    @Override
    protected void doShare(Type type, Bundle args, SNSShareCallback shareCallback) {
    }
}
