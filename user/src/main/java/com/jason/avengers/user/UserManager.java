package com.jason.avengers.user;

import com.jason.avengers.user.beans.UserBean;
import com.jason.avengers.user.services.UserService;
import com.jason.core.network.NetworkBuilder;
import com.jason.core.network.NetworkResult;
import com.jason.core.network.NetworkTools;
import com.jason.core.utils.SharedPrefUtils;
import com.jason.core.utils.StringUtils;

import java.util.List;
import java.util.Vector;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by jason on 2018/8/23.
 */

public enum UserManager {

    INSTANCE;

    private static final String USER_HOST_URL = "https://www.baidu.com/";

    private static final String SP_FILE_NAME = "user_info";
    private static final String SP_KEY_USERID = "user_id";
    private static final String SP_KEY_TOKEN = "user_token";
    private static final String SP_KEY_NAME = "user_name";
    private static final String SP_KEY_SEX = "user_sex";
    private static final String SP_KEY_AGE = "user_age";
    private static final String SP_KEY_WORKAGE = "user_workage";
    private static final String SP_KEY_AVATAR = "user_avatar";
    private static final String SP_KEY_EMAIL = "user_email";
    private static final String SP_KEY_PHONE = "user_phone";
    private static final String SP_KEY_MARK = "user_mark";
    private List<IUserLisener> mUserLiseners = new Vector<>();

    public void registerUserLisener(IUserLisener lisener) {
        if (!mUserLiseners.contains(lisener))
            mUserLiseners.add(lisener);
    }

    public void unregisterUserLisener(IUserLisener lisener) {
        if (mUserLiseners.contains(lisener))
            mUserLiseners.remove(lisener);
    }

    public String getToken() {
        return SharedPrefUtils.getString(SP_FILE_NAME, SP_KEY_TOKEN, null);
    }

    public boolean isLogin() {
        String token = getToken();
        return !StringUtils.isEmpty(token);
    }

    public void loginByUsername(String un, String pwd) {
        login(un, pwd, null, null, null);
    }

    public void loginByPhone(String phone, String code) {
        login(null, null, phone, null, code);
    }

    public void loginByEmail(String email, String code) {
        login(null, null, null, email, code);
    }

    /**
     * 登录
     *
     * @param un    登录账号
     * @param pwd   登录密码
     * @param phone 登录手机号
     * @param email 登录邮箱
     * @param code  验证码
     */
    public void login(final String un, final String pwd, final String phone, final String email, final String code) {
        Observable.empty()
                .flatMap(new Function<Object,
                        ObservableSource<NetworkResult<UserBean>>>() {
                    @Override
                    public ObservableSource<NetworkResult<UserBean>> apply(Object o) throws Exception {
                        return NetworkBuilder.build(USER_HOST_URL, UserService.class).login2GetToken(un, pwd, phone, email, code);
                    }
                })
                .flatMap(new Function<NetworkResult<UserBean>,
                        ObservableSource<NetworkResult<UserBean>>>() {
                    @Override
                    public ObservableSource<NetworkResult<UserBean>> apply(NetworkResult<UserBean> result) throws Exception {
                        if (NetworkTools.checkDataSuccess(result)
                                && !StringUtils.isEmpty(result.getData().getToken())) {
                            String token = result.getData().getToken();
                            return NetworkBuilder.build(USER_HOST_URL, UserService.class).getUser(token);
                        } else {
                            throw new Exception("Get token failure!");
                        }
                    }
                })
                .map(new Function<NetworkResult<UserBean>, UserBean>() {
                    @Override
                    public UserBean apply(NetworkResult<UserBean> result) throws Exception {
                        if (NetworkTools.checkDataSuccess(result)) {
                            return result.getData();
                        } else {
                            throw new Exception("Get user failure!");
                        }
                    }
                })
                .doOnNext(clearUserBean())
                .doOnNext(saveUserBean())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<UserBean>() {
                            @Override
                            public void accept(UserBean result) throws Exception {
                                for (IUserLisener lisener : mUserLiseners) {
                                    if (lisener instanceof LoginLisener) {
                                        ((LoginLisener) lisener).onLoginResult(true, result);
                                    }
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                for (IUserLisener lisener : mUserLiseners) {
                                    if (lisener instanceof LoginLisener) {
                                        ((LoginLisener) lisener).onLoginResult(false, null);
                                    }
                                }
                            }
                        });
    }

