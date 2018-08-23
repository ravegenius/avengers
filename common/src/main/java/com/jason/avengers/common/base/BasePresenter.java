package com.jason.avengers.common.base;

public interface BasePresenter<T extends BaseView> {

    void attach(T view);

    void detach();
}