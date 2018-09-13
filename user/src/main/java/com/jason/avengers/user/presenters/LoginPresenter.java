package com.jason.avengers.user.presenters;

import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.common.sms.SMSManager;
import com.jason.avengers.common.sns.SNSManager;
import com.jason.avengers.common.sns.Sns;
import com.jason.avengers.user.UserManager;
import com.jason.avengers.user.beans.UserBean;
import com.jason.avengers.user.views.LoginView;
import com.jason.core.utils.StringUtils;

/**
 * Created by jason on 2018/9/4.
 */

public class LoginPresenter extends BasePresenter<LoginView>
        implements UserManager.LoginLisener, SMSManager.SMSCallback, Sns.SNSAuthCallback {

    public static final String SNS_TYPE_WX = Sns.Type.WX.name();
    public static final String SNS_TYPE_QQ = Sns.Type.QQ.name();
    public static final String SNS_TYPE_WB = Sns.Type.WB.name();

    @Override
    public void attach(LoginView view) {
        UserManager.INSTANCE.registerUserLisener(this);
    }

    @Override
    public void detach() {
        UserManager.INSTANCE.unregisterUserLisener(this);
    }

    public void sendCode(String phone) {
        // 发送验证码 common 功能
        SMSManager.INSTANCE.sendMessage(phone, this);
    }

    public void loginByUsername(String un, String pwd) {
        if (StringUtils.isSpace(un)) {
            return;
        }
        if (StringUtils.isSpace(pwd)) {
            return;
        }
        UserManager.INSTANCE.loginByUsername(un, pwd);
    }

    public void loginByPhone(String phone, String code) {
        if (StringUtils.isSpace(phone)) {
            return;
        }
        if (StringUtils.isSpace(code)) {
            return;
        }
        UserManager.INSTANCE.loginByPhone(phone, code);
    }

    public void bindSns(String snsType) {
        // Sns common 功能
        if (StringUtils.equals(snsType, SNS_TYPE_WX)) {
            SNSManager.INSTANCE.doAuth(Sns.Type.WX, this);
        } else if (StringUtils.equals(snsType, SNS_TYPE_QQ)) {
            SNSManager.INSTANCE.doAuth(Sns.Type.QQ, this);
        } else if (StringUtils.equals(snsType, SNS_TYPE_WB)) {
            SNSManager.INSTANCE.doAuth(Sns.Type.WB, this);
        }
    }

    @Override
    public void onLoginResult(boolean success, UserBean userBean) {
    }

    @Override
    public void onSMSResult(boolean success) {
    }

    @Override
    public void onSNSResultSuccess() {
    }

    @Override
    public void onSNSResultFailure() {
    }

    @Override
    public void onSNSResultCancel() {
    }
}
