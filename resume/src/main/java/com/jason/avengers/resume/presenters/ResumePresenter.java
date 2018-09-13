package com.jason.avengers.resume.presenters;

import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.common.base.BasePresenter;
import com.jason.core.network.NetworkBuilder;
import com.jason.core.network.NetworkResult;
import com.jason.core.network.NetworkTools;
import com.jason.avengers.resume.services.ResumeService;
import com.jason.avengers.resume.views.ResumeView;
import com.jason.avengers.user.beans.UserBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by jason on 2018/3/28.
 */

public class ResumePresenter extends BasePresenter<ResumeView> {

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
//                                        UserBean userBean = onSMSResult.getData();
//                                        List<ResumeBean> resumeBeans = userBean.getResumes();
//                                        beans = new ArrayList<>();
//                                        beans.add(userBean);
//                                        beans.addAll(resumeBeans);
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
