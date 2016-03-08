package com.boyukliev.kestrel_app.ui.ui.view;

import com.boyukliev.kestrel_app.ui.ui.view.base.BaseView;

/**
 * Created by Admin on 3/8/2016.
 */
public interface MainView extends BaseView {
    void addToast(String text, int length);
    void displayText(String text);
}
