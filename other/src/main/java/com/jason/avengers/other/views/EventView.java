package com.jason.avengers.other.views;

import com.jason.avengers.common.base.BaseView;
import com.jason.avengers.other.beans.EventBean;

import java.util.List;

/**
 * @author Jason
 */
public interface EventView extends BaseView {

    void notifyView(List<EventBean> eventBeans);
}
