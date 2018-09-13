package com.jason.avengers.common.base;

public abstract class BasePresenter<T extends BaseView> {

    protected abstract void attach(T view);

    protected abstract void detach();
}