    /**
     * 三方登录绑定登录
     *
     * @param snsToken
     * @param snsType
     */
    public void bind(final String snsToken, final String snsType) {
        Observable.empty()
                .flatMap(new Function<Object,
                        ObservableSource<NetworkResult<UserBean>>>() {
                    @Override
                    public ObservableSource<NetworkResult<UserBean>> apply(Object o) throws Exception {
                        return NetworkBuilder.build(USER_HOST_URL, UserService.class).bind2GetToken(snsToken, snsType);
                    }
                })
                .flatMap(new Function<NetworkResult<UserBean>,
                        ObservableSource<NetworkResult<UserBean>>>() {
                    @Override
                    public ObservableSource<NetworkResult<UserBean>> apply(NetworkResult<UserBean> result) throws Exception {
                        if (NetworkTools.checkDataSuccess(result)
                                && !StringUtils.isEmpty(result.getData().getToken())) {
                            String token = result.getData().getToken();
                            return NetworkBuilder.build(USER_HOST_URL, UserService.class).getUser(token);
                        } else {
                            throw new Exception("Get token failure!");
                        }
                    }
                })
                .map(new Function<NetworkResult<UserBean>, UserBean>() {
                    @Override
                    public UserBean apply(NetworkResult<UserBean> result) throws Exception {
                        if (NetworkTools.checkDataSuccess(result)) {
                            return result.getData();
                        } else {
                            throw new Exception("Get user failure!");
                        }
                    }
                })
                .doOnNext(clearUserBean())
                .doOnNext(saveUserBean())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<UserBean>() {
                            @Override
                            public void accept(UserBean result) throws Exception {
                                for (IUserLisener lisener : mUserLiseners) {
                                    if (lisener instanceof LoginLisener) {
                                        ((LoginLisener) lisener).onLoginResult(true, result);
                                    }
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                for (IUserLisener lisener : mUserLiseners) {
                                    if (lisener instanceof LoginLisener) {
                                        ((LoginLisener) lisener).onLoginResult(false, null);
                                    }
                                }
                            }
                        });
    }

    /**
     * 登出
     */
    public void logout() {
        String token = getToken();
        Observable.just(token)
                .flatMap(new Function<String, ObservableSource<NetworkResult>>() {
                    @Override
                    public ObservableSource<NetworkResult> apply(String token) throws Exception {
                        return NetworkBuilder.build(USER_HOST_URL, UserService.class).logout(token);
                    }
                })
                .doOnNext(clearUserBean())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<NetworkResult>() {
                            @Override
                            public void accept(NetworkResult result) throws Exception {
                                for (IUserLisener lisener : mUserLiseners) {
                                    if (lisener instanceof LogoutLisener) {
                                        ((LogoutLisener) lisener).onLogoutResult(true);
                                    }
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                for (IUserLisener lisener : mUserLiseners) {
                                    if (lisener instanceof LogoutLisener) {
                                        ((LogoutLisener) lisener).onLogoutResult(true);
                                    }
                                }
                            }
                        });
    }

    /**
     * 登录成功后保存数据
     */
    private static Consumer<UserBean> saveUserBean() {
        return new Consumer<UserBean>() {
            @Override
            public void accept(UserBean userBean) throws Exception {
                SharedPrefUtils.putLong(SP_FILE_NAME, SP_KEY_USERID, userBean.getUserId());
                SharedPrefUtils.putString(SP_FILE_NAME, SP_KEY_TOKEN, userBean.getToken());
                SharedPrefUtils.putString(SP_FILE_NAME, SP_KEY_NAME, userBean.getUsername());
                SharedPrefUtils.putInt(SP_FILE_NAME, SP_KEY_SEX, userBean.getSex());
                SharedPrefUtils.putInt(SP_FILE_NAME, SP_KEY_AGE, userBean.getAge());
                SharedPrefUtils.putInt(SP_FILE_NAME, SP_KEY_WORKAGE, userBean.getWorkAge());
                SharedPrefUtils.putString(SP_FILE_NAME, SP_KEY_AVATAR, userBean.getAvatar());
                SharedPrefUtils.putString(SP_FILE_NAME, SP_KEY_EMAIL, userBean.getEmail());
                SharedPrefUtils.putString(SP_FILE_NAME, SP_KEY_PHONE, userBean.getPhone());
                SharedPrefUtils.putString(SP_FILE_NAME, SP_KEY_MARK, userBean.getMark());
            }
        };
    }

    /**
     * 登出后清理数据
     */
    private static Consumer<Object> clearUserBean() {
        return new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                SharedPrefUtils.clear(SP_FILE_NAME);
            }
        };
    }

    public interface LoginLisener extends IUserLisener {

        void onLoginResult(boolean success, UserBean userBean);
    }

    public interface LogoutLisener extends IUserLisener {

        void onLogoutResult(boolean success);
    }

    public interface IUserLisener {
    }
}
