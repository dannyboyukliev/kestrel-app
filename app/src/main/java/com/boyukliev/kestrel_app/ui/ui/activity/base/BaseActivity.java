package com.boyukliev.kestrel_app.ui.ui.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.boyukliev.kestrel_app.ui.KestrelApp;
import com.boyukliev.kestrel_app.ui.di.ApplicationComponent;

/**
 * Created by Admin on 3/8/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectComponent(KestrelApp.get(this).getComponent());
        super.onCreate(savedInstanceState);
    }

    protected abstract void injectComponent(ApplicationComponent component);
}
