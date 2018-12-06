package com.jason.avengers.common.sns.qq;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jason.avengers.common.base.BaseNoMVPActivity;

public class QQEntryActivity extends BaseNoMVPActivity {

    public static final String QQ_ENTRY_TYPE = "wb_entry_type";
    public static final int QQ_ENTRY_NONE = 0;
    public static final int QQ_ENTRY_AUTH = QQ_ENTRY_NONE + 1;
    public static final int QQ_ENTRY_SHARE = QQ_ENTRY_AUTH + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int entryType = getIntent() == null ? QQ_ENTRY_NONE : getIntent().getIntExtra(QQ_ENTRY_TYPE, QQ_ENTRY_NONE);
        if (entryType == QQ_ENTRY_NONE) {
            finish();
        } else if (entryType == QQ_ENTRY_AUTH) {
            // QQ authorize
            QQSns.getInstance().login(this);
            finish();
        } else if (entryType == QQ_ENTRY_SHARE) {
            // QQ share
            QQSns.getInstance().share(this, getIntent().getExtras());
            finish();
        }
    }
}
