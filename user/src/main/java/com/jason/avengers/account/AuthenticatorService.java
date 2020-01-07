package com.jason.avengers.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jason.avengers.user.R;

/**
 * @author jason
 */
public class AuthenticatorService extends Service {

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Udinic account";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Udinic account";
    /**
     * Account name
     */
    public static String mAccountName;
    /**
     * Account type
     */
    public static String mAccountType;

    private AccountAuthenticator mAccountAuthenticator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Utils.log("AuthenticatorService.onBind");

        mAccountName = getString(R.string.app_account_name);
        mAccountType = getString(R.string.app_account_type);

        if (mAccountAuthenticator == null) {
            mAccountAuthenticator = new AccountAuthenticator(this);
        }
        return mAccountAuthenticator.getIBinder();
    }
}
