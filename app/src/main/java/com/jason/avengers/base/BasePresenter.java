package com.jason.avengers.base;

public interface BasePresenter<T extends BaseView> {

    void attach(T view);

    void detach();
}