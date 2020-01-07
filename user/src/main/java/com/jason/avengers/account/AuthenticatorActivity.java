package com.jason.avengers.account;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.jason.avengers.user.R;

/**
 * @author jason
 */
class AuthenticatorActivity extends BaseNoMVPActivity {

    public static final String ACCOUNT_NEW = "accountNew";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String AUTHTOKEN_TYPE = "authTokenType";
    public static final String ACCOUNT_AUTHENTICATOR_RESPONSE = "accountAuthenticatorResponse";

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_authenticator);

        mAccountManager = AccountManager.get(getBaseContext());
        mAuthTokenType = getIntent().getStringExtra(AUTHTOKEN_TYPE);
        if (TextUtils.isEmpty(mAuthTokenType)) {
            mAuthTokenType = AuthenticatorService.AUTHTOKEN_TYPE_FULL_ACCESS;
        }

        boolean isNewAccount = getIntent().getBooleanExtra(ACCOUNT_NEW,false);

        String accountName = getIntent().getStringExtra(ACCOUNT_NAME);
        String accountType = getIntent().getStringExtra(ACCOUNT_TYPE);


    }
}
