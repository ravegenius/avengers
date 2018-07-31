package com.jason.avengers.resume.views;

import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseView;

import java.util.List;

/**
 * Created by jason on 2018/3/29.
 */

public interface ResumeView extends BaseView {

    void getResumeDataSuccess(List<BaseItemBean> beans);

    void getResumeDataFailure(String errorMsg);
}
