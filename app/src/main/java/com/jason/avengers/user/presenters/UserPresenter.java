package com.jason.avengers.user.presenters;

import com.jason.avengers.base.BasePresenter;
import com.jason.avengers.network.NetworkBuilder;
import com.jason.avengers.network.NetworkResult;
import com.jason.avengers.network.NetworkTools;
import com.jason.avengers.user.beans.UserBean;
import com.jason.avengers.user.services.UserService;
import com.jason.avengers.user.views.UserView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by jason on 2018/4/23.
 */

public class UserPresenter implements BasePresenter<UserView> {

    private UserView mView;

    @Override
    public void attach(UserView view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    public void getUserData() {
        NetworkBuilder.build(UserService.class)
                .getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<NetworkResult<UserBean>>() {
                            @Override
                            public void accept(NetworkResult<UserBean> result) throws Exception {
                                if (NetworkTools.checkNetworkSuccess(result)) {
                                    if (mView != null) mView.getUserDataSuccess(result.getData());
                                } else {
                                    String errorMsg = NetworkTools.parseNetworkThrowable(result);
                                    if (mView != null) mView.getUserDataFailure(errorMsg);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                String errorMsg = NetworkTools.parseNetworkThrowable(throwable);
                                if (mView != null) mView.getUserDataFailure(errorMsg);
                            }
                        });
    }
}
