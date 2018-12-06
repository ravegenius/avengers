package com.jason.avengers.main.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.user.UserManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jason on 2018/8/20.
 */

@Route(path = RouterPath.MAIN_SPLASH)
public class SplashActivity extends BaseNoMVPActivity {

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long l) throws Exception {
                        return UserManager.INSTANCE.isLogin();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isLogin) throws Exception {
                        if (isLogin) {
                            gotoMain();
                        } else {
                            gotoLogin();
                        }
                    }
                }));
    }

    private void gotoMain() {
        RouterBuilder.INSTANCE.build(RouterPath.MAIN_MAIN).navigation(this,
                new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                        finish();
                    }
                });
    }

    private void gotoLogin() {
        RouterBuilder.INSTANCE.build(RouterPath.USER_LOGIN).navigation(this,
                new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
