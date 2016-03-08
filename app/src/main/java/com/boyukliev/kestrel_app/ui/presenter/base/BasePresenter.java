package com.boyukliev.kestrel_app.ui.presenter.base;

import com.boyukliev.kestrel_app.ui.ui.view.base.BaseView;

/**
 * Created by Admin on 3/8/2016.
 */
public interface BasePresenter<T extends BaseView> {
    void bindView(T t);
    void destroy();
}
