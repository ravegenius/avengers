package com.jason.avengers.resume.presenters;

import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BasePresenter;
import com.jason.avengers.network.NetworkBuilder;
import com.jason.avengers.network.NetworkResult;
import com.jason.avengers.network.NetworkTools;
import com.jason.avengers.resume.beans.ResumeBean;
import com.jason.avengers.resume.services.ResumeService;
import com.jason.avengers.resume.views.ResumeView;
import com.jason.avengers.user.beans.UserBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by jason on 2018/3/28.
 */

public class ResumePresenter implements BasePresenter<ResumeView> {

    private ResumeView mView;

    @Override
    public void attach(ResumeView view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    public void getResumeData() {
        NetworkBuilder.build(ResumeService.class)
                .getResume()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<NetworkResult<UserBean>>() {
                            @Override
                            public void accept(NetworkResult<UserBean> result) throws Exception {
                                if (NetworkTools.checkNetworkSuccess(result)) {
                                    List<BaseItemBean> beans = null;
                                    if (result.getData() != null) {
                                        UserBean userBean = result.getData();
                                        List<ResumeBean> resumeBeans = userBean.getResumes();
                                        beans = new ArrayList<>();
                                        beans.add(userBean);
                                        beans.addAll(resumeBeans);
                                    }
                                    if (mView != null) mView.getResumeDataSuccess(beans);
                                } else {
                                    String errorMsg = NetworkTools.parseNetworkThrowable(result);
                                    if (mView != null) mView.getResumeDataFailure(errorMsg);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                String errorMsg = NetworkTools.parseNetworkThrowable(throwable);
                                if (mView != null) mView.getResumeDataFailure(errorMsg);
                            }
                        });
    }
}
