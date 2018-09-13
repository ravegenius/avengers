package com.jason.avengers.common.sns.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by jason on 2018/9/5.
 */

public abstract class WXAbEntryActivity extends BaseNoMVPActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXSns.getInstance().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXSns.getInstance().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        WXSns.getInstance().onReq(baseReq);
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        WXSns.getInstance().onResp(baseResp);
        finish();
    }
}
