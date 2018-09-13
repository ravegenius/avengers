package com.jason.avengers.common.sns.more;

import android.os.Bundle;

import com.jason.avengers.common.sns.Sns;

/**
 * Created by jason on 2018/9/4.
 */

public class MoreSns extends Sns {

    private static MoreSns sInstance;

    private MoreSns() {
    }

    public static synchronized MoreSns getInstance() {
        if (null == sInstance) {
            synchronized (MoreSns.class) {
                if (null == sInstance) {
                    sInstance = new MoreSns();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void doAuth(Sns.SNSAuthCallback authCallback) {
        throw new RuntimeException("Can`t doAuth this class MoreSns");
    }

    @Override
    protected void doShare(Type type, Bundle args, SNSShareCallback shareCallback) {
    }
}
