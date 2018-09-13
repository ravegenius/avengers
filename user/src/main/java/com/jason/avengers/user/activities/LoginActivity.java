package com.jason.avengers.user.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.user.R;
import com.jason.avengers.user.presenters.LoginPresenter;
import com.jason.avengers.user.views.LoginView;

/**
 * Created by jason on 2018/8/30.
 */

@Route(path = RouterPath.USER_LOGIN)
public class LoginActivity extends BaseActivity<LoginPresenter, LoginView> implements View.OnClickListener, LoginView {

    private static final int LOGIN_STATE_USERNAME = 1;
    private static final int LOGIN_STATE_PHONE = 2;

    private int loginState = LOGIN_STATE_USERNAME;

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected LoginView initAttachView() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_login);

        ((RadioGroup) findViewById(R.id.login_type_rg)).setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.login_type_username_rb) {
                            swtichLoginViews(true);
                        } else if (checkedId == R.id.login_type_phone_rb) {
                            swtichLoginViews(false);
                        }
                    }
                });

        findViewById(R.id.user_code_btn).setOnClickListener(this);
        findViewById(R.id.user_login_btn).setOnClickListener(this);
        findViewById(R.id.sns_login_wx).setOnClickListener(this);
        findViewById(R.id.sns_login_wb).setOnClickListener(this);
        findViewById(R.id.sns_login_qq).setOnClickListener(this);
    }

    private void swtichLoginViews(boolean isUsernameLogin) {
        loginState = isUsernameLogin ? LOGIN_STATE_USERNAME : LOGIN_STATE_PHONE;
        findViewById(R.id.user_un_label).setVisibility(isUsernameLogin ? View.VISIBLE : View.GONE);
        findViewById(R.id.user_un_input).setVisibility(isUsernameLogin ? View.VISIBLE : View.GONE);
        findViewById(R.id.user_pwd_label).setVisibility(isUsernameLogin ? View.VISIBLE : View.GONE);
        findViewById(R.id.user_pwd_input).setVisibility(isUsernameLogin ? View.VISIBLE : View.GONE);
        findViewById(R.id.user_phone_label).setVisibility(isUsernameLogin ? View.GONE : View.VISIBLE);
        findViewById(R.id.user_phone_input).setVisibility(isUsernameLogin ? View.GONE : View.VISIBLE);
        findViewById(R.id.user_code_label).setVisibility(isUsernameLogin ? View.GONE : View.VISIBLE);
        findViewById(R.id.user_code_input).setVisibility(isUsernameLogin ? View.GONE : View.VISIBLE);
        findViewById(R.id.user_code_btn).setVisibility(isUsernameLogin ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (getPresenter() == null) {
            return;
        }
        int id = v.getId();
        if (id == R.id.user_code_btn) {
            // 发送验证码
            String phone = ((EditText) findViewById(R.id.user_phone_input)).getText().toString();
            getPresenter().sendCode(phone);
        } else if (id == R.id.user_login_btn) {
            // 登录
            if (loginState == LOGIN_STATE_USERNAME) {
                String un = ((EditText) findViewById(R.id.user_un_input)).getText().toString();
                String pwd = ((EditText) findViewById(R.id.user_pwd_input)).getText().toString();
                getPresenter().loginByUsername(un, pwd);
            } else if (loginState == LOGIN_STATE_PHONE) {
                String phone = ((EditText) findViewById(R.id.user_phone_input)).getText().toString();
                String code = ((EditText) findViewById(R.id.user_code_input)).getText().toString();
                getPresenter().loginByPhone(phone, code);
            }
        } else if (id == R.id.sns_login_wx) {
            // 绑定微信
            getPresenter().bindSns(LoginPresenter.SNS_TYPE_WX);
        } else if (id == R.id.sns_login_wb) {
            // 绑定微博
            getPresenter().bindSns(LoginPresenter.SNS_TYPE_WB);
        } else if (id == R.id.sns_login_qq) {
            // 绑定QQ
            getPresenter().bindSns(LoginPresenter.SNS_TYPE_QQ);
        }
    }
}
