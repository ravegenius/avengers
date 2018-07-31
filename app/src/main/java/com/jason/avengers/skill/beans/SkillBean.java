package com.jason.avengers.skill.beans;


import com.google.gson.annotations.SerializedName;
import com.jason.avengers.base.BaseBean;

import java.util.List;

/**
 * Created by jason on 2018/3/21.
 */

public class SkillBean extends BaseBean {

    @SerializedName("skills")
    private List<SkillInfoBean> skillInfoBeans;

    public List<SkillInfoBean> getSkillInfoBeans() {
        return skillInfoBeans;
    }

    public void setSkillInfoBeans(List<SkillInfoBean> skillInfoBeans) {
        this.skillInfoBeans = skillInfoBeans;
    }
}
