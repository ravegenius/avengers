package com.jason.avengers.common.sns.sina;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jason.avengers.common.base.BaseNoMVPActivity;

public class WBEntryActivity extends BaseNoMVPActivity {

    public static final String WB_ENTRY_TYPE = "wb_entry_type";
    public static final int WB_ENTRY_NONE = 0;
    public static final int WB_ENTRY_AUTH = WB_ENTRY_NONE + 1;
    public static final int WB_ENTRY_SHARE = WB_ENTRY_AUTH + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int entryType = getIntent() == null ? WB_ENTRY_NONE : getIntent().getIntExtra(WB_ENTRY_TYPE, WB_ENTRY_NONE);
        if (entryType == WB_ENTRY_NONE) {
            finish();
        } else if (entryType == WB_ENTRY_AUTH) {
            // 微博 authorize
            WBSns.getInstance().authorize(this);
            // 微博 refresh token
            // WBSns.getInstance().refreshToken();
        } else if (entryType == WB_ENTRY_SHARE) {
            // 微博 share
            WBSns.getInstance().shareMessage(this, getIntent().getExtras());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WBSns.getInstance().doResultIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WBSns.getInstance().authorizeCallBack(this, requestCode, resultCode, data);
    }
}
