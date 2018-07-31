package com.jason.avengers.skill.views;

import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseView;

import java.util.List;

/**
 * Created by jason on 2018/3/29.
 */

public interface SkillView extends BaseView {

    void getSkillDataSuccess(List<BaseItemBean> beans);

    void getSkillDataFailure(String errorMsg);
}
