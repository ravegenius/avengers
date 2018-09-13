package com.jason.avengers.common.sns.callback;

public interface SNSCallback {

    void onSNSResultSuccess();

    void onSNSResultFailure();

    void onSNSResultCancel();
}