package com.jason.avengers.skill.presenters;

import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BasePresenter;
import com.jason.avengers.network.NetworkBuilder;
import com.jason.avengers.network.NetworkResult;
import com.jason.avengers.network.NetworkTools;
import com.jason.avengers.skill.beans.SkillBean;
import com.jason.avengers.skill.services.SkillService;
import com.jason.avengers.skill.views.SkillView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by jason on 2018/3/28.
 */

public class SkillPresenter implements BasePresenter<SkillView> {

    private SkillView mView;

    @Override
    public void attach(SkillView view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    public void getSkillData() {
        NetworkBuilder.build(SkillService.class)
                .getSkill()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<NetworkResult<SkillBean>>() {
                            @Override
                            public void accept(NetworkResult<SkillBean> result) throws Exception {
                                if (NetworkTools.checkNetworkSuccess(result)) {
                                    List<BaseItemBean> beans = null;
                                    if (result.getData() != null) {
                                        beans = new ArrayList<>();
                                        beans.addAll(result.getData().getSkillInfoBeans());
                                    }
                                    if (mView != null) mView.getSkillDataSuccess(beans);
                                } else {
                                    String errorMsg = NetworkTools.parseNetworkThrowable(result);
                                    if (mView != null) mView.getSkillDataFailure(errorMsg);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                String errorMsg = NetworkTools.parseNetworkThrowable(throwable);
                                if (mView != null) mView.getSkillDataFailure(errorMsg);
                            }
                        });
    }
}
