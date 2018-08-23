package com.jason.avengers.user.views;

import com.jason.avengers.common.base.BaseView;
import com.jason.avengers.user.beans.UserBean;

/**
 * Created by jason on 2018/4/23.
 */

public interface UserView extends BaseView {

    void getUserDataSuccess(UserBean bean);

    void getUserDataFailure(String errorMsg);
}
