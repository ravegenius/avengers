package com.jason.avengers.resume.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jason.avengers.common.base.BaseItemBean;
import com.jason.avengers.resume.beans.EducationBean;
import com.jason.avengers.resume.beans.ResumeBean;
import com.jason.avengers.user.beans.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/3/30.
 */

public class TestUtils extends com.jason.avengers.common.test.TestUtils {

    public static List<BaseItemBean> initResumeTestData() {
        List<BaseItemBean> beans = new ArrayList<>();
        beans.add(initUserTestData());
        beans.addAll(initEducationInfosTestData());
        beans.addAll(initResumeInfosTestData());
        return beans;
    }

    public static UserBean initUserTestData() {
        Gson gson = new Gson();
        UserBean userBean = gson.fromJson(userJson, UserBean.class);
        return userBean;
    }

    public static List<EducationBean> initEducationInfosTestData() {
        Gson gson = new Gson();
        List<EducationBean> educationBeans = gson.fromJson(educationJson, new TypeToken<List<EducationBean>>() {
        }.getType());
        return educationBeans;
    }

    public static List<ResumeBean> initResumeInfosTestData() {
        Gson gson = new Gson();
        List<ResumeBean> resumeBeans = gson.fromJson(resumeJson, new TypeToken<List<ResumeBean>>() {
        }.getType());
        return resumeBeans;
    }
}
