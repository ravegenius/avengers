package com.jason.avengers.skill.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.skill.beans.SkillInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/3/30.
 */

public class TestUtils extends com.jason.avengers.common.test.TestUtils {

    public static List<BaseItemBean> initSkillTestData() {
        List<BaseItemBean> beans = new ArrayList<>();
        Gson gson = new Gson();
        List<SkillInfoBean> skillInfoBeans = gson.fromJson(skillJson, new TypeToken<List<SkillInfoBean>>() {
        }.getType());
        beans.addAll(skillInfoBeans);
        return beans;
    }
}
